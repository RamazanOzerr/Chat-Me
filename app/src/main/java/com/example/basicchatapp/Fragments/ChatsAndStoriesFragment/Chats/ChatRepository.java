package com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Chats;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatRepository {

    private final MutableLiveData<List<ChatModel>> liveData;
    private final List<ChatModel> chatModelList;
    private final DatabaseReference reference;
    private final String currUserId;
    private final String TAG = "CHAT REPOSITORY";

    public ChatRepository(){
        chatModelList = new ArrayList<>();
        liveData = new MutableLiveData<>();
        reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            currUserId = firebaseUser.getUid();
        } else {
            // todo: yeniden giriş talep et
            currUserId = "";
        }
    }


    public MutableLiveData<List<ChatModel>> getChats(){

        Log.d(TAG, "getChats: ");
        Query query = reference.child("Messages")
                .child(currUserId)
                .orderByChild("timeS");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatModelList.clear();
                Log.d(TAG, "onDataChange: snapshot: " + snapshot.getValue());
                if(snapshot.getValue() == null) liveData.setValue(new ArrayList<>());
                else {

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();  // mesajın gönderildiği kişinin id si
                        // listelerken bilgileri lazım, onları çekmek için kullancaz
                        Log.d(TAG, "onDataChange: snapshot: " + dataSnapshot.getValue());
                        String text, time;
                        ChatModel chatModel = new ChatModel("name", "photoUrl"
                                , "text", "status", "time", key);
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            text = ds.child("text").getValue().toString();
                            time = ds.child("time").getValue().toString();
                            chatModel.setText(text);
                            chatModel.setTime(time);
                        }

                        if(key != null){
                            Query query1 = reference.child("Users").child(key);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChildren()){
                                        String name = snapshot.child("name").getValue().toString();
                                        String photoUrl = snapshot.child("photoUrl").getValue().toString();
                                        String status = snapshot.child("status").getValue().toString();
                                        chatModel.setUsername(name);
                                        chatModel.setPhoto_url(photoUrl);
                                        chatModel.setStatus(status);
                                        chatModelList.add(chatModel);
                                        Log.d(TAG, "onDataChange: chat model" + chatModel);
                                    } Log.d(TAG, "onDataChange: livedata: " + liveData.getValue());
                                    Log.d(TAG, "chatmodellist before: " + chatModelList.get(0).getTime());
                                    sortList();
                                    Log.d(TAG, "chatmodellist after: " + chatModelList.get(0).getTime());
                                    liveData.setValue(chatModelList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        ChatModel chatModel1 = new ChatModel("fatih terim"
//                ,"no_photo", "Where have you been?", "online", "19.02");
//
//        ChatModel chatModel2 = new ChatModel("Okan Buruk"
//                ,"no_photo", "Bunların hangi birini oyuna alayım ben şimdi"
//                , "online", "19.05");
//
//        ChatModel chatModel3 = new ChatModel("Ali Koç"
//                ,"no_photo", "Afrikanın kabilesinde bir topçu bulmuşummmm"
//                , "online", "15m ago");
//
//        ChatModel chatModel4 = new ChatModel("Jack London"
//                ,"no_photo", "I am dying...", "offline", "Yesterday");
//
//        ChatModel chatModel5 = new ChatModel("Yılmaz Vural"
//                ,"no_photo", "Fenerbahçeyi yönetcem", "online", "Today");

//        chatModelList.add(chatModel1);
//        chatModelList.add(chatModel2);
//        chatModelList.add(chatModel3);
//        chatModelList.add(chatModel4);
//        chatModelList.add(chatModel5);
//        chatModelList.add(chatModel1);
//        chatModelList.add(chatModel2);
//        chatModelList.add(chatModel3);
//        chatModelList.add(chatModel4);


//        liveData.setValue(chatModelList);

        return liveData;
    }

    private void sortList(){
        Collections.sort(chatModelList, new Comparator<ChatModel>() {
            @Override
            public int compare(ChatModel chatModel1, ChatModel chatModel2) {
                return chatModel2.getTime().compareTo(chatModel1.getTime());
            }
        });

    }

}
