package com.example.appchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Models.NguoiDung;
import com.example.appchat.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    ArrayList<NguoiDung> itemNguoiDung;

    public MyAdapter (Context context, ArrayList<NguoiDung> lstNguoDung){
        this.context = context;
        this.itemNguoiDung = lstNguoDung;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_recycle, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Glide.with(context).load(itemNguoiDung.get(position).getHoTen()).into(holder.nameUser)    get image to show
        holder.nameUser.setText(itemNguoiDung.get(position).getHoTen());
    }

    @Override
    public int getItemCount() {
        return itemNguoiDung.size();
    }
}
