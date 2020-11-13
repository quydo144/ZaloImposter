package com.example.appchat.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Models.NguoiDung;
import com.example.appchat.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    ArrayList<NguoiDung> itemNguoiDung;
    boolean statusBanBe;
    SharedPreferences preferences;
    OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public MyAdapter (Context context, ArrayList<NguoiDung> lstNguoDung, Boolean statusBanBe){
        preferences = context.getSharedPreferences("data_dang_nhap", Context.MODE_PRIVATE);
        this.context = context;
        this.itemNguoiDung = lstNguoDung;
        this.statusBanBe = statusBanBe;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_recycle, parent, false);
        return new MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameUser.setText(itemNguoiDung.get(position).getHoTen());
        if (statusBanBe){
            holder.btnChapNhanAdd.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return itemNguoiDung.size();
    }
}
