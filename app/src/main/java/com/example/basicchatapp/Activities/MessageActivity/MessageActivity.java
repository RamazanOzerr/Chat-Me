package com.example.basicchatapp.Activities.MessageActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.Utils.OneSignalAppID;
import com.example.basicchatapp.databinding.ActivityMessageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.onesignal.common.OneSignalUtils;
import com.onesignal.debug.LogLevel;
import com.onesignal.inAppMessages.IInAppMessageDidDismissEvent;
import com.onesignal.inAppMessages.IInAppMessageDidDisplayEvent;
import com.onesignal.inAppMessages.IInAppMessageLifecycleListener;
import com.onesignal.inAppMessages.IInAppMessageWillDismissEvent;
import com.onesignal.inAppMessages.IInAppMessageWillDisplayEvent;
import com.onesignal.inAppMessages.internal.display.impl.OneSignalBounceInterpolator;
import com.onesignal.internal.OneSignalImp;
import com.onesignal.notifications.internal.display.impl.NotificationDisplayBuilder;
import com.onesignal.user.subscriptions.IPushSubscriptionObserver;
import com.onesignal.user.subscriptions.PushSubscriptionChangedState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;
    private List<MessageModel> messageModelList;
    private MessageAdapter adapter;
    private HelperMethods helper;
    private DatabaseReference databaseReference;
    private String currUser, otherUser, username;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getData();
        listeners();

        //todo: one signal olayını muhtemelen iptal etcez
        OneSignalAppID appID = new OneSignalAppID();

        // OneSignal Initialization
        OneSignal.initWithContext(this, appID.getONESIGNAL_APP_ID());

        // optIn will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.getUser().getPushSubscription().optIn();

    }

    private void sendMessage(MessageModel message){
        // get a specific key for each message
        String messageId = databaseReference.child(currUser).child(otherUser).push().getKey();
        if(messageId != null){
            databaseReference.child(currUser).child(otherUser)
                    .child(messageId).setValue(message).addOnCompleteListener(task -> {
                        databaseReference.child(otherUser).child(currUser).child(messageId)
                                .setValue(message).addOnCompleteListener(task1 -> {
                                    binding.edittextMessageActivity.requestFocus();
                                    binding.edittextMessageActivity.setText("");
                                    binding.recyclerViewMessageActivity
                                            .smoothScrollToPosition(messageModelList.size());
                                });
                    });
        } else {  // key was not successfully created
            //todo: we should communicate with the ui in this scenario
        }
    }

    private void getData(){
        // create messageViewModel instance
        MessageViewModel viewModel = new ViewModelProvider(this
                , new MessageViewModelFactory(currUser, otherUser)).get(MessageViewModel.class);

        // get the messages when the activity first created
        viewModel.getMessages().observe(this, messageModels -> {
            messageModelList.clear();
            messageModelList.addAll(messageModels);
            adapter = new MessageAdapter(messageModelList);
            binding.recyclerViewMessageActivity.setAdapter(adapter);
            binding.recyclerViewMessageActivity.smoothScrollToPosition(messageModelList.size());
        });

        // get new messages
        viewModel.getNewMessages().observe(this, messageModels -> {
            messageModelList.clear();
            messageModelList.addAll(messageModels);
            adapter = new MessageAdapter(messageModelList);
            binding.recyclerViewMessageActivity.setAdapter(adapter);
            binding.recyclerViewMessageActivity.smoothScrollToPosition(messageModelList.size());
        });
    }

    private void listeners(){

        binding.imageMessageActivityVideoCall.setOnClickListener(
                view -> helper.showShortToast(MessageActivity.this
                , "Video call feature will be added later"));

        binding.imageMessageActivityVoiceCall.setOnClickListener(
                view -> helper.showShortToast(MessageActivity.this
                , "Voice call feature will be added later"));

        binding.imageMessageActivityCamera.setOnClickListener(view -> {
            helper.showShortToast(MessageActivity.this
                    , "Sending image feature will be added later");
        });

        // we can load older messages here
        binding.swipeToRefreshMessageActivity.setOnRefreshListener(()
                -> binding.swipeToRefreshMessageActivity.setRefreshing(false));

        binding.imageMessageActivityBackArrow.setOnClickListener(view -> finish());
        binding.imageMessageActivitySend.setOnClickListener(view -> {
            if(binding.edittextMessageActivity.getText() != null){
                String text = binding.edittextMessageActivity.getText().toString();
                String sender = currUser; //todo: bunu silcez, onun yerine
                //todo: aşağıda sender yerine direkt currUser yazcaz
                String time = getTime();
                sendMessage(new MessageModel(false, sender, text, time, "text"));

            }else{
                helper.showShortToast(this,
                        "you cannot send an empty message");
            }
        });


    }

    private void init(){
//        otherUser = getIntent().getStringExtra("otherUser");
//        username = getIntent().getStringExtra("username");
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(firebaseUser != null){
//            currUser = firebaseUser.getUid();
//        }else{
//            //todo: user giriş yapmamış gibi gözüküyor, yeniden giriş yapmasının bir yolunu bul
//            // Zaman aşımına uğradı gibi bir şey söyleyip yeniden giriş yapmasını isteyebiliriz
//            currUser = "";
//        }
        //todo: this is for testing
        currUser = "Okan Buruk";
        otherUser = "Fatih Terim";

        messageModelList = new ArrayList<>();
        helper = new HelperMethods();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewMessageActivity.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");

    }

    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

}