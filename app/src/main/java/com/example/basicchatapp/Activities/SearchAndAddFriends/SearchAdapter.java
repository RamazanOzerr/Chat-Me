package com.example.basicchatapp.Activities.SearchAndAddFriends;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Activities.UserProfileActivity.UserProfileActivity;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Constants;
import com.example.basicchatapp.Utils.FirebaseMethods;
import com.example.basicchatapp.Utils.HelperMethods;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final List<SearchModel> searchModelList;
    private boolean isClicked;  // true if add friends button clicked
    private final FirebaseMethods firebaseMethods;
    private final Activity activity;
    private final String TAG = "SEARCH ADAPTER";

    public SearchAdapter(List<SearchModel> searchModelList, Activity activity) {
        this.searchModelList = searchModelList;
        isClicked = false;
        this.activity = activity;
        firebaseMethods = new FirebaseMethods(activity.getApplicationContext());
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,
                parent,false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        try {
            Picasso.get()
                    .load(searchModelList.get(position).getPhotoUrl())
                    .into(holder.image_search_item_profile_photo);  // the view

        } catch (Exception e){
            HelperMethods.showShortToast(activity.getApplicationContext()
                    , "could not load the photo");
            // do nothing
//            holder.image_search_item_profile_photo.setImageResource(R.drawable.img);
        }

        try {
            holder.tv_search_item_username.setText(searchModelList.get(position).getName());
        } catch (Exception e){
            holder.tv_search_item_username.setText("");
        }

        holder.image_search_item_add_friends.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: " + searchModelList.get(position).getFriend_status());
            if(searchModelList.get(position).getFriend_status() == Constants.FRIEND_STATUS_ALREADY_FRIENDS){
                HelperMethods.showShortToast(view.getContext(), "already friends");
            } else if(searchModelList.get(position).getFriend_status() == Constants.FRIEND_STATUS_PENDING_REQUEST){
                HelperMethods.showShortToast(view.getContext(), "you already sent a request");
            } else {
                if(!isClicked){
                    holder.image_search_item_add_friends.setImageResource(R.drawable.outline_cancel_32);
                    isClicked = true;
                    firebaseMethods.sendFriendRequest(searchModelList.get(position).getUserId());
                } else {
                    holder.image_search_item_add_friends
                            .setImageResource(R.drawable.baseline_person_add_alt_32);
                    isClicked = false;
                    firebaseMethods.cancelFriendsRequest(searchModelList.get(position).getUserId());
                }
            }
        });

        holder.constraint_search_item.setOnClickListener(view -> {
            Intent intent = new Intent(activity.getApplicationContext(), UserProfileActivity.class);
            intent.putExtra(Constants.TARGET_USER_ID, searchModelList.get(position).getUserId());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout constraint_search_item;
        private final CircleImageView image_search_item_profile_photo;
        private final AppCompatTextView tv_search_item_username;
        private final AppCompatImageView image_search_item_add_friends;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            constraint_search_item = itemView.findViewById(R.id.constraint_search_item);
            image_search_item_profile_photo = itemView
                    .findViewById(R.id.image_search_item_profile_photo);
            tv_search_item_username = itemView.findViewById(R.id.tv_search_item_username);
            image_search_item_add_friends = itemView
                    .findViewById(R.id.image_search_item_add_friends);
        }
    }
}
