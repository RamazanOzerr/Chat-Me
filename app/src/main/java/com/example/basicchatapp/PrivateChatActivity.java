package com.example.basicchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PrivateChatActivity extends AppCompatActivity {

    ImageView backImage;
    ImageView image;
    EditText editTextMessage;
    TextView textUserName;
    FloatingActionButton btnSend;

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        image = findViewById(R.id.image);
        backImage = findViewById(R.id.backImage);
        editTextMessage = findViewById(R.id.editTextMessage);
        textUserName = findViewById(R.id.textUserName);
        btnSend = findViewById(R.id.btnSend);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send message
                sendMessage(firebaseUser.getUid(),"demo","text",getDate(),false,editTextMessage.getText().toString());
                editTextMessage.setText("");
            }
        });

    }

//    public String getID(){
//        String id = getIntent().getExtras().getString("id").toString();
//        return id;
//    }

    public String getDate(){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy MM:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        return reportDate;
    }


    public void sendMessage(String userID, String otherID, String textType,
                            String date, Boolean seen, String message){

        String messageID = reference.child("Messages").child(userID).child(otherID).push().getKey();
        Map messageMap = new HashMap();
        messageMap.put("type",textType);
        messageMap.put("seen",seen);
        messageMap.put("time",date);
        messageMap.put("text",message);
        messageMap.put("from",userID);

        reference.child("Messages").child(userID).child(otherID).child(messageID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Messages").child(otherID).child(userID).child(messageID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });



    }





}