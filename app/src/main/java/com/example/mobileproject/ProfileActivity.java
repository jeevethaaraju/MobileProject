package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Get the username and password passed from HomeActivity
        String loggedInUser = getIntent().getStringExtra("logged_in_user");
        String loggedInPassword = getIntent().getStringExtra("logged_in_password");

        // Display the username/email and password in TextViews
        TextView userEmailText = findViewById(R.id.emailField);
        TextView userPasswordText = findViewById(R.id.passwordField);

        userEmailText.setText("Email: " + loggedInUser);
        userPasswordText.setText("Password: " + loggedInPassword);

        // Add click listener for @+id/backIcon
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HomeActivity
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("logged_in_user", loggedInUser); // Pass back the logged-in email
                startActivity(intent);
                finish(); // Optional: Finish ProfileActivity so it won't remain in the back stack
            }
        });

        // Add click listener for @+id/logoutButton
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Login activity
                Intent intent = new Intent(ProfileActivity.this, Login.class);
                startActivity(intent);
                finish(); // Finish ProfileActivity so the user can't return by pressing the back button
            }
        });
    }
}
