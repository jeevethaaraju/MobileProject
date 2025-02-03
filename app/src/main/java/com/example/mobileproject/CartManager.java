package com.example.mobileproject;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Product> cartItems;
    private DatabaseHelper databaseHelper;
    private String currentUser;

    // Private constructor to enforce singleton pattern
    private CartManager(Context context) {
        cartItems = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);
    }

    // Singleton access with context initialization
    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context.getApplicationContext());
        }
        return instance;
    }

    // Set the current user and load their cart from the database
    public void setCurrentUser(String username) {
        if (username == null || username.isEmpty()) {
            Log.e("CartManager", "setCurrentUser: Username is null or empty!");
            return;
        }

        currentUser = username;
        loadCartFromDatabase();
    }

    // Add a product to the cart and persist it to the database
    public void addToCart(Product product) {
        if (product == null) {
            Log.e("CartManager", "addToCart: Product is null!");
            return;
        }

        cartItems.add(product);
        boolean isInserted = databaseHelper.insertCartItem(
                currentUser,
                product.getName(),
                product.getPrice(),
                1 // Default quantity is 1
        );

        if (!isInserted) {
            Log.e("CartManager", "Failed to insert product into database for user: " + currentUser);
        }
    }

    // Get all cart items for the current user
    public List<Product> getCartItems() {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    // Clear the cart and remove items from the database
    public void clearCart() {
        cartItems.clear();
        boolean isCleared = databaseHelper.clearCartForUser(currentUser);

        if (!isCleared) {
            Log.e("CartManager", "Failed to clear cart for user: " + currentUser);
        }
    }

    public void removeFromCart(Product product) {
        cartItems.remove(product); // Remove from the in-memory list
        databaseHelper.deleteCartItem(currentUser, product.getName()); // Remove from the database
    }
    // Load cart items for the current user from the database
    private void loadCartFromDatabase() {
        if (currentUser == null || currentUser.isEmpty()) {
            Log.e("CartManager", "loadCartFromDatabase: Current user is not set!");
            return;
        }

        cartItems.clear();
        List<Product> dbCartItems = databaseHelper.getCartItemsForUser(currentUser);

        if (dbCartItems != null) {
            cartItems.addAll(dbCartItems);
        } else {
            Log.e("CartManager", "No cart items found in database for user: " + currentUser);
        }
    }
}