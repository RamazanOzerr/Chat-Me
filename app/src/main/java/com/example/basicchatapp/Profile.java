package com.example.basicchatapp;

public class Profile {

    private String name;
    private int image;

    public Profile(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}
