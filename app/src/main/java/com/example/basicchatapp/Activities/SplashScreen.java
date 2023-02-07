package com.example.basicchatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basicchatapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    ImageView image;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        image = findViewById(R.id.image);
        auth = FirebaseAuth.getInstance();

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        image.startAnimation(animation);

//        MediaPlayer music = MediaPlayer.create(SplashScreen.this, R.raw.ft1);
//        music.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity();
                finish();
//                music.stop();
            }
        },2400);
    }

    private void startActivity(){
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }
}