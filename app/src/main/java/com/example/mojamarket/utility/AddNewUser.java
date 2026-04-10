package com.example.mojamarket.utility;

import android.content.Context;
import com.example.mojamarket.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AddNewUser {

    private static final String FILE_NAME = "users.json";

    // Add new user
    public static void addUser(Context context, JSONObject newUser) {
        try {
            JSONArray users = loadUsers(context);
            users.put(newUser);

            // Save back to internal storage
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(users.toString().getBytes(StandardCharsets.UTF_8));
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static JSONArray loadUsers(Context context) {
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