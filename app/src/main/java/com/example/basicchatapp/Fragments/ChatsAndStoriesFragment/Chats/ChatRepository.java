package com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Chats;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {

    private MutableLiveData<List<ChatModel>> liveData;
    private List<ChatModel> chatModelList;

    public ChatRepository(){
        chatModelList = new ArrayList<>();
        liveData = new MutableLiveData<>();
    }


    public MutableLiveData<List<ChatModel>> getChats(){

        ChatModel chatModel1 = new ChatModel("fatih terim"
                ,"no_photo", "Where have you been?", "online", "19.02");

        ChatModel chatModel2 = new ChatModel("Okan Buruk"
                ,"no_photo", "Bunların hangi birini oyuna alayım ben şimdi"
                , "online", "19.05");

        ChatModel chatModel3 = new ChatModel("Ali Koç"
                ,"no_photo", "Afrikanın kabilesinde bir topçu bulmuşummmm"
                , "online", "15m ago");

        ChatModel chatModel4 = new ChatModel("Jack London"
                ,"no_photo", "I am dying...", "offline", "Yesterday");

        ChatModel chatModel5 = new ChatModel("Yılmaz Vural"
                ,"no_photo", "Fenerbahçeyi yönetcem", "online", "Today");

        chatModelList.add(chatModel1);
        chatModelList.add(chatModel2);
        chatModelList.add(chatModel3);
        chatModelList.add(chatModel4);
        chatModelList.add(chatModel5);
        chatModelList.add(chatModel1);
        chatModelList.add(chatModel2);
        chatModelList.add(chatModel3);
        chatModelList.add(chatModel4);
//        chatModelList.add(chatModel5);
//        chatModelList.add(chatModel1);
//        chatModelList.add(chatModel2);
//        chatModelList.add(chatModel3);
//        chatModelList.add(chatModel4);
//        chatModelList.add(chatModel5);
//        chatModelList.add(chatModel1);
//        chatModelList.add(chatModel2);
//        chatModelList.add(chatModel3);
//        chatModelList.add(chatModel4);
//        chatModelList.add(chatModel5);

        liveData.setValue(chatModelList);

        return liveData;
    }

}
