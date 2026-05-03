package com.example.mojamarket.network;

public class ApiConstants {

    public static final String BASE_URL = "https://moja-market-backend-2.onrender.com";

    // Auth
    public static final String LOGIN    = BASE_URL + "/api/auth/login";
    public static final String REGISTER = BASE_URL + "/api/auth/register";

    // Posts
    public static final String ITEMS_FEED  = BASE_URL + "/api/posts/feed";
    public static final String ITEM_DETAIL = BASE_URL + "/api/posts/item";
    public static final String POST_ITEM   = BASE_URL + "/api/posts/items";

    // Wants
    public static final String WANTS_FEED  = BASE_URL + "/api/posts/wants/feed";
    public static final String WANT_DETAIL = BASE_URL + "/api/posts/want";
    public static final String POST_WANT   = BASE_URL + "/api/posts/wants";

    // User
    public static final String USER_PROFILE  = BASE_URL + "/api/user/profile";
    public static final String USER_LISTINGS = BASE_URL + "/api/user/listings";
    public static final String USER_WANTS    = BASE_URL + "/api/user/wants";

    // Upload
    public static final String UPLOAD_IMAGE = BASE_URL + "/api/upload";
}