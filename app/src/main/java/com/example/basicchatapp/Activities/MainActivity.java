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

import com.example.basicchatapp.Fragments.MainScreenFragment;
import com.example.basicchatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fad;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fad = findViewById(R.id.floating_action_button);
        firebaseAuth = FirebaseAuth.getInstance();

        setNavigationButtonActivity();
        contacts();
        replaceFragments(new MainScreenFragment());

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
//                    startActivity(new Intent(getApplicationContext(), PrivateChatActivity.class));
                    Toast.makeText(getApplicationContext(),"HOP HEMŞEHRİM BU KISIM ÇALIŞMIYOR DAHA",Toast.LENGTH_LONG).show();
                }else if(item.getItemId() == R.id.page2){
//                    replaceFragments(new ProfileFragment());
                    replaceFragments(new MainScreenFragment());
                }else if(item.getItemId() == R.id.page3){
//                    replaceFragments(new SettingsFragment());
                    startActivity(new Intent(MainActivity.this, UserProfileDesign.class));
                }else if(item.getItemId() == R.id.page4){
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