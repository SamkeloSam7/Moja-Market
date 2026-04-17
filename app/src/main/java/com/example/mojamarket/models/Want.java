package com.example.mojamarket.models;

import android.content.Context;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.utility.Helper;
import org.json.JSONObject;
import java.util.Date;
import java.util.UUID;

public class Want {
    private UUID id;
    private String item, description;
    private User buyer;
    private boolean wantStatus;
    private double budget;
    private final Date datePosted;

    public Want(String item, String description, double budget, User buyer) {
        this.id = UUID.randomUUID();
        this.item = item;
        this.description = description;
        this.buyer = buyer;
        this.budget = budget;
        this.wantStatus = true;
        this.datePosted = new Date();
    }

    public Want(String item, String description, double budget, User buyer, Date datePosted, UUID id) {
        this.id = id;
        this.item = item;
        this.description = description;
        this.buyer = buyer;
        this.budget = budget;
        this.wantStatus = true;
        this.datePosted = datePosted;
    }

    public UUID getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWantStatus(boolean wantStatus) {
        this.wantStatus = wantStatus;
    }

    public User getBuyer() {
        return buyer;
    }

    public boolean isWantStatus() {
        return wantStatus;
    }

    public void toggleWantStatus() {
        this.wantStatus = !wantStatus;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public static Want fromJSONObject(JSONObject json, Context context) {
        return Helper.wantFromJSON(json, context);
    }

    public JSONObject toJSONObject() {
        return Helper.wantToJSON(this);
    }
}