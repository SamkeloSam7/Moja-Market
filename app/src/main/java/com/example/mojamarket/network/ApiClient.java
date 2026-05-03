package com.example.mojamarket.network;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

    // callback interface used by all network calls
    public interface ApiCallback {
        void onSuccess(JSONObject response);
        void onFailure(String errorMessage);
    }

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static ApiClient instance;
    private final OkHttpClient client;

    // single instance shared across the app
    private ApiClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    // sends a JSON body to the given url
    public void post(String url, JSONObject body, ApiCallback callback) {
        RequestBody requestBody = RequestBody.create(body.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(response, callback);
            }
        });
    }

    // fetches data from the given url with optional query params appended by the caller
    public void get(String url, ApiCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(response, callback);
            }
        });
    }

    // parses the response body as JSON and routes to success or failure
    private void handleResponse(Response response, ApiCallback callback) throws IOException {
        String rawBody = response.body() != null ? response.body().string() : "";

        if (!response.isSuccessful()) {
            callback.onFailure("Server error " + response.code() + ": " + rawBody);
            return;
        }

        try {
            JSONObject json = new JSONObject(rawBody);
            callback.onSuccess(json);
        } catch (Exception e) {
            callback.onFailure("Invalid JSON response: " + rawBody);
        }
    }
}