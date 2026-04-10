package com.example.mojamarket.auth;
import java.util.HashMap;
import com.example.mojamarket.models.User;
import java.util.regex.Pattern;

public class Register {
    private String name, surname, username, email, password, confirmPassword;
    User user;
    public void RegisterUser(String name, String surname,String username,String email, String password, String confirmPassword) {

    }

    public boolean ValidateInput(boolean blnValidInput) {
        return blnValidInput;
    }
}
