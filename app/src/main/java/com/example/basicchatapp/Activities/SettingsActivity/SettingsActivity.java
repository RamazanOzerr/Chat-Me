package com.example.basicchatapp.Activities.SettingsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.basicchatapp.Activities.ProfileActivity.ProfileActivity;
import com.example.basicchatapp.Activities.WelcomeActivity;
import com.example.basicchatapp.Utils.FirebaseMethods;
import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.databinding.ActivitySettingsBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private TextWatcher textWatcher;
    private List<String> settingsList;
    private Context context;
    private final String SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES";
    private final String SHARED_PREFERENCES_DARK_MODE = "SETTINGS_SHARED_PREFERENCES_DARK_MODE";
    private final String SHARED_PREFERENCES_PROFILE_LOCK = "SETTINGS_SHARED_PREFERENCES_PROFILE_LOCK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        listeners();

    }

    private void addSettings(){
        settingsList.add("dark mode");
        settingsList.add("profile lock");
        settingsList.add("notification");
        settingsList.add("logout");
        settingsList.add("delete account");

    }

    private void init(){

        settingsList = new ArrayList<>();
        addSettings();
        context = SettingsActivity.this;
        setUserName();

        getSettingPreferences();

        // set toolbar
        setSupportActionBar(binding.toolbarSettings);

        // back feature
        binding.toolbarSettings.setNavigationOnClickListener(view -> finish());


        // textWatcher is for watching any changes in editText
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // this function is called before text is edited
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this function is called when text is edited
                String str = s.toString().toLowerCase().trim();
                for(String setting : settingsList){
                    if(setting.toLowerCase().contains(str)){
                        // do nothing
                    } else {
                        binding.constraintSettings.setVisibility(View.GONE);
                        binding.linearSettingsNoSearchResult.setVisibility(View.VISIBLE);
                    }
                }
                System.out.println("editte"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this function is called after text is edited

            }
        };

    }

    private void setUserName(){
        FirebaseMethods firebaseMethods = new FirebaseMethods(context);
        firebaseMethods.getCurrUsername().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cUsername = snapshot.child("name").getValue().toString();
                binding.tvSettingsUsername.setText(cUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // set all listeners in one method
    private void listeners(){
        binding.constraintSettingsDeleteAccount
                .setOnClickListener(view ->  showAlertDeleteAcc());
        binding.constraintSettingsLogOut.setOnClickListener(view -> showAlertLogout());
        binding.edittextSettingsSearch.setOnClickListener(view -> searchSetting());
        binding.constraintSettingsProfile.setOnClickListener(view -> getToProfile());
        binding.constraintSettingsNotification
                .setOnClickListener(view -> getToNotificationSettings());
        binding.constraintSettingsPrivacy.setOnClickListener(view -> getToPrivacy());
    }


    private void getToNotificationSettings(){
        HelperMethods.showShortToast(context, "this feature will be added soon");
    }

    private void getToPrivacy(){
        HelperMethods.showShortToast(context, "this feature will be added soon");
    }
    private void getToProfile(){
        startActivity(new Intent(context, ProfileActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        setSettingsPreferences();
    }

    private void setSettingsPreferences(){
        SharedPreferences preference =
                getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        // set dark mode
        editor.putBoolean(SHARED_PREFERENCES_DARK_MODE
                , binding.switchMaterialSettingsDarkMode.isChecked());

        editor.putBoolean(SHARED_PREFERENCES_PROFILE_LOCK
                , binding.switchMaterialSettingsProfileLock.isChecked());

        editor.apply();

    }

    private void getSettingPreferences(){
        SharedPreferences preference =
                getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        boolean dark_mode = preference.getBoolean(SHARED_PREFERENCES_DARK_MODE, false);
        boolean profile_lock = preference.getBoolean(SHARED_PREFERENCES_PROFILE_LOCK, false);
        binding.switchMaterialSettingsDarkMode.setChecked(dark_mode);
        binding.switchMaterialSettingsProfileLock.setChecked(profile_lock);
    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(context, WelcomeActivity.class));
        HelperMethods.showShortToast(context, "signed out successfully");
    }

    private void searchSetting(){


        //todo: we shoud probably remove this listener
        binding.edittextSettingsSearch.addTextChangedListener(textWatcher);

    }

    // delete account and the all data permanently
    private void deleteAccount(){

    }

    private void showAlertLogout(){
        new MaterialAlertDialogBuilder(this).setTitle("Sign out")
                .setMessage("Do you want to sign out? you will need to sign in again")
                .setNegativeButton("no"
                        , (dialogInterface, i) -> HelperMethods.showShortToast(
                                context, "cancelled"))
                .setPositiveButton("yes"
                        , (dialogInterface, i) -> {
                            logOut();
                            HelperMethods.showShortToast(
                                    context, "signed out successfully");
                        }).show();
    }

    // ask user if really want to delete the account
    private void showAlertDeleteAcc(){
        new MaterialAlertDialogBuilder(this).setTitle("Delete account")
                .setMessage("Do you want to delete your account permanently? you will lose all of your data")
                .setNegativeButton("no"
                        , (dialogInterface, i) -> HelperMethods.showShortToast(
                                context, "cancelled"))
                .setPositiveButton("yes"
                        , (dialogInterface, i) -> {
                    deleteAccount();
                            HelperMethods.showShortToast(
                            context, "your account deleted successfully");
                }).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}