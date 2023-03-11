package com.example.basicchatapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private Boolean state;
    int view_type_send=1,view_type_receive=2;

    public MessageAdapterr(List<String> userKeyList, Activity activity,
                           Context context,List<MessageModel> messageModelList) {
        this.activity = activity;
        this.context = context;
        this.messageModelList = messageModelList;
        this.userKeyList = userKeyList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
    }

    @NonNull
        @Override
        public MessageAdapterr.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,
                                                              int viewType) {
           View view;

           if(viewType == view_type_send){
               view = LayoutInflater.from(context).inflate(R.layout.message_send_layout,
                       parent,false);
               return new ViewHolder(view);
           }else{
               view = LayoutInflater.from(context).inflate(R.layout.message_receive_layout,
                       parent,false);
               return new ViewHolder(view);
           }

        }

        @Override
        public void onBindViewHolder (@NonNull MessageAdapterr.ViewHolder holder,
                                      @SuppressLint("RecyclerView") int position){
            try{
                holder.messageText.setText(messageModelList.get(position).getText());
            }catch (Exception e){
                holder.messageText.setText("");
            }
            try{
                holder.time.setText(messageModelList.get(position).getTime());
            }catch (Exception e){
                holder.time.setText("");
            }


//            holder.messageText.setOnLongClickListener(view -> {
//                createAlertDialog(userKeyList.get(position),messageModelList.
//                        get(position).getText());
//                return false;
//            });


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
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if(state){
                messageText = itemView.findViewById(R.id.sendermessage);
                time = itemView.findViewById(R.id.timeofmessage);
            }else{
                messageText = itemView.findViewById(R.id.receivermessage);
                time = itemView.findViewById(R.id.timeofmessage);
            }
        }
    }

    private void createAlertDialog(String otherUser, String targetText){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("DELETE MESSAGE");
        builder.setIcon(R.mipmap.ic_logo6);
        builder.setMessage("YOU SURE YOU WANNA DELETE THIS MESSAGE?");
        builder.setNegativeButton("NO",null);
        builder.setPositiveButton("YES", (dialogInterface, i) ->
                deleteMessage(otherUser, targetText));
        builder.show();
    }

    // finds the id of the message that will be deleted
    private void deleteMessage(String otherUser, String targetText){

        reference.child("Messages").child(firebaseUser.getUid()).child(otherUser)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("text").getValue().toString().equals(targetText)){
                        String temp = dataSnapshot.getKey();
                        reference.child("Messages").child(firebaseUser.getUid())
                                .child(otherUser).child(temp).removeValue((error, ref) -> {
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

