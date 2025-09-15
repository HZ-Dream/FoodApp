package com.example.foodapp.models;

public class CartItem {
    private int cartId;
    private String productName;
    private String productImage;
    private double productPrice;
    private int quantity;
    private double subTotal;

    public CartItem(int cartId, String productName, String productImage, double productPrice, int quantity, double subTotal) {
        this.cartId = cartId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    // Getters and Setters
    public int getCartId() { return cartId; }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}