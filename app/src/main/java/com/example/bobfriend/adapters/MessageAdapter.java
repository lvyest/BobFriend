package com.example.bobfriend.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.R;
import com.example.bobfriend.models.Message;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private String currentUser;

    public MessageAdapter(List<Message> messageList, String currentUser) {
        this.messageList = messageList;
        this.currentUser = currentUser;
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
        Message message = messageList.get(position);
        if (message.getSender().equals(currentUser)) {
            holder.llMyMessage.setVisibility(View.VISIBLE);
            holder.llOtherMessage.setVisibility(View.GONE);

            holder.tvMyMessage.setText(message.getContent());
            holder.tvMyMessageTime.setText(message.getTimestamp());
        } else {
            holder.llMyMessage.setVisibility(View.GONE);
            holder.llOtherMessage.setVisibility(View.VISIBLE);

            holder.tvOtherMessage.setText(message.getContent());
            holder.tvOtherMessageTime.setText(message.getTimestamp());
            holder.tvOtherNickname.setText(message.getSender());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMyMessage, llOtherMessage;
        TextView tvMyMessage, tvMyMessageTime;
        TextView tvOtherMessage, tvOtherMessageTime, tvOtherNickname;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            llMyMessage = itemView.findViewById(R.id.llMyMessage);
            llOtherMessage = itemView.findViewById(R.id.llOtherMessage);

            tvMyMessage = itemView.findViewById(R.id.tvMyMessage);
            tvMyMessageTime = itemView.findViewById(R.id.tvMyMessageTime);

            tvOtherMessage = itemView.findViewById(R.id.tvOtherMessage);
            tvOtherMessageTime = itemView.findViewById(R.id.tvOtherMessageTime);
            tvOtherNickname = itemView.findViewById(R.id.tvOtherNickname);
        }
    }
}
