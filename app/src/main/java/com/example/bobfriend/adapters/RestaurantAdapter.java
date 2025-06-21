package com.example.bobfriend.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.R;
import com.example.bobfriend.RestaurantDetailActivity;
import com.example.bobfriend.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private Context context;

    public RestaurantAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        holder.tvRestaurantName.setText(restaurant.getName());
        holder.tvCategory.setText(restaurant.getCategory());
        holder.ratingBar.setRating(restaurant.getRate());

        if (restaurant.getRate() > 0) {
            holder.tvRatingText.setText(String.format("평점 %.1f", restaurant.getRate()));
        } else {
            holder.tvRatingText.setText("아직 평점이 없어요");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra("restaurantId", restaurant.getId());
            intent.putExtra("restaurantName", restaurant.getName());
            intent.putExtra("restaurantCategory", restaurant.getCategory());
            intent.putExtra("restaurantAddress", restaurant.getAddress());
            intent.putExtra("restaurantPhone", restaurant.getPhone());
            intent.putExtra("restaurantRate", restaurant.getRate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurantName, tvCategory, tvRatingText;
        RatingBar ratingBar;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRatingText = itemView.findViewById(R.id.tvRatingText);
        }
    }
}
