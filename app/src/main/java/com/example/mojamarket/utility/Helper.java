package com.example.mojamarket.utility;

import android.content.Context;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.User;
import com.example.mojamarket.models.Want;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

            if (post.getImageUris() != null) {
                jsonObject.put("images", new JSONArray(post.getImageUris()));
            } else {
                jsonObject.put("images", new JSONArray());
            }

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

            ArrayList<String> images = new ArrayList<>();
            if (json.has("images")) {
                JSONArray imagesArray = json.getJSONArray("images");
                for (int i = 0; i < imagesArray.length(); i++) {
                    images.add(imagesArray.getString(i));
                }
            }
            post.setImageUris(images);

            return post;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject wantToJSON(Want want) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", want.getId().toString());
            jsonObject.put("item", want.getItem());
            jsonObject.put("description", want.getDescription());
            jsonObject.put("budget", want.getBudget());
            jsonObject.put("buyer", userToJSON(want.getBuyer()));
            jsonObject.put("wantStatus", want.isWantStatus());
            jsonObject.put("datePosted", want.getDatePosted().getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Want wantFromJSON(JSONObject json, Context context) {
        try {
            User buyer = userFromJSON(json.getJSONObject("buyer"));
            Date datePosted = new Date(json.getLong("datePosted"));
            UUID id = UUID.fromString(json.getString("id"));

            Want want = new Want(
                    json.getString("item"),
                    json.getString("description"),
                    json.getDouble("budget"),
                    buyer,
                    datePosted,
                    id
            );
            want.setWantStatus(json.getBoolean("wantStatus"));
            return want;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}