package com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Stories;

public class StoriesModel {

    private String name, photo_url;

    public StoriesModel(String name, String photo_url) {
        this.name = name;
        this.photo_url = photo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
