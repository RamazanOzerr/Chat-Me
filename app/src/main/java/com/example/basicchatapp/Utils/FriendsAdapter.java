package com.example.basicchatapp.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Activities.PrivateChatActivity;
import com.example.basicchatapp.Activities.UserProfileActivity;
import com.example.basicchatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{

    private List<String> friendsIDlist;
    Activity activity;
    Context context;
    FirebaseUser user;
    DatabaseReference reference;


    public FriendsAdapter(List<String> friendsIDlist, Activity activity, Context context) {
        this.friendsIDlist = friendsIDlist;
        this.activity = activity;
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_layout,parent,false);

        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // db den user info çekip set ledik
       reference.child("Users").child(friendsIDlist.get(position)).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String name = snapshot.child("name").getValue().toString();
               String bio = snapshot.child("bio").getValue().toString();
               String photoID = snapshot.child("photo").getValue().toString();
               holder.name_friends.setText(name);
               holder.bio_friends.setText(bio);
               Picasso.get().load(photoID).into(holder.imageViewfriends);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       holder.cardview_friends.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(view.getContext(), PrivateChatActivity.class);
               intent.putExtra("UserKey",friendsIDlist.get(position));
               activity.startActivity(intent);
           }
       });

       holder.imageViewfriends.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               reference.child("Users").child(friendsIDlist.get(position)).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String name = snapshot.child("name").getValue().toString();
                       String bio = snapshot.child("bio").getValue().toString();
                       String photoID = snapshot.child("photo").getValue().toString();
                       Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                       intent.putExtra("name",name);
                       intent.putExtra("photo",photoID);
                       intent.putExtra("bio",bio);
                       intent.putExtra("UserKey",friendsIDlist.get(position));
                       activity.startActivity(intent);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });



           }
       });


    }

    @Override
    public int getItemCount() {
        return friendsIDlist.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView name_friends, bio_friends;
        CircleImageView imageViewfriends;
        CardView cardview_friends;

        ViewHolder(View itemView){
            super(itemView);

            name_friends = itemView.findViewById(R.id.name_friends);
            imageViewfriends = itemView.findViewById(R.id.imageViewfriends);
            bio_friends = itemView.findViewById(R.id.bio_friends);
            cardview_friends = itemView.findViewById(R.id.cardview_friends);

        }
    }
}
