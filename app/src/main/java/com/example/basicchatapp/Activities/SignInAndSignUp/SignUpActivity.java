package com.example.basicchatapp.Activities.SignInAndSignUp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private boolean isReady;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        isReady = false;
        listeners();

    }

    private void listeners(){
        binding.buttonSignup.setOnClickListener(view -> signUpAndCreateAccount());
        binding.checkboxSignupTermConditions.setOnClickListener(view -> getTermsAndConditions());
        binding.tvSignupAlreadyHaveAccount.setOnClickListener(view -> signInScreen());
    }

    private void getTermsAndConditions(){
//        HelperMethods.showShortToast(SignUpActivity.this,
//                "terms and conditions are not available yet");
    }

    // sign up and create account
    private void signUpAndCreateAccount(){
        String email, password, password_confirm;
        if(binding.edittextSignupEmail.getText() != null){
            email = binding.edittextSignupEmail.getText().toString().trim();
            isReady = true;
        } else{
            email = "";
            binding.edittextSignupEmail.setError("email is required");
            isReady = false;
        }
        if(binding.edittextSignupPassword.getText() != null){
            password = binding.edittextSignupPassword.getText().toString().trim();
            isReady = true;
        } else{
            password = "";
            binding.edittextSignupPassword.setError("password is required");
            isReady = false;
        }
        if(password.length() < 8){
            binding.edittextSignupPassword.setError("password must be at least 8 characters");
            isReady = false;
        }

        if(binding.edittextSignupPasswordConfirm.getText() != null){
            password_confirm = binding.edittextSignupPasswordConfirm.getText().toString().trim();
            isReady = true;
        } else{
            password_confirm = "";
            binding.edittextSignupPasswordConfirm.setError("please confirm your password");
            isReady = false;
        }
        if(password_confirm.length() < 8){
            binding.edittextSignupPasswordConfirm.setError("password must be at least 8 characters");
            isReady = false;
        }

        if(!password.equals(password_confirm)){
            binding.edittextSignupPasswordConfirm.setError("passwords must be the same");
        }


        // create account
        if(isReady){
            binding.progressBarSignUp.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    emailverification();

                } else{
                    HelperMethods.showShortToast(SignUpActivity.this, "Authentication failed.");
                }
            });
        }
    }

    private void emailverification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;  // if user != null
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                HelperMethods.showLongToast(SignUpActivity.this,
                        "we sent an email to verify your account" +
                                ", please verify your account before trying to sign in");

                getToEmailVerification();

            }
        });
    }

    private void getToEmailVerification(){
        Intent intent = new Intent(SignUpActivity.this
                , EmailVerificationActivity.class);
        if(binding.edittextSignupEmail.getText() != null){
            intent.putExtra("email",binding.edittextSignupEmail.getText().toString());
        }
        if(binding.edittextSignupPassword.getText() != null){
            intent.putExtra("password",binding.edittextSignupPassword.getText().toString());
        }

        startActivity(intent);
        binding.progressBarSignUp.setVisibility(View.GONE);
    }

    // get to sign in screen
    private void signInScreen(){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish(); // destroy activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}