package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText email2Text, password2Text, password3Text;
    Button signButton;
    TextView loginText;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar2;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginText = findViewById(R.id.loginText);
        email2Text = findViewById(R.id.email2);
        password2Text = findViewById(R.id.password2);
        password3Text = findViewById(R.id.password3);
        signButton = findViewById(R.id.signInbutton);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });


        signUp();

    }

    public void signUp(){
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if(password.equals(password2) == false) {
                    Toast.makeText(RegisterActivity.this, "Passwords must be the same!",Toast.LENGTH_SHORT).show();
                    return;
                }


                progressBar2.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            startActivity(new Intent(getApplicationContext(),SignUpSetProfile.class));
//                            firebaseDatabase = FirebaseDatabase.getInstance();
//                            if(firebaseAuth.getUid() == null){
//                                Toast.makeText(getApplicationContext(),"NULL ÇIKTI",Toast.LENGTH_SHORT).show();
//                            }else{
//                                databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
//                                Map map = new HashMap();
//                                map.put("name","null");
//                                map.put("username","null");
//                                map.put("photo","null");
//                                map.put("bio","null");
//
//                                databaseReference.setValue(map);
//
//                                Toast.makeText(RegisterActivity.this, "Signed up Successfully",Toast.LENGTH_SHORT).show();
//                                openLogin();
//                            }


//
//                            Intent intent = new Intent(getApplicationContext(),UserProfileDesign.class);
//
//
//                            startActivity(intent);
//

                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }










    public void openLogin(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}