package com.example.mojamarket;

public class Item {

    private final int itemId;
    private final String name;
    private final String description;
    private final double price;
    private final String location;
    private final String condition;
    private final int stock;
    private final int imageResId;
    private final double rating;
    private final int ratingCount;
    private final String datePosted;
    private final String sellerUsername;

    public Item(int itemId, String name, String description, double price,
                String location, String condition, int stock, int imageResId,
                double rating, int ratingCount, String datePosted, String sellerUsername) {
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
        this.datePosted = datePosted;
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

    public String getDatePosted() {
        return datePosted;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }
}