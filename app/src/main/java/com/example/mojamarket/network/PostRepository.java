package com.example.mojamarket.network;

import android.util.Log;
import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.User;
import com.example.mojamarket.network.ApiConstants;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PostRepository {

    public interface PostsCallback {
        void onSuccess(List<Post> posts);
        void onFailure(String message);
    }

    public interface ActionCallback {
        void onSuccess(String message);
        void onFailure(String message);
    }

    public static void getFeed(PostsCallback callback) {
        ApiClient.getInstance().get(ApiConstants.ITEMS_FEED, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.getBoolean("success")) {
                        callback.onFailure(response.optString("message", "Error"));
                        return;
                    }
                    JSONArray arr = response.getJSONArray("data");
                    List<Post> posts = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        Post p = postFromRow(arr.getJSONObject(i));
                        if (p != null) posts.add(p);
                    }
                    callback.onSuccess(posts);
                } catch (Exception e) {
                    callback.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public static void postItem(Post post, ActionCallback callback) {
        try {
            ApiClient.getInstance().post(ApiConstants.POST_ITEM, post.toJSONObject(), new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Error"));
                        } else {
                            callback.onSuccess(response.optString("message", "Success"));
                        }
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    public static void updateItem(Post post, ActionCallback callback) {
        try {
            ApiClient.getInstance().post(ApiConstants.UPDATE_ITEM, post.toJSONObject(), new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Error updating item"));
                        } else {
                            callback.onSuccess(response.optString("message", "Success"));
                        }
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    public static void deleteItem(String itemID, ActionCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("itemID", itemID);

            ApiClient.getInstance().post(ApiConstants.DELETE_ITEM, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Error deleting item"));
                        } else {
                            callback.onSuccess(response.optString("message", "Success"));
                        }
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    static Post postFromRow(JSONObject row) {
        try {
            String itemUUID = row.optString("item_id", "").trim();
            if (itemUUID.isEmpty()) return null;

            User seller = new User(row.optString("name", ""), row.optString("surname", ""), row.optString("username", "unknown"), "", "");
            seller.setUserID(row.optString("user_id", UUID.randomUUID().toString()));

            Date date;
            try {
                String rawDate = row.optString("date_posted", "2000-01-01 00:00:00").replace("T", " ");
                if (rawDate.contains(".")) rawDate = rawDate.substring(0, rawDate.indexOf('.'));
                date = new Date(java.sql.Timestamp.valueOf(rawDate).getTime());
            } catch (Exception e) {
                date = new Date();
            }

            Post post = new Post(
                    row.optString("item_name", "Untitled"),
                    row.optString("item_description", ""),
                    row.optString("item_condition", ""),
                    row.optDouble("item_price", 0.0),
                    row.optInt("stock_amount", 0),
                    row.optString("status", ""),
                    row.optString("location", ""),
                    seller,
                    date,
                    itemUUID
            );

            ArrayList<String> images = new ArrayList<>();
            if (row.has("image_data") && !row.isNull("image_data")) images.add(row.getString("image_data"));
            post.setImageUris(images);
            return post;
        } catch (Exception e) {
            return null;
        }
    }
}