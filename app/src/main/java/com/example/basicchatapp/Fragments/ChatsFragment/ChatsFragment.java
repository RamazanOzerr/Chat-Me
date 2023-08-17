package com.example.basicchatapp.Fragments.ChatsFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.Fragments.ChatsFragment.Stories.StoriesAdapter;
import com.example.basicchatapp.Fragments.ChatsFragment.Stories.StoriesModel;
import com.example.basicchatapp.Fragments.ChatsFragment.Stories.StoriesViewModel;
import com.example.basicchatapp.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatAdapter adapter;
    private StoriesAdapter storiesAdapter;
    private List<ChatModel> chatModelList;
    private List<StoriesModel> storiesModelList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ChatViewModel chatsViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        StoriesViewModel storiesViewModel =
                new ViewModelProvider(this).get(StoriesViewModel.class);

        binding = FragmentChatsBinding.inflate(inflater, container, false);
        init();

        chatsViewModel.getChats().observe(getViewLifecycleOwner(), chatModels -> {
            chatModelList.clear();
            chatModelList.addAll(chatModels);
            adapter = new ChatAdapter(chatModelList);
            binding.recyclerViewChats.setAdapter(adapter);
        });

        storiesViewModel.getLiveData().observe(getViewLifecycleOwner(), storiesModels -> {
            storiesModelList.clear();
            storiesModelList.addAll(storiesModels);
            storiesAdapter = new StoriesAdapter(storiesModelList);
            binding.recyclerViewChatsStories.setAdapter(storiesAdapter);
        });

        return binding.getRoot();
    }

    private void init(){
        chatModelList = new ArrayList<>();
        storiesModelList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewChats.setLayoutManager(layoutManager);

        // Linear Layout Manager
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);

        binding.recyclerViewChatsStories.setLayoutManager(horizontalLayout);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}