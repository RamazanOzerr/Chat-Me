package com.example.basicchatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
public class RegisterActivity extends AppCompatActivity {

    private EditText email2Text, password2Text, password3Text;
    private Button signButton;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView loginText = findViewById(R.id.loginText);
        email2Text = findViewById(R.id.email2);
        password2Text = findViewById(R.id.password2);
        password3Text = findViewById(R.id.password3);
        signButton = findViewById(R.id.signInbutton);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);
        MaterialToolbar toolbar = findViewById(R.id.toolBar_signup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginText.setOnClickListener(view -> openLogin());


        signUp();

    }

    public void signUp(){
        signButton.setOnClickListener(v -> {
            String email = email2Text.getText().toString().trim();
            String password =  password2Text.getText().toString().trim();
            String password2 = password3Text.getText().toString().trim();



            if(TextUtils.isEmpty(email)) {
                email2Text.setError("Email is required");
                return;

            }
            if(TextUtils.isEmpty(password)) {
                password2Text.setError("Password is required");
                return;
            }
            if(password.length() < 8) {
                password3Text.setError("Password must be at least 8 characters");
                return;
            }
            if(!password.equals(password2)) {
                Toast.makeText(RegisterActivity.this, "Passwords must be the same!",Toast.LENGTH_SHORT).show();
                return;
            }


            progressBar2.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(task -> {

                if(task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(),SignUpSetProfile.class));
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Error!" , Toast.LENGTH_LONG).show();

                }
            });
        });
    }

    public void openLogin(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}