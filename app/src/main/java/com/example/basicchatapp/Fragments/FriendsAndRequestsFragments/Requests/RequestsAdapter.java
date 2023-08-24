package com.example.basicchatapp.Fragments.FriendsAndRequestsFragments.Requests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.HelperMethods;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>{

    private List<RequestModel> requestModelList;
    private View view;
    private HelperMethods helper;
    private final RequestsRepository repository;

    public RequestsAdapter(List<RequestModel> requestModelList) {
        this.requestModelList = requestModelList;
        helper = new HelperMethods();
        repository = new RequestsRepository();
    }

    @NonNull
    @Override
    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item,
                parent,false);

        return new RequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsViewHolder holder, int position) {
        try {
            holder.image_request_item_pp.setImageResource(R.drawable.img);
        } catch (Exception e){
            // do nothing
        }

        try {
            holder.tv_request_item_username.setText(requestModelList.get(position).getUsername());
        } catch (Exception e){
            holder.tv_request_item_username.setText("");
        }

        holder.button_request_item_accept.setOnClickListener(view1 -> {
            helper.showShortToast(view.getContext(), "accepted");
            repository.acceptRequest(requestModelList.get(position).getUserId());
//            requestModelList.remove(position);
//            notifyItemRemoved(position);
        });

        holder.button_request_item_reject.setOnClickListener(view1 -> {
            helper.showShortToast(view.getContext(), "rejected");
            repository.rejectRequest(requestModelList.get(position).getUserId());
        });

    }


    @Override
    public int getItemCount() {
        return requestModelList.size();
    }


    public static class RequestsViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout constraint_request_item_root;
        private final CircleImageView image_request_item_pp;
        private final AppCompatTextView tv_request_item_username;
        private final AppCompatButton button_request_item_accept;
        private final AppCompatButton button_request_item_reject;


        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            constraint_request_item_root = itemView.findViewById(R.id.constraint_request_item_root);
            image_request_item_pp = itemView.findViewById(R.id.image_request_item_pp);
            tv_request_item_username = itemView.findViewById(R.id.tv_request_item_username);
            button_request_item_accept = itemView.findViewById(R.id.button_request_item_accept);
            button_request_item_reject = itemView.findViewById(R.id.button_request_item_reject);
        }
    }
}
