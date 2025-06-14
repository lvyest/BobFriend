package com.example.bobfriend.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.R;
import com.example.bobfriend.models.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnQuantityChangeListener listener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public OrderAdapter(List<Order> orders, OnQuantityChangeListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvMenuName.setText(order.getMenuName());
        holder.tvPrice.setText(order.getPrice() + "원");
        holder.tvQuantity.setText(String.valueOf(order.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            order.setQuantity(order.getQuantity() + 1);
            holder.tvQuantity.setText(String.valueOf(order.getQuantity()));
            listener.onQuantityChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (order.getQuantity() > 0) {
                order.setQuantity(order.getQuantity() - 1);
                holder.tvQuantity.setText(String.valueOf(order.getQuantity()));
                listener.onQuantityChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuName, tvPrice, tvQuantity;
        Button btnPlus, btnMinus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.tvMenuName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}