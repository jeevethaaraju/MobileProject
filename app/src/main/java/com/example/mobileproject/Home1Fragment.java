package com.example.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Home1Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home1, container, false);

        // Initialize CartManager with the current context
        CartManager cartManager = CartManager.getInstance(requireContext());

        // Set up phone category click listener
        LinearLayout phoneCategory = view.findViewById(R.id.phoneCategory);
        phoneCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add Phone to Cart
                Product phone = new Product("Phone", 800.00, R.drawable.ic_phone);
                cartManager.addToCart(phone);
                Toast.makeText(getActivity(), "Phone added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
