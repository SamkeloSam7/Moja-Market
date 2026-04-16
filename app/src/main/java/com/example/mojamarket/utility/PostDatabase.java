package com.example.mojamarket.utility;

import android.content.Context;
import com.example.mojamarket.R;
import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class PostDatabase {

    private static final String FILE_NAME = "posts.json";

    public static void addPost(Context context, Post newPost) {
        try {
            JSONArray postsArray = loadRawArray(context);
            postsArray.put(newPost.toJSONObject());
            saveRawArray(context, postsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Post getPost(Context context, UUID postId) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("itemID") && obj.getString("itemID").equals(postId.toString())) {
                    return Post.fromJSONObject(obj, context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Post> getPosts(Context context) {
        ArrayList<Post> postList = new ArrayList<>();
        JSONArray jsonArray = loadRawArray(context);

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Post post = Post.fromJSONObject(jsonArray.getJSONObject(i), context);
                if (post != null) postList.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postList;
    }

    public static ArrayList<Post> getPostsBySeller(Context context, User seller) {
        ArrayList<Post> postList = new ArrayList<>();
        JSONArray jsonArray = loadRawArray(context);

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Post post = Post.fromJSONObject(jsonArray.getJSONObject(i), context);
                if (post != null && post.getSeller() != null
                        && post.getSeller().getUserID().equals(seller.getUserID())) {
                    postList.add(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postList;
    }

    public static void updatePost(Context context, Post updatedPost) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            JSONObject updatedObj = updatedPost.toJSONObject();
            String targetId = updatedPost.getItemID().toString();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("itemID") && obj.getString("itemID").equals(targetId)) {
                    jsonArray.put(i, updatedObj);
                    saveRawArray(context, jsonArray);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePost(Context context, UUID postId) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("itemID") && obj.getString("itemID").equals(postId.toString())) {
                    jsonArray.remove(i);
                    saveRawArray(context, jsonArray);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePostsBySeller(Context context, User seller) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            JSONArray filtered = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                Post post = Post.fromJSONObject(jsonArray.getJSONObject(i), context);
                if (post == null || post.getSeller() == null
                        || !post.getSeller().getUserID().equals(seller.getUserID())) {
                    filtered.put(jsonArray.getJSONObject(i));
                }
            }
            saveRawArray(context, filtered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveRawArray(Context context, JSONArray jsonArray) throws Exception {
        FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    private static JSONArray loadRawArray(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            return new JSONArray(new String(buffer, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return loadFromRaw(context);
        }
    }

    private static JSONArray loadFromRaw(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.posts);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return new JSONArray();
        }
    }
}