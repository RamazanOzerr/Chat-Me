package com.example.basicchatapp.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Utils.Profile;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = findViewById(R.id.recycler);

        List<Profile> profiles = fill_with_data();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(),profiles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public List<Profile> fill_with_data() {

        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile("User1", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User2", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User3", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User4", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User5", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User6", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User7", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User8", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User9", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User10", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User11", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User12", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User13", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User14", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User15", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User16", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User17", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User18", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User19", R.drawable.temporarypp,null,null));
        profiles.add(new Profile("User20", R.drawable.temporarypp,null,null));


        return profiles;
    }
}