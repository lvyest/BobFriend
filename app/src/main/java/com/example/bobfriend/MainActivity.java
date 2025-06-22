package com.example.bobfriend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.adapters.RestaurantAdapter;
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.Restaurant;
import com.example.bobfriend.models.RestaurantApiResponse;
import com.example.bobfriend.network.ApiService;
import com.example.bobfriend.network.RestaurantRetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String API_KEY = "de47c2e4aee94af599eb86e6be710eec";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private RecyclerView recyclerViewRestaurants;
    private RestaurantAdapter adapter;
    private ArrayList<Restaurant> restaurantList;

    private TextView tvWelcome, tvLocation;
    private ImageView ivProfile;
    private CardView cvChat, cvMyPage;

    private double currentLat = 37.5665;
    private double currentLon = 126.9780;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!SharedPrefManager.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // View 초기화
        tvWelcome = findViewById(R.id.tvWelcome);
        tvLocation = findViewById(R.id.tvLocation);
        ivProfile = findViewById(R.id.ivProfile);
        cvChat = findViewById(R.id.cvChat);
        cvMyPage = findViewById(R.id.cvMyPage);

        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList, this);
        recyclerViewRestaurants.setAdapter(adapter);

        cvChat.setOnClickListener(v -> startActivity(new Intent(this, ChatListActivity.class)));
        cvMyPage.setOnClickListener(v -> startActivity(new Intent(this, MyPageActivity.class)));

        loadUserInfo();
        getCurrentLocation();
        fetchRestaurants();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }

    private void loadUserInfo() {
        String nickname = SharedPrefManager.getCurrentUsername(this);
        tvWelcome.setText(nickname + "님, 안녕하세요!");
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            currentLat = location.getLatitude();
            currentLon = location.getLongitude();
            updateLocationText(currentLat, currentLon);
        } else {
            tvLocation.setText("위치 정보를 가져올 수 없습니다.");
            Log.w(TAG, "location == null");
        }
    }

    private void updateLocationText(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                tvLocation.setText("현재 위치: " + address);
            } else {
                tvLocation.setText("주소 정보를 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("주소 변환 실패");
        }
    }

    private void fetchRestaurants() {
        ApiService apiService = RestaurantRetrofitClient.getApiService();
        Call<RestaurantApiResponse> call = apiService.getRestaurants(API_KEY, "json", 1, 100);

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

                                    if (r.latitude != null && r.longitude != null &&
                                            !r.latitude.equals("0") && !r.longitude.equals("0")) {
                                        try {
                                            double lat = Double.parseDouble(r.latitude);
                                            double lon = Double.parseDouble(r.longitude);
                                            double dist = calculateDistance(currentLat, currentLon, lat, lon);
                                            restaurant.setDistance(dist);
                                        } catch (NumberFormatException ignored) {}
                                    }

                                    restaurantList.add(restaurant);
                                }
                            }

                            Collections.sort(restaurantList, Comparator.comparingDouble(Restaurant::getDistance));
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

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] result = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, result);
        return result[0] / 1000.0; // km
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }
}
