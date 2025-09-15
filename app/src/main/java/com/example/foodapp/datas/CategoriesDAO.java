package com.example.foodapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodapp.models.Categories;

import java.util.ArrayList;
import java.util.List;


public class CategoriesDAO {
    private SQLiteDatabase db;

    public CategoriesDAO(Context context) {
        dbConnect helper = new dbConnect(context);
        db = helper.getWritableDatabase();
    }

    public long addCategory(Categories category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("image", category.getImage());
        return db.insert("Categories", null, values);
    }

    public void deleteAllCategory() {
        db.delete("Categories", null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='Categories'"); // Reset AUTOINCREMENT
    }

    public List<Categories> getAllCategories() {
        List<Categories> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                list.add(new Categories(id, name, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}

