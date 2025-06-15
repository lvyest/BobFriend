package com.example.bobfriend.utils;

import android.content.Context;
import android.util.Log;

import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.LoginRequest;
import com.example.bobfriend.models.MessageRequest;
import com.example.bobfriend.models.MessageResponse;
import com.example.bobfriend.models.User;
import com.example.bobfriend.models.UserRequest;
import com.example.bobfriend.network.UserRetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApiHelper {
    private static final String TAG = "UserApiHelper";

    public interface UserRegistrationListener {
        void onSuccess(User user);
        void onError(String error);
    }

    public interface UserLoginListener {
        void onSuccess(User user);
        void onError(String error);
    }

    public interface MessageSendListener {
        void onSuccess(MessageResponse message);
        void onError(String error);
    }

    public interface MessagesLoadListener {
        void onSuccess(List<MessageResponse> messages);
        void onError(String error);
    }

    public static void registerUser(String username, String password, String name, UserRegistrationListener listener) {
        UserRequest request = new UserRequest(username, password, name);

        UserRetrofitClient.getApiService().registerUser(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("서버 오류가 발생했습니다.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "회원가입 실패", t);
                listener.onError("네트워크 오류가 발생했습니다.");
            }
        });
    }

    public static void loginUser(String username, String password, UserLoginListener listener) {
        LoginRequest request = new LoginRequest(username, password);

        UserRetrofitClient.getApiService().loginUser(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("서버 오류가 발생했습니다.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "로그인 실패", t);
                listener.onError("네트워크 오류가 발생했습니다.");
            }
        });
    }

    public static void sendMessage(int productId, int senderId, int receiverId, String content, MessageSendListener listener) {
        MessageRequest request = new MessageRequest(productId, senderId, receiverId, content);

        UserRetrofitClient.getApiService().sendMessage(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("메시지 전송에 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e(TAG, "메시지 전송 실패", t);
                listener.onError("네트워크 오류가 발생했습니다.");
            }
        });
    }

    public static void loadMessages(int productId, int user1Id, int user2Id, MessagesLoadListener listener) {
        UserRetrofitClient.getApiService().getMessagesBetweenUsers(productId, user1Id, user2Id).enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("메시지 로드에 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Log.e(TAG, "메시지 로드 실패", t);
                listener.onError("네트워크 오류가 발생했습니다.");
            }
        });
    }

    public static void getUserConversations(int userId, Callback<String> callback) {
        UserRetrofitClient.getApiService().getUserConversations(userId).enqueue(callback);
    }
}
