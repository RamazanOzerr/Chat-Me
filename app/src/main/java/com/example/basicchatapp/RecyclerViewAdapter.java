package com.example.basicchatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<Profile> data;

    public RecyclerViewAdapter(Context context, List<Profile> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);

        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.text.setText(data.get(position).getName());
        holder.image.setImageResource(data.get(position).getImage());

    }


    // the method where we should give the number of items we wanna display on the screen
    @Override
    public int getItemCount() {
        return data.size();
    }


    // we can see this one as the onCreate method, coz we do pretty similar stuff here
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
        }
    }


}


