package com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Chats;

public class ChatModel {

    private String username, photo_url, text, status, time;

    public ChatModel(String username, String photo_url, String text, String status, String time) {
        this.username = username;
        this.photo_url = photo_url;
        this.text = text;
        this.status = status;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
