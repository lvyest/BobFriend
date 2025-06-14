package com.example.bobfriend.models;

public class Restaurant {
    private int id;
    private String name;
    private String category;
    private String latitude;
    private String longitude;
    private float rating;

    public Restaurant() {}

    public Restaurant(String name, String category, String latitude, String longitude, float rating) {
        this.name = name;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}