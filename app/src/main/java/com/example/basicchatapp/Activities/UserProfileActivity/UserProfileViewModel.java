package com.example.basicchatapp.Activities.UserProfileActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<UserProfileModel> liveData;
    private final UserProfileRepository repository;


    public UserProfileViewModel() {
        repository = new UserProfileRepository();
    }

    public MutableLiveData<UserProfileModel> getProfileData(String targetUserId){
        liveData = repository.getLiveData(targetUserId);
        return liveData;
    }
}
