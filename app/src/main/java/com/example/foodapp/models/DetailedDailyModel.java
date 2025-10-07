package com.example.foodapp.models;

public class DetailedDailyModel {
    private String image;
    private String name;
    private String description;
    private String rating;
    private String price;
    private String timing;

    public DetailedDailyModel(String image, String name, String description, String rating, String price, String timing) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.timing = timing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getTiming() {
        return timing;
    }
}
