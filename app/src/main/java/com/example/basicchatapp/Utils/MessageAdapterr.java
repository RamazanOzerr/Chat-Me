package com.example.basicchatapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapterr extends RecyclerView.Adapter<MessageAdapterr.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;
    List<MessageModel> messageModelList;
    Boolean state;
    int view_type_send=1,view_type_receive=2;

    public MessageAdapterr(List<String> userKeyList, Activity activity, Context context,List<MessageModel> messageModelList) {
        this.activity = activity;
        this.context = context;
        this.messageModelList = messageModelList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
    }

//    public MessageAdapterr(List<String> keyList, Context applicationContext, Context applicationContext1, List<MessageModel> messageModelList) {
//        this.activity = activity;
//        this.context = context;
//        this.messageModelList = messageModelList;
//        this.state = state;
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        reference = firebaseDatabase.getReference();
//        auth = FirebaseAuth.getInstance();
//        firebaseUser = auth.getCurrentUser();
//        userId = firebaseUser.getUid();
//    }


    @NonNull
        @Override
        public MessageAdapterr.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType) {
           View view;

           if(viewType == view_type_send){
               view = LayoutInflater.from(context).inflate(R.layout.message_send_layout,parent,false);
               return new ViewHolder(view);
           }else{
               view = LayoutInflater.from(context).inflate(R.layout.message_receive_layout,parent,false);
               return new ViewHolder(view);
           }

        }

        @Override
        public void onBindViewHolder (@NonNull MessageAdapterr.ViewHolder holder,int position){
            holder.messageText.setText(messageModelList.get(position).getText());
        }

        @Override
        public int getItemCount () {
            return messageModelList.size();
        }



    public int getItemViewType(int position){
        if(messageModelList.get(position).getFrom().equals(userId)){
            state = true;
            return view_type_send;
        }else{
            state = false;
            return view_type_receive;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if(state == true){
                messageText = itemView.findViewById(R.id.sendermessage);
            }else{
                messageText = itemView.findViewById(R.id.receivermessage);
            }
        }
    }


}

