package com.example.bobfriend.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.R;
import com.example.bobfriend.models.MessageResponse;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageResponse> messageResponseList;
    private int currentUserId = -1;

    public MessageAdapter(List<MessageResponse> messageResponseList, int currentUserId) {
        this.messageResponseList = messageResponseList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageResponse message = messageResponseList.get(position);

        if (message.getSender_id() == currentUserId) {
            holder.llMyMessage.setVisibility(View.VISIBLE);
            holder.llOtherMessage.setVisibility(View.GONE);

            holder.tvMyMessage.setText(message.getContent());
            holder.tvMyMessageTime.setText(formatTimestamp(message.getCreated_at()));
        } else {
            holder.llMyMessage.setVisibility(View.GONE);
            holder.llOtherMessage.setVisibility(View.VISIBLE);

            holder.tvOtherMessage.setText(message.getContent());
            holder.tvOtherMessageTime.setText(formatTimestamp(message.getCreated_at()));
            holder.tvOtherUsername.setText(message.getSender_username());
        }
    }

    @Override
    public int getItemCount() {
        return messageResponseList != null ? messageResponseList.size() : 0;
    }

    private String formatTimestamp(String timestamp) {
        if (timestamp != null && timestamp.length() >= 16) {
            return timestamp.substring(11, 16);
        }
        return timestamp;
    }

    public void updateMessages(List<MessageResponse> newMessages) {
        this.messageResponseList = newMessages;
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMyMessage, llOtherMessage;
        TextView tvMyMessage, tvMyMessageTime;
        TextView tvOtherMessage, tvOtherMessageTime, tvOtherUsername;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            llMyMessage = itemView.findViewById(R.id.llMyMessage);
            llOtherMessage = itemView.findViewById(R.id.llOtherMessage);

            tvMyMessage = itemView.findViewById(R.id.tvMyMessage);
            tvMyMessageTime = itemView.findViewById(R.id.tvMyMessageTime);

            tvOtherMessage = itemView.findViewById(R.id.tvOtherMessage);
            tvOtherMessageTime = itemView.findViewById(R.id.tvOtherMessageTime);
            tvOtherUsername = itemView.findViewById(R.id.tvOtherUsername);
        }
    }
}