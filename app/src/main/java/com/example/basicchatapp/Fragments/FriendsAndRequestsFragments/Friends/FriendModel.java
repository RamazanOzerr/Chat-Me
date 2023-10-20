package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

public class FriendModel {

    private String photoUrl, username, status, userKey;

    public FriendModel(String photoUrl, String username, String status, String userKey) {
        this.photoUrl = photoUrl;
        this.username = username;
        this.status = status;
        this.userKey = userKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
