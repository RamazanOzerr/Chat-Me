package com.example.basicchatapp.Fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Activities.MessageActivity.MessageModelOld;
import com.example.basicchatapp.Utils.Profile;
import com.example.basicchatapp.Utils.RecentChats;
import com.example.basicchatapp.Adapters.RecentChatsAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecentChatsFragment extends Fragment {

    private MessageModelOld tempMm;
    private RecyclerView recentChatsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseUser fuser;
    DatabaseReference reference;
    RecentChatsAdapter recentChatsAdapter;
    private List<String> userList;  // id list of people whom user has talked

    private List<RecentChats> recentChatsInfoList;

    public RecentChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_chats, container, false);
//
//        recentChatsRecyclerView = view.findViewById(R.id.recentChatsRecyclerView);
//        recentChatsRecyclerView.setNestedScrollingEnabled(false);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recentChatsRecyclerView.setLayoutManager(layoutManager);
//        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);
//        recentChatsInfoList = new ArrayList<>();
//        userList = new ArrayList<>(); // id list
//
//        getUserKeys();
//        swipeToRemove();
//        swipeRefreshLayout.setOnRefreshListener(() -> {
//            recentChatsInfoList.clear();
//            getUserKeys();
//            swipeRefreshLayout.setRefreshing(false);
//        });
        return view;
    }


    private void getUserKeys(){

        System.out.println("user key methodu");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Messages");

        // get id list
        reference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); // listeyi temizledik
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userList.add(dataSnapshot.getKey()); // listed all id's
                }
                // get user info using id's
                getUserInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUserInfo(){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentChatsInfoList.clear();
                for(String id : userList){
                    System.out.println("id ler:"+id);
                    Profile profile = snapshot.child(id).getValue(Profile.class);
                    String photoPath = snapshot.child(id).child("photo").getValue().toString();
                    RecentChats recentChats = new RecentChats(photoPath, profile.getName()
                            ,null,userList.size(),profile.getBio(), id);
                    recentChatsInfoList.add(recentChats);

                }
                recentChatsAdapter = new RecentChatsAdapter(recentChatsInfoList,
                        getActivity(), getContext());
                recentChatsRecyclerView.setAdapter(recentChatsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // swipe to remove conversation
    private void swipeToRemove(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                RecentChats deletedChat = recentChatsInfoList.get(pos);
                recentChatsInfoList.remove(pos);
                recentChatsAdapter.notifyItemRemoved(pos);
                Snackbar snackbar = Snackbar.make(recentChatsRecyclerView, deletedChat.getName()
                                , Snackbar.LENGTH_LONG)
                        .setAction("undo", view -> {
                            recentChatsInfoList.add(pos, deletedChat);
                            recentChatsAdapter.notifyItemInserted(pos);

                        });
                snackbar.show();
                // remove chat from db as well if user didnt undo deletion
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            // Snackbar closed on its own, means chat will be removed from the db as well
                            deleteChat(deletedChat.getUserKey());
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

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recentChatsRecyclerView);

    }

    private void deleteChat(String otherUser){
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference();

        tempRef.child("Messages").child(fuser.getUid()).child(otherUser)
                .removeValue((error, ref) -> {
            tempRef.child("Messages").child(otherUser).child(fuser.getUid()).removeValue();
            Toast.makeText(getContext(),"MESSAGES WITH THAT USER WERE SUCCESSFULLY DELETED"
                    ,Toast.LENGTH_SHORT).show();
        });
    }

    // call onCreateOptionsMenu
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }
//
//    // set search feature
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                recentChatsAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
//    }

}