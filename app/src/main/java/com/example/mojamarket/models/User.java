package com.example.mojamarket.models;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.UUID;

public class User {
    private String name, surname, username, email, password;
    private UUID userID;
    public User(String name, String surname, String username, String email, String password) {
        this.userID = UUID.randomUUID(); // random ID for unique user identification
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    //getters for user data retrieval
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
    //setters for updating user information
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

    //Method to turn the object to JSON for HTTP requests
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", this.userID.toString());
            jsonObject.put("name", this.name);
            jsonObject.put("surname", this.surname);
            jsonObject.put("username", this.username);
            jsonObject.put("email", this.email);
            jsonObject.put("password", this.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
