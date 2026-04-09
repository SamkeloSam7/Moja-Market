package com.example.mojamarket;

public class Item {

    private int itemId;
    private String name;
    private String description;
    private double price;
    private String location;
    private String condition;
    private int stock;
    private int imageResId;
    private double rating;
    private int ratingCount;
    private String postedDate;
    private String sellerUsername;

    public Item(int itemId, String name, String description, double price,
                String location, String condition, int stock, int imageResId,
                double rating, int ratingCount, String postedDate, String sellerUsername) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.location = location;
        this.condition = condition;
        this.stock = stock;
        this.imageResId = imageResId;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.postedDate = postedDate;
        this.sellerUsername = sellerUsername;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getCondition() {
        return condition;
    }

    public int getStock() {
        return stock;
    }

    public int getImageResId() {
        return imageResId;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }
}