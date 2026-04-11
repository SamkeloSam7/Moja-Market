package com.example.mojamarket;

public class WantRequest {

    private int requestId;
    private String itemName;
    private String description;
    private double budget;
    private String datePosted;
    private String username;
    private boolean fulfilled;
    private boolean ownRequest;

    public WantRequest(int requestId, String itemName, String description, double budget,
                       String datePosted, String username, boolean fulfilled, boolean ownRequest) {
        this.requestId = requestId;
        this.itemName = itemName;
        this.description = description;
        this.budget = budget;
        this.datePosted = datePosted;
        this.username = username;
        this.fulfilled = fulfilled;
        this.ownRequest = ownRequest;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public double getBudget() {
        return budget;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public String getUsername() {
        return username;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public boolean isOwnRequest() {
        return ownRequest;
    }
}