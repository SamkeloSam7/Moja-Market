package com.example.mojamarket.network;

import com.example.mojamarket.models.Chat;
import com.example.mojamarket.models.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatRepository {

    public interface ChatsCallback {
        void onSuccess(List<Chat> chats);
        void onFailure(String message);
    }

    public interface MessagesCallback {
        void onSuccess(List<Message> messages);
        void onFailure(String message);
    }

    public interface ActionCallback {
        void onSuccess(String chatID);
        void onFailure(String message);
    }

    // Called when buyer taps "Contact Seller" on an item or "Respond" on a want request.
    // Returns existing chatID if one already exists for the same users + item/want.
    public static void createChat(String user1, String user2, String itemID, String wantID, ActionCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("chatID",  UUID.randomUUID().toString());
            body.put("user1",   user1);
            body.put("user2",   user2);
            body.put("itemID",  itemID != null ? itemID : JSONObject.NULL);
            body.put("wantID",  wantID != null ? wantID : JSONObject.NULL);

            ApiClient.getInstance().post(ApiConstants.CHAT_CREATE, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Failed to create chat"));
                            return;
                        }
                        String chatID = response.getJSONObject("data").getString("chatID");
                        callback.onSuccess(chatID);
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure("Failed to build request: " + e.getMessage());
        }
    }

    // Sends a message in a chat.
    public static void sendMessage(String chatID, String senderID, String content, ApiClient.ApiCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("messageID", UUID.randomUUID().toString());
            body.put("chatID",    chatID);
            body.put("senderID",  senderID);
            body.put("message",   content);

            ApiClient.getInstance().post(ApiConstants.CHAT_SEND, body, callback);
        } catch (Exception e) {
            callback.onFailure("Failed to build request: " + e.getMessage());
        }
    }

    // Loads full message history once when the chat screen opens.
    public static void getChatHistory(String chatID, String currentUserID, MessagesCallback callback) {
        String url = ApiConstants.CHAT_HISTORY + "?chatID=" + chatID;

        ApiClient.getInstance().get(url, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.getBoolean("success")) {
                        callback.onFailure(response.optString("message", "Failed to load history"));
                        return;
                    }
                    callback.onSuccess(parseMessages(response.getJSONArray("data"), currentUserID));
                } catch (Exception e) {
                    callback.onFailure("Failed to parse history: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    // Short polling — fetches only messages newer than lastTime.
    // Call this every 3 seconds from the activity.
    public static void getNewMessages(String chatID, String lastTime, String currentUserID, MessagesCallback callback) {
        String url = ApiConstants.CHAT_MESSAGES + "?chatID=" + chatID + "&lastTime=" + lastTime;

        ApiClient.getInstance().get(url, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.getBoolean("success")) {
                        callback.onFailure(response.optString("message", "Failed to get messages"));
                        return;
                    }
                    callback.onSuccess(parseMessages(response.getJSONArray("data"), currentUserID));
                } catch (Exception e) {
                    callback.onFailure("Failed to parse messages: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Silent fail on polling — don't surface network blips to the user
                callback.onFailure(errorMessage);
            }
        });
    }

    // Loads all chats for a user for the chat list screen.
    public static void getUserChats(String userID, ChatsCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("userID", userID);

            ApiClient.getInstance().post(ApiConstants.CHAT_LIST, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (!response.getBoolean("success")) {
                            callback.onFailure(response.optString("message", "Failed to load chats"));
                            return;
                        }
                        JSONArray arr = response.getJSONArray("data");
                        List<Chat> chats = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            Chat c = chatFromRow(arr.getJSONObject(i));
                            if (c != null) chats.add(c);
                        }
                        callback.onSuccess(chats);
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse chats: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    callback.onFailure(errorMessage);
                }
            });
        } catch (Exception e) {
            callback.onFailure("Failed to build request: " + e.getMessage());
        }
    }

    // Parses a JSON array of message rows into Message objects.
    private static List<Message> parseMessages(JSONArray arr, String currentUserID) throws Exception {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String senderID = obj.getString("message_sender");
            messages.add(new Message(
                    obj.getString("message_id"),
                    obj.getString("chat_id"),
                    senderID,
                    obj.getString("message_content"),
                    obj.getString("time_sent"),
                    senderID.equals(currentUserID)
            ));
        }
        return messages;
    }

    // Parses a single chat row into a Chat object.
    public static Chat chatFromRow(JSONObject obj) {
        try {
            return new Chat(
                    obj.getString("chat_id"),
                    obj.getString("other_user_id"),
                    obj.getString("name"),
                    obj.getString("username"),
                    obj.optString("item_id", null),
                    obj.optString("want_id", null),
                    obj.optString("last_message", ""),
                    obj.optString("last_message_time", "")
            );
        } catch (Exception e) {
            return null;
        }
    }
}