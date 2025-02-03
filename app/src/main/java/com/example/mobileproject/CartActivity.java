package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private TextView subtotalText;
    private TextView totalText;
    private TextView loggedInUserText;
    private Map<String, Integer> quantityMap; // Map using product name as key (prevents duplicates)
    private LinearLayout cartItemsContainer;
    private CartManager cartManager;
    private String loggedInUser;
    private List<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        // Initialize UI components
        cartItemsContainer = findViewById(R.id.cartItemsContainer);
        subtotalText = findViewById(R.id.subtotalText);
        totalText = findViewById(R.id.totalText);
        loggedInUserText = findViewById(R.id.loggedInUserText);
        Button payButton = findViewById(R.id.payButton);
        ImageView backIcon = findViewById(R.id.backIcon);

        // Get logged-in user
        loggedInUser = getIntent().getStringExtra("logged_in_user");
        loggedInUserText.setText(loggedInUser != null ? loggedInUser : "Logged in as: Unknown User");

        // Initialize CartManager and retrieve cart items
        cartManager = CartManager.getInstance(this);
        cartManager.setCurrentUser(loggedInUser);
        cartItems = cartManager.getCartItems();

        // Map to store product quantities
        quantityMap = new HashMap<>();

        // Load cart items into UI
        loadCartItems();

        // Back Button Navigation
        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
            intent.putExtra("logged_in_user", loggedInUser);
            startActivity(intent);
            finish();
        });

        // Pay Button Navigation
        payButton.setOnClickListener(v -> {
            String subtotalValue = subtotalText.getText().toString();
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("logged_in_user", loggedInUser);
            intent.putExtra("subtotal", subtotalValue);
            startActivity(intent);
        });
    }

    /**
     * Loads cart items into the UI, preventing duplicates and updating quantities.
     */
    private void loadCartItems() {
        cartItemsContainer.removeAllViews(); // Clear existing items to prevent duplicates

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Product product : cartItems) {
            String productName = product.getName();

            if (quantityMap.containsKey(productName)) {
                // If product already exists, update quantity
                int updatedQuantity = quantityMap.get(productName) + 1;
                quantityMap.put(productName, updatedQuantity);
                updateQuantityUI(productName, updatedQuantity);
            } else {
                // Add new product entry
                quantityMap.put(productName, 1);
                addCartItemView(product, 1);
            }
        }

        updateTotals();
    }

    /**
     * Adds a new product view to the cart.
     */
    private void addCartItemView(Product product, int initialQuantity) {
        View cartItemView = getLayoutInflater().inflate(R.layout.cart_item, cartItemsContainer, false);

        ImageView productImage = cartItemView.findViewById(R.id.productImage);
        TextView productNameText = cartItemView.findViewById(R.id.productName);
        TextView productPrice = cartItemView.findViewById(R.id.productPrice);
        Spinner quantitySpinner = cartItemView.findViewById(R.id.quantitySpinner);
        ImageView deleteIcon = cartItemView.findViewById(R.id.deleteIcon);

        // Set product image properly
        if (product.getImage() != null) {
            productImage.setImageBitmap(product.getImage());
        } else {
            productImage.setImageResource(product.getImageResId());
        }

        productNameText.setText(product.getName());
        productPrice.setText(String.format("$%.2f", product.getPrice()));

        // Setup quantity spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.quantity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapter);
        quantitySpinner.setSelection(initialQuantity - 1);

        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedQuantity = Integer.parseInt(parent.getItemAtPosition(position).toString());
                quantityMap.put(product.getName(), selectedQuantity);
                updateTotals();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Remove product from cart
        deleteIcon.setOnClickListener(v -> {
            cartManager.removeFromCart(product);
            cartItemsContainer.removeView(cartItemView);
            quantityMap.remove(product.getName());
            Toast.makeText(CartActivity.this, product.getName() + " removed from cart", Toast.LENGTH_SHORT).show();
            updateTotals();
        });

        cartItemView.setTag(product.getName()); // Store product name for reference
        cartItemsContainer.addView(cartItemView);
    }

    /**
     * Updates quantity UI for an existing product.
     */
    private void updateQuantityUI(String productName, int newQuantity) {
        int childCount = cartItemsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View cartItemView = cartItemsContainer.getChildAt(i);
            if (productName.equals(cartItemView.getTag())) {
                Spinner quantitySpinner = cartItemView.findViewById(R.id.quantitySpinner);
                quantitySpinner.setSelection(newQuantity - 1);
                break;
            }
        }
        updateTotals();
    }

    /**
     * Updates the subtotal and total amounts.
     */
    private void updateTotals() {
        double subtotal = 0.0;
        for (Product product : cartItems) {
            int quantity = quantityMap.getOrDefault(product.getName(), 1);
            subtotal += product.getPrice() * quantity;
        }
        subtotalText.setText(String.format("$%.2f", subtotal));
        totalText.setText(String.format("$%.2f", subtotal));
    }
}
