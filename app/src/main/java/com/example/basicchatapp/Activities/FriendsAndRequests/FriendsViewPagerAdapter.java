package com.example.basicchatapp.Activities.FriendsAndRequests;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends.FriendsFragment;
import com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests.RequestsFragment;

public class FriendsViewPagerAdapter extends FragmentStateAdapter {

    public FriendsViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FriendsFragment();
            case 1:
                return new RequestsFragment();
        }
        return new FriendsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
