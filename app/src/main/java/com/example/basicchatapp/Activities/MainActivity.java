package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.basicchatapp.Fragments.FriendsFragment;
import com.example.basicchatapp.Fragments.MainScreenFragment;
import com.example.basicchatapp.Fragments.RecentChatsFragment;
import com.example.basicchatapp.Fragments.RequestsFragment;
import com.example.basicchatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        firebaseAuth = FirebaseAuth.getInstance();

        setNavigationButtonActivity();
        replaceFragments(new RecentChatsFragment());

    }

    //TODO has not finished yet
    public void setNavigationButtonActivity(){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.page1){
//                    replaceFragments(new ChatFragment());
//                    startActivity(new Intent(getApplicationContext(), PrivateChatActivity.class));
                    replaceFragments(new RecentChatsFragment());
                    Toast.makeText(getApplicationContext(),"HOP HEMŞEHRİM BU KISIM ÇALIŞMIYOR DAHA",Toast.LENGTH_LONG).show();
                }else if(item.getItemId() == R.id.page2){
//                    replaceFragments(new ProfileFragment());
                    replaceFragments(new MainScreenFragment());
                }else if(item.getItemId() == R.id.page3){
                    replaceFragments(new FriendsFragment());
//                    startActivity(new Intent(MainActivity.this, UserProfileDesign.class));
                }else if(item.getItemId() == R.id.page4){
                    replaceFragments(new RequestsFragment());
                }
                else if(item.getItemId() == R.id.page5){
                    firebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return false;
            }
        });
    }


    public void replaceFragments(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}