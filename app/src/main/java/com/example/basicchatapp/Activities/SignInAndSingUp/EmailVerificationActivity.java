package com.example.basicchatapp.Activities.SignInAndSingUp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.databinding.ActivityEmailVerificationBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EmailVerificationActivity extends AppCompatActivity {

    private ActivityEmailVerificationBinding binding;
    private boolean isReset;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isReset = false;
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        listeners();
        timer();
    }

    // button listeners
    private void listeners(){
        binding.buttonEmailVerificationOpenGmailApp.setOnClickListener(view -> openGmailApp());
        binding.tvEmailVerificationSendAnotherEmail.setOnClickListener(view -> sendAnotherEmail());
        binding.tvEmailVerificationGetToSignIn.setOnClickListener(view -> getToSignIn());
    }

    private void openGmailApp(){
        Intent emailIntent = new Intent(Intent.ACTION_MAIN);
        emailIntent.addCategory(Intent.CATEGORY_APP_EMAIL);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line to handle back button behavior
        Intent chooser = Intent.createChooser(emailIntent, null);
        try{
            startActivity(chooser);
        } catch (Exception e){
            // do nothing
        }
    }

    // send another email if user do not get the email
    @SuppressLint("SetTextI18n")
    private void sendAnotherEmail(){
        if(isReset){
            // todo: we will implement this later
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;  // if user != null
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    HelperMethods.showLongToast(EmailVerificationActivity.this,
                            "we sent an email to verify your account");

                    binding.tvEmailVerificationTimer.setText("03.00");
                    timer();
                    isReset = false;
                }
            });
        } else {
            HelperMethods.showShortToast(EmailVerificationActivity.this
                    , "wait till timer is reset");
        }
    }

    // get to sign in screen if user already has an account
    private void getToSignIn(){
        startActivity(new Intent(EmailVerificationActivity.this, LoginActivity.class));
        finish();
    }

    private void getToSetUpProfile(){
        Intent intent = new Intent(EmailVerificationActivity.this
                , SetUpProfileActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void timer(){
        new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                binding.tvEmailVerificationTimer.setText(f.format(min) + ":" + f.format(sec));
            }

            @Override
            public void onFinish() {
                binding.tvEmailVerificationTimer.setText("00:00");
                isReset = true;
            }
        }.start();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if(user.isEmailVerified()){
            getToSetUpProfile();
            HelperMethods.showShortToast(this,"evet verified oldu");
        } else{
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User reauthenticated successfully, check email verification status again
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                getToSetUpProfile();
                                // User is now verified, continue with your logic
                            } else {
                                // Email verification still not complete
                                HelperMethods.showShortToast(this, "olmuyor aq");
                            }
                        } else {
                            // Reauthentication failed
                            HelperMethods.showShortToast(this,"reauth olmadı");
                        }
                    });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if(user.isEmailVerified()){
            getToSetUpProfile();
            HelperMethods.showShortToast(this,"evet verified oldu");
        } else{
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User reauthenticated successfully, check email verification status again
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                getToSetUpProfile();
                                // User is now verified, continue with your logic
                            } else {
                                // Email verification still not complete
                                HelperMethods.showShortToast(this, "olmuyor aq");
                            }
                        } else {
                            // Reauthentication failed
                            HelperMethods.showShortToast(this,"reauth olmadı");
                        }
                    });
        }
    }
}