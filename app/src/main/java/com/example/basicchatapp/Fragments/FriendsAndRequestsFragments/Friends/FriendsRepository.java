package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests.RequestModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsRepository {

    private MutableLiveData<List<FriendModel>> liveData;

    List<FriendModel> friendModelList;
    private final DatabaseReference databaseReference;
    private final String currUser;
    private final String TAG = "FRIENDS REPOSITORY";


    public FriendsRepository() {
        friendModelList = new ArrayList<>();
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

    public MutableLiveData<List<FriendModel>> getLiveData(){

        Query query = databaseReference
                .child("Requests")
                .child(currUser)
                .orderByChild("friend")
                .equalTo(true);

        Log.d(TAG, "getLiveData: yes");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d(TAG, "onDataChange: snapshot size" + snapshot.hasChildren());

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    // we need to photoUrl and username of the user
                    Log.d(TAG, "onDataChange: key: " +  dataSnapshot.getKey()); // target user key

                    String userKey = dataSnapshot.getKey();
                    if(userKey != null){
                        Query query1 = databaseReference  // get user info
                                .child("Users")
                                .child(userKey);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name, photoUrl, status;

                                if(snapshot.child("status").exists()){
                                    status = snapshot.child("status").getValue().toString();
                                    Log.d(TAG, "onDataChange: status exists");
                                } else {
                                    status = "";
                                    Log.d(TAG, "onDataChange: status not exists");
                                }

                                if(snapshot.child("name").exists()){
                                    name = snapshot.child("name").getValue().toString();
                                    Log.d(TAG, "onDataChange: name exists: " + name);
                                } else {
                                    name = "";
                                    Log.d(TAG, "onDataChange: name not exists");
                                }

                                if(snapshot.child("photoUrl").exists()){
                                    photoUrl = snapshot.child("photoUrl").getValue().toString();
                                    Log.d(TAG, "onDataChange: photoUrl exists: " + photoUrl);
                                } else {
                                    photoUrl = "";
                                    Log.d(TAG, "onDataChange: photoUrl not exists");
                                }

//                                FriendModel friendModel = new FriendModel(
//                                        snapshot.child("name").getValue().toString()
//                                        , snapshot.child("photoUrl").getValue().toString()
//                                        , status);

                                FriendModel friendModel = new FriendModel(
                                        photoUrl
                                        , name
                                        , status
                                        , userKey);


                                friendModelList.add(friendModel);
                                Log.d(TAG, "onDataChange: new friend found");

                                liveData.setValue(friendModelList);
                                Log.d(TAG, "onDataChange: " + friendModelList.toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        // there is no request
                        Log.d(TAG, "onDataChange: no friends");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return liveData;
    }
}
