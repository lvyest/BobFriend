package com.example.bobfriend;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.adapters.ReviewAdapter;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.models.Restaurant;
import com.example.bobfriend.models.Review;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView tvRestaurantName, tvCategory, tvLocation;
    private RatingBar ratingBar;
    private String restaurantId;
    private Button btnSoloMatching, btnOrder, btnWriteReview;
    private DatabaseHelper dbHelper;
    private Restaurant restaurant;

    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

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
        ratingBar = findViewById(R.id.ratingBar);             // 평균 별점 표시용
        btnSoloMatching = findViewById(R.id.btnSoloMatching);
        btnOrder = findViewById(R.id.btnOrder);
        btnWriteReview = findViewById(R.id.btnWriteReview);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList, this);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
    }

    private void loadRestaurantData() {
        // ✅ Intent에서 전달받은 데이터로 Restaurant 객체 생성
        restaurantId = getIntent().getStringExtra("restaurantId");
        String restaurantName = getIntent().getStringExtra("restaurantName");
        String restaurantCategory = getIntent().getStringExtra("restaurantCategory");
        String restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        String restaurantPhone = getIntent().getStringExtra("restaurantPhone");
        float restaurantRate = getIntent().getFloatExtra("restaurantRate", 0.0f);

        if (restaurantId != null && restaurantName != null) {
            restaurant = new Restaurant(restaurantId, restaurantName, restaurantCategory,
                    restaurantAddress, restaurantPhone, restaurantRate);

            // ✅ UI에 데이터 설정
            tvRestaurantName.setText(restaurant.getName());
            tvCategory.setText(restaurant.getCategory());
            tvLocation.setText(restaurant.getAddress() != null ? restaurant.getAddress() : "주소 정보 없음");
            ratingBar.setRating(restaurant.getRate());
        }
        loadReviews();

    }

    private void setupClickListeners() {
        btnSoloMatching.setOnClickListener(v -> {
            if (restaurant != null) {
                Intent intent = new Intent(this, SoloMatchingActivity.class);
                intent.putExtra("restaurantId", Integer.parseInt(restaurantId.split("_")[1])); // ID에서 숫자 부분 추출
                intent.putExtra("restaurantName", restaurant.getName());
                startActivity(intent);
            }
        });

        btnOrder.setOnClickListener(v -> {
            if (restaurant != null) {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            }
        });

        btnWriteReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, WriteReviewActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            intent.putExtra("restaurantName", restaurant.getName());
            startActivity(intent);
        });
    }

    private void loadReviews() {
        if (restaurantId != null) {
            reviewList.clear();
            reviewList.addAll(dbHelper.getReviewsByRestaurant(restaurantId));
            reviewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadReviews();
    }
}