package com.example.foodapp.datas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbConnect extends SQLiteOpenHelper {
    private static final String dbName = "FoodApp";
    private static final int dbVersion = 7;

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "name TEXT, " +
                "phone TEXT, " +
                "password TEXT, " +
                "isAdmin BOOLEAN)");

        db.execSQL("CREATE TABLE Categories (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "image TEXT)");

        db.execSQL("CREATE TABLE Products (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "price REAL, " +
                "timeCook TEXT, " +
                "image TEXT, " +
                "catId INTEGER, " +
                "FOREIGN KEY(catId) REFERENCES Categories(id))");

        db.execSQL("CREATE TABLE Carts (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "foodId INTEGER, " +
                "price REAL, " +
                "quantity INTEGER, " +
                "subTotal REAL, " +
                "FOREIGN KEY(userId) REFERENCES Users(id), " +
                "FOREIGN KEY(foodId) REFERENCES Products(id))");

        db.execSQL("CREATE TABLE Orders (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "subTotal REAL, " +
                "status TEXT, " +
                "dateOrdered TEXT, " +
                "FOREIGN KEY(userId) REFERENCES Users(id))");

        db.execSQL("CREATE TABLE OrderDetails (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderId INTEGER, " +
                "productName TEXT, " +
                "quantity INTEGER, " +
                "price REAL, " +
                "FOREIGN KEY(orderId) REFERENCES Orders(id))");
    }

    // version now: 7
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < dbVersion) {
            Cursor cursor = db.rawQuery("PRAGMA table_info(Users)", null);
            boolean isAdminColumnExists = false;
            if (cursor != null) {
                int nameColumnIndex = cursor.getColumnIndex("name");
                while (cursor.moveToNext()) {
                    if (nameColumnIndex != -1 && "isAdmin".equalsIgnoreCase(cursor.getString(nameColumnIndex))) {
                        isAdminColumnExists = true;
                        break;
                    }
                }
                cursor.close();
            }

            if (!isAdminColumnExists) {
                db.execSQL("ALTER TABLE Users ADD COLUMN isAdmin BOOLEAN DEFAULT FALSE");
            }
        }
    }
}