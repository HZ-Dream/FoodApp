package com.example.foodapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.foodapp.models.Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        values.put("wishList", users.getWishList());
        values.put("isAdmin", users.isAdmin());
        long result = db.insert("Users", null, values);
        return result != -1;
    }

    public String getWishListString(int userId) {
        Cursor cursor = db.rawQuery("SELECT wishList FROM Users WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String wishList = cursor.getString(0);
            cursor.close();
            return wishList != null ? wishList : "";
        }
        cursor.close();
        return "";
    }

    public boolean isProductInWishList(int userId, int productId) {
        String wishListStr = getWishListString(userId);
        if (wishListStr.isEmpty()) {
            return false;
        }
        List<String> wishListIds = Arrays.asList(wishListStr.split(","));
        return wishListIds.contains(String.valueOf(productId));
    }

    public int addProductToWishList(int userId, int productId) {
        String currentWishList = getWishListString(userId);
        String productIdStr = String.valueOf(productId);

        if (isProductInWishList(userId, productId)) {
            return 0;
        }

        String newWishList;
        if (currentWishList.isEmpty()) {
            newWishList = productIdStr;
        } else {
            newWishList = currentWishList + "," + productIdStr;
        }

        ContentValues values = new ContentValues();
        values.put("wishList", newWishList);
        return db.update("Users", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public int removeProductFromWishList(int userId, int productId) {
        String currentWishList = getWishListString(userId);
        if (!isProductInWishList(userId, productId)) {
            return 0;
        }

        List<String> wishListIds = new ArrayList<>(Arrays.asList(currentWishList.split(",")));
        wishListIds.remove(String.valueOf(productId));

        String newWishList = TextUtils.join(",", wishListIds);

        ContentValues values = new ContentValues();
        values.put("wishList", newWishList);
        return db.update("Users", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public boolean checkEmail(String email) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int updateUser(Users user) {
        SQLiteDatabase db = this.db;
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("password", user.getPassword());

        return db.update("Users", values, "id = ?", new String[]{String.valueOf(user.getId())});
    }

    public List<Users> getUserByUserId(int userId) {
        List<Users> usersList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                Users user = new Users(id, email, name, phone, password);
                usersList.add(user);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return usersList;
    }

    public boolean checkLogin(String email, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkAdmin(String email) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND isAdmin = 1", new String[]{email});
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

    public void deleteUser(int userId) {
        db.delete("Users", "id = ?", new String[]{String.valueOf(userId)});
    }

    public List<Users> getAllUsersList() {
        List<Users> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1;

                Users user = new Users(email, name, phone, password, isAdmin);
                user.setId(id);  // <-- rất quan trọng
                list.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public void setAdmin(int userId, boolean isAdmin) {
        ContentValues values = new ContentValues();
        values.put("isAdmin", isAdmin ? 1 : 0);
        db.update("Users", values, "id = ?", new String[]{String.valueOf(userId)});
    }
}
