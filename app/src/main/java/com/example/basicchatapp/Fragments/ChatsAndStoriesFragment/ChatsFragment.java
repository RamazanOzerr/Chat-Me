package com.example.basicchatapp.Fragments.ChatsAndStoriesFragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Chats.ChatAdapter;
import com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Chats.ChatModel;
import com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Chats.ChatViewModel;
import com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Stories.StoriesAdapter;
import com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Stories.StoriesModel;
import com.example.basicchatapp.Fragments.ChatsAndStoriesFragment.Stories.StoriesViewModel;
import com.example.basicchatapp.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatAdapter adapter;
    private StoriesAdapter storiesAdapter;
    private List<ChatModel> chatModelList;
    private List<StoriesModel> storiesModelList;
    private Activity activity;


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
            adapter = new ChatAdapter(chatModelList, activity);
            binding.progressBarFragmentChats.setVisibility(View.GONE);
            if(adapter.getItemCount() != 0){
                binding.linearFragmantChatsNoChat.setVisibility(View.GONE);
                binding.recyclerViewChats.setVisibility(View.VISIBLE);
                binding.recyclerViewChats.setAdapter(adapter);
            } else {
                binding.recyclerViewChats.setVisibility(View.GONE);
                binding.linearFragmantChatsNoChat.setVisibility(View.VISIBLE);
            }

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
        activity = getActivity();
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