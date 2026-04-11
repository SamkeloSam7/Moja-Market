package com.example.mojamarket.auth;
import android.content.Context;

import com.example.mojamarket.models.User;
import com.example.mojamarket.utility.UserDatabase;

import org.json.JSONArray;

import java.util.ArrayList;

public class Login {
    private String email,username, password;
    public static boolean LoginUserIn(String loginID, String password, Context context) {
       ArrayList<User> users = UserDatabase.getUsers(context);
       boolean isValidLogin = false;
       for (User user: users) {
           if ((loginID.equals(user.getEmail()) || loginID.equals(user.getUsername())) && password.equals(user.getPassword())) {
                isValidLogin = true;
                break;
           }
       }

       return isValidLogin;
    }
}
