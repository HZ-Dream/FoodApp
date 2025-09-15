package com.example.foodapp.models;

public class Carts {
    private int id;
    private int userId;
    private int foodId;
    private double price;
    private int quantity;
    private double subTotal;

    public Carts(int id, int userId, int foodId, double price, int quantity, double subTotal) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public Carts(int userId, int foodId, double price, int quantity, double subTotal) {
        this.userId = userId;
        this.foodId = foodId;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getFoodId() {
        return foodId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
