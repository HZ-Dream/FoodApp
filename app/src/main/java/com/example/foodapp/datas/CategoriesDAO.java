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

    public int updateCategory(Categories category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("image", category.getImage());
        return db.update("Categories", values, "id = ?", new String[]{String.valueOf(category.getId())});
    }

    public int deleteCategory(int id) {
        return db.delete("Categories", "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAllCategory() {
        db.delete("Categories", null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='Categories'"); // Reset AUTOINCREMENT
    }

    public boolean checkProductExists(int categoryId) {
        String query = "SELECT COUNT(*) FROM Products WHERE catId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
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

