package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private EditText addressEditText;
    private ImageButton btnEditAddress;
    private Button btnSaveAddress;
    private TextView cartTotalTextView, subtotalTextView;
    private String savedAddress = "276 St Paul's Rd, London N1 2LL, UK\nContact: +44-78-xyz-123"; // Default address
    private CartManager cartManager;
    private String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        // Retrieve data passed from CartActivity
        loggedInUser = getIntent().getStringExtra("logged_in_user");
        String subtotal = getIntent().getStringExtra("subtotal");

        // Initialize CartManager
        cartManager = CartManager.getInstance(this);
        cartManager.setCurrentUser(loggedInUser);

        // Fetch total product quantity in cart
        int totalQuantity = getTotalCartQuantity();

        // Debug logs for validation
        Log.d("CheckoutActivity", "Logged-in User: " + (loggedInUser != null ? loggedInUser : "Unknown"));
        Log.d("CheckoutActivity", "Subtotal: " + (subtotal != null ? subtotal : "$0.00"));
        Log.d("CheckoutActivity", "Total Quantity in Cart: " + totalQuantity);

        // Reference UI components
        subtotalTextView = findViewById(R.id.subtotalTextView);

        addressEditText = findViewById(R.id.tv_delivery_address);
        btnEditAddress = findViewById(R.id.btn_edit_address);
        btnSaveAddress = findViewById(R.id.btn_save_address);

        // Set default address
        addressEditText.setText(savedAddress);
        addressEditText.setEnabled(false); // Disable editing initially

        // Set subtotal value
        subtotalTextView.setText("Subtotal: " + (subtotal != null ? subtotal : "$0.00"));



        // Back button navigation
        ImageView backButton = findViewById(R.id.ic_back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, CartActivity.class);
            if (loggedInUser != null) {
                intent.putExtra("logged_in_user", loggedInUser);
            }
            startActivity(intent);
            finish();
        });

        // Payment button navigation
        Button paymentButton = findViewById(R.id.btn_checkout);
        paymentButton.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(CheckoutActivity.this, PaymentActivity.class);
            paymentIntent.putExtra("subtotal", subtotal != null ? subtotal : "$0.00");
            paymentIntent.putExtra("total_quantity", totalQuantity);
            if (loggedInUser != null) {
                paymentIntent.putExtra("logged_in_user", loggedInUser);
            }
            paymentIntent.putExtra("saved_address", savedAddress);
            startActivity(paymentIntent);
        });

        // Edit Address Button Click
        btnEditAddress.setOnClickListener(v -> {
            addressEditText.setEnabled(true);
            addressEditText.requestFocus();
            btnEditAddress.setVisibility(View.GONE);
            btnSaveAddress.setVisibility(View.VISIBLE);
        });

        // Save Address Button Click
        btnSaveAddress.setOnClickListener(v -> {
            savedAddress = addressEditText.getText().toString();
            addressEditText.setEnabled(false);
            btnEditAddress.setVisibility(View.VISIBLE);
            btnSaveAddress.setVisibility(View.GONE);
        });
    }

    // **Method to calculate total quantity of products in cart**
    private int getTotalCartQuantity() {
        List<Product> cartItems = cartManager.getCartItems();
        int totalQuantity = 0;

        for (Product product : cartItems) {
            totalQuantity += product.getQuantity(); // Ensure getQuantity() is correctly implemented
        }
        return totalQuantity;
    }
}
