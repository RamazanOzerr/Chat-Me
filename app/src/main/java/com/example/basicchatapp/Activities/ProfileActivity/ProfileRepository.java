package com.example.basicchatapp.Activities.ProfileActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.basicchatapp.Utils.HelperMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileRepository {

    private final MutableLiveData<ProfileModel> liveData;
    private final String currUserId;
    private String email;
    private String phoneNumber;
    private final DatabaseReference databaseReference;

    public ProfileRepository(){
        liveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            currUserId = firebaseUser.getUid();
            try {
                email = firebaseUser.getEmail();
            } catch (Exception e){
                email = null;
            }

            try {
                phoneNumber = firebaseUser.getPhoneNumber();
            } catch (Exception e){
                phoneNumber = null;
            }

        } else {
            //todo: occur an error, ask user to login again
            currUserId = "";
            email = null;
            phoneNumber = null;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public MutableLiveData<ProfileModel> getLiveData(){

        Query query = databaseReference.child("Users").child(currUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    ProfileModel profileModel = snapshot.getValue(ProfileModel.class);
                    liveData.setValue(profileModel);
                } catch (Exception e){
                    String name, photoUrl, aboutMe;
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

                    ProfileModel profileModel = new ProfileModel(name, photoUrl, aboutMe
                            , email, phoneNumber);
                    liveData.setValue(profileModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return liveData;
    }

    public void saveChanges(String name, String aboutMe){

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("aboutMe", aboutMe);
        databaseReference.child("Users").child(currUserId).updateChildren(map);

    }
}
