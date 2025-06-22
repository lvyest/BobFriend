package com.example.bobfriend.models;

public class ChatListItem {
    private int productId;
    private int otherUserId;
    private String otherUsername;
    private String lastMessage;
    private String lastMessageTime;
    private int unreadCount;

    public ChatListItem() {}

    public ChatListItem(int productId, int otherUserId, String otherUsername,
                        String lastMessage, String lastMessageTime, int unreadCount) {
        this.productId = productId;
        this.otherUserId = otherUserId;
        this.otherUsername = otherUsername;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getOtherUserId() { return otherUserId; }
    public void setOtherUserId(int otherUserId) { this.otherUserId = otherUserId; }

    public String getOtherUsername() { return otherUsername; }
    public void setOtherUsername(String otherUsername) { this.otherUsername = otherUsername; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(String lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
}