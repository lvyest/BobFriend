package com.example.bobfriend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.adapters.MessageAdapter;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.Message;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private static final int REFRESH_INTERVAL = 5000; // 5초

    private RecyclerView recyclerMessages;
    private EditText etMessage;
    private Button btnSend;
    private MessageAdapter messageAdapter;
    private DatabaseHelper dbHelper;
    private List<Message> messageList = new ArrayList<>();

    private String currentUser;
    private String otherUser;
    private Handler refreshHandler;
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbHelper = new DatabaseHelper(this);
        currentUser = SharedPrefManager.getCurrentUserNickname(this);
        otherUser = getIntent().getStringExtra("otherUser");

        if (otherUser == null) {
            otherUser = "테스트유저"; // 임시 상대방
        }

        initViews();
        setupMessageRecyclerView();
        setupSendButton();
        loadMessages();
        setupMessageRefresh();
    }

    private void initViews() {
        recyclerMessages = findViewById(R.id.recyclerMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
    }

    private void setupMessageRecyclerView() {
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList, currentUser);
        recyclerMessages.setAdapter(messageAdapter);
    }

    private void setupSendButton() {
        btnSend.setOnClickListener(v -> {
            String content = etMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                sendMessage(content);
            } else {
                Toast.makeText(this, "메시지를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String content) {
        Message message = new Message(currentUser, otherUser, content, getCurrentTime());
        long result = dbHelper.insertMessage(message);

        if (result > 0) {
            messageList.add(message);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerMessages.smoothScrollToPosition(messageList.size() - 1);
            etMessage.setText("");
        } else {
            Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMessages() {
        messageList.clear();
        messageList.addAll(dbHelper.getMessagesBetweenUsers(currentUser, otherUser));
        messageAdapter.notifyDataSetChanged();

        if (!messageList.isEmpty()) {
            recyclerMessages.smoothScrollToPosition(messageList.size() - 1);
        }
    }

    private void setupMessageRefresh() {
        refreshHandler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                loadMessages();
                refreshHandler.postDelayed(this, REFRESH_INTERVAL);
            }
        };
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshHandler != null && refreshRunnable != null) {
            refreshHandler.postDelayed(refreshRunnable, REFRESH_INTERVAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (refreshHandler != null && refreshRunnable != null) {
            refreshHandler.removeCallbacks(refreshRunnable);
        }
    }
}