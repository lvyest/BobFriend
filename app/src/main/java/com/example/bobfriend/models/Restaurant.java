package com.example.bobfriend.models;

import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("RESTRT_NM")
    private String name;

    @SerializedName("REPRSNT_FOOD_NM")
    private String category;

    @SerializedName("REFINE_ROADNM_ADDR")
    private String address;

    @SerializedName("REFINE_WGS84_LAT")
    private double latitude;

    @SerializedName("REFINE_WGS84_LOGT")
    private double longitude;

    private String id;
    private String phone;
    private float rate = 0.0f;
    private double distance = 0.0;

    // ✅ 문제 해결용 생성자 추가
    public Restaurant(String id, String name, String category, String address, String phone, float rate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.rate = rate;
    }


    public Restaurant() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
