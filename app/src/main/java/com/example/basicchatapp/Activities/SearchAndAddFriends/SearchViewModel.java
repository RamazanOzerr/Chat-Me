package com.example.basicchatapp.Activities.SearchAndAddFriends;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<SearchModel>> liveDataSearchList;
    private final SearchRepository searchRepository;
    private String username;


    public SearchViewModel(String username) {
        searchRepository = new SearchRepository();
        this.username = username;
    }

    public MutableLiveData<List<SearchModel>> getLiveDataSearchList(String name){
        liveDataSearchList = searchRepository.getSearchResult(name);
        return liveDataSearchList;
    }
}
