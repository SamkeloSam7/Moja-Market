package com.example.mojamarket.utility;

import android.content.Context;
import com.example.mojamarket.R;
import com.example.mojamarket.models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UserDatabase {

    private static final String FILE_NAME = "users.json";

    public static void addUser(Context context, User newUser) {
        try {
            JSONArray usersArray = loadRawArray(context);
            usersArray.put(newUser.toJSONObject());
            saveRawArray(context, usersArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUser(Context context, String userId) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("userID") && obj.getString("userID").equals(userId)) {
                    return User.fromJSONObject(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<User> getUsers(Context context) {
        ArrayList<User> userList = new ArrayList<>();
        JSONArray jsonArray = loadRawArray(context);

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                User user = User.fromJSONObject(jsonArray.getJSONObject(i));
                if (user != null) userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static void updateUser(Context context, User updatedUser) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            JSONObject updatedObj = updatedUser.toJSONObject();
            String targetId = updatedUser.getUserID().toString();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("userID") && obj.getString("userID").equals(targetId)) {
                    jsonArray.put(i, updatedObj);
                    saveRawArray(context, jsonArray);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(Context context, String userId) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("userID") && obj.getString("userID").equals(userId)) {
                    jsonArray.remove(i);
                    saveRawArray(context, jsonArray);
                    break;
                }
            }
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
            InputStream is = context.getResources().openRawResource(R.raw.users);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return new JSONArray();
        }
    }
}