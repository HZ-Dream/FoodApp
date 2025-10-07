package com.example.foodapp.models;

public class Products {
    private int id;
    private String name;
    private double price;
    private String timeCook;
    private String image;
    private int catId;


    public Products(int id, String name, double price, String timeCook, String image, int catId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.timeCook = timeCook;
        this.image = image;
        this.catId = catId;
    }

    public Products(String name, double price, String timeCook, String image, int catId) {
        this.name = name;
        this.price = price;
        this.timeCook = timeCook;
        this.image = image;
        this.catId = catId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTimeCook() { return timeCook; }

    public void setTimeCook(String timeCook) { this.timeCook = timeCook; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) { this.image = image; }

    public int getCatId() {
        return catId;
    }
    public void setCatId(int catId) {
        this.catId = catId;
    }
}
