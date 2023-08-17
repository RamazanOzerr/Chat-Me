package com.example.basicchatapp.Fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.example.basicchatapp.R;
import com.example.basicchatapp.Adapters.FriendsAdapter;
import com.example.basicchatapp.Utils.Friend;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FriendsFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    FirebaseUser user;
    DatabaseReference reference;
    private List<Friend> friendList;
    FriendsAdapter friendsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout_friends;
    View view;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);
//        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
//        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        init();
//        getFriendsIDs();

        return view;
    }

    private void init(){
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        friendList = new ArrayList<>();
        swipeRefreshLayout_friends = view.findViewById(R.id.swipe_to_refresh_layout_friends);
        // set refresh listener
        swipeRefreshLayout_friends.setOnRefreshListener(() -> {
            friendList.clear();
            getFriendsIDs();
            swipeRefreshLayout_friends.setRefreshing(false);
        });
        // set swipe to remove
        swipeToRemove();
    }

    private void getFriendsIDs(){

        reference.child("Requests").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    // we only check one side, no need to the other coz they both are the same
                    if(dataSnapshot.child("isFriend").getValue().toString().equals("true")){
                        setFriend(dataSnapshot.getKey());
                        System.out.println("friend id:"+dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setFriend(String otherUser){
        // get user info from the db and set them
        reference.child("Users").child(otherUser)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                String photoID = snapshot.child("photo").getValue().toString();
                friendList.add(new Friend(photoID,name,bio,otherUser));
                friendsAdapter = new FriendsAdapter(friendList,getActivity(),getContext());
                friendsRecyclerView.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void swipeToRemove(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Friend deletedFriend = friendList.get(pos);
                friendList.remove(pos);
                friendsAdapter.notifyItemRemoved(pos);
                Snackbar snackbar = Snackbar.make(friendsRecyclerView, deletedFriend.getName()
                                , Snackbar.LENGTH_LONG)
                        .setAction("undo", view -> {
                            friendList.add(pos, deletedFriend);
                            friendsAdapter.notifyItemInserted(pos);

                        });
                snackbar.show();
                // remove chat from db as well if user didnt undo deletion
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            // Snackbar closed on its own, means chat will be removed from the db as well
                            deleteFriend(deletedFriend.getUserKey());
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                    float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                        .addActionIcon(R.mipmap.ic_delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(friendsRecyclerView);

    }

    private void deleteFriend(String userKey){
        reference.child("Requests").child(user.getUid()).child(userKey)
                .removeValue().addOnCompleteListener(task -> reference.child("Requests")
                        .child(userKey).child(user.getUid())
                        .removeValue());
    }

    // call onCreateOptionsMenu
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }

    // set search feature
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
                friendsAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

}