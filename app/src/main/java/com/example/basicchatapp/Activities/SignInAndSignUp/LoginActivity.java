package com.example.basicchatapp.Activities.SignInAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basicchatapp.Activities.MainActivity.MainActivity;
import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ActivityLoginBinding binding;
    private boolean isReady; // true if email and password are both valid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        isReady = false;
        listeners();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            doNothing();
        }
    }

    private void doNothing(){

    }

    private void listeners(){
        binding.buttonSignin.setOnClickListener(view -> signInViaEmail());
        binding.tvSigninDontHaveAccount.setOnClickListener(view -> signUp());
        binding.tvSigninForgotPassword.setOnClickListener(view -> resetPassword());
        binding.googleSignin.setOnClickListener(view -> signInGoogle());
        binding.constraintSignInPhone.setOnClickListener(view -> {

            // gives an animation like buttons have
            binding.constraintSignInPhone.animate()
                    .alpha(0.5f)
                    .scaleX(1.0f)
                    .setDuration(80)
                    .withEndAction(() -> {
                        // Restore the original properties
                        binding.constraintSignInPhone.setAlpha(1.0f);
                        binding.constraintSignInPhone.setScaleX(1.0f);
                    })
                    .start();

            signInViaPhoneNumber();

        });
    }

    //todo: we will add this feature later
    private void signInViaPhoneNumber(){

    }

    private void signInViaEmail(){
        String email, password;
        if(binding.edittextSigninEmail.getText() != null){
            email = binding.edittextSigninEmail.getText().toString().trim();
            isReady = true;
        } else{
            email = "";
            binding.edittextSigninEmail.setError("email is required");
            isReady = false;
        }
        if(binding.edittextSigninPassword.getText() != null){
            password = binding.edittextSigninPassword.getText().toString().trim();
            isReady = true;
        } else{
            password = "";
            binding.edittextSigninPassword.setError("password is required");
            isReady = false;
        }
        if(password.length() < 8){
            binding.edittextSigninPassword.setError("password must be at least 8 characters");
            isReady = false;
        }

        if(isReady){
            binding.progressBarSignIn.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            if(Objects.requireNonNull(firebaseAuth.getCurrentUser())
                                    .isEmailVerified()){
                                HelperMethods.showShortToast(LoginActivity.this,
                                        "signed in successfully");
                                binding.progressBarSignIn.setVisibility(View.VISIBLE);
                                signIn();

                            } else{
                                HelperMethods.showShortToast(LoginActivity.this
                                        , "please verify your account first");
                            }

                        } else{
                            HelperMethods.showShortToast(LoginActivity.this, "Authentication failed.");
                        }
                    });
        }
    }

    //todo: we will add this feature later
    private void signInGoogle(){

    }

    private void resetPassword(){
        if(binding.edittextSigninEmail.getText() != null){
            firebaseAuth.sendPasswordResetEmail
                            (binding.edittextSigninEmail.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            HelperMethods.showShortToast(this
                                    , "we sent an email to reset your password" +
                                            ", please check your mails");
                        }
            });
        }
    }

    private void signUp(){
        startActivity(new Intent(this, SignUpActivity.class));
        finish();  // destroy activity
    }

    // launch MainActivity
    private void signIn(){
        startActivity(new Intent(this, MainActivity.class));
        finish();  // destroy activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}