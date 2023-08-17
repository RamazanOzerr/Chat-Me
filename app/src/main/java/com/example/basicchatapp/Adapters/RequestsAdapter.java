package com.example.basicchatapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Requests;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> implements Filterable {

    private List<Requests> userList;
    private List<Requests> userListFull;

    Activity activity;
    Context context;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    public RequestsAdapter(List<Requests> userList, Activity activity, Context context) {
        this.userList = userList;
        this.activity = activity;
        this.context = context;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userListFull = new ArrayList<>(userList);
    }

    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requests_layout,
                parent,false);

        return new RequestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        try{
            holder.requestsnameUser.setText(userList.get(position).getName());
        }catch (Exception e){
            holder.requestsnameUser.setText("");
        }
        try{
            Picasso.get().load(userList.get(position).getPhotoPath()).into(holder.profile_image);
        }catch (Exception e){
            holder.profile_image.setImageResource(R.mipmap.ic_account);
        }

        holder.profile_image.setOnClickListener(view -> {
//            Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
//            intent.putExtra("name",userList.get(position).getName());
//            intent.putExtra("photo",userList.get(position).getPhotoPath());
//            intent.putExtra("bio",userList.get(position).getBio());
//            intent.putExtra("UserKey",userList.get(position).getUserKey());
//            activity.startActivity(intent);
        });

        holder.acceptBtn.setOnClickListener(view -> {
//                Requests acceptedReq = userList.get(position);
            acceptReq(userList.get(position).getUserKey(), position);
            Toast.makeText(view.getContext(),"ADDED AS FRIEND, CHECK YOUR FRIENDS LIST",
                    Toast.LENGTH_SHORT).show();
            //TODO burda da checkReq işlemi yapılıp, bu kişi recyclerView dan silinmesi lazım
            // ve Friends fragment/activity içerisinde listelenmesi lazım
        });

        holder.rejectBtn.setOnClickListener(view -> {
            rejectReq(userList.get(position).getUserKey());
            userList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(view.getContext(),"REQUEST REJECTED",Toast.LENGTH_SHORT).show();
            //TODO: ret işleminden sonra kullanıcının recyclerView den silinmesi gerekiyor, bunun için
            // checkReq methodunu yeniden çalıştırmamız lazım
        });
    }

    private void acceptReq(String userKey, int position){
        Map<String, String > map = new HashMap<>();
        map.put("isFriend","true");
        map.put("type","taken");
        reference.child("Requests").child(firebaseUser.getUid()).child(userKey)
                .setValue(map).addOnCompleteListener(task -> {
                    map.put("type","sent");
                    reference.child("Requests").child(userKey).child(firebaseUser
                            .getUid()).setValue(map).addOnCompleteListener(task1 -> {
                            });
                });
//        notifyDataSetChanged();
    }

    private void rejectReq(String userKey){
        reference.child("Requests").child(firebaseUser.getUid()).child(userKey)
                .removeValue((error, ref) -> {
            reference.child("Requests").child(userKey).child(firebaseUser.getUid())
                    .removeValue();
                notifyDataSetChanged();
            Toast.makeText(context.getApplicationContext(),
                    "REQUEST HAS BEEN REJECTED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView requestsnameUser;
        CircleImageView profile_image;
        Button acceptBtn, rejectBtn;

        public ViewHolder(View itemView){
            super(itemView);

            requestsnameUser = itemView.findViewById(R.id.requestsnameUser);
            profile_image = itemView.findViewById(R.id.profile_image);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);

        }

    }

    @Override
    public Filter getFilter() {
        return friendFilter;
    }

    private Filter friendFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Requests> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(userListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Requests item : userListFull){
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
