package com.example.basicchatapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.Activities.UserProfileActivity;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Requests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    // şimdilik liste yazdık, sonradan düzenlicez
    private List<Requests> userList;

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
    }


    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requests_layout,parent,false);

        return new RequestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.requestsnameUser.setText(userList.get(position).getName());
        Picasso.get().load(userList.get(position).getPhotoPath()).into(holder.profile_image);

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("name",userList.get(position).getName());
                intent.putExtra("photo",userList.get(position).getPhotoPath());
                intent.putExtra("bio",userList.get(position).getBio());
                intent.putExtra("UserKey",userList.get(position).getUserKey());
                activity.startActivity(intent);
            }
        });

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptReq(userList.get(position).getUserKey());
                Toast.makeText(view.getContext(),"ADDED AS FRIEND, CHECK YOUR FRIENDS LIST",Toast.LENGTH_SHORT).show();
                //TODO burda da checkReq işlemi yapılıp, bu kişi recyclerView dan silinmesi lazım
                // ve Friends fragment/activity içerisinde listelenmesi lazım
            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectReq(userList.get(position).getUserKey());
                Toast.makeText(view.getContext(),"REQUEST REJECTED",Toast.LENGTH_SHORT).show();
                //TODO ret işleminden sonra kullanıcının recyclerView den silinmesi gerekiyor, bunun için
                // checkReq methodunu yeniden çalıştırmamız lazım
            }
        });
//        holder.requestsnameUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), PrivateChatActivity.class);
//                intent.putExtra("UserKey",userList.get(position).getUserKey());
//                activity.startActivity(intent);
//            }
//        });
    }

    private void acceptReq(String userKey){
        Map map = new HashMap();
        map.put("isFriend","true");
        map.put("type","taken");
        reference.child("Requests").child(firebaseUser.getUid()).child(userKey).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                map.put("type","sent");
                reference.child("Requests").child(userKey).child(firebaseUser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }

    private void rejectReq(String userKey){
        reference.child("Requests").child(firebaseUser.getUid()).child(userKey).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                reference.child("Requests").child(userKey).child(firebaseUser.getUid()).removeValue();

                Toast.makeText(context.getApplicationContext(), "REQUEST HAS BEEN REJECTED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

            }
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

        ViewHolder(View itemView){
            super(itemView);

            requestsnameUser = itemView.findViewById(R.id.requestsnameUser);
            profile_image = itemView.findViewById(R.id.profile_image);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);

        }
    }
}