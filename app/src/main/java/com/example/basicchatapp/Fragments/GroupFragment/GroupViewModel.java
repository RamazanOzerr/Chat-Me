package com.example.basicchatapp.Fragments.GroupFragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class GroupViewModel extends ViewModel {

    private MutableLiveData<List<GroupModel>> liveData;

    public GroupViewModel(){
        GroupRepository repository = new GroupRepository();
        liveData = repository.getGroups();
    }

    public MutableLiveData<List<GroupModel>> getGroups(){
        return liveData;
    }
}
