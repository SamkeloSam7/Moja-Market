package com.example.mojamarket.utility;

import android.content.Context;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Helper {
    public static JSONObject userToJSON(User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", user.getUserID().toString());
            jsonObject.put("name", user.getName());
            jsonObject.put("surname", user.getSurname());
            jsonObject.put("username", user.getUsername());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static User userFromJSON(JSONObject json) {
        try {
            User user = new User(
                    json.getString("name"),
                    json.getString("surname"),
                    json.getString("username"),
                    json.getString("email"),
                    json.getString("password")
            );
            user.setUserID(UUID.fromString(json.getString("userID")));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject postToJSON(Post post) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemID", post.getItemID().toString());
            jsonObject.put("seller", userToJSON(post.getSeller()));
            jsonObject.put("datePosted", post.getDatePosted().getTime());
            jsonObject.put("itemName", post.getItemName());
            jsonObject.put("itemDescription", post.getItemDescription());
            jsonObject.put("condition", post.getCondition());
            jsonObject.put("sellerLocation", post.getSellerLocation());
            jsonObject.put("stockStatus", post.getStockStatus());
            jsonObject.put("quantity", post.getQuantity());
            jsonObject.put("price", post.getPrice());
            jsonObject.put("averageRating", post.getAverageRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Post postFromJSON(JSONObject json, Context context) {
        try {
            User seller = userFromJSON(json.getJSONObject("seller"));
            Date datePosted = new Date(json.getLong("datePosted"));
            UUID itemID = UUID.fromString(json.getString("itemID"));

            Post post = new Post(
                    json.getString("itemName"),
                    json.getString("itemDescription"),
                    json.getString("condition"),
                    json.getDouble("price"),
                    json.getInt("quantity"),
                    json.getString("stockStatus"),
                    json.getString("sellerLocation"),
                    seller,
                    datePosted,
                    itemID
            );
            post.setAverageRating(json.getDouble("averageRating"));
            return post;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}