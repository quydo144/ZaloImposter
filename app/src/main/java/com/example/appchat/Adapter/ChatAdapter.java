package com.example.appchat.Adapter;

<<<<<<< HEAD
=======
import android.content.ClipData;
>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Models.ConversationMap;
import com.example.appchat.Models.NguoiDung;
import com.example.appchat.NhanTinDonActivity;
import com.example.appchat.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<ConversationMap> Chats;
<<<<<<< HEAD

    public ChatAdapter(ArrayList<ConversationMap> chats) {
=======
    public ChatAdapter(ArrayList<ConversationMap> chats){
>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
        this.Chats = chats;
    }

    private OnChatItemListener mListener;

<<<<<<< HEAD
    public interface OnChatItemListener {
=======
    public interface OnChatItemListener{
>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
        void onChatItemClick(View v, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Chats.get(position).getSender() != null) {
            holder.container_recylerview_item_user.setVisibility(View.VISIBLE);
            holder.tvTenNguoiNhan.setText(Chats.get(position).getSender().getHoTen());
<<<<<<< HEAD
=======

>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
            holder.setOnChatItemListener(new OnChatItemListener() {
                @Override
                public void onChatItemClick(View v, int position) {
                    NguoiDung nd = Chats.get(position).getSender();
<<<<<<< HEAD
=======

>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
                    Intent intent = new Intent(v.getContext(), NhanTinDonActivity.class);
                    intent.putExtra("sdt", nd.getSoDienThoai());
                    intent.putExtra("ten", nd.getHoTen());
                    intent.putExtra("id_user", nd.getMaNguoiDung());
                    v.getContext().startActivity(intent);
                }
            });
<<<<<<< HEAD
        } else {
=======

        }else {
>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
            holder.container_recylerview_item_user.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Chats.size();
    }

<<<<<<< HEAD
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
=======
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnChatItemListener onChatItemListener;

>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
        TextView tvTenNguoiNhan;
        CardView container_recylerview_item_user;
        OnChatItemListener onChatItemListener;

        public ViewHolder(@NonNull View itemView, OnChatItemListener listener) {
            super(itemView);
            tvTenNguoiNhan = (TextView) itemView.findViewById(R.id.nameUser);
            container_recylerview_item_user = (CardView) itemView.findViewById(R.id.container_recylerview_item_user);
<<<<<<< HEAD
=======

>>>>>>> 9c83b9d9793fd70885bbdaab25377bcf7165ddd1
            this.onChatItemListener = listener;
            itemView.setOnClickListener(this::onClick);
        }

        public void setOnChatItemListener(OnChatItemListener listener) {
            this.onChatItemListener = listener;
        }

        @Override
        public void onClick(View v) {
            this.onChatItemListener.onChatItemClick(v, getAdapterPosition());
        }
    }
}
