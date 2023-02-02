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

import com.example.basicchatapp.Activities.PrivateChatActivity;
import com.example.basicchatapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public UserAdapter(List<String> userKeyList, Activity activity, Context context) {
        this.userKeyList = userKeyList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    // layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false);

        return new ViewHolder(view);
    }

    // TODO burada int position kısmında bir hata oluşuyordu, hata sebebi setOnClickListener atıyor
    // TODO oluşumuz, onu silince düzeliyordu, şimdilik şu @SuppressLint("RecyclerView") şeyini ekledi
    //  TODOhata çıkarırsa silicez onu
    // view lere setlemeler yapılacak
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        holder.nameUser.setText(userKeyList.get(position).toString());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userKeyList.get(position);
                Intent intent = new Intent(view.getContext(), PrivateChatActivity.class);
                intent.putExtra("UserKey",userKeyList.get(position));
                activity.startActivity(intent);
            }
        });

        reference.child("Users").child(userKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);
                String photoPath = snapshot.child("photo").getValue().toString();
//                Picasso.get().load(profile.getImage()).into(holder.imageView);
                Picasso.get().load(photoPath).into(holder.imageView);
                holder.nameUser.setText(profile.getName());
                holder.bioUser.setText(profile.getBio());
//                if(profile.getImage() == 0){
//                    Picasso.get().load(profile.getImage()).into(holder.imageView);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



    }

    // adapteri oluşturacak listenin size ı
    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    // view ların tanımlanma işlemi
    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView nameUser, bioUser;
        CircleImageView imageView;

        ViewHolder(View itemView){
            super(itemView);

            nameUser = itemView.findViewById(R.id.nameUser);
            imageView = itemView.findViewById(R.id.imageView);
            bioUser = itemView.findViewById(R.id.bioUser);

        }
    }
}
