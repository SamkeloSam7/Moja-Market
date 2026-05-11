package com.example.mojamarket.models;

import android.content.Context;
import android.media.Image;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.utility.Helper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Post {
    private final String itemID;
    private final User seller;
    private final Date datePosted;

    private String itemName;
    private String itemDescription;
    private String condition;
    private String sellerLocation;
    private String stockStatus;
    private int quantity;
    private double price;
    private double averageRating;
    private Image itemImage;

    public Post(String itemName, String itemDescription, String condition, double price, int quantity, String stockStatus, String sellerLocation, Context context) {
        this.itemID = UUID.randomUUID().toString();
        this.seller = SessionManager.getLoggedInUser(context);
        this.datePosted = new Date();

        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.condition = condition;
        this.price = price;
        this.quantity = quantity;
        this.stockStatus = stockStatus;
        this.sellerLocation = sellerLocation;
        this.itemImage = this.uploadImage();
        this.averageRating = 0.0;
    }

    public Post(String itemName, String itemDescription, String condition, double price, int quantity, String stockStatus, String sellerLocation, User seller, Date datePosted, String itemID) {
        this.itemID = itemID;
        this.seller = seller;
        this.datePosted = datePosted;

        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.condition = condition;
        this.price = price;
        this.quantity = quantity;
        this.stockStatus = stockStatus;
        this.sellerLocation = sellerLocation;
        this.averageRating = 0.0;
    }

    private Image uploadImage() {
        Image img = null;
        return img;
    }

    private ArrayList<String> imageUris;

    public ArrayList<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(ArrayList<String> imageUris) {
        this.imageUris = imageUris;
    }

    public String getItemID() { return itemID; }
    public User getSeller() { return seller; }
    public Date getDatePosted() { return datePosted; }
    public String getItemName() { return itemName; }
    public String getItemDescription() { return itemDescription; }
    public String getCondition() { return condition; }
    public String getSellerLocation() { return sellerLocation; }
    public String getStockStatus() { return stockStatus; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getAverageRating() { return averageRating; }
    public Image getItemImage() { return itemImage; }

    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setSellerLocation(String sellerLocation) { this.sellerLocation = sellerLocation; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setItemImage(Image itemImage) { this.itemImage = itemImage; }

    public static Post fromJSONObject(JSONObject json, Context context) {
        return Helper.postFromJSON(json, context);
    }

    public JSONObject toJSONObject() {
        return Helper.postToJSON(this);
    }
}