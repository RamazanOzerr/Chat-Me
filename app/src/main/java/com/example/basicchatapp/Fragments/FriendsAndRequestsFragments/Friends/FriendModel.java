package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

public class FriendModel {

    private String photoUrl, username, status;

    public FriendModel(String photoUrl, String username, String status) {
        this.photoUrl = photoUrl;
        this.username = username;
        this.status = status;
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
