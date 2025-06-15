package com.example.bobfriend.models;

public class MessageRequest {
    private int product_id;
    private int sender_id;
    private int receiver_id;
    private String content;

    public MessageRequest(int product_id, int sender_id, int receiver_id, String content) {
        this.product_id = product_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
    }

    // Getters and Setters
    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public int getSender_id() { return sender_id; }
    public void setSender_id(int sender_id) { this.sender_id = sender_id; }

    public int getReceiver_id() { return receiver_id; }
    public void setReceiver_id(int receiver_id) { this.receiver_id = receiver_id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}