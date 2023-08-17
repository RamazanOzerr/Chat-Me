package com.example.basicchatapp.Fragments.ChatsFragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private View view;
    private List<ChatModel> chatModelList;

    public ChatAdapter(List<ChatModel> chatModelList){
        this.chatModelList = chatModelList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,
                parent,false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        try{
            holder.text_chat_item_last_message.setText(chatModelList.get(position).getText());
        } catch (Exception e){
            holder.text_chat_item_last_message.setText("");
        }

        try{
            holder.text_username_chat_item.setText(chatModelList.get(position).getUsername());
        } catch (Exception e){
            holder.text_username_chat_item.setText("");
        }

        try{
            holder.text_chat_item_time.setText(chatModelList.get(position).getTime());
        } catch (Exception e){
            holder.text_chat_item_time.setText("");
        }

        // set online status
        if(chatModelList.get(position).getStatus().equals("online")){
            holder.image_chat_item_online_status.setImageResource(R.drawable.baseline_circle_24);
        }else {
            holder.image_chat_item_online_status.setImageResource(R.drawable.baseline_circle_red_24);
        }

        try{
            //todo: debug için şimdilik fotoyu kendimiz set ediyoruz, db devreye girince yorumdan
            // çıkarcaz methodu
//            Picasso.get().load(chatModelList.get(position)
//                    .getPhoto_url()).into(holder.image_noti);
            holder.image_chat_item_profile.setImageResource(R.drawable.img);

        }catch (Exception e){
            // it'll set the default photo, so it'll remain as it is setted in xml file
            holder.image_chat_item_profile.setImageResource(R.drawable.img);
        }
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView image_chat_item_profile;
        private final AppCompatImageView image_chat_item_online_status;
        private final MaterialTextView text_username_chat_item
                , text_chat_item_last_message, text_chat_item_time;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            image_chat_item_profile = itemView.findViewById(R.id.image_chat_item_profile);
            image_chat_item_online_status = itemView.findViewById(R.id.image_chat_item_online_status);
            text_username_chat_item = itemView.findViewById(R.id.text_username_chat_item);
            text_chat_item_last_message = itemView.findViewById(R.id.text_chat_item_last_message);
            text_chat_item_time = itemView.findViewById(R.id.text_chat_item_time);
        }
    }

}
