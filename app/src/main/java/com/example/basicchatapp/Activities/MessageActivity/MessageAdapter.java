package com.example.basicchatapp.Activities.MessageActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<MessageModel> messageModelList;

    private final int view_type_send, view_type_receive;
    private boolean state;
    private final String userId;

    public MessageAdapter(List<MessageModel> messageModelList) {
        this.messageModelList = messageModelList;
        view_type_send = 1;
        view_type_receive=2;
        state = false;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //todo: we comment out the line below for testing, fix it later on
//            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            userId = "Okan Buruk";
        } else {
            userId = "null";
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == view_type_send){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_item,
                    parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_item,
                    parent,false);
        }
        return new MessageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        try {
            holder.tv_message_time.setText(messageModelList.get(position).getTime());
        } catch (Exception e){
            holder.tv_message_time.setText("");
        }

        try {
            holder.tv_text_body.setText(messageModelList.get(position).getText());
        } catch (Exception e){
            holder.tv_text_body.setText("");
        }


    }

    public int getItemViewType(int position){
        if(messageModelList.get(position).getSender().equals(userId)){
            state = true;
            return view_type_send;
        }else{
            state = false;
            return view_type_receive;
        }
    }


    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        private final AppCompatTextView tv_message_time, tv_text_body;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text_body = itemView.findViewById(R.id.tv_text_body);
            tv_message_time = itemView.findViewById(R.id.tv_message_time);
        }
    }
}
