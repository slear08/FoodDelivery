package com.activity.fooddelivery.Models;

import com.activity.fooddelivery.Domain.FoodDomain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String username;
    private String email;
    private ArrayList<FoodDomain> products;

    public Cart() {
        // Default constructor required for Firebase
    }


    public Cart(String username, String email, ArrayList<FoodDomain> products) {
        this.username = username;
        this.email = email;
        this.products = products;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<FoodDomain> getProducts() {
        return products;
    }
}
