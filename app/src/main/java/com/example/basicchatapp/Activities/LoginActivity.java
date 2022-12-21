package com.example.basicchatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.basicchatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView signInTextView, forgetPasswordId;
    EditText email, password;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String email_user, password_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInTextView = findViewById(R.id.signIntextView);
        forgetPasswordId = findViewById(R.id.forgetPasswordId);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        //TODO log out olmak için FirebaseAuth.getInstance().signOut();
        //TODO app in içine log out kısmı koyduğumuzda bu methodu kullancaz, sign in olduğu activity
        //TODO ye dönmenin yanı sıra bu methodu da çalıştırcaz işte
        firebaseAuth = FirebaseAuth.getInstance();
        openSignIn();
        login();


    }

    public void login(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_user = email.getText().toString().trim();
                password_user = password.getText().toString().trim();

                if (TextUtils.isEmpty(email_user)) {
                    email.setError("Email is required");

                }
                if (TextUtils.isEmpty(password_user)) {
                    password.setError("Password is required");
                }
                if (password.length() < 8) {
                    password.setError("Password must be at least 8 characters");
                }

                progressBar.setVisibility(View.VISIBLE);
                try {
                    firebaseAuth.signInWithEmailAndPassword(email_user , password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                catch (IllegalArgumentException e) {
                    Toast.makeText(LoginActivity.this, "You must enter the password",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }









            public void openSignIn() {
                signInTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    }
                });
            }


}