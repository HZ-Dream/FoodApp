package com.example.foodapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodapp.models.Users;

public class UsersDAO {
    private SQLiteDatabase db;

    public UsersDAO(Context context) {
        dbConnect dbConnect = new dbConnect(context);
        db = dbConnect.getWritableDatabase();
    }

    public boolean addUser(Users users) {
        ContentValues values = new ContentValues();
        values.put("email", users.getEmail());
        values.put("name", users.getName());
        values.put("phone", users.getPhone());
        values.put("password", users.getPassword());
        long result = db.insert("Users", null, values);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkLogin(String email, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String getUserNameByEmail(String email) {
        String name = "";
        Cursor cursor = db.rawQuery("SELECT name FROM Users WHERE email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        cursor.close();
        return name;
    }

    public int getUserIdByEmail(String email) {
        int userId = 0;
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }

    public Cursor getAllUsers() {
        return db.rawQuery("SELECT * FROM Users", null);
    }
}
