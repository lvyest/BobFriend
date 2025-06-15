package com.example.bobfriend.models;

public class MessageResponse {
    private int id;
    private int product_id;
    private int sender_id;
    private int receiver_id;
    private String content;
    private String created_at;
    private String sender_username;
    private String receiver_username;

    public MessageResponse() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public int getSender_id() { return sender_id; }
    public void setSender_id(int sender_id) { this.sender_id = sender_id; }

    public int getReceiver_id() { return receiver_id; }
    public void setReceiver_id(int receiver_id) { this.receiver_id = receiver_id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getSender_username() { return sender_username; }
    public void setSender_username(String sender_username) { this.sender_username = sender_username; }

    public String getReceiver_username() { return receiver_username; }
    public void setReceiver_username(String receiver_username) { this.receiver_username = receiver_username; }
}