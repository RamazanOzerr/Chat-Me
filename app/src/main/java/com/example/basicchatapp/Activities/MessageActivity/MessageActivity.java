package com.example.basicchatapp.Activities.MessageActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.basicchatapp.Notifications.Client;
import com.example.basicchatapp.Notifications.FCMResponse;
import com.example.basicchatapp.Services.APIService;
import com.example.basicchatapp.Utils.Constants;
import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.Utils.OneSignalAppID;
import com.example.basicchatapp.databinding.ActivityMessageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;
    private List<MessageModel> messageModelList;
    private MessageAdapter adapter;
    private DatabaseReference databaseReference;
    private String currUser, otherUser;
    private FirebaseUser firebaseUser;
    private final String TAG = "MESSAGE ACTIVITY";
    private Context context;

    // notification
    private APIService apiService;
    private Boolean notify;
    private String username, currUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getData();
        listeners();


//        //todo: one signal olayını muhtemelen iptal etcez
//        OneSignalAppID appID = new OneSignalAppID();
//
//        // OneSignal Initialization
//        OneSignal.initWithContext(this, appID.getONESIGNAL_APP_ID());
//
//        // optIn will show the native Android notification permission prompt.
//        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
//        OneSignal.getUser().getPushSubscription().optIn();

    }

    private void getNotificationToken(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByKey().equalTo(otherUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("token")){
                    String token = Objects.requireNonNull(snapshot.child("token").getValue()).toString();
                    Log.d(TAG, "onDataChange: we have the token" + token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void setUserInfo(String username, String online_status, String photoUrl){
        binding.textMessageActivityUsername.setText(username);
        binding.textMessageActivityStatus.setText(online_status);
        try{
            Picasso.get().load(photoUrl).into(binding.imageMessageActivityProfilePhoto);
        } catch (Exception e){
            // it will remain as default photo
        }

    }

    private void init(){

        //notification
        String url = "https://fcm.googleapis.com/";
        apiService = Client.getClient(url).create(APIService.class);
        notify = false;

        context = MessageActivity.this;

        Intent intent = getIntent();
        otherUser = intent.getStringExtra(Constants.TARGET_USER_ID);
        username = intent.getStringExtra(Constants.USER_NAME);
        currUsername = intent.getStringExtra(Constants.CURR_USERNAME);
        String online_status = intent.getStringExtra(Constants.ONLINE_STATUS);
        String photoUrl = intent.getStringExtra(Constants.PHOTO_URL);
        setUserInfo(username, online_status, photoUrl);

//        getNotificationToken();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            currUser = firebaseUser.getUid();

            Log.d(TAG, "init: currUser available");
        }else{
            //todo: user giriş yapmamış gibi gözüküyor, yeniden giriş yapmasının bir yolunu bul
            // Zaman aşımına uğradı gibi bir şey söyleyip yeniden giriş yapmasını isteyebiliriz
            currUser = "";
            Log.d(TAG, "init: currUser is not available");
        }
        //todo: this is for testing
//        currUser = "Okan Buruk";
//        otherUser = "Fatih Terim";

        messageModelList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewMessageActivity.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");


    }

    private void sendMessage(MessageModel message){
        // get a specific key for each message
        String messageId = databaseReference.child(currUser).child(otherUser).push().getKey();
        if(messageId != null){
            databaseReference.child(currUser).child(otherUser)
                    .child(messageId).setValue(message).addOnCompleteListener(task -> {
                        databaseReference.child(otherUser).child(currUser).child(messageId)
                                .setValue(message).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        binding.edittextMessageActivity.requestFocus();
                                        binding.edittextMessageActivity.setText("");
//                                        binding.recyclerViewMessageActivity
//                                                .smoothScrollToPosition(messageModelList.size());

                                        // notification
                                        String text = message.getText();
                                        if(notify){
                                            sendNotification(text);
                                            Log.d(TAG, "sendMessage: token: ");
                                        } else {
                                            Log.d(TAG, "sendMessage: token null aq ");
                                        }
                                        notify = false;

                                    }
                                });
                    });
        } else {  // key was not successfully created
            //todo: we should communicate with the ui in this scenario
            HelperMethods.showShortToast(MessageActivity.this
                    , "message cannot send, please try again later");
        }
    }

    private void sendNotification(String text){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByKey().equalTo(otherUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot: " +  snapshot.getValue());
                if(snapshot.exists()){
                    String token = Objects.requireNonNull(snapshot.child(otherUser)
                            .child("token").getValue()).toString();

                    Log.d(TAG, "onDataChange: we have the token" + token);

//                    // create json data
//                    JsonObject jsonData = new JsonObject();
//                    JsonObject jsonBody = new JsonObject();
//                    jsonData.addProperty("body", text);
//                    jsonData.addProperty("title", username);
//                    jsonBody.addProperty("to", token);
//                    Log.d(TAG, "sendNotification: token: " + token);
//                    jsonBody.add("notification", jsonData);


                    JsonObject jsonData = new JsonObject();
                    jsonData.addProperty("userId", currUser);

                    JsonObject jsonNotificiation = new JsonObject();
                    jsonNotificiation.addProperty("title", currUsername);
                    jsonNotificiation.addProperty("body", text);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("notification", jsonNotificiation);
                    jsonObject.add("data", jsonData);
                    jsonObject.addProperty("to", token);


                    // send post request
                    apiService.sendNotification(jsonObject).enqueue(new Callback<FCMResponse>() {
                            @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            Log.d(TAG, "onResponse: response: " + response);
                            Log.d(TAG, "onResponse: response: " + response.message());
                            Log.d(TAG, "onResponse: response: " + response.isSuccessful());
                            Log.d(TAG, "onResponse: call: " + call);

                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onFailure: database error " + error.getMessage());
            }
        });

    }

    private void getData(){
        // create messageViewModel instance
        MessageViewModel viewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        // get the messages when the activity first created
//        viewModel.getMessages().observe(this, messageModels -> {
//            messageModelList.clear();
//            messageModelList.addAll(messageModels);
//            Log.d(TAG, "getData: get messages: " + messageModels.size());
//            adapter = new MessageAdapter(messageModelList);
//            binding.recyclerViewMessageActivity.setAdapter(adapter);
//            binding.recyclerViewMessageActivity.smoothScrollToPosition(messageModelList.size());
//        });

        // get new messages
        viewModel.getNewMessages(currUser, otherUser).observe(this, messageModels -> {
            messageModelList.clear();
            messageModelList.addAll(messageModels);
//            for(int i = 0; i < messageModelList.size(); i++){
////                Log.d(TAG, "getData: get new messages: " + messageModels.get(i).getText());
//            }

            adapter = new MessageAdapter(messageModelList);
            binding.recyclerViewMessageActivity.setAdapter(adapter);
            binding.recyclerViewMessageActivity.smoothScrollToPosition(messageModelList.size());
        });
    }

    private void listeners(){

        binding.imageMessageActivityVideoCall.setOnClickListener(
                view -> HelperMethods.showShortToast(MessageActivity.this
                , "Video call feature will be added later"));

        binding.imageMessageActivityVoiceCall.setOnClickListener(
                view -> HelperMethods.showShortToast(MessageActivity.this
                , "Voice call feature will be added later"));

        binding.imageMessageActivityCamera.setOnClickListener(view -> {
            HelperMethods.showShortToast(MessageActivity.this
                    , "Sending image feature will be added later");
        });

        // we can load older messages here
        binding.swipeToRefreshMessageActivity.setOnRefreshListener(()
                -> binding.swipeToRefreshMessageActivity.setRefreshing(false));

        binding.imageMessageActivityBackArrow.setOnClickListener(view -> finish());
        binding.imageMessageActivitySend.setOnClickListener(view -> {
            notify = true;  // notification
//            Log.d(TAG, "listeners: text message" + binding.edittextMessageActivity.getText());
            if(binding.edittextMessageActivity.getText() != null
                    && !TextUtils.isEmpty(binding.edittextMessageActivity.getText())){
                String text = binding.edittextMessageActivity.getText().toString();
                String sender = currUser; //todo: bunu silcez, onun yerine
                //todo: aşağıda sender yerine direkt currUser yazcaz
                String time = getTime();
                sendMessage(new MessageModel(false, sender, text, time, "text"
                        , ServerValue.TIMESTAMP.toString(), System.currentTimeMillis()));
//                HelperMethods.showShortToast(context, ": " + System.currentTimeMillis());

            }else{
                HelperMethods.showShortToast(this,
                        "you cannot send an empty message");
            }
        });
    }

    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

}