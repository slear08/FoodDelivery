package com.activity.fooddelivery.Models;

public class Product {
    private String name;
    private int quantity;

    public Product() {
        // Default constructor required for Firebase
    }

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
