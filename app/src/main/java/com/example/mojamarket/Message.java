package com.example.mojamarket;

public class Message {

    private String text;
    private String time;
    private boolean sentByMe;

    public Message(String text, String time, boolean sentByMe) {
        this.text = text;
        this.time = time;
        this.sentByMe = sentByMe;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }
}