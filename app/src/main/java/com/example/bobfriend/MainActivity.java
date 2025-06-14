package com.example.bobfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.bobfriend.database.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private CardView cvRestaurantList, cvSoloMatching, cvMyPage, cvChat;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인 체크
        if (!SharedPrefManager.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadUserInfo();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        cvRestaurantList = findViewById(R.id.cvRestaurantList);
        cvSoloMatching = findViewById(R.id.cvSoloMatching);
        cvMyPage = findViewById(R.id.cvMyPage);
        cvChat = findViewById(R.id.cvChat);
        ivProfile = findViewById(R.id.ivProfile);
    }

    private void setupClickListeners() {
        cvRestaurantList.setOnClickListener(v ->
                startActivity(new Intent(this, RestaurantListActivity.class)));

        cvSoloMatching.setOnClickListener(v ->
                startActivity(new Intent(this, SoloMatchingActivity.class)));

        cvMyPage.setOnClickListener(v ->
                startActivity(new Intent(this, MyPageActivity.class)));

        cvChat.setOnClickListener(v ->
                startActivity(new Intent(this, ChatActivity.class)));
    }

    private void loadUserInfo() {
        String nickname = SharedPrefManager.getCurrentUserNickname(this);
        tvWelcome.setText(nickname + "님, 안녕하세요!");
    }
}