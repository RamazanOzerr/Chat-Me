package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Activities.MessageActivity.MessageActivity;
import com.example.basicchatapp.Activities.UserProfileActivity.UserProfileActivity;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Constants;
import com.example.basicchatapp.Utils.FirebaseMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<FriendModel> friendModelList;
    private View view;
    private Activity activity;
    private final String TAG = "FRIENDS REPOSITORY";

    public FriendsAdapter(List<FriendModel> friendModelList, Activity activity) {
        this.friendModelList = friendModelList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item,
                parent,false);

        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        try{
            holder.tv_friends_item_status.setText(friendModelList.get(position).getStatus());
        } catch (Exception e){
            holder.tv_friends_item_status.setText("");
        }

        try{
            holder.tv_friends_item_username.setText(friendModelList.get(position).getUsername());
        } catch (Exception e){
            holder.tv_friends_item_username.setText("");
        }

        try{
            holder.image_friends_item_pp.setImageResource(R.drawable.img);
        } catch (Exception e){
            // do nothing
        }

        holder.linear_friends_item.setOnClickListener(view1 -> {

            getToMessageActivity(friendModelList.get(position).getUserKey()
                    , friendModelList.get(position).getUsername()
                    , friendModelList.get(position).getStatus()
                    , friendModelList.get(position).getPhotoUrl());

//            Intent intent = new Intent(activity.getApplicationContext(), MessageActivity.class);
//            intent.putExtra(Constants.TARGET_USER_ID, friendModelList.get(position).getUserKey());
//            activity.startActivity(intent);
        });
    }

    // get to chat screen
    private void getToMessageActivity(String targetUserId, String username, String status, String photoUrl){
        Intent intent = new Intent(activity.getApplicationContext(), MessageActivity.class);
        intent.putExtra(Constants.TARGET_USER_ID, targetUserId);
        intent.putExtra(Constants.USER_NAME, username);
        intent.putExtra(Constants.ONLINE_STATUS, status);
        intent.putExtra(Constants.PHOTO_URL, photoUrl);
        FirebaseMethods firebaseMethods = new FirebaseMethods(activity.getApplicationContext());
        firebaseMethods.getCurrUsername().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cUsername = snapshot.child("name").getValue().toString();
                intent.putExtra(Constants.CURR_USERNAME, cUsername);
                activity.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return friendModelList.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linear_friends_item;
        private final CircleImageView image_friends_item_pp;
        private final AppCompatTextView tv_friends_item_username;
        private final AppCompatTextView tv_friends_item_status;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            linear_friends_item = itemView.findViewById(R.id.linear_friends_item);
            image_friends_item_pp = itemView.findViewById(R.id.image_friends_item_pp);
            tv_friends_item_username = itemView.findViewById(R.id.tv_friends_item_username);
            tv_friends_item_status = itemView.findViewById(R.id.tv_friends_item_status);
        }
    }
}
