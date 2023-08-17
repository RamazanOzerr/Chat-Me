package com.example.basicchatapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicchatapp.Adapters.RequestsAdapter;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Requests;
import com.example.basicchatapp.Adapters.RequestsAdapter;
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

public class RequestsFragment extends Fragment {

    private RecyclerView requestsRecyclerView;
    FirebaseUser user;
    DatabaseReference reference;
    List<Requests> requestsList;
    RequestsAdapter requestsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout_requests;
    View view;


    //TODO şu MissingInflatedId olayına bakalım
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_requests, container, false);

//        init();
//        getRequests();

        return view;
    }
    private void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        requestsList = new ArrayList<>();
        swipeRefreshLayout_requests = view.findViewById(R.id.swipe_to_refresh_layout_requests);
        requestsRecyclerView = view.findViewById(R.id.requestsRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout_requests.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestsList.clear();
                getRequests();
                swipeRefreshLayout_requests.setRefreshing(false);
            }
        });

    }


    private void getRequests(){

//        reference.child("Requests").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                System.out.println("key: "+snapshot.getKey()); // istek atan kişinin id si
//                String otherUser = snapshot.getKey();
//                String type = snapshot.child(otherUser).child(user.getUid())
//                        .child("type").getValue().toString();
//                String friend = snapshot.child(otherUser).child(user.getUid())
//                        .child("isFriend").getValue().toString();
//
//                if(type.equals("sent") && friend.equals("false")){
//                    reference.child("Users").child(otherUser)
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            String photoID = snapshot.child("photo").getValue().toString();
//                            String name = snapshot.child("name").getValue().toString();
//                            String bio = snapshot.child("bio").getValue().toString();
//                            requestsList.add(new Requests(photoID, name, bio, otherUser));
//                            requestsAdapter = new RequestsAdapter(requestsList, getActivity(), getContext());
//                            requestsRecyclerView.setAdapter(requestsAdapter);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        reference.child("Requests").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String type = dataSnapshot.child("type").getValue().toString();
                    String friend = dataSnapshot.child("isFriend").getValue().toString();
                    if(type.equals("taken") && friend.equals("false")){
                        getUserInfo(dataSnapshot.getKey());
                    }
                }
//                requestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        reference.child("Requests").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                requestsList.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    if(dataSnapshot.hasChild(user.getUid())){
//                        if(dataSnapshot.child(user.getUid()).child("type").getValue()
//                                .toString().equals("sent") && dataSnapshot.child(user.getUid())
//                                .child("isFriend").getValue().toString().equals("false")){
//
//                            String otherID = dataSnapshot.getKey();
//                            reference.child("Users").child(otherID)
//                                    .addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    String photoID = snapshot.child("photo").getValue().toString();
//                                    String name = snapshot.child("name").getValue().toString();
//                                    String bio = snapshot.child("bio").getValue().toString();
//                                    requestsList.add(new Requests(photoID, name, bio, otherID));
//                                    System.out.println("requests: "+requestsList.size()+" "+requestsList.get(0).getUserKey());
//                                    requestsAdapter = new RequestsAdapter(requestsList, getActivity(), getContext());
//                                    requestsRecyclerView.setAdapter(requestsAdapter);
////                                    requestsAdapter.notifyDataSetChanged();
//                                }
//
//
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                        }
//
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void getUserInfo(String otherUser){
        reference.child("Users").child(otherUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String photoID = snapshot.child("photo").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                requestsList.add(new Requests(photoID, name, bio, otherUser));
                requestsAdapter = new RequestsAdapter(requestsList, getActivity(), getContext());
                requestsRecyclerView.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }

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
                requestsAdapter.getFilter().filter(s);
                return false;
            }
        });
    }




}