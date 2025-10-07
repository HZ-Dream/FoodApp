package com.example.foodapp.models;

public class Orders {
    private int id;
    private int userId;
    private double subTotal;
    private String status;
    private String dateOrdered;
    private String address;

    public Orders(int id, int userId, double subTotal, String status, String dateOrdered, String address) {
        this.id = id;
        this.userId = userId;
        this.subTotal = subTotal;
        this.status = status;
        this.dateOrdered = dateOrdered;
        this.address = address;
    }

    public Orders(int userId, double subTotal, String status, String dateOrdered, String address) {
        this.userId = userId;
        this.subTotal = subTotal;
        this.status = status;
        this.dateOrdered = dateOrdered;
        this.address = address;
    }

    // Getter
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

    public String getAddress() {
        return address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
