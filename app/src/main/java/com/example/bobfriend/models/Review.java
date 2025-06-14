package com.example.bobfriend.models;

public class Review {
    private int id;
    private String username;
    private String restaurantId;
    private String restaurantName;
    private float rating;
    private String comment;
    private String createdAt;

    public Review() {}

    public Review(String username, String restaurantId, String restaurantName,
                  float rating, String comment, String createdAt) {
        this.username = username;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}