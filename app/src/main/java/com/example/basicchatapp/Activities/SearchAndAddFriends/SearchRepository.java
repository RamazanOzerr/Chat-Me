package com.example.basicchatapp.Activities.SearchAndAddFriends;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.basicchatapp.Utils.Constants;
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
import java.util.Objects;

public class SearchRepository {

    private final List<SearchModel> searchModelList;
    private final MutableLiveData<List<SearchModel>> liveDataSearchList;
    private final DatabaseReference databaseReference;
    private final String currUser;
    private final String TAG = "SEARCH REPOSITORY";

    public SearchRepository(){
        searchModelList = new ArrayList<>();
        liveDataSearchList = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            currUser = firebaseUser.getUid();
        } else {
            currUser = "";
        }
    }

    //todo: in this method we do not control if the user is friend with this user, or there is a
    // pending request or not, we will delete this method later
    public MutableLiveData<List<SearchModel>> getSearchResult(String username){
        System.out.println("USERNAME: "+ username);

        searchModelList.clear();

        Query query = databaseReference
                .child("Users")
                .orderByChild("name")
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String name, photoUrl, token, userId;
                    name =  Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    photoUrl = Objects.requireNonNull(dataSnapshot.child("photoUrl").getValue()).toString();
                    token = Objects.requireNonNull(dataSnapshot.child("token").getValue()).toString();
                    userId = dataSnapshot.getKey();

                    try {
                        searchModelList.add(dataSnapshot.getValue(SearchModel.class));
                    } catch (Exception e){
                        // do nothing
                        SearchModel searchModel
                                = new SearchModel(name, photoUrl, token, userId
                                , Constants.FRIEND_STATUS_NOT_FRIENDS);
                        searchModelList.add(searchModel);
                    }
                }
                liveDataSearchList.setValue(searchModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("model: " + liveDataSearchList);
        return liveDataSearchList;
    }

    public MutableLiveData<List<SearchModel>> getLiveData(String username){

        searchModelList.clear();

        Query query1 = databaseReference
                .child("Users")
                .orderByChild("name")
                .equalTo(username);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String name, photoUrl, token, userId;
                    name =  Objects.requireNonNull(dataSnapshot
                            .child("name")
                            .getValue())
                            .toString();

                    photoUrl = Objects.requireNonNull(dataSnapshot
                            .child("photoUrl")
                            .getValue())
                            .toString();

                    token = Objects.requireNonNull(dataSnapshot
                            .child("token")
                            .getValue())
                            .toString();

                    userId = dataSnapshot.getKey();

                    Query query2 = databaseReference
                            .child("Requests")
                            .child(currUser)
                            .orderByChild("friend");

                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "onDataChange: SNAPSHOT EXIST?" + snapshot.getValue());
                            if(userId != null && snapshot.hasChild(userId)){
                                if (snapshot.getValue() == null){
                                    Log.d(TAG, "onDataChange: NOT FRIENDS: " + snapshot.getValue());
                                    SearchModel searchModel
                                            = new SearchModel(name, photoUrl, token, userId
                                            , Constants.FRIEND_STATUS_NOT_FRIENDS);
                                    searchModelList.add(searchModel);
                                } else if(Objects.requireNonNull(snapshot
                                                .child(Objects.requireNonNull(userId))
                                                .child("type")
                                                .getValue())
                                        .toString()
                                        .equals("seen")){
                                    Log.d(TAG,"onDataChange: ALREADY FRIENDS: " + snapshot.getValue());
                                    SearchModel searchModel
                                            = new SearchModel(name, photoUrl, token, userId
                                            , Constants.FRIEND_STATUS_ALREADY_FRIENDS);
                                    searchModelList.add(searchModel);
                                } else {
                                    Log.d(TAG,"onDataChange: PENDING REQUEST: " + snapshot.getValue());
                                    SearchModel searchModel
                                            = new SearchModel(name, photoUrl, token, userId
                                            , Constants.FRIEND_STATUS_PENDING_REQUEST);
                                    searchModelList.add(searchModel);
                                }

                            } else {
                                SearchModel searchModel
                                        = new SearchModel(name, photoUrl, token, userId
                                        , Constants.FRIEND_STATUS_NOT_FRIENDS);
                                searchModelList.add(searchModel);
                            }
                            liveDataSearchList.setValue(searchModelList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return liveDataSearchList;

    }

}
