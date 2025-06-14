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
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurants;
    private Context context;
    private DatabaseHelper dbHelper;

    public RestaurantAdapter(List<Restaurant> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.tvName.setText(restaurant.getName());
        holder.tvCategory.setText(restaurant.getCategory());
        holder.ratingBar.setIsIndicator(true); // 이 줄 추가

        // 데이터베이스에서 평균 별점 가져오기
        float avgRating = dbHelper.getAverageRating(restaurant.getId());

        if (avgRating > 0) {
            holder.ratingBar.setRating(avgRating);
            holder.tvRatingText.setText(String.format("%.1f점", avgRating));
        } else {
            holder.ratingBar.setRating(0f);
            holder.tvRatingText.setText("평점 없음");
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
        return restaurants.size();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvRatingText;
        RatingBar ratingBar;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRestaurantName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRatingText = itemView.findViewById(R.id.tvRatingText);
        }
    }
}