package com.example.basicchatapp.Activities.ProfileActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.databinding.ActivityProfileBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private Context context;
    private ProfileRepository repository;
    private boolean isChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new ProfileRepository();
        context = ProfileActivity.this;
        isChanged = false;
        setSupportActionBar(binding.toolbarProfileUpdate);
        getData();
        binding.toolbarProfileUpdate.setNavigationOnClickListener(view -> {
            if(isChanged){
                new MaterialAlertDialogBuilder(context)
                        .setTitle("DISCARD CHANGES")
                        .setMessage("if you go back now, your draft will be discarded")
                        .setNegativeButton("discard", (dialogInterface, i) -> {
                            finish();  // finish the activity
                        })
                        .setPositiveButton("keep", (dialogInterface, i) -> {
                            saveChanges();
                            finish();
                        }).show();
            } else {
                finish();
            }
        });

        listeners();
    }

    private void listenChanges(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isChanged = true;
            }
        };

        binding.edittextProfileUpdateName.addTextChangedListener(textWatcher);
        binding.edittextProfileUpdateAboutMe.addTextChangedListener(textWatcher);
        binding.edittextProfileUpdateEmail.addTextChangedListener(textWatcher);

    }


    @SuppressLint("SetTextI18n")
    private void getData(){
        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading data...");
        progress.setIcon(R.mipmap.my_ic_launcher2_round);
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        ProfileViewModel viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.getProfileData().observe(this, profileModel -> {
            binding.edittextProfileUpdateName.setText(profileModel.getName());
            try{
                Picasso.get().load(profileModel.getPhotoUrl()).into(binding.imageProfileUpdatePp);
            } catch (Exception e){
                HelperMethods.showShortToast(context, "could not load the photo");
            }
            binding.edittextProfileUpdateAboutMe.setText(profileModel.getAboutMe());
            if(profileModel.getEmail() != null){
                binding.edittextProfileUpdateEmail.setText(profileModel.getEmail());
            } else {
                binding.edittextProfileUpdateEmail.setText("you did not provide your email yet");
            }
            if(profileModel.getPhoneNumber() != null){
                binding.edittextUpdateProfilePhoneNumber.setText(profileModel.getPhoneNumber());
            } else {
                binding.edittextUpdateProfilePhoneNumber.setText("you did not provide a phone number yet");
            }

            progress.dismiss();
            listenChanges();
        });

    }

    private void listeners(){
        binding.buttonUpdateProfile.setOnClickListener(view -> saveChanges());
    }

    private void saveChanges(){
        String name, aboutMe;

        if(binding.edittextProfileUpdateName.getText() != null){
            name = binding.edittextProfileUpdateName.getText().toString();
        } else {
            name = "";
        }

        if(binding.edittextProfileUpdateAboutMe.getText() != null){
            aboutMe = binding.edittextProfileUpdateAboutMe.getText().toString();
        } else {
            aboutMe = "";
        }

        repository.saveChanges(name, aboutMe);
        HelperMethods.showShortToast(context, "saved");
        finish();

    }
}