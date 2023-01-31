package com.example.basicchatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.Activities.PrivateChatActivity;
import com.example.basicchatapp.Activities.UserProfileDesign;
import com.example.basicchatapp.Utils.Profile;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainScreenFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> userKeyList;
    RecyclerView userListRecyclerView;
    View view;
    UserAdapter userAdapter;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        define();
        getUsers();

        return view;
    }


    public void define(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        userKeyList = new ArrayList<>();
        userListRecyclerView = view.findViewById(R.id.userListRecyclerView);
        // alttaki snapCount değişkeni bir satırda kaç tane görüntülenmesini istediğimizi belirler
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(),1);
        userListRecyclerView.setLayoutManager(mng);
        userAdapter = new UserAdapter(userKeyList,getActivity(),getContext());
        userListRecyclerView.setAdapter(userAdapter);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    public void getUsers(){
        reference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                reference.child("Users").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile user1 = snapshot.getValue(Profile.class);

                        if(!user1.getUsername().equals("null") && !snapshot.getKey().equals(user.getUid())){
                            userKeyList.add(snapshot.getKey());
                            userAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}