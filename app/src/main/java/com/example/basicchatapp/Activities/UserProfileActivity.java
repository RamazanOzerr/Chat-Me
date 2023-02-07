package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.BooleanNode;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class UserProfileActivity extends AppCompatActivity {

    ImageView p_image, likeImagebtn, msgImagebtn;
    TextView textUserName, textBio;
    Button likeBtn, msgBtn;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Boolean sent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        String username = intent.getStringExtra("name");
        String photoPath = intent.getStringExtra("photo");
        String bio = intent.getStringExtra("bio");
        String userKey = intent.getStringExtra("UserKey");

        init();
        setUserInfo(username,photoPath,bio);
        setBtnFunctions(userKey);
        checkIfRequestAlreadySent(userKey);

    }


    private void init(){
        p_image = findViewById(R.id.p_image);
        textUserName = findViewById(R.id.textUserName);
        textBio = findViewById(R.id.textBio);
        likeBtn = findViewById(R.id.likeBtn);
        msgBtn = findViewById(R.id.msgBtn);
        likeImagebtn = findViewById(R.id.likeImageBtn);
        msgImagebtn = findViewById(R.id.msgImageBtn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();  // curr user
        reference = FirebaseDatabase.getInstance().getReference();  // ref to reach db
        sent = false;
    }

    private void setUserInfo(String username, String photoPath, String bio){
        // recentChatsFragment tan alıyoruz bu bilgileri
        textUserName.setText(username);
        textBio.setText(bio);
        Picasso.get().load(photoPath).into(p_image);
    }

    private void setBtnFunctions(String userKey){

        msgImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrivateChatActivity.class);
                intent.putExtra("UserKey",userKey);
                startActivity(intent);
            }
        });
        likeImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLikeRequest(userKey);
            }
        });
//        msgBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), PrivateChatActivity.class);
//                intent.putExtra("UserKey",userKey);
//                startActivity(intent);
//            }
//        });
//
//        likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendLikeRequest(userKey);
//            }
//        });
    }
    private void sendLikeRequest(String userKey){
        Map map = new HashMap();
        map.put("isFriend","false");
        map.put("type","sent");

        reference.child("Requests").child(firebaseUser.getUid()).child(userKey).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"REQUEST HAS BEEN SENT",Toast.LENGTH_SHORT).show();
                map.put("type","taken");
                reference.child("Requests").child(userKey).child(firebaseUser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        likeImagebtn.setImageResource(R.drawable.taken);
                        sent = true;
//                        likeBtn.setText("REQUESTED");
//                        likeBtn.animate();
//                        likeBtn.setHintTextColor(390442);
                    }
                });

            }
        });
    }

    private void checkIfRequestAlreadySent(String userKey){
        reference.child("Requests").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(firebaseUser.getUid())){
                    String type = snapshot.child(firebaseUser.getUid())
                            .child("type").getValue().toString();

                    // eğer biz istek göndermişsek button src update ediyoruz
                    if(type.equals("taken")){
                        likeImagebtn.setImageResource(R.drawable.taken);
                        sent = true;

                        // eğer buton request gönderilmiş durumdayken butona basılırsa, isteği geri alacak
                        likeImagebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               if(sent){
                                   takeReqBack(userKey);
                               }else{
                                   sendLikeRequest(userKey);
                               }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void takeReqBack(String userKey){
        reference.child("Requests").child(firebaseUser.getUid()).child(userKey).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                reference.child("Requests").child(userKey).child(firebaseUser.getUid()).removeValue();
                likeImagebtn.setImageResource(R.drawable.sent);
                sent = false;
                Toast.makeText(getApplicationContext(), "REQUEST HAS BEEN CANCELED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

            }
        });
    }

}