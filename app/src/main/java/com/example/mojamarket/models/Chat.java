package com.example.mojamarket.models;

public class Chat {
    private final String chatID;
    private final String otherUserID;
    private final String otherUserName;
    private final String otherUsername;
    private final String itemID;
    private final String wantID;
    private final String lastMessage;
    private final String lastMessageTime;

    public Chat(String chatID, String otherUserID, String otherUserName, String otherUsername,
                String itemID, String wantID, String lastMessage, String lastMessageTime) {
        this.chatID          = chatID;
        this.otherUserID     = otherUserID;
        this.otherUserName   = otherUserName;
        this.otherUsername   = otherUsername;
        this.itemID          = itemID;
        this.wantID          = wantID;
        this.lastMessage     = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public String getChatID()          { return chatID; }
    public String getOtherUserID()     { return otherUserID; }
    public String getOtherUserName()   { return otherUserName; }
    public String getOtherUsername()   { return otherUsername; }
    public String getItemID()          { return itemID; }
    public String getWantID()          { return wantID; }
    public String getLastMessage()     { return lastMessage; }
    public String getLastMessageTime() { return lastMessageTime; }
}