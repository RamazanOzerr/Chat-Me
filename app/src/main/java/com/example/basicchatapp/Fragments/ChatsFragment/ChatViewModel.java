package com.example.basicchatapp.Fragments.ChatsFragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private final MutableLiveData<List<ChatModel>> liveData;
    private final List<ChatModel> chatModelList;

    public ChatViewModel() {
        chatModelList = new ArrayList<>();
        ChatRepository repository = new ChatRepository();
        liveData = repository.getChats();
    }

    public MutableLiveData<List<ChatModel>> getChats(){
        return liveData;
    }
}
