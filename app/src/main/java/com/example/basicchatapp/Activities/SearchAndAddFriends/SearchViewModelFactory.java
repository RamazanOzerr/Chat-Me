package com.example.basicchatapp.Activities.SearchAndAddFriends;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class SearchViewModelFactory implements ViewModelProvider.Factory{

    private final String username;

    public SearchViewModelFactory(String username) {
        this.username = username;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(username);
    }

}
