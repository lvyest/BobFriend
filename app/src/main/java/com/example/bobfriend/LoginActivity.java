package com.example.bobfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etNickname;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        etNickname = findViewById(R.id.etNickname);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> register());
    }

    private void login() {
        String nickname = etNickname.getText().toString().trim();
        if (nickname.isEmpty()) {
            Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = dbHelper.getUser(nickname);
        if (user != null) {
            SharedPrefManager.saveUser(this, user);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "존재하지 않는 사용자입니다", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        String nickname = etNickname.getText().toString().trim();
        if (nickname.isEmpty()) {
            Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.getUser(nickname) != null) {
            Toast.makeText(this, "이미 존재하는 닉네임입니다", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(nickname, "default_profile.png");
        long result = dbHelper.insertUser(newUser);
        if (result > 0) {
            SharedPrefManager.saveUser(this, newUser);
            Toast.makeText(this, "회원가입 완료!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
