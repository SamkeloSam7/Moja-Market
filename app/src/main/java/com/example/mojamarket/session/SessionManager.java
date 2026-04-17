package com.example.mojamarket.session;
import android.content.Context;
import com.example.mojamarket.models.User;
import com.example.mojamarket.models.Post;
public class SessionManager {
    private static User loggedInUser;
    private static Post currentClickedItem;
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
    public static void setCurrentClickedItem (Post item) { currentClickedItem = item; }
    public static User getLoggedInUser(Context context) {
        return loggedInUser;
    }
    public static Post getCurrentClickedItem(Context context) { return currentClickedItem; }
    public static void logout() {
        loggedInUser = null;
    }
}