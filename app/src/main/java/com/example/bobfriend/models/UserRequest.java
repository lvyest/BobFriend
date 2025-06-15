package com.example.bobfriend.models;

public class UserRequest {
    private String username;
    private String password;
    private String name;

    public UserRequest(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String profileImage) { this.name = profileImage; }
}