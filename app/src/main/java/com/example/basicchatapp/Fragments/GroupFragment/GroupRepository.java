package com.example.basicchatapp.Fragments.GroupFragment;

import androidx.lifecycle.MutableLiveData;

import com.example.basicchatapp.Fragments.ChatsFragment.Stories.StoriesModel;

import java.util.ArrayList;
import java.util.List;

public class GroupRepository {

    private MutableLiveData<List<GroupModel>> liveData;
    private List<GroupModel> groupModelList;

    public GroupRepository(){
        groupModelList = new ArrayList<>();
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<GroupModel>> getGroups(){

        GroupModel groupModel1
                = new GroupModel("null", "CodinCrew", true);

        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);
        groupModelList.add(groupModel1);


        liveData.setValue(groupModelList);
        return liveData;
    }


}
