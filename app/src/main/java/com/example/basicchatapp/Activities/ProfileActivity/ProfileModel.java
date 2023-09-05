package com.example.basicchatapp.Activities.ProfileActivity;

public class ProfileModel {

    private String name, photoUrl, aboutMe, email, phoneNumber;

    public ProfileModel(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public ProfileModel(String name, String photoUrl, String aboutMe
            , String email, String phoneNumber){
        this.name = name;
        this.photoUrl = photoUrl;
        this.aboutMe = aboutMe;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
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
}
