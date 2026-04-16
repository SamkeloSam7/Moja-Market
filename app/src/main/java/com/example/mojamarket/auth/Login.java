package com.example.mojamarket.auth;

import android.content.Context;

import com.example.mojamarket.models.User;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.utility.UserDatabase;

import java.util.ArrayList;

public class Login {

    public static boolean LoginUserIn(String loginID, String password, Context context) {

        ArrayList<User> users = UserDatabase.getUsers(context);

        for (User user : users) {
            if ((loginID.equals(user.getEmail()) || loginID.equals(user.getUsername()))
                    && password.equals(user.getPassword())) {

                SessionManager.setLoggedInUser(user);
                return true;
            }
        }
        return false;
    }
}