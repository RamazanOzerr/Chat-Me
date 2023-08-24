package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsRepository {

    private final MutableLiveData<List<RequestModel>> liveData;
    List<RequestModel> requestModelList;
    private final DatabaseReference databaseReference;
    private final String currUser;
    private final String TAG = "REQUEST";

    public RequestsRepository(){
        requestModelList = new ArrayList<>();
        liveData = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            currUser = firebaseUser.getUid();
        } else {
            currUser = "";
            //todo: user have to log in again
        }
    }

    public MutableLiveData<List<RequestModel>> getLiveData(){

        Query query = databaseReference
                .child("Requests")
                .child(currUser)
                .orderByChild("type")
                .equalTo("received");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   // we need to photoUrl and username of the user who sent request
                    System.out.println("REQUEST: " + dataSnapshot.getKey()); // target user key

                    String userKey = dataSnapshot.getKey();
                    if(userKey != null){
                        Query query1 = databaseReference  // get user info
                                .child("Users")
                                .child(userKey);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                RequestModel requestModel = new RequestModel(
                                        snapshot.child("name").getValue().toString()
                                        , snapshot.child("photoUrl").getValue().toString()
                                        , userKey);

                                requestModelList.add(requestModel);
                                Log.d(TAG, "onDataChange: new request found");

                                liveData.setValue(requestModelList);
                                Log.d(TAG, "onDataChange: " + requestModelList.toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        // there is no request
                            Log.d(TAG, "onDataChange: no requests");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return liveData;
    }


    public void acceptRequest(String userKey){

        Map<String, Object> map = new HashMap<>();
        map.put("type", "seen");
        map.put("friend", true);

        databaseReference
                .child("Requests")
                .child(currUser)
                .child(userKey)
                .updateChildren(map)
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        databaseReference
                                .child("Requests")
                                .child(userKey)
                                .child(currUser)
                                .updateChildren(map);
                    }
                });

    }

    public void rejectRequest(String userKey){
        databaseReference
                .child("Requests")
                .child(currUser)
                .child(userKey)
                .removeValue().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        databaseReference
                                .child("Requests")
                                .child(userKey)
                                .child(currUser)
                                .removeValue();
                    }
                });


    }

    // this will be triggered when there is a change on the db, like accepting a request
    // or deleting it
    public MutableLiveData<List<RequestModel>> getLiveDataListener(){

        Query query = databaseReference
                .child("Requests")
                .child(currUser);
        
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: ");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildChanged: request accepted" + snapshot.getValue().toString());

                String userId = snapshot.getKey();
                if(userId != null){
                    Query query1 = databaseReference  // get user info
                            .child("Users")
                            .child(userId);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            RequestModel requestModel = new RequestModel(
                                    snapshot.child("name").getValue().toString()
                                    , snapshot.child("photoUrl").getValue().toString()
                                    , userId);

                            for(RequestModel request : requestModelList){
                                if(request.getUserId().equals(userId)){
                                    requestModelList.remove(request);
                                    Log.d(TAG, "onDataChange: new request change");
                                }
                            }

                            liveData.setValue(requestModelList);
                            Log.d(TAG, "onDataChange: " + requestModelList.size());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onChildRemoved: request deleted");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildMoved: ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ");
            }
        });

        return liveData;
    }


}
