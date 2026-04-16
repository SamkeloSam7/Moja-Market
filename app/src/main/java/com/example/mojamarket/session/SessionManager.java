package com.example.mojamarket.session;
import android.content.Context;
import com.example.mojamarket.models.User;
public class SessionManager {
    private static User loggedInUser;
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
    public static User getLoggedInUser(Context context) {
        return loggedInUser;
    }
    public static void logout() {
        loggedInUser = null;
    }
}