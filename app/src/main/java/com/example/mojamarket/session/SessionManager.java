package com.example.mojamarket.session;

import com.example.mojamarket.models.User;

public class SessionManager {
    private static User loggedinUser;

    public static User getLoggedinUser() {
        return loggedinUser;
    }
}
