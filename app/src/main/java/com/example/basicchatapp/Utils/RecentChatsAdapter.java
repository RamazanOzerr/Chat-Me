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
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Activities.UserProfileActivity;
import com.example.basicchatapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatsAdapter extends RecyclerView.Adapter<RecentChatsAdapter.ViewHolder>{

    private List<RecentChats> recentChatsInfoList;
    Activity activity;
    Context context;

    public RecentChatsAdapter(List<RecentChats> recentChatsInfoList, Activity activity, Context context) {
        this.recentChatsInfoList = recentChatsInfoList;
        this.activity = activity;
        this.context = context;
    }

    // layout tanımlaması yaptığımız yer
    @NonNull
    @Override
    public RecentChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recent_user_layout,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecentChatsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        System.out.println("adapter içindeyiz");
        holder.recentnameUser.setText(recentChatsInfoList.get(position).getName());
        Picasso.get().load(recentChatsInfoList.get(position).getPhotoPath()).into(holder.recentImage);

//        if(recentChatsInfoList.get(position).getName().equals("")){
//            holder.textLoser.setVisibility(View.VISIBLE);
//        }

        holder.recentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("name",recentChatsInfoList.get(position).getName());
                intent.putExtra("photo",recentChatsInfoList.get(position).getPhotoPath());
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return recentChatsInfoList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView recentnameUser, textLoser;
        CircleImageView recentImage;

        ViewHolder(View itemView){
            super(itemView);

            recentnameUser = itemView.findViewById(R.id.recentnameUser);
            recentImage = itemView.findViewById(R.id.recentChatImage);
            textLoser = itemView.findViewById(R.id.textLoser);

        }
    }
}
