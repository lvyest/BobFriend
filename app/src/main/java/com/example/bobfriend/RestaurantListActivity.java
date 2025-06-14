package com.example.bobfriend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.adapters.RestaurantAdapter;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.models.Restaurant;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView recyclerRestaurants;
    private RestaurantAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadRestaurants();
    }

    private void initViews() {
        recyclerRestaurants = findViewById(R.id.recyclerRestaurants);
        recyclerRestaurants.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadRestaurants() {
        List<Restaurant> restaurants = dbHelper.getAllRestaurants();

        // 더미 데이터 추가 (첫 실행시)
        if (restaurants.isEmpty()) {
            addDummyRestaurants();
            restaurants = dbHelper.getAllRestaurants();
        }

        adapter = new RestaurantAdapter(restaurants, this);
        recyclerRestaurants.setAdapter(adapter);
    }

    private void addDummyRestaurants() {
        dbHelper.insertRestaurant(new Restaurant("맛있는 김치찌개", "한식", "37.5665", "126.9780", 4.5f));
        dbHelper.insertRestaurant(new Restaurant("돈까스 천국", "일식", "37.5660", "126.9775", 4.2f));
        dbHelper.insertRestaurant(new Restaurant("피자 스테이션", "양식", "37.5670", "126.9785", 4.0f));
        dbHelper.insertRestaurant(new Restaurant("중화루", "중식", "37.5655", "126.9770", 4.3f));
    }
}