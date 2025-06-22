package com.example.bobfriend.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.R;
import com.example.bobfriend.models.ChatListItem;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private List<ChatListItem> chatList;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(ChatListItem chatItem);
    }

    public ChatListAdapter(List<ChatListItem> chatList, OnChatClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_list, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ChatListItem chatItem = chatList.get(position);

        holder.tvUsername.setText(chatItem.getOtherUsername());
        holder.tvLastMessage.setText(chatItem.getLastMessage() != null ? chatItem.getLastMessage() : "대화를 시작해보세요!");
        holder.tvTime.setText(formatTime(chatItem.getLastMessageTime()));

        // 읽지 않은 메시지 수 표시
        if (chatItem.getUnreadCount() > 0) {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(String.valueOf(chatItem.getUnreadCount()));
        } else {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(chatItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList != null ? chatList.size() : 0;
    }

    private String formatTime(String timestamp) {
        if (timestamp != null && timestamp.length() >= 16) {
            return timestamp.substring(11, 16); // HH:MM 형태로 표시
        }
        return "";
    }

    static class ChatListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvUsername, tvLastMessage, tvTime, tvUnreadCount;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvUnreadCount = itemView.findViewById(R.id.tvUnreadCount);
        }
    }
}