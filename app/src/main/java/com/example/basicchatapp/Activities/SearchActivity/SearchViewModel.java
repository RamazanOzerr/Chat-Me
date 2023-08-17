package com.example.basicchatapp.Activities.SearchActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<SearchModel> liveData;


    public SearchViewModel() {
        SearchRepository searchRepository = new SearchRepository();
        liveData = getData();
    }

    public MutableLiveData<SearchModel> getData(){
        return liveData;
    }
}
