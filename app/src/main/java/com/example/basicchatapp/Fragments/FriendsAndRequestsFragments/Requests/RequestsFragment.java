package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.R;
import com.example.basicchatapp.databinding.FragmentRequestsBinding;

import java.util.ArrayList;
import java.util.List;


public class RequestsFragment extends Fragment {


    private FragmentRequestsBinding binding;
    private List<RequestModel> requestModelList;
    private RequestsAdapter adapter;
    private RequestsViewModel viewModel;

    private final String TAG = "REQUESTS FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRequestsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        requestModelList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewFragmentRequests.setLayoutManager(layoutManager);

        binding.recyclerViewFragmentRequests.setVisibility(View.GONE);
        binding.linearFragmentRequestsNoRequest.setVisibility(View.VISIBLE);
        getData();


        viewModel.getLiveData().observe(getViewLifecycleOwner(), requestModels -> {
            // do nothing
            requestModelList.clear();
            requestModelList.addAll(requestModels);
            Log.d(TAG, "getData: model list: " + requestModelList.size());
            Log.d(TAG, "getData: model: " + requestModels.size());
            adapter = new RequestsAdapter(requestModelList);
            if(!requestModelList.isEmpty()){
                binding.linearFragmentRequestsNoRequest.setVisibility(View.GONE);
                binding.recyclerViewFragmentRequests.setVisibility(View.VISIBLE);
                Log.d(TAG, "onCreateView: request list" + requestModelList.size());
                binding.recyclerViewFragmentRequests.setAdapter(adapter);
            } else {
                binding.recyclerViewFragmentRequests.setVisibility(View.GONE);
                binding.linearFragmentRequestsNoRequest.setVisibility(View.VISIBLE);
            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void getData(){
        viewModel.getRequests().observe(getViewLifecycleOwner(), requestModels -> {
//            requestModelList.clear();
            requestModelList.addAll(requestModels);
            Log.d(TAG, "getData: model list: " + requestModelList.size());
            Log.d(TAG, "getData: model: " + requestModels.size());
            adapter = new RequestsAdapter(requestModelList);
            if(!requestModelList.isEmpty()){
                binding.linearFragmentRequestsNoRequest.setVisibility(View.GONE);
                binding.recyclerViewFragmentRequests.setAdapter(adapter);
            } else {
                binding.linearFragmentRequestsNoRequest.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}