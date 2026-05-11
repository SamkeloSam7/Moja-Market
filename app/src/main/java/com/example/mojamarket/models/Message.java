package com.example.mojamarket.models;

public class Message {
    private final String messageID;
    private final String chatID;
    private final String senderID;
    private final String content;
    private final String timeSent;
    private final boolean isMine;

    public Message(String messageID, String chatID, String senderID, String content, String timeSent, boolean isMine) {
        this.messageID = messageID;
        this.chatID    = chatID;
        this.senderID  = senderID;
        this.content   = content;
        this.timeSent  = timeSent;
        this.isMine    = isMine;
    }

    public String getMessageID()  { return messageID; }
    public String getChatID()     { return chatID; }
    public String getSenderID()   { return senderID; }
    public String getContent()    { return content; }
    public String getTimeSent()   { return timeSent; }
    public boolean isMine()       { return isMine; }
}