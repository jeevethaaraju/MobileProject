package com.example.mobileproject;

import android.graphics.Bitmap;

public class Product {
    private String name;
    private double price;
    private int imageResId;
    private Bitmap image;
    private int quantity;

    // Constructor for drawable-based images
    public Product(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.image = null; // No Bitmap, only drawable resource
    }

    // Constructor for database-stored images (Bitmap)
    public Product(String name, double price, Bitmap image) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.imageResId = -1; // No drawable resource ID
    }

    // Getters
    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters (if needed)
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
