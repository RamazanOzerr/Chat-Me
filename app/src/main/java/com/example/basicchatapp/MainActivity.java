package com.example.basicchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fad = findViewById(R.id.floating_action_button);

        setNavigationButtonActivity();
        contacts();

    }

    public void contacts(){
        fad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO contacts ın olduğu activity veya fragment açılacak
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));

            }
        });
    }

    //TODO has not finished yet
    public void setNavigationButtonActivity(){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.page1){
//                    replaceFragments(new ChatFragment());
                    startActivity(new Intent(getApplicationContext(),PrivateChatActivity.class));

                }else if(item.getItemId() == R.id.page2){
                    replaceFragments(new ProfileFragment());
                }else if(item.getItemId() == R.id.page3){
                    replaceFragments(new SettingsFragment());
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