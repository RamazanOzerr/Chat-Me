package com.example.basicchatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.basicchatapp.Activities.PrivateChatActivity;
import com.example.basicchatapp.Activities.UserProfileDesign;
import com.example.basicchatapp.Adapters.UserAdapter;
import com.example.basicchatapp.Utils.Profile;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Adapters.UserAdapter;
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
    private List<Profile> userList;
    RecyclerView userListRecyclerView;
    View view;
    UserAdapter userAdapter;
    FirebaseAuth auth;
    FirebaseUser user;
    private SwipeRefreshLayout swipeRefreshLayout_main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        init();
        getUsers();

        return view;
    }


    public void init(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        userListRecyclerView = view.findViewById(R.id.userListRecyclerView);
        // alttaki snapCount değişkeni bir satırda kaç tane görüntülenmesini istediğimizi belirler
//        RecyclerView.LayoutManager mng =
        RecyclerView.LayoutManager mng = new LinearLayoutManager(getActivity());
        userListRecyclerView.setLayoutManager(mng);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userList = new ArrayList<>();
        swipeRefreshLayout_main = view.findViewById(R.id.swipe_to_refresh_layout_main);
        swipeRefreshLayout_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userList.clear();
                getUsers();
                swipeRefreshLayout_main.setRefreshing(false);
            }
        });
    }

    public void getUsers(){
        reference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                reference.child("Users").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Profile user1 = snapshot.getValue(Profile.class);
                        String name = snapshot.child("name").getValue().toString();
                        String bio = snapshot.child("bio").getValue().toString();
                        String photo = snapshot.child("photo").getValue().toString();
                        String username = snapshot.child("username").getValue().toString();
                        if(!name.equals("null") && !snapshot.getKey().equals(user.getUid())){
//                            System.out.println("key: "+snapshot.getKey());
//                            userKeyList.add(snapshot.getKey());
                            userList.add(new Profile(bio, name, photo, username, snapshot.getKey()));
//                            userAdapter.notifyDataSetChanged();
                        }
                        userAdapter = new UserAdapter(userList,getActivity(),getContext());
                        userListRecyclerView.setAdapter(userAdapter);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                userAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}