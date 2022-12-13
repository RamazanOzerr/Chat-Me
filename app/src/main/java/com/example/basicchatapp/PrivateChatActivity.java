package com.example.basicchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    List<String> messageIDList;


    MessageAdapter messageAdapter;
    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    RecyclerView mmessagerecyclerview;
    ArrayList<MessageModel> messagesArrayList;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        msendmessagecardview = findViewById(R.id.carviewofsendmessage);
        mtoolbarofspecificchat=findViewById(R.id.toolbarofspecificchat);
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);
        messagesArrayList=new ArrayList<>();

        image = findViewById(R.id.image);
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


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        messageAdapter=new MessageAdapter(getApplicationContext(),messagesArrayList);
        mmessagerecyclerview.setAdapter(messageAdapter);

        intent = getIntent();

        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Toolbar is Clicked",Toast.LENGTH_SHORT).show();
                //TODO şimdilik boş bıraktık ama buraya tıklandığında
                // profil activity açılacak şekilde ayarlanabilir

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
                sendMessage(firebaseUser.getUid(),"demo","text",getDate(),false,editTextMessage.getText().toString());
//                getMessages(firebaseUser.getUid(),"demo");
                readData(firebaseUser.getUid(),"demo");
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
                    }
                });
            }
        });
    }


    // dunno if we can handle it with this method here tho
    public void readData(String userID, String otherID){

//        String messageID = reference.child("Messages").child(userID).child(otherID).push().getKey();
        String messageID = messageIDList.get(0);

        reference = FirebaseDatabase.getInstance().getReference("Messages");
        reference.child(userID).child(otherID).child(messageID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()){

                    if (task.getResult().exists()){

                        Toast.makeText(getApplicationContext(),"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String from = String.valueOf(dataSnapshot.child("from").getValue());
                        String seen = String.valueOf(dataSnapshot.child("seen").getValue());
                        String text = String.valueOf(dataSnapshot.child("text").getValue());
                        String time = String.valueOf(dataSnapshot.child("time").getValue());
                        String type = String.valueOf(dataSnapshot.child("type").getValue());
//
                        textUserName.setText(text);


                    }else {

                        Toast.makeText(getApplicationContext(),"User Doesn't Exist",Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(getApplicationContext(),"Failed to read",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



//    StackOverFLow dan çaldık

//    FirebaseDatabase.getInstance().getReference()
//            .child("messages")
//            .addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot rootSnapshot) {
//            if (rootSnapshot.exists()) {
//                final Map<String, Map<String, String>> messageNameMap = new HashMap<>();
//                for (DataSnapshot messageSnapshot : rootSnapshot.getChildren()) {
//                    final String messageKey = messageSnapshot.getKey();
//                    HashMap<String, String> nameMap = new HashMap<String, String>();
//                    if (messageSnapshot.child("name").exists()) {
//                        for (DataSnapshot nameSnapshot : messageSnapshot.child("name").getChildren()) {
//                            final String nameKey = nameSnapshot.getKey();
//                            final String nameValue = nameSnapshot.getValue(String.class);
//                            nameMap.put(nameKey, nameValue);
//                        }
//                    }
//                    if (!nameMap.isEmpty()) {
//                        messageNameMap.put(messageKey, nameMap);
//                    }
//                }
//
//                if (!messageNameMap.isEmpty()) {
//                    // Here messageNameMap contains your data as such
//
//                    // key: -Ko8bLawP4... val: Map Containing:
//                    //                          key: -Ko8wk... val: Jonathan Smith
//                    //                          key: -Ko8wk... val: Kapler Moe
//                }
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    });


    @Override
    public void onStart() {
        super.onStart();
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messageAdapter!=null)
        {
            messageAdapter.notifyDataSetChanged();
        }
    }



}