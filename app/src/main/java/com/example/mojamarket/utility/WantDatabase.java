package com.example.mojamarket.utility;

import android.content.Context;
import com.example.mojamarket.R;
import com.example.mojamarket.models.Want;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WantDatabase {

    private static final String FILE_NAME = "wants.json";

    public static void addWant(Context context, Want newWant) {
        try {
            JSONArray wantsArray = loadRawArray(context);
            wantsArray.put(newWant.toJSONObject());
            saveRawArray(context, wantsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Want getWant(Context context, String wantId) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("id") && obj.getString("id").equals(wantId)) {
                    return Want.fromJSONObject(obj, context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Want> getWants(Context context) {
        ArrayList<Want> wantList = new ArrayList<>();
        JSONArray jsonArray = loadRawArray(context);

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Want want = Want.fromJSONObject(jsonArray.getJSONObject(i), context);
                if (want != null) wantList.add(want);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wantList;
    }

    public static void updateWant(Context context, Want updatedWant) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            JSONObject updatedObj = updatedWant.toJSONObject();
            String targetId = updatedWant.getId().toString();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("id") && obj.getString("id").equals(targetId)) {
                    jsonArray.put(i, updatedObj);
                    saveRawArray(context, jsonArray);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteWant(Context context, String wantId) {
        try {
            JSONArray jsonArray = loadRawArray(context);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("id") && obj.getString("id").equals(wantId)) {
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
            InputStream is = context.getResources().openRawResource(R.raw.wants);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return new JSONArray();
        }
    }
}