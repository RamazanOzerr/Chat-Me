package com.example.basicchatapp.Activities.ProfileActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<ProfileModel> liveData;
    private final ProfileRepository repository;

    public ProfileViewModel(){
         repository = new ProfileRepository();
    }

    public MutableLiveData<ProfileModel> getProfileData(){
        liveData = repository.getLiveData();
        return liveData;
    }
}
