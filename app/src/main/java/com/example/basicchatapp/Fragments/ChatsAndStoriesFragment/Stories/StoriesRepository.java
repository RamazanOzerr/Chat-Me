package com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Stories;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class StoriesRepository {

    private MutableLiveData<List<StoriesModel>> liveData;
    private List<StoriesModel> storiesModelList;

    public StoriesRepository(){
        storiesModelList = new ArrayList<>();
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<StoriesModel>> getStories(){
        StoriesModel storiesModel1 = new StoriesModel("empty", "no_photo");
        StoriesModel storiesModel2 = new StoriesModel("Fatih Terim", "no_photo");
        StoriesModel storiesModel4 = new StoriesModel("Okan Buruk", "no_photo");
        StoriesModel storiesModel5 = new StoriesModel("Jack London", "no_photo");
        StoriesModel storiesModel7 = new StoriesModel("Igor Tudor", "no_photo");
        StoriesModel storiesModel6 = new StoriesModel("empty", "no_photo");


//        storiesModelList.add(storiesModel1);
//        storiesModelList.add(storiesModel1);
//        storiesModelList.add(storiesModel1);
//        storiesModelList.add(storiesModel1);
//        storiesModelList.add(storiesModel1);
        storiesModelList.add(storiesModel2);
        storiesModelList.add(storiesModel4);
        storiesModelList.add(storiesModel5);
        storiesModelList.add(storiesModel7);
        storiesModelList.add(storiesModel1);
        storiesModelList.add(storiesModel2);
        storiesModelList.add(storiesModel4);
        storiesModelList.add(storiesModel5);
        storiesModelList.add(storiesModel7);
        storiesModelList.add(storiesModel1);
        storiesModelList.add(storiesModel2);
        storiesModelList.add(storiesModel4);
        storiesModelList.add(storiesModel5);
        storiesModelList.add(storiesModel7);
        storiesModelList.add(storiesModel6);

        liveData.setValue(storiesModelList);

        return liveData;
    }

}
