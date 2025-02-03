package com.example.mobileproject;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.net.Uri;
import android.view.LayoutInflater;

public class AdminDashboardActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedImageBitmap = null;

    private RecyclerView adminOrdersRecyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        adminOrdersRecyclerView = findViewById(R.id.adminOrdersRecyclerView);
        adminOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load all orders
        loadAllOrders();
    }

    private void loadAllOrders() {
        List<Order> allOrders = databaseHelper.getAllOrders();
        if (allOrders.isEmpty()) {
            Toast.makeText(this, "No orders available", Toast.LENGTH_SHORT).show();
        }

        // Use OrderAdapter with admin mode enabled
        orderAdapter = new OrderAdapter(allOrders, true, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                showUpdateStatusDialog(order); // Ensure this method exists
            }
        });


        adminOrdersRecyclerView.setAdapter(orderAdapter);
    }
    private void addProduct(String productName, double price, Bitmap productImage) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if (productImage == null) {
            Toast.makeText(this, "Please select an image for the product", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertProduct(productName, price, productImage);

        if (success) {
            Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add product!", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to show the Add Product Dialog
    public void showAddProductDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        ImageView imageViewProduct = dialogView.findViewById(R.id.imageViewProduct);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        Button btnAddProduct = dialogView.findViewById(R.id.btnAddProduct);

        // Image selection logic
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Add Product logic
        btnAddProduct.setOnClickListener(v -> {
            String productName = editTextProductName.getText().toString().trim();
            String priceText = editTextProductPrice.getText().toString().trim();

            if (productName.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Please enter product name and price", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceText);
            addProduct(productName, price, selectedImageBitmap);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Handle image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void showUpdateStatusDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Order Status");

        // Dropdown list (Spinner) for status selection
        String[] statusOptions = {"Pending", "Processing", "Shipped", "Delivered", "Cancelled"};
        final int[] selectedOption = {0}; // Default selection

        builder.setSingleChoiceItems(statusOptions, 0, (dialog, which) -> selectedOption[0] = which);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newStatus = statusOptions[selectedOption[0]];

            boolean updated = databaseHelper.updateOrderStatus(order.getOrderId(), newStatus);
            if (updated) {
                Toast.makeText(this, "Order updated to: " + newStatus, Toast.LENGTH_SHORT).show();
                loadAllOrders(); // Refresh orders
            } else {
                Toast.makeText(this, "Failed to update order", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Logout function
    private void logoutAdmin() {
        Toast.makeText(AdminDashboardActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back to Login Activity
        Intent intent = new Intent(AdminDashboardActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);
        finish();
    }

    // Show Pop-up Menu for Logout
    public void showAdminMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.admin_menu, popupMenu.getMenu()); // Create admin_menu.xml in res/menu

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_logout) {
                logoutAdmin();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


}
