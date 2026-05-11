package com.example.mojamarket.network;

import android.util.Log;

import com.example.mojamarket.models.User;
import com.example.mojamarket.models.Want;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WantRepository {

    public interface WantsCallback {
        void onSuccess(List<Want> wants);
        void onFailure(String message);
    }

    public interface ActionCallback {
        void onSuccess(String message);
        void onFailure(String message);
    }

    public static void getFeed(WantsCallback callback) {
        ApiClient.getInstance().get(ApiConstants.WANTS_FEED, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Log.d("WantRepository", "Raw feed response: " + response.toString());

                    if (!response.getBoolean("success")) {
                        callback.onFailure(response.optString("message", "Failed to load wants"));
                        return;
                    }

                    JSONArray arr = response.getJSONArray("data");
                    Log.d("WantRepository", "Feed array length: " + arr.length());

                    List<Want> wants = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject row = arr.getJSONObject(i);
                        Log.d("WantRepository", "Row " + i + ": " + row.toString());
                        Want w = wantFromRow(row);
                        Log.d("WantRepository", "Want " + i + " parsed: " + (w != null ? w.getItem() : "NULL - FAILED TO PARSE"));
                        if (w != null) wants.add(w);
                    }
                    callback.onSuccess(wants);
                } catch (Exception e) {
                    Log.e("WantRepository", "Parse exception: " + e.getMessage(), e);
                    callback.onFailure("Failed to parse wants: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("WantRepository", "Network failure: " + errorMessage);
                callback.onFailure(errorMessage);
            }
        });
    }

    public static void postWant(Want want, ActionCallback callback) {
        try {
            ApiClient.getInstance().post(ApiConstants.POST_WANT, want.toJSONObject(), new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Failed to post want"));
                        } else {
                            callback.onSuccess(response.optString("message", "Want posted successfully"));
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

    static Want wantFromRow(JSONObject row) {
        try {
            String id = row.optString("wants_id", "").trim();
            if (id.isEmpty()) {
                Log.e("WantRepository", "Dropped want: Missing database ID (wants_id)");
                return null;
            }

            String userID   = row.optString("user_id", "");
            String name     = row.optString("name", "");
            String surname  = row.optString("surname", "");
            String username = row.optString("username", "unknown");

            if (userID.isEmpty()) userID = UUID.randomUUID().toString();

            User buyer = new User(name, surname, username, "", "");
            buyer.setUserID(userID);

            Date date;
            try {
                String rawDate = row.optString("date_posted", "2000-01-01 00:00:00");
                rawDate = rawDate.trim();
                if (rawDate.contains(".")) rawDate = rawDate.substring(0, rawDate.indexOf('.'));
                if (rawDate.contains("+")) rawDate = rawDate.substring(0, rawDate.indexOf('+'));
                if (rawDate.contains("T")) rawDate = rawDate.replace("T", " ");
                date = new Date(java.sql.Timestamp.valueOf(rawDate).getTime());
            } catch (Exception e) {
                Log.e("WantRepository", "Date parse failed: " + e.getMessage());
                date = new Date();
            }

            Want want = new Want(
                    row.optString("item_name", ""),
                    row.optString("item_description", ""),
                    row.optDouble("budget", 0),
                    buyer,
                    date,
                    id
            );

            // "false" string means inactive true is active
            want.setWantStatus(!"false".equalsIgnoreCase(row.optString("status", "true")));
            return want;

        } catch (Exception e) {
            Log.e("WantRepository", "wantFromRow failed: " + e.getMessage(), e);
            return null;
        }
    }
}