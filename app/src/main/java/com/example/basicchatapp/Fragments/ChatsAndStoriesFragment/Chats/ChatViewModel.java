package com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Chats;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private final MutableLiveData<List<ChatModel>> liveData;

    public ChatViewModel() {
        ChatRepository repository = new ChatRepository();
        liveData = repository.getChats();
    }

    public MutableLiveData<List<ChatModel>> getChats(){
        return liveData;
    }
}
