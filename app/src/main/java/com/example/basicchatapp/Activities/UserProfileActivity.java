package com.example.basicchatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basicchatapp.Fragments.RecentChatsFragment;
import com.example.basicchatapp.R;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    ImageView p_image;
    TextView textUserName, textBio;
    Button likeBtn, msgBtn;
    ImageButton backImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        init();
        setUserInfo();
        setBtnFunctions();
    }


    private void init(){
        p_image = findViewById(R.id.p_image);
        textUserName = findViewById(R.id.textUserName);
        textBio = findViewById(R.id.textBio);
        likeBtn = findViewById(R.id.likeBtn);
        msgBtn = findViewById(R.id.msgBtn);
        backImageBtn = findViewById(R.id.backImageBtn);
    }

    private void setUserInfo(){
        Intent intent = getIntent();
        String username = intent.getStringExtra("name");
        String photoPath = intent.getStringExtra("photo");
        textUserName.setText(username);
        Picasso.get().load(photoPath).into(p_image);
    }

    private void setBtnFunctions(){
//        backImageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                replaceFragments(new RecentChatsFragment());
//            }
//        });
    }



    public void replaceFragments(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}