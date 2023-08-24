package com.example.basicchatapp.Activities.SearchAndAddFriends;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchRepository {

    private final List<SearchModel> searchModelList;
    private final MutableLiveData<List<SearchModel>> liveDataSearchList;
    private final DatabaseReference databaseReference;

    public SearchRepository(){
        searchModelList = new ArrayList<>();
        liveDataSearchList = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

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
                    try {
                        searchModelList.add(dataSnapshot.getValue(SearchModel.class));
                    } catch (Exception e){
                        // do nothing
                        SearchModel searchModel
                                = new SearchModel(
                                        dataSnapshot.child("name").getValue().toString()
                                ,dataSnapshot.child("photoUrl").getValue().toString()
                                , dataSnapshot.child("token").getValue().toString()
                                , dataSnapshot.getKey());
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

}
