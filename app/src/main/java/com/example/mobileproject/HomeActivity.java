package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    private String loggedInUser;
    private String loggedInPassword;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper; // Corrected: Properly declared

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize RecyclerView
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        // Load and display products from the database
        loadProductsFromDatabase();

        // Initialize CartManager with the current context
        CartManager cartManager = CartManager.getInstance(this);

        // Get the username/email and password passed from LoginActivity
        Intent intent = getIntent();
        loggedInUser = intent.getStringExtra("logged_in_user");
        loggedInPassword = intent.getStringExtra("logged_in_password");

        // Display the username/email in @+id/profileIcon
        TextView profileIcon = findViewById(R.id.profileIcon);
        if (loggedInUser != null && !loggedInUser.isEmpty()) {
            profileIcon.setText(loggedInUser);
        } else {
            profileIcon.setText("Guest"); // Default text for guest users
        }

        // Add click listener for @+id/profileImage
        ImageView profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity with the logged-in user details
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("logged_in_user", loggedInUser);
                intent.putExtra("logged_in_password", loggedInPassword);
                showProfileMenu(v);

            }
        });

        // Add click listener for @+id/laptopCategory
        LinearLayout laptopCategory = findViewById(R.id.laptopCategory);
        laptopCategory.setOnClickListener(v -> {
            Intent intent1 = new Intent(HomeActivity.this, Home1Activity.class);
            intent1.putExtra("logged_in_user", loggedInUser);
            startActivity(intent1);
        });

        // Add click listener for @+id/headphoneCategory
        LinearLayout headphoneCategory = findViewById(R.id.headphoneCategory);
        headphoneCategory.setOnClickListener(v -> {
            Intent intent12 = new Intent(HomeActivity.this, Home2Activity.class);
            intent12.putExtra("logged_in_user", loggedInUser);
            startActivity(intent12);
        });

        // Add click listener for @+id/cartIcon
        ImageView cartIcon = findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(v -> {
            Intent intent13 = new Intent(HomeActivity.this, CartActivity.class);
            intent13.putExtra("logged_in_user", loggedInUser);
            startActivity(intent13);
        });
    }

    // ✅ FIXED: Correct method to fetch and display products from the database
    private void loadProductsFromDatabase() {
        List<Product> productList = databaseHelper.getAllProducts();

        if (productList.isEmpty()) {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
        } else {
            productAdapter = new ProductAdapter(this, productList);
            recyclerViewProducts.setAdapter(productAdapter);
        }
    }

    private void showProfileMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            Log.d("MenuClick", "Clicked ID: " + item.getItemId());

            if (item.getItemId() == R.id.menu_profile) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("logged_in_user", loggedInUser);
                intent.putExtra("logged_in_password", loggedInPassword);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.menu_orders) {
                Intent intent = new Intent(HomeActivity.this, UserOrdersActivity.class);
                intent.putExtra("logged_in_user", loggedInUser);
                startActivity(intent);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}
