package com.example.foodapp.models;

public class Orders {
    private int id;
    private int userId;
    private double subTotal;
    private String status;
    private String dateOrdered;

    public Orders(int id, int userId, double subTotal, String status, String dateOrdered) {
        this.id = id;
        this.userId = userId;
        this.subTotal = subTotal;
        this.status = status;
        this.dateOrdered = dateOrdered;
    }

    public Orders(int userId, double subTotal, String status, String dateOrdered) {
        this.userId = userId;
        this.subTotal = subTotal;
        this.status = status;
        this.dateOrdered = dateOrdered;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public String getStatus() {
        return status;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }
}
