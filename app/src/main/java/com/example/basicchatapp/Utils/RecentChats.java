package com.example.basicchatapp.Utils;

public class RecentChats {

    private String photoPath;;
    private String name;
    private String text;

    private int count;

    public RecentChats(String photoPath, String name, String text, int count) {
        this.photoPath = photoPath;
        this.name = name;
        this.text = text;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "RecentChats{" +
                "photoPath='" + photoPath + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
