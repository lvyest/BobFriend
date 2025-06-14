package com.example.bobfriend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.models.Review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteReviewActivity extends AppCompatActivity {

    private TextView tvRestaurantName;
    private RatingBar ratingBar;
    private EditText etComment;
    private Button btnSubmitReview, btnCancel;

    private DatabaseHelper dbHelper;
    private String restaurantId;
    private String restaurantName;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadData();
        setupClickListeners();
    }

    private void initViews() {
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        ratingBar = findViewById(R.id.ratingBar);
        etComment = findViewById(R.id.etComment);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadData() {
        // Intent에서 데이터 받기
        restaurantId = getIntent().getStringExtra("restaurantId");
        restaurantName = getIntent().getStringExtra("restaurantName");

        // 현재 로그인된 사용자 정보 가져오기 (SharedPreferences에서)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentUsername = prefs.getString("current_user", "익명사용자");

        // UI 설정
        tvRestaurantName.setText(restaurantName);

        // 이미 리뷰를 작성했는지 확인
        if (dbHelper.hasUserReviewed(currentUsername, restaurantId)) {
            Toast.makeText(this, "이미 이 식당에 리뷰를 작성하셨습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        btnSubmitReview.setOnClickListener(v -> submitReview());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void submitReview() {
        float rating = ratingBar.getRating();
        String comment = etComment.getText().toString().trim();

        // 유효성 검사
        if (rating == 0) {
            Toast.makeText(this, "별점을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (comment.isEmpty()) {
            Toast.makeText(this, "리뷰 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 현재 시간
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // 리뷰 객체 생성
        Review review = new Review(currentUsername, restaurantId, restaurantName,
                rating, comment, currentTime);

        // 데이터베이스에 저장
        long result = dbHelper.insertReview(review);

        if (result != -1) {
            Toast.makeText(this, "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show();

            // 결과를 이전 액티비티로 전달
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "리뷰 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}