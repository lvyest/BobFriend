package com.example.bobfriend.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.R;
import com.example.bobfriend.models.SoloPost;

import java.util.List;

public class SoloPostAdapter extends RecyclerView.Adapter<SoloPostAdapter.SoloPostViewHolder> {

    private List<SoloPost> soloPosts;

    public SoloPostAdapter(List<SoloPost> soloPosts) {
        this.soloPosts = soloPosts;
    }

    @NonNull
    @Override
    public SoloPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_solo_post, parent, false);
        return new SoloPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoloPostViewHolder holder, int position) {
        SoloPost post = soloPosts.get(position);

        holder.tvNickname.setText(post.getNickname());
        holder.tvAge.setText(post.getAge() + "세");
        holder.tvGender.setText(post.getGender());
        holder.tvCreatedAt.setText(post.getCreatedAt());
        holder.tvDateTime.setText(post.getDateTime());
        holder.tvMealStyle.setText(post.getMealStyle());

        // 버튼 클릭 리스너 (필요에 따라 추가 가능)
        holder.btnJoin.setOnClickListener(v -> {
            // 합류하기 클릭 이벤트 처리
        });

        holder.btnChat.setOnClickListener(v -> {
            // 채팅하기 클릭 이벤트 처리
        });
    }

    @Override
    public int getItemCount() {
        return soloPosts.size();
    }

    static class SoloPostViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname, tvAge, tvGender, tvCreatedAt, tvDateTime, tvMealStyle;
        Button btnJoin, btnChat;
        ImageView ivProfile; // 현재는 고정 이미지 사용 중

        public SoloPostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvMealStyle = itemView.findViewById(R.id.tvMealStyle);
            btnJoin = itemView.findViewById(R.id.btnJoin);
            btnChat = itemView.findViewById(R.id.btnChat);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }
    }
}
