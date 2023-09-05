package com.example.basicchatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.basicchatapp.Activities.MainActivity.MainActivity;
import com.example.basicchatapp.Activities.SignInAndSingUp.LoginActivity;
import com.example.basicchatapp.Activities.SignInAndSingUp.SignUpActivity;
import com.example.basicchatapp.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAlreadySignedIn();
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
            getToMain();
        }
    }

    private void getToMain(){
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }
    // get to sign in screen
    private void getToSignIN(){
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
    }

    // get to sign up screen
    private void getToSignUp(){
        startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}