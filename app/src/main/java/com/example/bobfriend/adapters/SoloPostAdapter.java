package com.example.bobfriend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.R;
import com.example.bobfriend.models.SoloPost;
import java.util.List;

public class SoloPostAdapter extends RecyclerView.Adapter<SoloPostAdapter.SoloPostViewHolder> {

    private List<SoloPost> soloPosts;
    private Context context;

    public SoloPostAdapter(List<SoloPost> soloPosts, Context context) {
        this.soloPosts = soloPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public SoloPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_solo_post, parent, false);
        return new SoloPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoloPostViewHolder holder, int position) {
        SoloPost post = soloPosts.get(position);

        holder.tvNickname.setText(post.getNickname());
        holder.tvAge.setText(post.getAge() + "세");
        holder.tvGender.setText(post.getGender());
        holder.tvDateTime.setText(post.getDateTime());
        holder.tvMealStyle.setText(post.getMealStyle());
        holder.tvCreatedAt.setText(post.getCreatedAt());

        // 버튼 클릭 이벤트 처리도 여기서 추가 가능 (예: holder.btnJoin, holder.btnChat)
    }

    @Override
    public int getItemCount() {
        return soloPosts.size();
    }

    static class SoloPostViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname, tvAge, tvGender, tvDateTime, tvMealStyle, tvCreatedAt;
        Button btnJoin, btnChat;

        public SoloPostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvMealStyle = itemView.findViewById(R.id.tvMealStyle);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);

            btnJoin = itemView.findViewById(R.id.btnJoin);
            btnChat = itemView.findViewById(R.id.btnChat);
        }
    }
}
