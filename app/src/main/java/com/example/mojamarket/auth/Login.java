package com.example.mojamarket.auth;

import android.content.Context;

import com.example.mojamarket.models.User;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.utility.UserDatabase;

import java.util.ArrayList;

public class Login {

    public interface LoginCallback {
        void onSuccess(User user);
        void onFailure(String message);
    }

    // calls the backend and stores the logged in user in session
    public static void loginUserIn(String loginID, String password,
                                   Context context, LoginCallback callback) {
        AuthRepository.login(loginID, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                SessionManager.setLoggedInUser(user);
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }
}