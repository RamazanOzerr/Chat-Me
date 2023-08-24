package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FriendsViewModel extends ViewModel {

    private MutableLiveData<List<FriendModel>> liveData;
    private final FriendsRepository repository;

    public FriendsViewModel(){
        repository = new FriendsRepository();
    }

    public MutableLiveData<List<FriendModel>> getFriendsList(){
        liveData = repository.getLiveData();
        return liveData;
    }

}
