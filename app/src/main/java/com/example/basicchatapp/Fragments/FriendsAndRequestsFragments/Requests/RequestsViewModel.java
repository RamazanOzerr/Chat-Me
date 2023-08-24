package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class RequestsViewModel extends ViewModel {

    private MutableLiveData<List<RequestModel>> liveData;
    private final RequestsRepository repository;

    public RequestsViewModel(){
        repository = new RequestsRepository();
    }

    public MutableLiveData<List<RequestModel>> getRequests(){
        liveData = repository.getLiveData();
        return liveData;
    }

    public MutableLiveData<List<RequestModel>> getLiveData(){
        liveData = repository.getLiveDataListener();
        return liveData;
    }
}
