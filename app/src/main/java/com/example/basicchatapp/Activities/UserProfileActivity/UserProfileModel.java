package com.example.basicchatapp.Activities.UserProfileActivity;

public class UserProfileModel {

    private String name, photoUrl, aboutMe, status;

    public UserProfileModel(String name, String photoUrl, String aboutMe, String status) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.aboutMe = aboutMe;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public String toString() {
        return "UserProfileModel{" +
                "name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", aboutMe='" + aboutMe + '\'' +
                '}';
    }
}
