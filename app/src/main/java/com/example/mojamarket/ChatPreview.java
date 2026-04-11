package com.example.mojamarket;

public class ChatPreview {

    private int userId;
    private String name;
    private String username;
    private String lastMessage;
    private String time;
    private int unread;

    public ChatPreview(int userId, String name, String username, String lastMessage, String time, int unread) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.lastMessage = lastMessage;
        this.time = time;
        this.unread = unread;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    public int getUnread() {
        return unread;
    }
}