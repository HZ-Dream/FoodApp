package com.example.foodapp.models;

public class OrderDetails {
    private int id;
    private long orderId;
    private String productName;
    private int quantity;
    private double price;

    public OrderDetails(int id, long orderId, String productName, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderDetails(long orderId, String productName, int quantity, double price) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
