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
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.MessageResponse;
import com.example.bobfriend.utils.UserApiHelper;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final int REFRESH_INTERVAL = 5000; // 5초

    private RecyclerView recyclerMessages;
    private EditText etMessage;
    private Button btnSend;
    private MessageAdapter messageAdapter;
    private List<MessageResponse> messageList = new ArrayList<>();

    private int currentUserId;
    private int otherUserId;
    private int productId;
    private Handler refreshHandler;
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUserId = SharedPrefManager.getCurrentUserId(this);
        otherUserId = getIntent().getIntExtra("otherUserId", -1);
        productId = getIntent().getIntExtra("productId", 1); // 기본값 1

        if (otherUserId == -1) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
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
        messageAdapter = new MessageAdapter(messageList, currentUserId);
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
        UserApiHelper.sendMessage(productId, currentUserId, otherUserId, content, new UserApiHelper.MessageSendListener() {
            @Override
            public void onSuccess(MessageResponse message) {
                etMessage.setText("");
                loadMessages(); // 메시지 목록 새로고침
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ChatActivity.this, "메시지 전송 실패: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        UserApiHelper.loadMessages(productId, currentUserId, otherUserId, new UserApiHelper.MessagesLoadListener() {
            @Override
            public void onSuccess(List<MessageResponse> messages) {
                messageList.clear();
                messageList.addAll(messages);
                messageAdapter.updateMessages(messageList);

                if (!messageList.isEmpty()) {
                    recyclerMessages.smoothScrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ChatActivity.this, "메시지 로드 실패: " + error, Toast.LENGTH_SHORT).show();
            }
        });
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