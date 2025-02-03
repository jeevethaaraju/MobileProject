package com.example.mobileproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private boolean isAdmin;
    private OnItemClickListener onItemClickListener;

    public OrderAdapter(List<Order> orders, boolean isAdmin,OnItemClickListener onItemClickListener) {
        this.orders = orders;
        this.isAdmin = isAdmin;
        this.onItemClickListener = onItemClickListener;
    }




    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.usernameTextView.setText("Username: " + order.getUsername());
        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.productTextView.setText("Product: " + order.getProduct());
        holder.quantityTextView.setText("Quantity: " + order.getQuantity());
        holder.priceTextView.setText("Price: $" + order.getPrice());
        holder.statusTextView.setText("Status: " + order.getStatus());

        if (isAdmin) {
            // If admin, show update button
            holder.updateStatusButton.setVisibility(View.VISIBLE);
            holder.updateStatusButton.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(order);
                }
            });
        } else {
            // If user, hide update button
            holder.updateStatusButton.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView,orderIdTextView, productTextView, quantityTextView, priceTextView, statusTextView;
        Button updateStatusButton;

        public OrderViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            productTextView = itemView.findViewById(R.id.productTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            updateStatusButton = itemView.findViewById(R.id.updateStatusButton);
        }
    }

    // Interface to handle status update click events
    // Interface to handle order clicks
    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

}
