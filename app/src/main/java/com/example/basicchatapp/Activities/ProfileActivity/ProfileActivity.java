package com.example.basicchatapp.Activities.ProfileActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.basicchatapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}