//package com.example.basicchatapp.Activities.MessageActivity;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//public class MessageViewModelFactory implements ViewModelProvider.Factory {
//
//    private final String currUser, otherUser;
//
//    public MessageViewModelFactory(String currUser, String otherUser) {
//        this.currUser = currUser;
//        this.otherUser = otherUser;
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T) new MessageViewModel(currUser, otherUser);
//    }
//
//}
