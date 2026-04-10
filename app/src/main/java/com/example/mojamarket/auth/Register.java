package com.example.mojamarket.auth;
import android.content.Context;

import com.example.mojamarket.models.User;
import com.example.mojamarket.utility.UserDatabase;

import org.json.JSONObject;

public class Register {
    private String name, surname, username, email, password;
    User user;
    public static void RegisterUser(String name, String surname,String username,String email, String password, Context context) {
        User newUser = new User(name,surname,username,email,password);
        //Add the user to the local JSON data for testing
        JSONObject user = newUser.toJSONObject();
        UserDatabase.addUser(context, User.fromJSONObject(user));
    }
}
