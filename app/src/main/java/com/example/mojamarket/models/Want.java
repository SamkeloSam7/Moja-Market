package com.example.mojamarket.models;
import android.content.Context;

import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.models.User;
import com.example.mojamarket.utility.Helper;

import org.json.JSONObject;

import java.util.Date;

public class Want {
    private String wantName, wantDescription;
    private User buyer;

    boolean wantStatus;
    private double budget;

    private final Date datePosted;

    Want(String wantName, String wantDescription, double budget, Context context) {
        this.wantName = wantName;
        this.wantDescription = wantDescription;
        this.buyer = SessionManager.getLoggedInUser(context);
        this.budget = budget;
        this.wantStatus = true;
        this.datePosted =  new Date();
  ;  }

    public String getWantName() {
        return wantName;
    }

    public void setWantName(String wantName) {
        this.wantName = wantName;
    }

    public String getWantDescription() {
        return wantDescription;
    }

    public boolean isWantStatus() {
        return wantStatus;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setWantDescription(String wantDescription) {
        this.wantDescription = wantDescription;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void toggleWantStatus() {
        this.wantStatus = !wantStatus;
    }

    public static Post fromJSONObject(JSONObject json, Context context) {
        return Helper.postFromJSON(json, context);
    }

    public JSONObject toJSONObject() {
        return Helper.postToJSON(this);
    }
}
