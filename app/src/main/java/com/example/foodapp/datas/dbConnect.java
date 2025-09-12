package com.example.foodapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.foodapp.models.Users;

public class dbConnect extends SQLiteOpenHelper {
    private static String dbName = "FoodApp";
    private static String tableName = "Users";
    private static int dbVersion = 1;
    private static String ID = "id";
    private static String Email = "email";
    private static String Name = "name";
    private static String Phone = "phone";
    private static String Password = "password";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDb) {
        String query = "CREATE TABLE " + tableName + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Email + " TEXT, " + Name + " TEXT, " + Phone + " TEXT, " + Password + " TEXT)";
        sqlDb.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    public boolean addUser(Users users) {
        SQLiteDatabase sqlDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Email, users.getEmail());
        values.put(Name, users.getName());
        values.put(Phone, users.getPhone());
        values.put(Password, users.getPassword());
        long result = sqlDb.insert(tableName, null, values);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase sqlDb = this.getWritableDatabase();
        Cursor cursor = sqlDb.rawQuery("SELECT * FROM " + tableName + " WHERE " + Email + " = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public boolean checkLogin(String email, String password){
        SQLiteDatabase sqlDb = this.getWritableDatabase();
        Cursor cursor = sqlDb.rawQuery("SELECT * FROM " + tableName + " WHERE " + Email + " = ? AND " + Password + " = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }
}
