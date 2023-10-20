package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.R;
import com.example.basicchatapp.databinding.FragmentFriendsBinding;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    private List<FriendModel> friendModelList;
    private FriendsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FriendsViewModel viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        binding = FragmentFriendsBinding.inflate(inflater, container, false);

        friendModelList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewFragmentFriends.setLayoutManager(layoutManager);

        viewModel.getFriendsList().observe(getViewLifecycleOwner(), friendModels -> {
            friendModelList.clear();
            friendModelList.addAll(friendModels);
            adapter = new FriendsAdapter(friendModelList, getActivity());
            if(adapter.getItemCount() != 0){
                binding.linearFragmentFriendsNoFriend.setVisibility(View.GONE);
                binding.recyclerViewFragmentFriends.setAdapter(adapter);
            } else {
                binding.linearFragmentFriendsNoFriend.setVisibility(View.VISIBLE);
            }

        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}