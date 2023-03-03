package com.example.basicchatapp.Utils;

public class Profile {

    private String name, bio, username;
    private String photo, otherUser;

    public Profile(){}

    public Profile(String bio, String name, String photo,  String username, String otherUser) {
        this.name = name;
        this.photo = photo;
        this.bio = bio;
        this.username = username;
        this.otherUser = otherUser;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public String getName() {return name;}

    public String getPhoto() {
        return photo;
    }

    public String getBio() {return bio;}

    public String getUsername() {return username;}

}
