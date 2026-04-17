package com.example.mojamarket.models;

import android.content.Context;

import com.example.mojamarket.utility.Helper;
import com.example.mojamarket.utility.PostDatabase;
import com.example.mojamarket.utility.WantDatabase;

import org.json.JSONObject;

import java.util.UUID;

public class User {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private UUID userID;

    public User(String name, String surname, String username, String email, String password) {
        this.userID = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UUID getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void postItem(Context context, Post post) {
        PostDatabase.addPost(context, post);
    }

    public void updatePost(Context context, Post post) {
        PostDatabase.updatePost(context, post);
    }

    public void deletePost(Context context, Post post) {
        PostDatabase.deletePost(context, post.getItemID());
    }

    public static User fromJSONObject(JSONObject json) {
        return Helper.userFromJSON(json);
    }

    public JSONObject toJSONObject() {
        return Helper.userToJSON(this);
    }

    public void postWant(Context context, Want want) {
        WantDatabase.addWant(context, want);
    }

    public void updateWant(Context context, Want want) {
        WantDatabase.updateWant(context, want);
    }

    public void deleteWant(Context context, Want want) {
        WantDatabase.deleteWant(context, want.getId().toString());
    }
}