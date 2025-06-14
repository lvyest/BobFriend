package com.example.bobfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bobfriend.database.SharedPrefManager;

public class MyPageActivity extends AppCompatActivity {

    private TextView tvNickname, tvReservationCount, tvReviewCount;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        initViews();
        loadUserInfo();
        setupClickListeners();
    }

    private void initViews() {
        tvNickname = findViewById(R.id.tvNickname);
        tvReservationCount = findViewById(R.id.tvReservationCount);
        tvReviewCount = findViewById(R.id.tvReviewCount);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void loadUserInfo() {
        String nickname = SharedPrefManager.getCurrentUserNickname(this);
        tvNickname.setText("닉네임: " + nickname);
        tvReservationCount.setText("예약 내역: 3건");
        tvReviewCount.setText("리뷰 작성: 2건");
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> {
            SharedPrefManager.clearUser(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}