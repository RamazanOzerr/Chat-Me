package com.example.basicchatapp.Activities.UserProfileActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.basicchatapp.Activities.MessageActivity.MessageActivity;
import com.example.basicchatapp.Activities.ProfileActivity.ProfileViewModel;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Constants;
import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.databinding.ActivityUserProfileBinding;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    private Context context;
    private final String TAG = "USER PROFILE ACTIVITY";
    private String targetUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set toolbar
        setSupportActionBar(binding.toolbarUserProfile);
        // set back feature
        binding.toolbarUserProfile.setNavigationOnClickListener(view -> finish());
        context = UserProfileActivity.this;

        // get target user id
        Intent intent = getIntent();
        targetUserId = intent.getStringExtra(Constants.TARGET_USER_ID);
        // get data and set it to the view
        getData(targetUserId);
        // set listeners
        listeners();
    }

    private void listeners(){
        binding.linearUserProfileSendMessage.setOnClickListener(view -> getToMessageActivity());

        binding.linearUserProfilePhoneCall.setOnClickListener(view ->
                HelperMethods.showShortToast(context
                        , "phone call feature will be added later"));

        binding.linearUserProfileVideoCall.setOnClickListener(view ->
                HelperMethods.showShortToast(context
                        , "video call feature will be added later"));

    }

    // get to chat screen
    private void getToMessageActivity(){
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(Constants.TARGET_USER_ID, targetUserId);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void getData(String targetUserId){
        // progress dialog
        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading data...");
        progress.setIcon(R.mipmap.my_ic_launcher2_round);
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        // only be invoked when the dialog is cancelled
        progress.setOnCancelListener(dialogInterface -> {
            HelperMethods.showShortToast(context,"error");
            progress.cancel();
        });

        UserProfileViewModel viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        viewModel.getProfileData(targetUserId).observe(this, profileModel -> {
            Log.d(TAG, "getData: is empty?" + profileModel.toString());
            Log.d(TAG, "getData: name " + profileModel.getName());
            Log.d(TAG, "getData: photo url " + profileModel.getPhotoUrl());
            Log.d(TAG, "getData: about me " + profileModel.getAboutMe());
            binding.toolbarUserProfile.setTitle(profileModel.getName());  // set name
            try{  // set photo
                Picasso.get().load(profileModel.getPhotoUrl()).into(binding.imageUserProfilePp);
            } catch (Exception e){  // error loading photo
                HelperMethods.showShortToast(context, "could not load the photo");
            }
            binding.tvUserProfileAboutMe.setText(profileModel.getAboutMe());

            // dismiss progress if everything goes right
            progress.dismiss();

        });

    }
}