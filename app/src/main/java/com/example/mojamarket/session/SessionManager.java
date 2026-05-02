package com.example.mojamarket.session;

import android.content.Context;
import com.example.mojamarket.models.User;
import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.Want;

public class SessionManager {
    private static User loggedInUser;
    private static Post currentClickedItem;
    private static Want currentClickedWantRequest;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
    public static void setCurrentClickedItem(Post item) {
        currentClickedItem = item;
    }
    public static void setCurrentClickedWantRequest(Want want) {
        currentClickedWantRequest = want;
    }
    public static User getLoggedInUser(Context context) {
        return loggedInUser;
    }

    public static Post getCurrentClickedItem(Context context) {
        return currentClickedItem;
    }

    public static Want getCurrentClickedWantRequest(Context context) {
        return currentClickedWantRequest;
    }

    public static void logout() {
        loggedInUser = null;
        currentClickedItem = null;
        currentClickedWantRequest = null;
    }
}