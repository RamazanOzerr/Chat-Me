package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class FriendsRepository {

    private MutableLiveData<List<FriendModel>> liveData;

    List<FriendModel> friendModelList;


    public FriendsRepository() {
        friendModelList = new ArrayList<>();
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<FriendModel>> getLiveData(){

        FriendModel friend = new FriendModel("no", "fatih terim"
                , "people like me don't have people, we are the people that people have");

        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);
        friendModelList.add(friend);


        liveData.setValue(friendModelList);
        return liveData;
    }
}
