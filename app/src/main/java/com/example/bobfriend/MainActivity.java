package com.example.bobfriend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.adapters.RestaurantAdapter;
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.Restaurant;
import com.example.bobfriend.models.RestaurantApiResponse;
import com.example.bobfriend.network.ApiService;
import com.example.bobfriend.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRestaurants;
    private RestaurantAdapter adapter;
    private ArrayList<Restaurant> restaurantList;
    private static final String TAG = "MainActivity";
    private static final String API_KEY = "de47c2e4aee94af599eb86e6be710eec";

    private TextView tvWelcome;
    private ImageView ivProfile;
    private CardView cvChat, cvMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인 여부 확인
        if (!SharedPrefManager.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // View 초기화
        tvWelcome = findViewById(R.id.tvWelcome);
        ivProfile = findViewById(R.id.ivProfile);
        cvChat = findViewById(R.id.cvChat);
        cvMyPage = findViewById(R.id.cvMyPage);

        // RecyclerView 초기화
        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList, this);
        recyclerViewRestaurants.setAdapter(adapter);

        // 클릭 리스너 설정
        cvChat.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
        cvMyPage.setOnClickListener(v -> startActivity(new Intent(this, MyPageActivity.class)));

        // 사용자 정보 로드
        loadUserInfo();

        // API 호출
        fetchRestaurants();
    }

    private void loadUserInfo() {
        String nickname = SharedPrefManager.getCurrentUserNickname(this);
        tvWelcome.setText(nickname + "님, 안녕하세요!");
        // 이미지 설정은 생략 (프로필 이미지 URL 처리 필요 시 여기에 추가)
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
                if (response.isSuccessful() && response.body() != null) {
                    RestaurantApiResponse apiResponse = response.body();

                    if (apiResponse.responseList != null && !apiResponse.responseList.isEmpty()) {
                        RestaurantApiResponse.ResponseWrapper dataWrapper = null;

                        for (RestaurantApiResponse.ResponseWrapper wrapper : apiResponse.responseList) {
                            if (wrapper.rows != null && !wrapper.rows.isEmpty()) {
                                dataWrapper = wrapper;
                                break;
                            }
                        }

                        if (dataWrapper != null && dataWrapper.rows != null) {
                            List<RestaurantApiResponse.RestaurantRaw> rawList = dataWrapper.rows;
                            restaurantList.clear();
                            for (int i = 0; i < rawList.size(); i++) {
                                RestaurantApiResponse.RestaurantRaw r = rawList.get(i);
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
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "식당 데이터가 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "응답이 비어있습니다", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "API 응답 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API 호출 실패", t);
            }
        });
    }
}
