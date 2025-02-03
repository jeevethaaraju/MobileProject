package com.example.mobileproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    // Cart Table
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_CART_USERNAME = "username";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
    private static final String COLUMN_PRODUCT_IMAGE = "product_image";

    // Orders Table
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_USERNAME = "username";
    private static final String COLUMN_ORDER_PRODUCT = "product_name";  // Product name in orders table
    private static final String COLUMN_ORDER_QUANTITY = "quantity";      // Quantity in orders table
    private static final String COLUMN_ORDER_PRICE = "price";            // Price in orders table
    private static final String COLUMN_ORDER_STATUS = "status";          // Order status
    // Products Table
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "product_id";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table with role
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_ROLE + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

         // Create Cart Table with Image Storage
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CART_USERNAME + " TEXT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_PRICE + " REAL, "
                + COLUMN_PRODUCT_QUANTITY + " INTEGER, "
                + COLUMN_PRODUCT_IMAGE + " BLOB)";  // Store Image as BLOB
        db.execSQL(CREATE_CART_TABLE);

        // Create Orders Table with product name, quantity, price, and status
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ORDER_USERNAME + " TEXT, "
                + COLUMN_ORDER_PRODUCT + " TEXT, "
                + COLUMN_ORDER_QUANTITY + " INTEGER, "
                + COLUMN_ORDER_PRICE + " REAL, "
                + COLUMN_ORDER_STATUS + " TEXT)";
        db.execSQL(CREATE_ORDERS_TABLE);

        // Create Products Table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_PRICE + " REAL, "
                + COLUMN_PRODUCT_IMAGE + " BLOB)"; // Ensure this column exists
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Insert default admin user
        ContentValues adminValues = new ContentValues();
        adminValues.put(COLUMN_USERNAME, "admin");
        adminValues.put(COLUMN_PASSWORD, "admin123");
        adminValues.put(COLUMN_ROLE, "admin");
        db.insert(TABLE_USERS, null, adminValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // Insert a new user
    public boolean insertUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Get user role
    public String getUserRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_ROLE); // Get column index for the role
            if (columnIndex >= 0) { // Ensure the column exists
                String role = cursor.getString(columnIndex);
                cursor.close();
                return role;
            }
        }
        return null; // Return null if role not found
    }

    // Check if a user exists
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Insert a cart item
    public boolean insertCartItem(String username, String productName, double productPrice, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_USERNAME, username);
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_PRICE, productPrice);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);

        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }

    // Removing the methods if not needed
// Clear the cart for a specific user
    public boolean clearCartForUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CART, COLUMN_CART_USERNAME + "=?", new String[]{username});
        return rowsDeleted > 0; // Return true if at least one row is deleted
    }

    // Delete a cart item for a specific user
    public void deleteCartItem(String username, String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "username=? AND product_name=?", new String[]{username, productName});
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE));

                // Convert byte array to Bitmap
                Bitmap image = null;
                if (imageBytes != null && imageBytes.length > 0) {
                    image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }

                // Add product with only Bitmap image (no drawable ID)
                productList.add(new Product(name, price, image)); // Uses the correct constructor
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    // ✅ **Insert Product into Database**
    public boolean insertProduct(String productName, double price, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_IMAGE, getBitmapAsByteArray(image));

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }

    // ✅ **Convert Bitmap to ByteArray**
    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Retrieve cart items for a specific user
    public List<Product> getCartItemsForUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Product> cartItems = new ArrayList<>();
        String query = "SELECT " + COLUMN_PRODUCT_NAME + ", " + COLUMN_PRODUCT_PRICE + ", " + COLUMN_PRODUCT_QUANTITY +
                " FROM " + TABLE_CART + " WHERE " + COLUMN_CART_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String productName = cursor.getString(0);
                double productPrice = cursor.getDouble(1);
                int quantity = cursor.getInt(2);

                cartItems.add(new Product(productName, productPrice,0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cartItems;
    }

    // Insert an order

    public boolean insertOrder(String username, String product, int quantity, double price, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USERNAME, username);
        values.put(COLUMN_ORDER_PRODUCT, product);
        values.put(COLUMN_ORDER_QUANTITY, quantity);
        values.put(COLUMN_ORDER_PRICE, price);
        values.put(COLUMN_ORDER_STATUS, status);

        long result = db.insert(TABLE_ORDERS, null, values);
        return result != -1; // Returns true if insertion is successful
    }


    // Retrieve all orders
    public List<Order> getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ORDERS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Log column names for debugging (optional)
            String[] columnNames = cursor.getColumnNames();
            for (String column : columnNames) {
                Log.d("DatabaseDebug", "Column: " + column);
            }

            // Ensure column indexes are valid before accessing them
            int orderIdIndex = cursor.getColumnIndex(COLUMN_ORDER_ID);
            int usernameIndex = cursor.getColumnIndex(COLUMN_ORDER_USERNAME);
            int productIndex = cursor.getColumnIndex(COLUMN_ORDER_PRODUCT);
            int quantityIndex = cursor.getColumnIndex(COLUMN_ORDER_QUANTITY);
            int priceIndex = cursor.getColumnIndex(COLUMN_ORDER_PRICE);
            int statusIndex = cursor.getColumnIndex(COLUMN_ORDER_STATUS);

            if (orderIdIndex == -1 || usernameIndex == -1 || productIndex == -1 ||
                    quantityIndex == -1 || priceIndex == -1 || statusIndex == -1) {
                Log.e("DatabaseError", "One or more columns not found in the query result!");
                cursor.close();
                return orders;
            }

            do {
                int orderId = cursor.getInt(orderIdIndex);
                String username = cursor.getString(usernameIndex);
                String product = cursor.getString(productIndex);
                int quantity = cursor.getInt(quantityIndex);
                double price = cursor.getDouble(priceIndex);
                String status = cursor.getString(statusIndex);

                orders.add(new Order(orderId, username, product, quantity, price, status));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.e("DatabaseError", "No data returned for orders.");
        }

        return orders;
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, newStatus);

        int rowsUpdated = db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
        return rowsUpdated > 0;
    }

    // Retrieve orders for a specific user
    public List<Order> getUserOrders(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> orders = new ArrayList<>();

        if (username == null || username.isEmpty()) {
            Log.e("DatabaseHelper", "Invalid username for fetching orders.");
            return orders; // Return empty list if username is invalid
        }

        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                String product = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_QUANTITY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRICE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS));

                orders.add(new Order(orderId, username, product, quantity, price, status));
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Log.e("DatabaseHelper", "No orders found for user: " + username);
        }

        return orders;
    }




}
