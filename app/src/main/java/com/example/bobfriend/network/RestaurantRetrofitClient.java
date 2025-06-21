package com.example.bobfriend.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://openapi.gg.go.kr/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // ✅ ApiService 인스턴스 반환용 메서드 추가
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
