package com.example.basicchatapp.Fragments.ChatsFragment.Stories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder> {

    private List<StoriesModel> storiesModelList;

    public StoriesAdapter(List<StoriesModel> storiesModelList) {
        this.storiesModelList = storiesModelList;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item_create_story,
                    parent,false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item_empty,
                    parent,false);
        }
        return new StoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        if(position != 0){
            if(!storiesModelList.get(position).getName().equals("empty")){
                holder.tv_story_item.setText(storiesModelList.get(position).getName());
                //todo: we wet it manually for now
                holder.image_story_item.setImageResource(R.drawable.img);
            }
        }


    }

    public int getItemViewType(int position){

        if (position == 0) {
            return 0; // create story view type
        } else {
            return 1; // other view type
        }

    }

    @Override
    public int getItemCount() {
        return storiesModelList.size();
    }

    public static class StoriesViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView image_story_item;
        private final AppCompatTextView tv_story_item;

        public StoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            image_story_item = itemView.findViewById(R.id.image_story_item);
            tv_story_item = itemView.findViewById(R.id.tv_story_item);
        }
    }
}
