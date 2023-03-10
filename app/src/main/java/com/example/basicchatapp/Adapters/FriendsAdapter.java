package com.example.basicchatapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Activities.PrivateChatActivity;
import com.example.basicchatapp.Activities.UserProfileActivity;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Friend;
import com.example.basicchatapp.Utils.RecentChats;
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

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> implements Filterable{

    private List<Friend> friendlist;
    private List<Friend> friendlistfull;
    Activity activity;
    Context context;
    FirebaseUser user;
    DatabaseReference reference;


    public FriendsAdapter(List<Friend> friendList, Activity activity, Context context) {
        this.friendlist = friendList;
        this.activity = activity;
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        friendlistfull = new ArrayList<>(friendList);
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_layout,
                parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        try{
            holder.name_friends.setText(friendlist.get(position).getName());
        }catch (Exception e){
            holder.name_friends.setText("friend name");
        }
        try{
            holder.bio_friends.setText(friendlist.get(position).getBio());
        }catch (Exception e){
            holder.bio_friends.setText("bio");
        }

        try{
            Picasso.get().load(friendlist.get(position).getPhotoPath()).into(holder.imageViewfriends);
        }catch (Exception e){
            holder.imageViewfriends.setImageResource(R.mipmap.ic_account);
        }




       holder.cardview_friends.setOnClickListener(view -> {
           Intent intent = new Intent(view.getContext(), PrivateChatActivity.class);
           intent.putExtra("UserKey",friendlist.get(position).getUserKey());
           activity.startActivity(intent);
       });

       holder.imageViewfriends.setOnClickListener(view -> {
           Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
           intent.putExtra("name",friendlist.get(position).getName());
           intent.putExtra("photo",friendlist.get(position).getPhotoPath());
           intent.putExtra("bio",friendlist.get(position).getBio());
           intent.putExtra("UserKey",friendlist.get(position).getUserKey());
           activity.startActivity(intent);
       });


    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

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

    @Override
    public Filter getFilter() {
        return friendFilter;
    }

    private Filter friendFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Friend> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(friendlistfull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Friend item : friendlistfull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            friendlist.clear();
            friendlist.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
