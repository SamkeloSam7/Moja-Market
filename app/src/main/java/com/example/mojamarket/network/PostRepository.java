package com.example.mojamarket.network;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.User;

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

    public interface PostCallback {
        void onSuccess(Post post);
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
                        callback.onFailure(response.optString("message", "Failed to load feed"));
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
                    callback.onFailure("Failed to parse feed: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public static void getItem(String itemID, PostCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("itemID", itemID);

            ApiClient.getInstance().post(ApiConstants.ITEM_DETAIL, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Item not found"));
                            return;
                        }
                        JSONArray arr = response.getJSONArray("data");
                        callback.onSuccess(postFromRow(arr.getJSONObject(0)));
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse item: " + e.getMessage());
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

    public static void postItem(Post post, ActionCallback callback) {
        try {
            JSONObject imageObj = new JSONObject();
            imageObj.put("imageID", UUID.randomUUID().toString());
            String firstImg = (post.getImageUris() != null && !post.getImageUris().isEmpty())
                    ? post.getImageUris().get(0) : "";
            imageObj.put("imagePath", firstImg);

            JSONObject body = post.toJSONObject();
            body.put("itemImage", imageObj);

            ApiClient.getInstance().post(ApiConstants.POST_ITEM, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Failed to post item"));
                        } else {
                            callback.onSuccess(response.optString("message", "Item posted successfully"));
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

    // Package-private so UserRepository can access it within the same package
    static Post postFromRow(JSONObject row) {
        try {
            User seller = new User("", "", "", "", "");
            seller.setUserID(row.optString("user_id", UUID.randomUUID().toString()));

            // sql.Timestamp.valueOf expects "yyyy-MM-dd HH:mm:ss" format
            Date date = new Date(
                    java.sql.Timestamp.valueOf(
                            row.optString("date_posted", "2000-01-01 00:00:00")).getTime());

            Post post = new Post(
                    row.optString("item_name"),
                    row.optString("item_description"),
                    row.optString("item_condition"),
                    row.optDouble("item_price", 0),
                    row.optInt("stock_amount", 0),
                    row.optString("status"),
                    row.optString("location"),
                    seller,
                    date,
                    UUID.fromString(row.optString("item_id", UUID.randomUUID().toString()))
            );

            ArrayList<String> images = new ArrayList<>();
            if (row.has("image_data") && !row.isNull("image_data")) {
                images.add(row.getString("image_data"));
            }
            post.setImageUris(images);
            return post;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}