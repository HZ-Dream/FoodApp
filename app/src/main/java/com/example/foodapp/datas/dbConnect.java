package com.example.foodapp.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbConnect extends SQLiteOpenHelper {
    private static String dbName = "FoodApp";
    private static int dbVersion = 6;

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "name TEXT, " +
                "phone TEXT, " +
                "password TEXT)");

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

    // version now: 6
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
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
    }
}