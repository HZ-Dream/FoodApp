package com.example.foodapp.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbConnect extends SQLiteOpenHelper {
    private static String dbName = "FoodApp";
    private static int dbVersion = 5;

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    // Chỉ chạy khi chưa có database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, name TEXT, phone TEXT, password TEXT)");

        db.execSQL("CREATE TABLE Categories (id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, image TEXT)");

        db.execSQL("CREATE TABLE Products (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, price REAL, timeCook TEXT, image TEXT, catId INTEGER, FOREIGN KEY(catId) REFERENCES Categories(id))");

        db.execSQL("CREATE TABLE Carts (id INTEGER PRIMARY KEY AUTOINCREMENT, " + "userId INTEGER, " +
                "foodId INTEGER, price REAL, quantity INTEGER, subTotal REAL, " +
                "FOREIGN KEY(userId) REFERENCES Users(id), " +
                "FOREIGN KEY(foodId) REFERENCES Products(id))");
    }

    // Chỉ chạy khi có thay đổi version trong db
    // version now: 5
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            db.execSQL("DROP TABLE IF EXISTS Carts");

            db.execSQL("CREATE TABLE Carts (id INTEGER PRIMARY KEY AUTOINCREMENT, " + "userId INTEGER, " +
                    "foodId INTEGER, price REAL, quantity INTEGER, subTotal REAL, " +
                    "FOREIGN KEY(userId) REFERENCES Users(id), " +
                    "FOREIGN KEY(foodId) REFERENCES Products(id))");
        }
    }
}