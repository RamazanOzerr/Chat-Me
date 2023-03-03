package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Adapters.MessageAdapterr;
import com.example.basicchatapp.Utils.MessageModel;
import com.example.basicchatapp.Utils.Profile;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateChatActivity extends AppCompatActivity {

    ImageView backImage;
    ImageView image;
    EditText editTextMessage;
    TextView textUserName, demoText;
    ImageButton btnSend;
    ScrollView scrollView;

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    MediaPlayer mediaPlayer;

    List<String> messageIDList;

    List<MessageModel> messageModelList;
    List<String> keyList;

    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    RecyclerView mmessagerecyclerview;
    ArrayList<MessageModel> messagesArrayList;
    Intent intent;
    RecyclerView chatRecyView;
    MessageAdapterr messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        messageModelList = new ArrayList<>();
        keyList = new ArrayList<>();
        chatRecyView = findViewById(R.id.recyclerviewofspecific);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        chatRecyView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapterr(keyList, PrivateChatActivity.this, PrivateChatActivity.this, messageModelList);
        chatRecyView.setAdapter(messageAdapter);

        scrollView = findViewById(R.id.scrollView);

        msendmessagecardview = findViewById(R.id.carviewofsendmessage);
        mtoolbarofspecificchat=findViewById(R.id.toolbarofspecificchat);
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);
        messagesArrayList=new ArrayList<>();

        image = findViewById(R.id.specificuserimageinimageview);
        backImage = findViewById(R.id.backImage);
        editTextMessage = findViewById(R.id.editTextMessage);
        textUserName = findViewById(R.id.textUserName);
        btnSend = findViewById(R.id.btnSend);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

//        demoText = findViewById(R.id.demoText);

        messageIDList = new ArrayList<>();
        Intent intent = getIntent();
        String userKey = intent.getStringExtra("UserKey");
        setUserInfo(userKey);

        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Toolbar is Clicked",Toast.LENGTH_SHORT).show();
                getUserInfo(userKey);
//                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

            }
        });



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
                String text = editTextMessage.getText().toString();
                if(text.equals("")){
                    Toast.makeText(getApplicationContext(),"write a message first",Toast.LENGTH_LONG).show();
                }else{
                    sendMessage(firebaseUser.getUid(),userKey,"text",getDate(),false, text);
//                getMessages(firebaseUser.getUid(),"demo");
//                readData(firebaseUser.getUid(),"demo");
                    editTextMessage.setText("");
                }

            }
        });

        loadMessage(userKey);
        scrollView.fullScroll(scrollView.FOCUS_DOWN);
    }

    public String getDate(){
//        SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }


    public void setUserInfo(String userKey){

        reference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);

                textUserName.setText(profile.getName());
                String photoPath = snapshot.child("photo").getValue().toString();
                Picasso.get().load(photoPath).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
    public void sendMessage(String userID, String otherID, String textType,
                            String date, Boolean seen, String message){

        String messageID = reference.child("Messages").child(userID).child(otherID).push().getKey();
        messageIDList.add(messageID);

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
                        // looks like we can leave here empty for now

//                        sendNotification("hahaha mesaj gönderdim hahah:  "+message);
//                        scrollView.fullScroll(scrollView.FOCUS_DOWN);

                    }
                });
            }
        });
    }

    // userKey değeri bizim other user ımızın id si
    public void loadMessage(String userKey){
        reference.child("Messages").child(firebaseUser.getUid()).child(userKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                System.out.println("sayi: "+messageModelList.size());
                messageAdapter.notifyDataSetChanged();
                keyList.add(userKey);
//                mmessagerecyclerview.smoothScrollToPosition(mmessagerecyclerview.getAdapter().getItemCount()+1);
                mmessagerecyclerview.smoothScrollToPosition(messageModelList.size());
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // UserProfileActivity ye göndermemiz gereken user info yu çekiyoruz db den
    private void getUserInfo(String userKey){
        reference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String photoPath = snapshot.child("photo").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                Intent intent1 = new Intent(getApplicationContext(),UserProfileActivity.class);
                intent1.putExtra("photo",photoPath);
                intent1.putExtra("name",name);
                intent1.putExtra("bio",bio);
                intent1.putExtra("UserKey",userKey);
                startActivity(intent1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    // contentText e önce username i, sonrasında da message ı gircez
    // username+": "+ message gibi
//    public void sendNotification(String contentText){
//        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "have no clue what it is")
//                .setSmallIcon(R.drawable.iconnotif)
//                .setContentTitle("New message")
//                .setContentText(contentText)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//
//// notificationId is a unique int for each notification that you must define
//        notificationManager.notify(1, builder.build()); // notificationID ye 1 verdik
//
//        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.notif);
//        mediaPlayer.start();
//    }

}