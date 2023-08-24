package com.example.basicchatapp.Activities.SearchAndAddFriends;

public class SearchModel {

    private String name, photoUrl, token, userId;

    public SearchModel(String name, String photoUrl, String token, String userId){
        this.photoUrl = photoUrl;
        this.name = name;
        this.token = token;
        this.userId = userId;
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
