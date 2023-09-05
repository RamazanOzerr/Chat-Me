package com.example.basicchatapp.Activities.MessageActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MessageViewModel extends ViewModel{

    // first attribute is for when the user open the chat screen for the first time
    // and the second time is for real time messaging
    private MutableLiveData<List<MessageModel>> liveDataFirst, liveData;
    private MessageRepository repository;

    public MessageViewModel(){
        repository = new MessageRepository();
//        liveDataFirst = repository.getLiveData(currUser, otherUser);

    }

//    public MutableLiveData<List<MessageModel>> getMessages(){
//        return liveDataFirst;
//    }

    public MutableLiveData<List<MessageModel>> getNewMessages(String currUser, String otherUser){
        liveData = repository.getNewMessages(currUser, otherUser);
        return liveData;
    }
}
