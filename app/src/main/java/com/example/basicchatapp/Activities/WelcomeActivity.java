package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.basicchatapp.Activities.MainActivity.MainActivity;
import com.example.basicchatapp.Activities.MessageActivity.MessageActivity;
import com.example.basicchatapp.Activities.SignInAndSignUp.LoginActivity;
import com.example.basicchatapp.Activities.SignInAndSignUp.SignUpActivity;
import com.example.basicchatapp.Utils.Constants;
import com.example.basicchatapp.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WELCOME ACTIVITY";
    private ActivityWelcomeBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference();
//        isAlreadySignedIn();
        checkIntent();
        listeners();

    }

    // set all the listeners in one method
    private void listeners(){
        binding.buttonWelcomeSignIn.setOnClickListener(view -> getToSignIN());
        binding.buttonWelcomeSignUp.setOnClickListener(view -> getToSignUp());
    }

    private void isAlreadySignedIn(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            Log.d(TAG, "isAlreadySignedIn: true");
            getToMain();
        } else {
            Log.d(TAG, "isAlreadySignedIn: false");
        }
    }

    private void checkIntent(){
        Log.d(TAG, "checkIntent extra: " + getIntent().getExtras());
        Log.d(TAG, "checkIntent intent?: " + getIntent());
        if(getIntent().getStringExtra(Constants.TARGET_USER_ID) == null){
            Log.d(TAG, "checkIntent: no extra intent");
            isAlreadySignedIn();
        } else {
            Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


            Log.d(TAG, "checkIntent: extra intent");

            String userId = getIntent().getStringExtra(Constants.TARGET_USER_ID);
            Log.d(TAG, "checkIntent: userId: " + userId);

            Query query = databaseReference.child("Users").child(userId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // get user data
                    String name = snapshot.child("name").getValue().toString();
                    String photoUrl = snapshot.child("photoUrl").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();

                    // create intent and send data to ChatActivity
                    Intent intent = new Intent(WelcomeActivity.this, MessageActivity.class);
                    intent.putExtra(Constants.TARGET_USER_ID, userId);
                    intent.putExtra(Constants.USER_NAME, name);
                    intent.putExtra(Constants.ONLINE_STATUS, status);
                    intent.putExtra(Constants.PHOTO_URL, photoUrl);
                    startActivity(mainIntent);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void getToMain(){
        Log.d(TAG, "getToMain: ");
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }
    // get to sign in screen
    private void getToSignIN(){
        Log.d(TAG, "getToSignIN: ");
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
    }

    // get to sign up screen
    private void getToSignUp(){
        Log.d(TAG, "getToSignUp: ");
        startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        binding = null;
    }
}