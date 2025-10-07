package com.example.foodapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodapp.models.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductsDAO {
    private SQLiteDatabase db;

    public ProductsDAO(Context context) {
        dbConnect helper = new dbConnect(context);
        db = helper.getWritableDatabase();
    }

    public long addProduct(Products product) {
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("timeCook", product.getTimeCook());
        values.put("image", product.getImage());
        values.put("catId", product.getCatId());
        return db.insert("Products", null, values);
    }

    public int updateProduct(Products product) {
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("timeCook", product.getTimeCook());
        values.put("image", product.getImage());
        values.put("catId", product.getCatId());
        return db.update("Products", values, "id = ?", new String[]{String.valueOf(product.getId())});
    }

    public int deleteProduct(int id) {
        return db.delete("Products", "id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteAllProduct() {
        db.delete("Products", null, null);
    }

    public List<Products> getAllProducts() {
        List<Products> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Products", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String timeCook = cursor.getString(cursor.getColumnIndexOrThrow("timeCook"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                int catId = cursor.getInt(cursor.getColumnIndexOrThrow("catId"));
                list.add(new Products(id, name, price, timeCook, image, catId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Products> getProductsByCategoryId(int categoryId) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE catId = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String timeCook = cursor.getString(cursor.getColumnIndexOrThrow("timeCook"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                int catId = cursor.getInt(cursor.getColumnIndexOrThrow("catId"));
                list.add(new Products(id, name, price, timeCook, image, catId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public List<Products> getProductsByName(String name) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE name LIKE ?";
        String[] selectionArgs = {"%" + name + "%"};

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String timeCook = cursor.getString(cursor.getColumnIndexOrThrow("timeCook"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                int catId = cursor.getInt(cursor.getColumnIndexOrThrow("catId"));

                list.add(new Products(id, productName, price, timeCook, image, catId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
