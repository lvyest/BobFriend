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
        setupClickListeners();
    }

    private void initViews() {
        tvNickname = findViewById(R.id.tvNickname);

        btnLogout = findViewById(R.id.btnLogout);
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