package com.example.foodapp.models;

public class Categories {
    private int id;
    private String name;
    private String image;

    public Categories(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Categories(String name, String image) {
        this.name = name;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Id: " + id + " - Name: " + name;
    }
}
