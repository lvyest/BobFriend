package com.example.bobfriend.models;

public class Order {
    private int id;
    private String menuName;
    private int price;
    private int quantity;
    private String username;
    private int restaurantId;
    private String orderTime;

    public Order() {}

    public Order(String menuName, int price, int quantity) {
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public Order(String menuName, int price, int quantity, String username, int restaurantId, String orderTime) {
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
        this.username = username;
        this.restaurantId = restaurantId;
        this.orderTime = orderTime;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }
}