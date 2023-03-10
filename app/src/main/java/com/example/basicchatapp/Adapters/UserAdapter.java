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
import com.example.basicchatapp.Activities.UserProfileActivity;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable{
    List<Profile> userList;
    List<Profile> userListFull;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public UserAdapter(List<Profile> userList, Activity activity, Context context) {
        this.userList = userList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        // copy
        userListFull = new ArrayList<>(userList);
    }

    // layout initialization
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout,
                parent,false);

        return new ViewHolder(view);
    }

    // set views
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.cardView.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
            intent.putExtra("name",userList.get(position).getName());
            intent.putExtra("photo",userList.get(position).getPhoto());
            intent.putExtra("bio",userList.get(position).getBio());
            intent.putExtra("UserKey",userList.get(position).getOtherUser());
            activity.startActivity(intent);

        });

        holder.imageView.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
            intent.putExtra("name",userList.get(position).getName());
            intent.putExtra("photo",userList.get(position).getPhoto());
            intent.putExtra("bio",userList.get(position).getBio());
            intent.putExtra("UserKey",userList.get(position).getOtherUser());
            activity.startActivity(intent);
        });

        try {
            Picasso.get().load(userList.get(position).getPhoto()).into(holder.imageView);
        }catch (Exception e){
            holder.imageView.setImageResource(R.mipmap.ic_account);
        }
        try {
            holder.nameUser.setText(userList.get(position).getName());
        }catch (Exception e){
            holder.nameUser.setText("");
        }
        try {
            holder.bioUser.setText(userList.get(position).getBio());
        }catch (Exception e){
            holder.bioUser.setText("");
        }
    }

    // size of the list
    @Override
    public int getItemCount() {
        return userList.size();
    }

    // initialize views
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView nameUser, bioUser;
        CircleImageView imageView;
        CardView cardView;

        ViewHolder(View itemView){
            super(itemView);

            nameUser = itemView.findViewById(R.id.nameUser);
            imageView = itemView.findViewById(R.id.imageView);
            bioUser = itemView.findViewById(R.id.bioUser);
            cardView = itemView.findViewById(R.id.cardview_users);
        }
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Profile> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(userListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Profile item : userListFull){
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
            userList.clear();
            userList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
