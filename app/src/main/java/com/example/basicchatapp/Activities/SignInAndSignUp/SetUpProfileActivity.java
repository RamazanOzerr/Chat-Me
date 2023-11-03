package com.example.basicchatapp.Activities.SignInAndSignUp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.basicchatapp.Activities.MainActivity.MainActivity;
import com.example.basicchatapp.Activities.ProfileActivity.ProfileModel;
import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.Utils.RandomString;
import com.example.basicchatapp.databinding.ActivitySetUpProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class SetUpProfileActivity extends AppCompatActivity {

    private ActivitySetUpProfileBinding binding;
    private FirebaseUser user;
    private String photoUrl, email, password;
    private StorageReference storageReference;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetUpProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        photoUrl = "";
        context = SetUpProfileActivity.this;
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        listeners();
    }

    private void listeners(){
        binding.imageSetupProfile.setOnClickListener(view -> setProfilePhoto());
        binding.buttonSetupProfileNext.setOnClickListener(view -> {
            if(binding.edittextSetupProfileName.getText() != null){
                String name = binding.edittextSetupProfileName.getText().toString();
                if(name.equals("")){
                    HelperMethods.showShortToast(context, "please enter your name first");
                } else {
                    saveData(name, photoUrl);
                }

            }
        });
    }

    private void saveData(String name, @Nullable String photoUrl){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ProfileModel profile = new ProfileModel(name, photoUrl
                , "online", "Hey there! I am using chat me");
        reference.child("Users").child(user.getUid()).setValue(profile)
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                login();
            }
        });
    }

    private void login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        HelperMethods.showShortToast(this,"logged in successfully");
                        startActivity(new Intent(this, MainActivity.class));
                    } else{  // if an error occurs, it will redirect user to the LoginScreen
                        HelperMethods.showShortToast(this,
                                "an error occur while logging in, please try again");
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                });

        finish();
    }

    private void setProfilePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK
                , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            final StorageReference ref = storageReference.child("Pictures")
                    .child(RandomString.getSaltString() + ".jpg");
            UploadTask uploadTask = ref.putFile(selectedImage);

                uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    photoUrl = downloadUri.toString();
                    binding.imageSetupProfile.setImageURI(selectedImage);

                }
            });
        }
    }
}