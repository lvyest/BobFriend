package com.example.bobfriend;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.adapters.RestaurantAdapter;
import com.example.bobfriend.models.Restaurant;
import com.example.bobfriend.models.RestaurantApiResponse;
import com.example.bobfriend.network.ApiService;
import com.example.bobfriend.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRestaurants;
    private RestaurantAdapter adapter;
    private ArrayList<Restaurant> restaurantList;
    private static final String TAG = "RestaurantList";
    private static final String API_KEY = "de47c2e4aee94af599eb86e6be710eec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList, this);
        recyclerViewRestaurants.setAdapter(adapter);

        fetchRestaurants();
    }

    private void fetchRestaurants() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<RestaurantApiResponse> call = apiService.getRestaurants(
                API_KEY,
                "json",
                1,
                100
        );

        call.enqueue(new Callback<RestaurantApiResponse>() {
            @Override
            public void onResponse(Call<RestaurantApiResponse> call, Response<RestaurantApiResponse> response) {
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    RestaurantApiResponse apiResponse = response.body();

                    // 응답 구조 확인을 위한 로그
                    Log.d(TAG, "Response list size: " + (apiResponse.responseList != null ? apiResponse.responseList.size() : "null"));

                    if (apiResponse.responseList != null && !apiResponse.responseList.isEmpty()) {
                        // 첫 번째 인덱스는 head 정보, 두 번째 인덱스가 실제 데이터
                        RestaurantApiResponse.ResponseWrapper dataWrapper = null;

                        // head와 row 정보가 담긴 wrapper 찾기
                        for (RestaurantApiResponse.ResponseWrapper wrapper : apiResponse.responseList) {
                            if (wrapper.rows != null && !wrapper.rows.isEmpty()) {
                                dataWrapper = wrapper;
                                break;
                            }
                        }

                        if (dataWrapper != null && dataWrapper.rows != null) {
                            List<RestaurantApiResponse.RestaurantRaw> rawList = dataWrapper.rows;
                            Log.d(TAG, "Raw list size: " + rawList.size());

                            restaurantList.clear();
                            for (int i = 0; i < rawList.size(); i++) {
                                RestaurantApiResponse.RestaurantRaw r = rawList.get(i);

                                // null 체크 추가
                                if (r.name != null && r.category != null) {
                                    String id = (r.sigunCd != null ? r.sigunCd : "unknown") + "_" + i;
                                    Restaurant restaurant = new Restaurant(
                                            id,
                                            r.name,
                                            r.category,
                                            r.address != null ? r.address : "주소 정보 없음",
                                            r.phone != null ? r.phone : "전화번호 없음",
                                            0f
                                    );
                                    restaurantList.add(restaurant);

                                    // 로그로 데이터 확인
                                    Log.d(TAG, "Restaurant " + i + ": " + r.name + ", " + r.category);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Final restaurant list size: " + restaurantList.size());
                        } else {
                            Log.e(TAG, "No data wrapper found or rows is empty");
                            Toast.makeText(RestaurantListActivity.this, "데이터를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Response list is null or empty");
                        Toast.makeText(RestaurantListActivity.this, "응답 데이터가 비어있습니다", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "API response unsuccessful or body is null");
                    Toast.makeText(RestaurantListActivity.this, "API 응답 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantApiResponse> call, Throwable t) {
                Log.e(TAG, "API 호출 실패", t);
                Toast.makeText(RestaurantListActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}