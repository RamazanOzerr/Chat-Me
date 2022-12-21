package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.MessageAdapterr;
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
        //TODO alttaki method için yeni constructor yazmamızı istedi, default oluşturduk, yeniden bak
        messageAdapter = new MessageAdapterr(keyList, PrivateChatActivity.this, getApplicationContext(), messageModelList);
        chatRecyView.setAdapter(messageAdapter);


        msendmessagecardview = findViewById(R.id.carviewofsendmessage);
        mtoolbarofspecificchat=findViewById(R.id.toolbarofspecificchat);
//        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);
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

//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
//        //TODO ALTTAKI SATIRI HATA GITSIN DIYE YORUM SATIRINA ALDIK, ONU DUZELT
//        // HATA CIKMA SEBEBI, MESSAGE ADAPTER IN ICINE USER ADAPTER SINIFINI KOPYALAMIS OLMAMIZ
////        messageAdapter=new MessageAdapter(getApplicationContext(),messagesArrayList);
//        mmessagerecyclerview.setAdapter(messageAdapter);

        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Toolbar is Clicked",Toast.LENGTH_SHORT).show();
                //TODO şimdilik boş bıraktık ama buraya tıklandığında
                // profil activity açılacak şekilde ayarlanabilir

            }
        });

        Intent intent = getIntent();
        String userKey = intent.getStringExtra("UserKey");
        setUserInfo(userKey);

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
                sendMessage(firebaseUser.getUid(),userKey,"text",getDate(),false,editTextMessage.getText().toString());
//                getMessages(firebaseUser.getUid(),"demo");
//                readData(firebaseUser.getUid(),"demo");
                editTextMessage.setText("");

            }
        });



        loadMessage(userKey);
//        getMessage(userKey);
    }

//    public String getID(){
//        String id = getIntent().getExtras().getString("id").toString();
//        return id;
//    }

    // TODO bunu şu an kullanmıyoruz, ama belki sonra kullanılabilir
    public String getDate(){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy MM:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        return reportDate;
    }



    public void setUserInfo(String userKey){

        reference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);

                textUserName.setText(profile.getName());
                String photoPath = snapshot.child("photo").getValue().toString();
                Picasso.get().load(photoPath).into(image);
                Toast.makeText(getApplicationContext(),photoPath,Toast.LENGTH_LONG).show();
//                Glide.with(getApplicationContext()).load(photoPath).into(image);

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
                    }
                });
            }
        });
    }

    // TODO hocanınkine alternatif kendi methodumuzu yazmaya çalıştık, ama bu method işe yaramaz
    // TODO şimdilik dursun, sonra silicez

    public void getMessage(String userKey){
        reference.child("Messages").child(userKey).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String text = snapshot.child("-NJo0ud3_l6vPX7YqOUU").child("text").getValue().toString();
                MessageModel messageModel = new MessageModel(userKey, false, text, null, null);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                keyList.add(snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // userKey değeri bizim other user ımızın id si
    public void loadMessage(String userKey){
        reference.child("Messages").child(firebaseUser.getUid()).child(userKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                System.out.println(snapshot.getChildren());
                System.out.println("model: " + messageModel.toString());
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                keyList.add(snapshot.getKey());
                System.out.println(keyList);
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





    // dunno if we can handle it with this method here tho
//    public void readData(String userID, String otherID){
//
////        String messageID = reference.child("Messages").child(userID).child(otherID).push().getKey();
//        String messageID = messageIDList.get(0);
//
//        reference = FirebaseDatabase.getInstance().getReference("Messages");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                MessageModel messages = snapshot.getValue(MessageModel.class);
//                String text = snapshot.child(messageID).child(userID).child(otherID).getValue().toString();
//                textUserName.setText(text);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
////        reference.child(userID).child(otherID).child(messageID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DataSnapshot> task) {
////
////                if (task.isSuccessful()){
////
////                    if (task.getResult().exists()){
////
////                        Toast.makeText(getApplicationContext(),"Successfully Read",Toast.LENGTH_SHORT).show();
////                        DataSnapshot dataSnapshot = task.getResult();
////                        String from = String.valueOf(dataSnapshot.child("from").getValue());
////                        String seen = String.valueOf(dataSnapshot.child("seen").getValue());
////                        String text = String.valueOf(dataSnapshot.child("text").getValue());
////                        String time = String.valueOf(dataSnapshot.child("time").getValue());
////                        String type = String.valueOf(dataSnapshot.child("type").getValue());
//////
////                        textUserName.setText(text);
////                        MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(),messagesArrayList);
////                        messageAdapter.notifyDataSetChanged();
////
////                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
////                        linearLayoutManager.setStackFromEnd(true);
////                        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
////                        messageAdapter=new MessageAdapter(getApplicationContext(),messagesArrayList);
////                        mmessagerecyclerview.setAdapter(messageAdapter);
////
////
////                    }else {
////
////                        Toast.makeText(getApplicationContext(),"User Doesn't Exist",Toast.LENGTH_SHORT).show();
////
////                    }
////
////
////                }else {
////
////                    Toast.makeText(getApplicationContext(),"Failed to read",Toast.LENGTH_SHORT).show();
////                }
////
////            }
////        });
//
//
//    }



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


//    @Override
//    public void onStart() {
//        super.onStart();
//        messageAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if(messageAdapter!=null)
//        {
//            messageAdapter.notifyDataSetChanged();
//        }
//    }



}