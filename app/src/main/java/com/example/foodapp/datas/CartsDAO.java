package com.example.foodapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodapp.models.Carts;
import com.example.foodapp.models.CartItem;

import java.util.ArrayList;
import java.util.List;


public class CartsDAO {
    private final SQLiteDatabase db;
    private final dbConnect dbHelper;

    public CartsDAO(Context context) {
        dbHelper = new dbConnect(context);
        db = dbHelper.getWritableDatabase();
    }

    public long addCart(Carts cart) {
        ContentValues values = new ContentValues();
        values.put("userId", cart.getUserId());
        values.put("foodId", cart.getFoodId());
        values.put("price", cart.getPrice());
        values.put("quantity", cart.getQuantity());
        values.put("subTotal", cart.getSubTotal());
        return db.insert("Carts", null, values);
    }

    public void deleteAllCart() {
        db.delete("Carts", null, null);
    }

    public void removeCart(int id) {
        db.delete("Carts", "id = ?", new String[]{String.valueOf(id)});
    }

    public double getTotalPrice(int userId) {
        double totalPrice = 0.0;
        SQLiteDatabase readableDb = dbHelper.getReadableDatabase();
        String query = "SELECT SUM(subTotal) FROM Carts WHERE userId = ?";
        Cursor cursor = readableDb.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            totalPrice = cursor.getDouble(0);
        }
        cursor.close();
        return totalPrice;
    }

    public List<CartItem> getCartItemsForUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase readableDb = dbHelper.getReadableDatabase();

        String query = "SELECT c.id, p.name, p.image, p.price, c.quantity, c.subTotal " +
                "FROM Carts c INNER JOIN Products p ON c.foodId = p.id " +
                "WHERE c.userId = ?";

        Cursor cursor = readableDb.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(0);
                String productName = cursor.getString(1);
                String productImage = cursor.getString(2);
                double productPrice = cursor.getDouble(3);
                int quantity = cursor.getInt(4);
                double subTotal = cursor.getDouble(5);

                CartItem cartItem = new CartItem(cartId, productName, productImage, productPrice, quantity, subTotal);
                list.add(cartItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
}