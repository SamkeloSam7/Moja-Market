package com.example.mojamarket.models;
import android.media.Image;
import java.util.Date;
import java.util.UUID;
public class Item {
    private String itemName, itemDescription, condition;
    User seller;
    private UUID itemID;
    private boolean stockStatus;
    private int quantity;
    private double price,averageRating;
    private final Date datePosted; //final to make sure date does not get modified
    private Image itemImage;
    Item(String itemName,User seller,String itemDescription,String condition,double price,int quantity, boolean stockStatus) {
        this.itemID = UUID.randomUUID(); //unique ID for identification
        this.seller = seller;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.datePosted = new Date();
        this.price = price;
        this.quantity = quantity;
        this.stockStatus = stockStatus;
        this.condition = condition;
    }

    //getters for item data retrieval
    public boolean isStockStatus() {
        return stockStatus;
    }
    public double getAverageRating() {
        return averageRating;
    }
    public double getPrice() {
        return price;
    }
    public String getCondition() {
        return condition;
    }
    public Date getDatePosted() {
        return datePosted;
    }

    public Image getItemImage() {
        return itemImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public UUID getItemID() {
        return itemID;
    }

    public User getSeller() {
        return seller;
    }

    //setters for item data update
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemImage(Image itemImage) {
        this.itemImage = itemImage;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
