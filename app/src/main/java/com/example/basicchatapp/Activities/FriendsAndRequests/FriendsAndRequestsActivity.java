package com.example.basicchatapp.Activities.FriendsAndRequests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.example.basicchatapp.databinding.ActivityFriendsAndRequestsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class FriendsAndRequestsActivity extends AppCompatActivity {

    private ActivityFriendsAndRequestsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendsAndRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<String> tabNames = new ArrayList<>();
        tabNames.add("Friends");
        tabNames.add("Requests");

        setSupportActionBar(binding.toolbarMain);
        binding.toolbarMain.setNavigationOnClickListener(view -> finish());

        FriendsViewPagerAdapter adapter = new FriendsViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        binding.viewpagerFriendsActivity.setAdapter(adapter);

        new TabLayoutMediator(binding.tablayoutFriendsActivity, binding.viewpagerFriendsActivity,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabNames.get(position));
                    }
                }).attach();


    }
}