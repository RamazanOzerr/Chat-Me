package com.example.basicchatapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.ChatsFragment;
import com.example.basicchatapp.Fragments.GroupFragment.GroupFragment;
import com.example.basicchatapp.Fragments.MainScreenFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ChatsFragment();
            case 1:
                return new GroupFragment();
            case 2:
                return new MainScreenFragment();  //todo: burayı güncelle
        }
        return new ChatsFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
