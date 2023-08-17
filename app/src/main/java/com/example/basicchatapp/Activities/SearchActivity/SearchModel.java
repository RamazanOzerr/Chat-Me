package com.example.basicchatapp.Activities.SearchActivity;

public class SearchModel {

    private String photo_url, username;

    public SearchModel(String photo_url, String username){
        this.photo_url = photo_url;
        this.username = username;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
