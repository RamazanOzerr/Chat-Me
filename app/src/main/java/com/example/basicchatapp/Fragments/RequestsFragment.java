package com.example.basicchatapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Requests;
import com.example.basicchatapp.Utils.RequestsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView requestsRecyclerView;
    FirebaseUser user;
    DatabaseReference reference;
    List<Requests> requestsList;



    //TODO şu MissingInflatedId olayına bakalım
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        requestsRecyclerView = view.findViewById(R.id.requestsRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        init();
        getRequests();







        return view;
    }
    private void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        requestsList = new ArrayList<>();

    }


    private void getRequests(){


        reference.child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.hasChild(user.getUid())){
                        if(dataSnapshot.child(user.getUid()).child("type").getValue()
                                .toString().equals("sent") && dataSnapshot.child(user.getUid())
                                .child("isFriend").getValue().toString().equals("false")){

                            String otherID = dataSnapshot.getKey();
                            reference.child("Users").child(otherID)
                                    .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String photoID = snapshot.child("photo").getValue().toString();
                                    String name = snapshot.child("name").getValue().toString();
                                    String bio = snapshot.child("bio").getValue().toString();
                                    requestsList.add(new Requests(photoID, name, bio, otherID));
                                    RequestsAdapter requestsAdapter = new RequestsAdapter(requestsList, getActivity(), getContext());
                                    requestsRecyclerView.setAdapter(requestsAdapter);
                                }



                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}