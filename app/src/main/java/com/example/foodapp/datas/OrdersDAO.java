package com.example.foodapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodapp.models.OrderDetails;
import com.example.foodapp.models.Orders;

import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private final SQLiteDatabase db;
    private final dbConnect dbHelper;

    public OrdersDAO(Context context) {
        dbHelper = new dbConnect(context);
        db = dbHelper.getWritableDatabase();
    }

    public void deleteAllOrder() {
        db.delete("Orders", null, null);
    }

    public void deleteAllOrderDetails() {
        db.delete("OrderDetails", null, null);
    }

    public long addOrder(Orders orders) {
        ContentValues values = new ContentValues();
        values.put("userId", orders.getUserId());
        values.put("subTotal", orders.getSubTotal());
        values.put("status", orders.getStatus());
        values.put("dateOrdered", orders.getDateOrdered());
        values.put("address", orders.getAddress());
        return db.insert("Orders", null, values);
    }

    public long addOrderDetail(OrderDetails orderDetails) {
        ContentValues values = new ContentValues();
        values.put("orderId", orderDetails.getOrderId());
        values.put("productName", orderDetails.getProductName());
        values.put("quantity", orderDetails.getQuantity());
        values.put("price", orderDetails.getPrice());
        return db.insert("OrderDetails", null, values);
    }

    public boolean deleteOrder(long orderId) {
        String sql = "SELECT * FROM Orders WHERE id = ?";
        String[] selectionArgs = {String.valueOf(orderId)};

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    if (status.equals("Đã xác nhận") || status.equals("Đang giao hàng") || status.equals("Đã giao hàng")) {
                        return false;
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        db.delete("Orders", "id = ?", new String[]{String.valueOf(orderId)});
        db.delete("OrderDetails", "orderId = ?", new String[]{String.valueOf(orderId)});
        return true;
    }

    public List<Orders> getOrdersByUserId(int userId) {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE userId = ? ORDER BY id DESC";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int userID = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                double subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("subTotal"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String dateOrdered = cursor.getString(cursor.getColumnIndexOrThrow("dateOrdered"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));

                list.add(new Orders(id, userID, subTotal, status, dateOrdered, address));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<OrderDetails> getOrderDetails(long orderId) {
        List<OrderDetails> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM OrderDetails WHERE orderId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

                list.add(new OrderDetails(id, orderId, productName, quantity, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void updateOrderStatus(long orderId, String newStatus) {
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        db.update("Orders", values, "id = ?", new String[]{String.valueOf(orderId)});
    }

    public List<Orders> getAllOrders(String status) {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE status = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{status});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int userID = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                double subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("subTotal"));
                String dateOrdered = cursor.getString(cursor.getColumnIndexOrThrow("dateOrdered"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address")); // địa chỉ

                list.add(new Orders(id, userID, subTotal, status, dateOrdered, address));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
