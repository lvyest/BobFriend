package com.example.bobfriend.network;

import com.example.bobfriend.models.RestaurantApiResponse;
import com.example.bobfriend.models.LoginRequest;
import com.example.bobfriend.models.UserRequest;
import com.example.bobfriend.models.User;
import com.example.bobfriend.models.MessageRequest;
import com.example.bobfriend.models.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // 경기도 공공데이터 식당 목록 API
    @GET("PlaceThatDoATasteyFoodSt")
    Call<RestaurantApiResponse> getRestaurants(
            @Query("KEY") String apiKey,
            @Query("Type") String type,
            @Query("pIndex") int pageIndex,
            @Query("pSize") int pageSize
    );

    // 사용자 회원가입
    @POST("users/register")
    Call<User> registerUser(@Body UserRequest userRequest);

    //  사용자 로그인
    @POST("users/login")
    Call<User> loginUser(@Body LoginRequest loginRequest);

    // 메시지 전송
    @POST("messages")
    Call<MessageResponse> sendMessage(@Body MessageRequest messageRequest);

    // 사용자 간 메시지 가져오기
    @GET("messages/{product_id}/{user1_id}/{user2_id}")
    Call<List<MessageResponse>> getMessagesBetweenUsers(
            @Path("product_id") int productId,
            @Path("user1_id") int user1Id,
            @Path("user2_id") int user2Id
    );

    //대화 목록 가져오기
    @GET("messages/conversations/{user_id}")
    Call<String> getUserConversations(@Path("user_id") int userId);
}
