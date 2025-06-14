package com.example.bobfriend;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.adapters.SoloPostAdapter;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.models.SoloPost;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SoloMatchingActivity extends AppCompatActivity {

    private EditText etAge, etDateTime;
    private RadioGroup rgGender, rgMealStyle;
    private Button btnCreatePost;
    private RecyclerView recyclerSoloPosts;
    private SoloPostAdapter adapter;
    private ArrayList<SoloPost> soloPosts;
    private DatabaseHelper dbHelper;
    private String restaurantId, restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_matching);

        int restaurantId = getIntent().getIntExtra("restaurantId", -1);
        restaurantName = getIntent().getStringExtra("restaurantName");

        // ✅ Null 체크
        if (restaurantId == -1 || restaurantName == null) {
            Toast.makeText(this, "식당 정보가 누락되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);

        initViews();
        loadSoloPosts();
        setupClickListeners();
    }

    private void initViews() {
        etAge = findViewById(R.id.etAge);
        etDateTime = findViewById(R.id.etDateTime);
        rgGender = findViewById(R.id.rgGender);
        rgMealStyle = findViewById(R.id.rgMealStyle);
        btnCreatePost = findViewById(R.id.btnCreatePost);
        recyclerSoloPosts = findViewById(R.id.recyclerSoloPosts);
        recyclerSoloPosts.setLayoutManager(new LinearLayoutManager(this));
        soloPosts = new ArrayList<>();
        adapter = new SoloPostAdapter(soloPosts);
        recyclerSoloPosts.setAdapter(adapter);
    }

    private void loadSoloPosts() {
        soloPosts.clear();
        restaurantId = String.valueOf(getIntent().getIntExtra("restaurantId", -1));
        soloPosts.addAll(dbHelper.getSoloPostsByRestaurant(restaurantId));
        adapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        btnCreatePost.setOnClickListener(v -> {
            String ageStr = etAge.getText().toString().trim();
            String dateTime = etDateTime.getText().toString().trim();

            if (TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(dateTime) ||
                    rgGender.getCheckedRadioButtonId() == -1 || rgMealStyle.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr);
            String gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
            String mealStyle = ((RadioButton) findViewById(rgMealStyle.getCheckedRadioButtonId())).getText().toString();

            String nickname = "익명"; // SharedPreferences 등에서 가져와도 됩니다.

            SoloPost post = new SoloPost(nickname, age, gender, dateTime, mealStyle,
                    Integer.parseInt(restaurantId), restaurantName, getCurrentTime());

            dbHelper.insertSoloPost(post);
            Toast.makeText(this, "모집글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

            etAge.setText("");
            etDateTime.setText("");
            rgGender.clearCheck();
            rgMealStyle.clearCheck();

            loadSoloPosts();
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
