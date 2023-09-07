package com.example.basicchatapp.Utils;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests.RequestModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseMethods {

    private final DatabaseReference databaseReference;
    private final Context context;
    private String userId;
    private final String TAG = "FIREBASE METHODS";

    // public constructor
    public FirebaseMethods(Context context){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        this.context = context;
        if(firebaseUser != null){
            userId = firebaseUser.getUid();
        } else {
            userId = "";
        }
    }

    public void getFriendRequests(){
        Query query = databaseReference
                .child("Requests")
                .child(userId)
                .orderByChild("isFriend")
                .equalTo(false)
                .orderByChild("type")
                .equalTo("received");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendFriendRequest(String otherUserId){

        databaseReference
                .child("Requests")
                .child(userId)
                .child(otherUserId)
                .setValue(new RequestModel("sent", false, userId));

        databaseReference
                .child("Requests")
                .child(otherUserId)
                .child(userId)
                .setValue(new RequestModel("received", false, userId));

//        Query query = databaseReference
//                .child("Requests")
//                .child(userId)
//                .orderByChild("type")
//                .equalTo("seen");
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getValue() == null){
//                    Log.d(TAG, "onDataChange: " + snapshot);
//                    databaseReference
//                            .child("Requests")
//                            .child(userId)
//                            .child(otherUserId)
//                            .setValue(new RequestModel("sent", false, userId));
//
//                    databaseReference
//                            .child("Requests")
//                            .child(otherUserId)
//                            .child(userId)
//                            .setValue(new RequestModel("received", false, userId));
//                } else {
//                    HelperMethods.showShortToast(context, "you are already friends");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void cancelFriendsRequest(String otherUserId){

        databaseReference
                .child("Requests")
                .child(userId)
                .child(otherUserId)
                .removeValue().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        databaseReference
                                .child("Requests")
                                .child(otherUserId)
                                .child(userId)
                                .removeValue();
                    }
                });


//        Query query = databaseReference
//                .child("Requests")
//                .child(userId)
//                .orderByChild("type")
//                .equalTo("seen");
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getValue() == null){
//                    Log.d(TAG, "onDataChange: " + snapshot);
//                    databaseReference
//                            .child("Requests")
//                            .child(userId)
//                            .child(otherUserId)
//                            .removeValue().addOnCompleteListener(task -> {
//                                if(task.isSuccessful()){
//                                    databaseReference
//                                            .child("Requests")
//                                            .child(otherUserId)
//                                            .child(userId)
//                                            .removeValue();
//                                }
//                            });
//                } else {
//                    HelperMethods.showShortToast(context, "you are already friends");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    // update username
    public void updateUsername(String username){
        databaseReference
                .child(context.getString(R.string.db_users))
                .child(userId)
                .child(context.getString(R.string.db_users_username))
                .setValue(username);
    }

    // update photo url
    public void updatephotoUrl(String photoUrl){
        databaseReference
                .child(context.getString(R.string.db_users))
                .child(userId)
                .child(context.getString(R.string.db_users_photo))
                .setValue(photoUrl);
    }

    public void getNotificationToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                updateNotificationToken(token);
            }
        });

    }

    // update notification token
    public void updateNotificationToken(String token){
        databaseReference
                .child(context.getString(R.string.db_users))
                .child(userId)
                .child(context.getString(R.string.db_users_notification_token))
                .setValue(token);
    }

    // check if user name exists
    public void checkIfUsernameExist(String username){

        Query query = databaseReference
                .child(context.getString(R.string.db_users))
                .orderByChild(context.getString(R.string.db_users_username))
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    // update username
                }

                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.exists()){
                        // username exists
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
