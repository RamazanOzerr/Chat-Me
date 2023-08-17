package com.example.basicchatapp.Activities.SearchActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.basicchatapp.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set toolbar
        setSupportActionBar(binding.toolbarSearchPeople);
        // set back feature enable
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



    }
}