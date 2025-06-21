package com.example.bobfriend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobfriend.R;
import com.example.bobfriend.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    private Context context;

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.tvUsername.setText(review.getUsername());
        holder.ratingBar.setRating(review.getRating());
        holder.tvRating.setText(String.format("%.1f", review.getRating()));
        holder.tvComment.setText(review.getComment());
        holder.tvCreatedAt.setText(formatDate(review.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private String formatDate(String dateString) {
        if (dateString != null && dateString.length() >= 10) {
            return dateString.substring(0, 10);
        }
        return dateString;
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvRating, tvComment, tvCreatedAt;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
