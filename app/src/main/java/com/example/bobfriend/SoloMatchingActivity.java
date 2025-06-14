package com.example.bobfriend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.adapters.SoloPostAdapter;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.SoloPost;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SoloMatchingActivity extends AppCompatActivity {

    private RecyclerView recyclerSoloPosts;
    private EditText etAge, etDateTime;
    private RadioGroup rgGender, rgMealStyle;
    private Button btnCreatePost;
    private SoloPostAdapter adapter;
    private DatabaseHelper dbHelper;
    private int restaurantId;
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_matching);

        dbHelper = new DatabaseHelper(this);
        restaurantId = getIntent().getIntExtra("restaurantId", -1);
        restaurantName = getIntent().getStringExtra("restaurantName");

        initViews();
        setupClickListeners();
        loadSoloPosts();
    }

    private void initViews() {
        recyclerSoloPosts = findViewById(R.id.recyclerSoloPosts);
        etAge = findViewById(R.id.etAge);
        etDateTime = findViewById(R.id.etDateTime);
        rgGender = findViewById(R.id.rgGender);
        rgMealStyle = findViewById(R.id.rgMealStyle);
        btnCreatePost = findViewById(R.id.btnCreatePost);

        recyclerSoloPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnCreatePost.setOnClickListener(v -> createSoloPost());
    }

    private void createSoloPost() {
        String nickname = SharedPrefManager.getCurrentUserNickname(this);
        String ageStr = etAge.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();

        if (ageStr.isEmpty() || dateTime.isEmpty()) {
            Toast.makeText(this, "모든 정보를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        String gender = rgGender.getCheckedRadioButtonId() == R.id.rbMale ? "남성" : "여성";
        String mealStyle = rgMealStyle.getCheckedRadioButtonId() == R.id.rbQuiet ? "조용히 식사" : "대화하며 식사";

        SoloPost post = new SoloPost(nickname, age, gender, dateTime, mealStyle,
                restaurantId, restaurantName, getCurrentTime());

        long result = dbHelper.insertSoloPost(post);
        if (result > 0) {
            Toast.makeText(this, "모집글 등록 완료!", Toast.LENGTH_SHORT).show();
            loadSoloPosts();
            clearInputs();
        } else {
            Toast.makeText(this, "등록 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSoloPosts() {
        List<SoloPost> posts = dbHelper.getSoloPostsByRestaurant(restaurantId);
        adapter = new SoloPostAdapter(posts, this);
        recyclerSoloPosts.setAdapter(adapter);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void clearInputs() {
        etAge.setText("");
        etDateTime.setText("");
        rgGender.clearCheck();
        rgMealStyle.clearCheck();
    }
}