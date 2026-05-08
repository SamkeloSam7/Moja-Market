package com.example.mojamarket.network;

import com.example.mojamarket.models.User;

import org.json.JSONObject;

import java.util.UUID;

public class AuthRepository {

    public interface AuthCallback {
        void onSuccess(User user);
        void onFailure(String message);
    }

    public static void login(String loginID, String password, AuthCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("loginID", loginID);
            body.put("password", password);

            ApiClient.getInstance().post(ApiConstants.LOGIN, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.optBoolean("success", false)) {
                            callback.onFailure(response.optString("message", "Login failed"));
                            return;
                        }

                        // data must be present and non-null for login
                        if (!response.has("data") || response.isNull("data")) {
                            callback.onFailure("Login error: server returned no user data");
                            return;
                        }

                        JSONObject data = response.getJSONObject("data");
                        User user = userFromJson(data);
                        callback.onSuccess(user);

                    } catch (Exception e) {
                        callback.onFailure("Failed to parse login response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });

        } catch (Exception e) {
            callback.onFailure("Failed to build login request: " + e.getMessage());
        }
    }

    public static void register(String name, String surname, String username,
                                String email, String password, AuthCallback callback) {
        try {
            String userID = UUID.randomUUID().toString();

            JSONObject body = new JSONObject();
            body.put("userID",   userID);
            body.put("name",     name);
            body.put("surname",  surname);
            body.put("username", username);
            body.put("email",    email);
            body.put("password", password);

            ApiClient.getInstance().post(ApiConstants.REGISTER, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.optBoolean("success", false)) {
                            callback.onFailure(response.optString("message", "Registration failed"));
                            return;
                        }

                        // data is null on register — build User from what we sent
                        User user = new User(name, surname, username, email, password);
                        user.setUserID(userID);
                        callback.onSuccess(user);

                    } catch (Exception e) {
                        callback.onFailure("Failed to parse register response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });

        } catch (Exception e) {
            callback.onFailure("Failed to build register request: " + e.getMessage());
        }
    }

    private static User userFromJson(JSONObject data) throws Exception {
        String rawID = data.optString("user_id", data.optString("userID", ""));
        if (rawID.isEmpty()) {
            throw new Exception("user_id missing from login response");
        }

        User user = new User(
                data.getString("name"),
                data.getString("surname"),
                data.getString("username"),
                data.getString("email"),
                data.optString("password", "") // password may not come back from server
        );
        user.setUserID(rawID);
        return user;
    }
}