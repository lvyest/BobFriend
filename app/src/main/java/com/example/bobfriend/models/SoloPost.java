package com.example.bobfriend.models;

public class SoloPost {
    private int id;
    private String nickname;
    private int age;
    private String gender;
    private String dateTime;
    private String mealStyle;
    private int restaurantId;
    private String restaurantName;
    private String createdAt;

    public SoloPost() {}

    public SoloPost(String nickname, int age, String gender, String dateTime,
                    String mealStyle, int restaurantId, String restaurantName, String createdAt) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.dateTime = dateTime;
        this.mealStyle = mealStyle;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getMealStyle() { return mealStyle; }
    public void setMealStyle(String mealStyle) { this.mealStyle = mealStyle; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}