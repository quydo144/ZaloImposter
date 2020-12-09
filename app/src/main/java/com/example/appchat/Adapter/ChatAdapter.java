package com.example.appchat.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Models.ConversationMap;
import com.example.appchat.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<ConversationMap> Chats;

    public ChatAdapter(ArrayList<ConversationMap> chats){
        this.Chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(Chats.get(position).getSender() != null) {
            holder.container_recylerview_item_user.setVisibility(View.VISIBLE);
            holder.tvTenNguoiNhan.setText(Chats.get(position).getSender().getHoTen());
        }else {
            holder.container_recylerview_item_user.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenNguoiNhan;
        CardView container_recylerview_item_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTenNguoiNhan = (TextView) itemView.findViewById(R.id.nameUser);
            container_recylerview_item_user = (CardView) itemView.findViewById(R.id.container_recylerview_item_user);
        }
    }
}
