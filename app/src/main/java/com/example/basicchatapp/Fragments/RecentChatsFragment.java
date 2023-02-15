package com.example.basicchatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.MessageModel;
import com.example.basicchatapp.Utils.Profile;
import com.example.basicchatapp.Utils.RecentChats;
import com.example.basicchatapp.Utils.RecentChatsAdapter;
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

public class RecentChatsFragment extends Fragment {

    private MessageModel tempMm;
    private RecyclerView recentChatsRecyclerView;
    FirebaseUser fuser;
    DatabaseReference reference;
    RecentChatsAdapter recentChatsAdapter;
    private List<String> userList;  // kişinin konuştuğu kişilerin id lerinin listesi

    private List<RecentChats> recentChatsInfoList;

    private List<String> textList;  // temp
    public RecentChatsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_chats, container, false);

        recentChatsRecyclerView = view.findViewById(R.id.recentChatsRecyclerView);
//        recentChatsRecyclerView.hasFixedSize();
        recentChatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>(); // kişinin konuştuğu kişilerin id lerinin listesi

        getUserKeys();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void getUserKeys(){

        System.out.println("user key methodu");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Messages");

        // user ın konuştuğu kişilerin listesini aldık
        reference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userList.clear(); // listeyi temizledik
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userList.add(dataSnapshot.getKey()); // tüm konuştuğu kişilerin id lerini listeledik
                    //TODO Debug için yazdık, keylerin hepsini başarılı şekilde alıyor
                    System.out.println("key:"+ dataSnapshot.getKey());
                }

                getUserInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUserInfo(){

        recentChatsInfoList = new ArrayList<>();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String id : userList){
                    System.out.println("id ler:"+id);
                    Profile profile = snapshot.child(id).getValue(Profile.class);
                    String photoPath = snapshot.child(id).child("photo").getValue().toString();
                    // kullanıcının foto ve isim bilgilerini alıp bir class yapısında depoladık
                    RecentChats recentChats = new RecentChats(photoPath, profile.getName(),null,userList.size(),profile.getBio(), id);
                    recentChatsInfoList.add(recentChats);

                    System.out.println(recentChats);

                }
                recentChatsAdapter = new RecentChatsAdapter(recentChatsInfoList, getActivity(), getContext());
                recentChatsRecyclerView.setAdapter(recentChatsAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getLastMessage(){
//        Date d1,d2;
//        String pattern = "yyyy-MM-dd";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        textList = new ArrayList<>();

        recentChatsInfoList = new ArrayList<>();
        tempMm = new MessageModel();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Messages");

        reference.child(fuser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        reference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String id : userList){
                    // tüm mesaj id leri üzerinde dolaşıyoruz, db de en sondaki en son atılan mesaj
                    // olacağı için kıyaslama yapmaktan ziyade, direkt bi öncekinin üzerine yazarak
                    // en sona kadar ilerliyoruz
                    for(DataSnapshot dataSnapshot : snapshot.child(id).getChildren()){
                        MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                        System.out.println("message: " + messageModel);
                        tempMm.setFrom(messageModel.getFrom());
                        tempMm.setSeen(messageModel.getSeen());
                        tempMm.setText(messageModel.getText());
                        tempMm.setTime(messageModel.getTime());
                        tempMm.setType(messageModel.getType());
                    }
                    textList.add(tempMm.getText());
                    // tarih eklemek istersek, benzer işlemi burda yapcaz

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}