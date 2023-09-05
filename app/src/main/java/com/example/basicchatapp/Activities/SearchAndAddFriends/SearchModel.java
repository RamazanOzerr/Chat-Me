package com.example.basicchatapp.Activities.SearchAndAddFriends;

public class SearchModel {

    private String name, photoUrl, token, userId;
    private int friend_status; // 1 if already friends, 2 if pending request, 3 if no value

    public SearchModel(String name, String photoUrl, String token, String userId, int friend_status){
        this.photoUrl = photoUrl;
        this.name = name;
        this.token = token;
        this.userId = userId;
        this.friend_status = friend_status;
    }

    public int getFriend_status() {
        return friend_status;
    }

    public void setFriend_status(int friend_status) {
        this.friend_status = friend_status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
