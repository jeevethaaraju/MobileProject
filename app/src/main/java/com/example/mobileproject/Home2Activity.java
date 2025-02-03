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

import androidx.appcompat.app.AppCompatActivity;

public class Home2Activity extends AppCompatActivity {
    private String loggedInUser;
    private ImageView profileImage; // Profile icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home2);

        // Initialize CartManager with the current context
        CartManager cartManager = CartManager.getInstance(this);

        // Get the logged-in email passed from the previous activity
        Intent intent = getIntent();
        loggedInUser = intent.getStringExtra("logged_in_user");

        // Display the logged-in email in @+id/profileIcon
        TextView profileIcon = findViewById(R.id.profileIcon);
        if (loggedInUser != null) {
            profileIcon.setText(loggedInUser);
        }
        // Add click listener for @+id/profileImage
        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileMenu(v);
            }
        });


        // Add click listener for @+id/phoneCategory
        LinearLayout phoneCategory = findViewById(R.id.phoneCategory);
        phoneCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HomeActivity and pass the logged-in email
                Intent intent = new Intent(Home2Activity.this, HomeActivity.class);
                intent.putExtra("logged_in_user", loggedInUser);
                startActivity(intent);
            }
        });



        // Add click listener for @+id/cartIcon
        ImageView cartIcon = findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CartActivity
                Intent intent = new Intent(Home2Activity.this, CartActivity.class);
                intent.putExtra("logged_in_user", loggedInUser); // Pass the email to CartActivity
                startActivity(intent);
            }
        });
    }

    private void showProfileMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            Log.d("MenuClick", "Clicked ID: " + item.getItemId());

            if (item.getItemId() == R.id.menu_profile) {
                startActivity(new Intent(Home2Activity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.menu_orders) {
                Intent intent = new Intent(Home2Activity.this, UserOrdersActivity.class);
                intent.putExtra("logged_in_user", loggedInUser);
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

}
