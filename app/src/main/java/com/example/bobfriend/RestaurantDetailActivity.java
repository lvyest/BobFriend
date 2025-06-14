package com.example.bobfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.models.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView tvRestaurantName, tvCategory, tvLocation;
    private RatingBar ratingBar;
    private Button btnSoloMatching, btnOrder;
    private DatabaseHelper dbHelper;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadRestaurantData();
        setupClickListeners();
    }

    private void initViews() {
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvCategory = findViewById(R.id.tvCategory);
        tvLocation = findViewById(R.id.tvLocation);
        ratingBar = findViewById(R.id.ratingBar);
        btnSoloMatching = findViewById(R.id.btnSoloMatching);
        btnOrder = findViewById(R.id.btnOrder);
    }

    private void loadRestaurantData() {
        int restaurantId = getIntent().getIntExtra("restaurantId", -1);
        if (restaurantId != -1) {
            restaurant = dbHelper.getRestaurant(restaurantId);
            if (restaurant != null) {
                tvRestaurantName.setText(restaurant.getName());
                tvCategory.setText(restaurant.getCategory());
                tvLocation.setText("위치: " + restaurant.getLatitude() + ", " + restaurant.getLongitude());
                ratingBar.setRating(restaurant.getRating());
            }
        }
    }

    private void setupClickListeners() {
        btnSoloMatching.setOnClickListener(v -> {
            Intent intent = new Intent(this, SoloMatchingActivity.class);
            intent.putExtra("restaurantId", restaurant.getId());
            intent.putExtra("restaurantName", restaurant.getName());
            startActivity(intent);
        });

        btnOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra("restaurantId", restaurant.getId());
            startActivity(intent);
        });
    }
}