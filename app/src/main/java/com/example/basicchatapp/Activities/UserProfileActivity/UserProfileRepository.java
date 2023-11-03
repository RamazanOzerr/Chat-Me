package com.example.basicchatapp.Activities.UserProfileActivity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserProfileRepository {

    private final MutableLiveData<UserProfileModel> liveData;
    private final DatabaseReference databaseReference;

    private final String TAG = "USER PROFILE REPOSITORY";

    public UserProfileRepository() {
        liveData = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public MutableLiveData<UserProfileModel> getLiveData(String targetUserId){
        Log.d(TAG, "getLiveData: target user id: " + targetUserId);
        Query query = databaseReference.child("Users").child(targetUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot" +  snapshot.getValue());
                try {
                    UserProfileModel profileModel = snapshot.getValue(UserProfileModel.class);
                    liveData.setValue(profileModel);
                } catch (Exception e){
                    String name, photoUrl, aboutMe, status;
                    if(snapshot.hasChild("name")){
                        name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    } else {
                        name = "";
                    }

                    if(snapshot.hasChild("photoUrl")){
                        photoUrl = Objects.requireNonNull(snapshot.child("photoUrl").getValue()).toString();
                    } else {
                        photoUrl = "";
                    }

                    if(snapshot.hasChild("aboutMe")){
                        aboutMe = Objects.requireNonNull(snapshot.child("aboutMe").getValue()).toString();
                    } else {
                        aboutMe = "";
                    }
                    if(snapshot.hasChild("status")){
                        status = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                    } else {
                        status = "";
                    }

                    UserProfileModel profileModel =
                            new UserProfileModel(name, photoUrl, aboutMe, status);
                    liveData.setValue(profileModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: error loading user data");
            }
        });

        Log.d(TAG, "getLiveData: return value: " + liveData.getValue());
        return liveData;
    }
}
