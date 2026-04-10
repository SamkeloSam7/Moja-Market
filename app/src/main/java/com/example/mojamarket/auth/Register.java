package com.example.mojamarket.auth;
import android.content.Context;

import java.util.HashMap;
import com.example.mojamarket.models.User;
import com.example.mojamarket.utility.AddNewUser;
import com.example.mojamarket.utility.Validation;
import com.example.mojamarket.utility.ValidationResult;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class Register {
    private String name, surname, username, email, password;
    User user;
    public static void RegisterUser(String name, String surname,String username,String email, String password, Context context) {
        User newUser = new User(name,surname,username,email,password);
        //Add the user to the local JSON data for testing
        JSONObject user = newUser.toJSONObject();
        AddNewUser.addUser(context,user);
    }
}
