package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signUpText = findViewById(R.id.signUpText);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Handle Login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Validate inputs
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Debugging log
                Log.d("LoginDebug", "Username: " + username + ", Password: " + password);

                // Check user in database
                if (databaseHelper.checkUser(username, password)) {
                    // Get user role (admin or user)
                    String role = databaseHelper.getUserRole(username, password);

                    if (role != null) {
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Initialize CartManager for the logged-in user
                        CartManager.getInstance(Login.this).setCurrentUser(username);

                        // Navigate to the correct activity based on the role
                        Intent intent;
                        if (role.equals("admin")) {
                            intent = new Intent(Login.this, AdminDashboardActivity.class);
                        } else {
                            intent = new Intent(Login.this, HomeActivity.class);
                        }

                        // Pass user details
                        intent.putExtra("logged_in_user", username);
                        intent.putExtra("logged_in_password", password);
                        intent.putExtra("user_role", role);

                        startActivity(intent);
                        finish(); // Close Login activity
                    } else {
                        Toast.makeText(Login.this, "Invalid role", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle Sign Up link click
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}
