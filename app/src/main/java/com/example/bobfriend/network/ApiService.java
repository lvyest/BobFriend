package com.example.bobfriend.network;

import com.example.bobfriend.models.RestaurantApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("PlaceThatDoATasteyFoodSt")
    Call<RestaurantApiResponse> getRestaurants(
        @Query("KEY") String apiKey,
        @Query("Type") String type,
        @Query("pIndex") int pageIndex,
        @Query("pSize") int pageSize
    );
}
