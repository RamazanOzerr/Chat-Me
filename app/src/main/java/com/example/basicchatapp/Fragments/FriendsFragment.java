package com.example.basicchatapp.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Adapters.FriendsAdapter;
import com.example.basicchatapp.Utils.Requests;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    FirebaseUser user;
    DatabaseReference reference;
    private List<String> friendsIDlist;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        init();
        getFriendsIDs();


        return view;
    }

    private void init(){
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        friendsIDlist = new ArrayList<>();

    }

    private void getFriendsIDs(){

        reference.child("Requests").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendsIDlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    // we only check one side, no need to the other coz they both are the same
                    if(dataSnapshot.child("isFriend").getValue().toString().equals("true")){
                        friendsIDlist.add(dataSnapshot.getKey());
                    }
                }
//                System.out.println("friends keys: "+friendsIDlist);
                FriendsAdapter friendsAdapter = new FriendsAdapter(friendsIDlist,getActivity(),getContext());
//                System.out.println("sizee: "+friendsIDlist.size());
//                System.out.println("size: "+friendsAdapter.getItemCount());
                friendsRecyclerView.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}