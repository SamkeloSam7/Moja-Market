package com.example.mojamarket.network;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.Want;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public interface PostsCallback {
        void onSuccess(List<Post> posts);
        void onFailure(String message);
    }

    public interface WantsCallback {
        void onSuccess(List<Want> wants);
        void onFailure(String message);
    }

    public interface ActionCallback {
        void onSuccess(String message);
        void onFailure(String message);
    }

    public static void getListings(String userID, PostsCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("userID", userID);

            ApiClient.getInstance().post(ApiConstants.USER_LISTINGS, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "No listings found"));
                            return;
                        }
                        JSONArray arr = response.getJSONArray("data");
                        List<Post> posts = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            Post p = PostRepository.postFromRow(arr.getJSONObject(i));
                            if (p != null) posts.add(p);
                        }
                        callback.onSuccess(posts);
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse listings: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure("Failed to build request: " + e.getMessage());
        }
    }

    public static void getWantRequests(String userID, WantsCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("userID", userID);

            ApiClient.getInstance().post(ApiConstants.USER_WANTS, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "No want requests found"));
                            return;
                        }
                        JSONArray arr = response.getJSONArray("data");
                        List<Want> wants = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            Want w = WantRepository.wantFromRow(arr.getJSONObject(i));
                            if (w != null) wants.add(w);
                        }
                        callback.onSuccess(wants);
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse want requests: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure("Failed to build request: " + e.getMessage());
        }
    }

    public static void updateProfile(String userID, String name, String surname,
                                     String username, String email, String password,
                                     ActionCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("userID", userID);
            body.put("name", name);
            body.put("surname", surname);
            body.put("username", username);
            body.put("email", email);
            body.put("password", password);

            ApiClient.getInstance().post(ApiConstants.USER_PROFILE, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Failed to update profile"));
                        } else {
                            callback.onSuccess(response.optString("message", "Profile updated successfully"));
                        }
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure("Failed to build request: " + e.getMessage());
        }
    }
}