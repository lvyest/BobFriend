package com.example.bobfriend.models;

public class Restaurant {
    private String id;
    private String name;
    private String category;
    private String address;
    private String phone;
    private float rate;

    public Restaurant(String id, String name, String category, String address, String phone, float rate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.rate = rate;
    }

    public String getId() {
        return id;
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
}
