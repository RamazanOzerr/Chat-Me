package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.basicchatapp.Fragments.FriendsFragment;
import com.example.basicchatapp.Fragments.MainScreenFragment;
import com.example.basicchatapp.Fragments.RecentChatsFragment;
import com.example.basicchatapp.Fragments.RequestsFragment;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Adapters.CustomViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    ViewPager viewPager;
    //Tablayoutu fragment içine tanımlattık
    TabLayout tabLayout;
    CustomViewPager customViewPager;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        firebaseAuth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.viewpager_message);
        tabLayout = findViewById(R.id.tablayout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CHATHUB");


        customViewPager = new CustomViewPager(getSupportFragmentManager());
        customViewPager.addFragment(new RecentChatsFragment(),"Chats");
        customViewPager.addFragment(new MainScreenFragment(),"Users");
        customViewPager.addFragment(new RequestsFragment(),"Requests");
        customViewPager.addFragment(new FriendsFragment(),"Friends");

        viewPager.setAdapter(customViewPager);

        tabLayout.setupWithViewPager(viewPager);

//        setNavigationButtonActivity();
//        replaceFragments(new RecentChatsFragment());
    }

    //TODO has not finished yet
    public void setNavigationButtonActivity(){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.page1){
//                    replaceFragments(new ChatFragment());
//                    startActivity(new Intent(getApplicationContext(), PrivateChatActivity.class));
                    Toast.makeText(getApplicationContext(),"RECENT CHATS",Toast.LENGTH_SHORT).show();
                    replaceFragments(new RecentChatsFragment());
//                    Toast.makeText(getApplicationContext(),"HOP HEMŞEHRİM BU KISIM ÇALIŞMIYOR DAHA",Toast.LENGTH_LONG).show();
                }else if(item.getItemId() == R.id.page2){
//                    replaceFragments(new ProfileFragment());
                    Toast.makeText(getApplicationContext(),"ALL THE USERS",Toast.LENGTH_SHORT).show();
                    replaceFragments(new MainScreenFragment());
                }else if(item.getItemId() == R.id.page3){
                    Toast.makeText(getApplicationContext(),"FRIENDS",Toast.LENGTH_SHORT).show();
                    replaceFragments(new FriendsFragment());
//                    startActivity(new Intent(MainActivity.this, UserProfileDesign.class));
                }else if(item.getItemId() == R.id.page4){
                    Toast.makeText(getApplicationContext(),"FRIEND REQUESTS",Toast.LENGTH_SHORT).show();
                    replaceFragments(new RequestsFragment());
                }
                else if(item.getItemId() == R.id.page5){
                    startActivity(new Intent(MainActivity.this, UserProfileDesign.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplicationContext(),"CLICKED SEARCH",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(),UserProfileDesign.class));
                return true;
            case R.id.log_out:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}