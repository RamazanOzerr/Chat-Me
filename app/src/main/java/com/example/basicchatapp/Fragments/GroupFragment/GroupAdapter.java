package com.example.basicchatapp.Fragments.GroupFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<GroupModel> groupModels;
    private boolean isStarred;

    public GroupAdapter(List<GroupModel> groupModels) {
        this.groupModels = groupModels;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_create,
                    parent,false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,
                    parent,false);
        }

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        if(position != 0){
            if(!groupModels.get(position).getGroupName().equals("null")){
                holder.tv_group_item_group_name.setText(groupModels.get(position).getGroupName());
                holder.image_create_new_group.setImageResource(R.drawable.img);
            }
        }

//        isStarred = groupModels.get(position).isStarred();
//        checkIsStarred(holder);
//        holder.image_star.setOnClickListener(view -> checkIsStarred(holder));
    }

    private void checkIsStarred(@NonNull GroupViewHolder holder){
        if(isStarred){
            setStarred(holder);
        } else {
            setNotStarred(holder);
        }
    }

    private void setStarred(@NonNull GroupViewHolder holder){
        holder.image_star.setImageResource(R.drawable.baseline_star_50);
    }

    private void setNotStarred(@NonNull GroupViewHolder holder){
        holder.image_star.setImageResource(R.drawable.baseline_star_gray_50);
    }

    @Override
    public int getItemCount() {
        return groupModels.size();
    }

    public int getItemViewType(int position){

        if (position == 0) {
            return 0; // create group view type
        } else {
            return 1; // other view type
        }

    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        private final AppCompatImageView image_star, image_more_vert, image_create_new_group;
        private final AppCompatTextView tv_group_item_group_name;


        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            image_star = itemView.findViewById(R.id.image_star);
            image_more_vert = itemView.findViewById(R.id.image_more_vert);
            image_create_new_group = itemView.findViewById(R.id.image_create_new_group);
            tv_group_item_group_name = itemView.findViewById(R.id.tv_group_item_group_name);
        }
    }
}
