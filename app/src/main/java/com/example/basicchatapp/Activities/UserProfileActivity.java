package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.basicchatapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UserProfileActivity extends AppCompatActivity {

    private ImageView p_image, likeImagebtn, msgImagebtn;
    private TextView textUserName, textBio;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private Boolean sent;  // true if request been sent

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
        isAlreadyFriend(userKey);
        setUserInfo(username,photoPath,bio);
        setBtnFunctions(userKey);
        checkIfRequestAlreadySent(userKey);
    }


    private void init(){
        p_image = findViewById(R.id.p_image);
        textUserName = findViewById(R.id.textUserName);
        textBio = findViewById(R.id.textBio);
        likeImagebtn = findViewById(R.id.likeImageBtn);
        msgImagebtn = findViewById(R.id.msgImageBtn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();  // curr user
        reference = FirebaseDatabase.getInstance().getReference();  // ref to reach db
        sent = false;
        MaterialToolbar toolbar = findViewById(R.id.toolbar_user_profile_screen);
        setSupportActionBar(toolbar);
        toolbar.setTitle("PROFILE");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUserInfo(String username, String photoPath, String bio){
        // getting these info from recentChatsFragment
        textUserName.setText(username);
        textBio.setText(bio);
        Picasso.get().load(photoPath).into(p_image);
    }

    private void setBtnFunctions(String userKey){
        msgImagebtn.setOnClickListener(view -> reference.child("Requests")
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userKey)){
                    if(snapshot.child(userKey).child("isFriend").getValue()
                            .toString().equals("true")){
                        Intent intent = new Intent(getApplicationContext(),
                                PrivateChatActivity.class);
                        intent.putExtra("UserKey",userKey);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"YOU GOTTA BE FRIENDS TO CHAT",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        likeImagebtn.setOnClickListener(view -> {
            if(sent){
                takeReqBack(userKey);
            }
            else{
                sendLikeRequest(userKey);
            }
        });
    }
    private void sendLikeRequest(String userKey){

        Map<String, Object> map = new HashMap<>();
        map.put("isFriend","false");
        map.put("type","sent");

        reference.child("Requests").child(firebaseUser.getUid()).child(userKey)
                .setValue(map).addOnCompleteListener(task -> {
                    Toast.makeText(getApplicationContext(),"REQUEST HAS BEEN SENT",
                            Toast.LENGTH_SHORT).show();
                    map.put("type","taken");
                    reference.child("Requests").child(userKey).child(firebaseUser.
                            getUid()).setValue(map).addOnCompleteListener(task1 -> {
                                likeImagebtn.setImageResource(R.mipmap.ic_remove_friend);
                                sent = true;
                            });
                });
    }

    private void checkIfRequestAlreadySent(String userKey){
        reference.child("Requests").child(userKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(firebaseUser.getUid())){
                    String type = snapshot.child(firebaseUser.getUid())
                            .child("type").getValue().toString();

                    // update button src if already requested
                    if(type.equals("taken")){
                        likeImagebtn.setImageResource(R.mipmap.ic_remove_friend);
                        sent = true;

                        // take back req
                        likeImagebtn.setOnClickListener(view -> {
                           if(sent){
                               takeReqBack(userKey);
                           }else{
                               sendLikeRequest(userKey);
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
        reference.child("Requests").child(firebaseUser.getUid()).child(userKey)
                .removeValue((error, ref) -> {
                    reference.child("Requests").child(userKey)
                            .child(firebaseUser.getUid()).removeValue();
                    likeImagebtn.setImageResource(R.mipmap.ic_add_friend);
                    sent = false;
                    Toast.makeText(getApplicationContext(),
                            "REQUEST HAS BEEN CANCELED SUCCESSFULLY",
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void isAlreadyFriend(String userKey){
        reference.child("Requests").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(userKey)){
                        if(dataSnapshot.child("isFriend")
                                .getValue().toString().equals("true")){
                            likeImagebtn.setVisibility(View.GONE);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}