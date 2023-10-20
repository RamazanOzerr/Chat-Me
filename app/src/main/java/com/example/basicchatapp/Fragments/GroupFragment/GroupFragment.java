package com.example.basicchatapp.Fragments.GroupFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.databinding.FragmentGroupBinding;
import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {

    private FragmentGroupBinding binding;
    private List<GroupModel> groupModelsList;
    private GroupAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GroupViewModel groupViewModel =
                new ViewModelProvider(this).get(GroupViewModel.class);

        binding = FragmentGroupBinding.inflate(inflater, container, false);
        init();

        groupViewModel.getGroups().observe(getViewLifecycleOwner(), groupModels -> {
            groupModelsList.clear();
            groupModelsList.addAll(groupModels);
            adapter = new GroupAdapter(groupModels);
            if (adapter.getItemCount() >= 1) { // we set as 1 because
                // the first item in the list is "create new group view"
                binding.linearGroupFragmentNoGroup.setVisibility(View.GONE);
                binding.recyclerViewFragmentGroup.setVisibility(View.VISIBLE);
                binding.recyclerViewFragmentGroup.setAdapter(adapter);
            } else if(adapter.getItemCount() == 0){
                binding.recyclerViewFragmentGroup.setVisibility(View.GONE);
                binding.linearGroupFragmentNoGroup.setVisibility(View.VISIBLE);
            }

        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    // handle initialization
    private void init(){
        groupModelsList = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.recyclerViewFragmentGroup.setLayoutManager(layoutManager);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}