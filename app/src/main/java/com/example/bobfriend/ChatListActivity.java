package com.example.bobfriend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.adapters.ChatListAdapter;
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.ChatListItem;
import com.example.bobfriend.models.MessageResponse;
import com.example.bobfriend.utils.UserApiHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivity extends AppCompatActivity implements ChatListAdapter.OnChatClickListener {

    private static final String TAG = "ChatListActivity";

    private RecyclerView recyclerChatList;
    private ChatListAdapter chatListAdapter;
    private List<ChatListItem> chatList = new ArrayList<>();
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        currentUserId = SharedPrefManager.getCurrentUserId(this);

        initViews();
        setupRecyclerView();
        loadChatList();
    }

    private void initViews() {
        recyclerChatList = findViewById(R.id.recyclerChatList);
    }

    private void setupRecyclerView() {
        recyclerChatList.setLayoutManager(new LinearLayoutManager(this));
        chatListAdapter = new ChatListAdapter(chatList, this);
        recyclerChatList.setAdapter(chatListAdapter);
    }

    private void loadChatList() {
        UserApiHelper.getUserConversations(currentUserId, new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MessageResponse> messageList = response.body();
                    Set<String> addedKeys = new HashSet<>();
                    chatList.clear();

                    for (MessageResponse msg : messageList) {
                        int otherUserId = (msg.getSender_id() == currentUserId) ? msg.getReceiver_id() : msg.getSender_id();
                        String otherUsername = msg.getSender_id() == currentUserId ? msg.getReceiver_username() : msg.getSender_username();

                        String uniqueKey = msg.getProduct_id() + "_" + otherUserId;
                        if (addedKeys.contains(uniqueKey)) continue;
                        addedKeys.add(uniqueKey);

                        ChatListItem item = new ChatListItem(
                                msg.getProduct_id(),
                                otherUserId,
                                otherUsername,
                                msg.getContent(),             // 마지막 메시지
                                msg.getCreated_at(),          // 마지막 시간
                                0                             // 읽지 않은 메시지 수 (기본 0으로 설정)
                        );

                        chatList.add(item);
                    }

                    chatListAdapter.notifyDataSetChanged();

                    if (chatList.isEmpty()) {
                        Toast.makeText(ChatListActivity.this, "아직 채팅 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChatListActivity.this, "채팅 목록을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Log.e(TAG, "채팅 목록 로드 실패", t);
                Toast.makeText(ChatListActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onChatClick(ChatListItem chatItem) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("otherUserId", chatItem.getOtherUserId());
        intent.putExtra("productId", chatItem.getProductId());
        intent.putExtra("otherUsername", chatItem.getOtherUsername());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChatList(); // 화면이 다시 보일 때마다 채팅 목록 새로고침
    }
}
