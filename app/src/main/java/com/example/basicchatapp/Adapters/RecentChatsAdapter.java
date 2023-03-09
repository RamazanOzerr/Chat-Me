package com.example.basicchatapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Activities.PrivateChatActivity;
import com.example.basicchatapp.Activities.UserProfileActivity;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.RecentChats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatsAdapter extends RecyclerView.Adapter<RecentChatsAdapter.ViewHolder> implements Filterable {

    private List<RecentChats> recentChatsInfoList;
    private List<RecentChats> recentChatsInfoListFull;
    Activity activity;
    Context context;

    FirebaseUser user;
    DatabaseReference reference;

    public RecentChatsAdapter(List<RecentChats> recentChatsInfoList, Activity activity, Context context) {
        this.recentChatsInfoList = recentChatsInfoList;
        this.activity = activity;
        this.context = context;

        // copy isini oluşturduk
        recentChatsInfoListFull = new ArrayList<>(recentChatsInfoList);
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

        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
//        System.out.println("adapter içindeyiz");
        holder.recentnameUser.setText(recentChatsInfoList.get(position).getName());
        Picasso.get().load(recentChatsInfoList.get(position).getPhotoPath()).into(holder.recentImage);

//        if(recentChatsInfoList.get(position).getName().equals("")){
//            holder.textLoser.setVisibility(View.VISIBLE);
//
//        }

        holder.recentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("name",recentChatsInfoList.get(position).getName());
                intent.putExtra("photo",recentChatsInfoList.get(position).getPhotoPath());
                intent.putExtra("bio",recentChatsInfoList.get(position).getBio());
                intent.putExtra("UserKey",recentChatsInfoList.get(position).getUserKey());
                activity.startActivity(intent);
            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PrivateChatActivity.class);
                intent.putExtra("UserKey",recentChatsInfoList.get(position).getUserKey());
                activity.startActivity(intent);
            }
        });

//        holder.recentImage.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                createAlertDialog(recentChatsInfoList.get(position).getUserKey());
//                return false;
//            }
//        });

//        holder.recentnameUser.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                createAlertDialog(recentChatsInfoList.get(position).getUserKey());
//                return false;
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return recentChatsInfoList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView recentnameUser;
        CircleImageView recentImage;
        CardView cardView;

        ViewHolder(View itemView){
            super(itemView);

            recentnameUser = itemView.findViewById(R.id.recentnameUser);
            recentImage = itemView.findViewById(R.id.recentChatImage);
            cardView = itemView.findViewById(R.id.cardview_recent);

        }
    }

    private void createAlertDialog(String otherUser){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("DELETE CHAT");
        builder.setIcon(R.mipmap.ic_logo6);
        builder.setMessage("YOU SURE YOU WANNA DELETE ALL THE CHAT WITH THAT USER?");
        builder.setNegativeButton("NO",null);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reference.child("Messages").child(user.getUid()).child(otherUser).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        reference.child("Messages").child(otherUser).child(user.getUid()).removeValue();
                        Toast.makeText(context.getApplicationContext(),"MESSAGES WITH THAT USER WERE SUCCESSFULLY DELETED",Toast.LENGTH_SHORT).show();
                    }
                });

                //TODO SILME ISLEMLERINI BURDA YAPICAZ, SIMDILIK TOAST VERIYORUM

            }
        });
        builder.show();
    }


    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<RecentChats> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(recentChatsInfoListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(RecentChats item : recentChatsInfoListFull){
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
            recentChatsInfoList.clear();
            recentChatsInfoList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
