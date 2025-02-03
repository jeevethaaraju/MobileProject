package com.example.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserOrdersActivity extends AppCompatActivity {

    private RecyclerView userOrdersRecyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseHelper databaseHelper;
    private String loggedInUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        // Get the logged-in username
        Intent intent = getIntent();
        loggedInUser = intent.getStringExtra("logged_in_user");

        if (loggedInUser == null || loggedInUser.isEmpty()) {
            Toast.makeText(this, "Error: No user found! Please log in again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        userOrdersRecyclerView = findViewById(R.id.userOrdersRecyclerView);
        userOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load user orders
        loadUserOrders();
        // Back Button Navigation
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOrdersActivity.this, HomeActivity.class);
                intent.putExtra("logged_in_user", getIntent().getStringExtra("logged_in_user"));
                startActivity(intent);
                finish();
            }
        });
    }



    private void loadUserOrders() {
        List<Order> userOrders = databaseHelper.getUserOrders(loggedInUser);
        if (userOrders == null || userOrders.isEmpty()) {
            Toast.makeText(this, "No orders found for " + loggedInUser, Toast.LENGTH_SHORT).show();
        } else {
            orderAdapter = new OrderAdapter(userOrders, false, null);
            userOrdersRecyclerView.setAdapter(orderAdapter);
        }
    }

}
