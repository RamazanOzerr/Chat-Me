package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Friends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<FriendModel> friendModelList;
    private View view;

    public FriendsAdapter(List<FriendModel> friendModelList) {
        this.friendModelList = friendModelList;
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
