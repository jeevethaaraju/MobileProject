package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvOrderDetails;
    private RadioGroup radioGroupPayment;
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Reference the UI elements
        tvOrderDetails = findViewById(R.id.tv_order_details);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        btnPay = findViewById(R.id.btn_pay);

        // Retrieve the order details (subtotal, total quantity) from CheckoutActivity
        String subtotal = getIntent().getStringExtra("subtotal");
        int totalQuantity = getIntent().getIntExtra("total_quantity", 0);

        // Display the order details
        tvOrderDetails.setText("Subtotal: " + subtotal + "\nTotal Quantity: " + totalQuantity);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected payment method
                int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
                RadioButton selectedPaymentMethod = findViewById(selectedPaymentId);
                String paymentMethod = selectedPaymentMethod.getText().toString();

                // Get current user (ensure you pass the logged-in user from the previous activity)
                String loggedInUser = getIntent().getStringExtra("logged_in_user");

                // Get cart items from CartManager
                List<Product> cartItems = CartManager.getInstance(PaymentActivity.this).getCartItems();

                if (cartItems.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(PaymentActivity.this);

                // Insert each item in the cart as an order
                for (Product product : cartItems) {
                    databaseHelper.insertOrder(
                            loggedInUser,                      // Username
                            product.getName(),                 // Product name
                            product.getQuantity(),             // Quantity
                            product.getPrice(),                // Price
                            "Processing"                       // Default order status
                    );
                }

                // Clear the cart after successful order placement
                CartManager.getInstance(PaymentActivity.this).clearCart();

                // Show success message
                Toast.makeText(PaymentActivity.this, "Payment with " + paymentMethod + " is successful! Order placed.", Toast.LENGTH_LONG).show();

                // Navigate to Order Confirmation Activity (or HomeActivity)
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                intent.putExtra("logged_in_user", loggedInUser);
                startActivity(intent);
                finish(); // Close payment activity
            }
        });

        // Back Button Navigation
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                intent.putExtra("logged_in_user", getIntent().getStringExtra("logged_in_user"));
                startActivity(intent);
                finish();
            }
        });

    }
}
