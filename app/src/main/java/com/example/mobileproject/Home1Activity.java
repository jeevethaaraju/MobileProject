package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Home1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home1);

        // Get the username/email passed from LoginActivity
        Intent intent = getIntent();
        String loggedInUser = intent.getStringExtra("logged_in_user");

        // Display the username/email in @+id/profileIcon
        TextView profileIcon = findViewById(R.id.profileIcon);
        if (loggedInUser != null && !loggedInUser.isEmpty()) {
            profileIcon.setText(loggedInUser);
        } else {
            profileIcon.setText("Guest User"); // Fallback if no email is provided
        }

        // Initialize CartManager with the current context and set the logged-in user
        CartManager cartManager = CartManager.getInstance(this);
        cartManager.setCurrentUser(loggedInUser);

        // Add click listener for @+id/phoneCategory
        LinearLayout phoneCategory = findViewById(R.id.phoneCategory);
        phoneCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HomeActivity
                Intent intent = new Intent(Home1Activity.this, HomeActivity.class);
                intent.putExtra("logged_in_user", loggedInUser); // Pass the email to HomeActivity
                startActivity(intent);
            }
        });

        // Add click listener for @+id/headphoneCategory
        LinearLayout headphoneCategory = findViewById(R.id.headphoneCategory);
        headphoneCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Home2Activity
                Intent intent = new Intent(Home1Activity.this, Home2Activity.class);
                intent.putExtra("logged_in_user", loggedInUser); // Pass the email to Home2Activity
                startActivity(intent);
            }
        });

        // Add click listener for @+id/cartIcon2
        ImageView cartIcon2 = findViewById(R.id.cartIcon2);
        cartIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add a Product to Cart
                Product product = new Product("Product From Cart Icon 2", 500.00, R.drawable.ic_laptop);
                cartManager.addToCart(product);
                Toast.makeText(Home1Activity.this, "Product added to cart", Toast.LENGTH_SHORT).show();

                // Navigate to CartActivity
                Intent intent = new Intent(Home1Activity.this, CartActivity.class);
                intent.putExtra("logged_in_user", loggedInUser); // Pass the email to CartActivity
                startActivity(intent);
            }
        });

        // Add click listener for @+id/cartIcon
        ImageView cartIcon = findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CartActivity
                Intent intent = new Intent(Home1Activity.this, CartActivity.class);
                intent.putExtra("logged_in_user", loggedInUser); // Pass the email to CartActivity
                startActivity(intent);
            }
        });
    }
}
