package com.example.mojamarket.auth;

import android.content.Context;

import com.example.mojamarket.models.User;
import com.example.mojamarket.network.AuthRepository;

public class Register {

    public interface RegisterCallback {
        void onSuccess(User user);
        void onFailure(String message);
    }

    // sends user details to the backend and returns the created user
    public static void registerUser(String name, String surname, String username,
                                    String email, String password,
                                    Context context, RegisterCallback callback) {
        AuthRepository.register(name, surname, username, email, password,
                new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess(User user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    public void onFailure(String message) {
                        android.util.Log.e("AuthRepo", "Register failed: " + message);
                        callback.onFailure(message);
                    }
                });
    }
}