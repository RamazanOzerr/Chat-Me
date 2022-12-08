package com.example.basicchatapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

   List<String> userKeyList;
   Activity activity;
   Context context;
   FirebaseDatabase firebaseDatabase;
   DatabaseReference reference;
   FirebaseAuth auth;
   FirebaseUser firebaseUser;
   String userID;

   public MessageAdapter(List<String> userKeyList, Activity activity, Context context){
       this.userKeyList = userKeyList;
       this.activity = activity;
       this.context = context;
       firebaseDatabase = FirebaseDatabase.getInstance();
       reference = firebaseDatabase.getReference();
       auth = FirebaseAuth.getInstance();
       userID = firebaseUser.getUid();
   }

//   public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType){
//       View view = LayoutInflater.from(context).inflate(R.layout)
//   }



    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public int getItemViewType(int position){
       if(messageMod)
    }


}
