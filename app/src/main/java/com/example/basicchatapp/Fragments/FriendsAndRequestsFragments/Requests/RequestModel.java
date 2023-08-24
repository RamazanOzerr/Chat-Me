package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests;

public class RequestModel {

    private String type, username, photoUrl, userId;
    private boolean isFriend;

    public RequestModel(String username, String photoUrl, String userId){
        this.username = username;
        this.photoUrl = photoUrl;
        this.userId = userId;
    }

    public RequestModel(String type, boolean isFriend, String sender) {
        this.type = type;
        this.isFriend = isFriend;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
