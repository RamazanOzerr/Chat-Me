package com.example.basicchatapp;

public class Profile {

    private String name, bio, username;
    private int image;

    public Profile(){}

    public Profile(String name, int image, String bio, String username) {
        this.name = name;
        this.image = image;
        this.bio = bio;
        this.username = username;
    }

    public String getName() {return name;}

    public int getImage() {return image;}

    public String getBio() {return bio;}

    public String getUsername() {return username;}

}
