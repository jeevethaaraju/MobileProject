package com.example.mobileproject;

public class Order {
    private int orderId;
    private String username;
    private String product;
    private int quantity;
    private double price;
    private String status;

    // Constructor
    public Order(int orderId, String username, String product, int quantity, double price, String status) {
        this.orderId = orderId;
        this.username = username;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
