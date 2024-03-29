package com.example.basicchatapp.Activities.MessageActivity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    private final MutableLiveData<List<MessageModel>> liveData;
    private final List<MessageModel> messageModelList;
    private final DatabaseReference databaseReference;
    private final String TAG = "MESSAGE REPOSITORY";

    public MessageRepository(){
        messageModelList = new ArrayList<>();
        liveData = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    public MutableLiveData<List<MessageModel>> getLiveData(String currUser, String otherUser){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try {
                        MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                        messageModelList.add(messageModel);
                    } catch (Exception e){
                        // do nothing
                        String sender = dataSnapshot.child("sender").getValue().toString();
                        String text = dataSnapshot.child("text").getValue().toString();
                        String time = dataSnapshot.child("time").getValue().toString();
                        String timeStamp = dataSnapshot.child("timeStamp").getValue().toString();
                        long timeS = Long.parseLong(dataSnapshot.child("timeS").getValue().toString());
                        MessageModel messageModel = new MessageModel(false, sender, text, time, "text", timeStamp, timeS);
                        messageModelList.add(messageModel);
                    }
                }
                liveData.setValue(messageModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // we loaded the prev messages
        Query query = databaseReference.child("Messages")
                .child(currUser)
                .child(otherUser);
        query.addListenerForSingleValueEvent(valueEventListener);

        return liveData;
    }

    public MutableLiveData<List<MessageModel>> getNewMessages(String currUser, String  otherUser){
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Log.d(TAG, "onChildAdded:  " + snapshot.getValue());
                try {
                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                    messageModelList.add(messageModel);
                } catch (Exception e){
                    // do nothing
                    String sender = snapshot.child("sender").getValue().toString();
                    String text = snapshot.child("text").getValue().toString();
                    String time = snapshot.child("time").getValue().toString();
                    String timeStamp = snapshot.child("timeStamp").getValue().toString();
//                    long timeS = Long.parseLong(snapshot.child("timeS").getValue().toString());
                    long timeS = 1;
                    MessageModel messageModel = new MessageModel(false, sender, text, time, "text", timeStamp, timeS);
                    messageModelList.add(messageModel);
                }
                liveData.setValue(messageModelList);
                
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query query = FirebaseDatabase.getInstance().getReference("Messages")
                .child(currUser).child(otherUser);
        query.addChildEventListener(childEventListener);
        return liveData;
    }

}
