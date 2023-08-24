package com.example.basicchatapp.Activities.StoryActivity;

public class StoryModel {

    private String profile_photo_url, username, time, content_photo_url;

    public StoryModel(String profile_photo_url, String username, String time, String content_photo_url) {
        this.profile_photo_url = profile_photo_url;
        this.username = username;
        this.time = time;
        this.content_photo_url = content_photo_url;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent_photo_url() {
        return content_photo_url;
    }

    public void setContent_photo_url(String content_photo_url) {
        this.content_photo_url = content_photo_url;
    }
}
