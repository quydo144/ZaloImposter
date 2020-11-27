package com.example.appchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Models.NhanTin;
import com.example.appchat.R;

import java.util.ArrayList;

public class NhanTinAdapter extends RecyclerView.Adapter<NhanTinAdapter.ViewHolder> {

    Context context;
    ArrayList<NhanTin> NhanTins;

    public NhanTinAdapter(Context context, ArrayList<NhanTin> nhanTins){
        this.context= context;
        NhanTins= nhanTins;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhanTinAdapter.ViewHolder holder, int position) {

        if(NhanTins.get(position).getTinNhan() == ""){
            holder.tvGui.setText(NhanTins.get(position).getTinGui());
            holder.container_nguoi_nhan.setVisibility(View.GONE);
            holder.tvGui.setVisibility(View.VISIBLE);
        }
        if(NhanTins.get(position).getTinGui() == ""){
            holder.tvNhan.setText(NhanTins.get(position).getTinNhan());
            holder.tvGui.setVisibility(View.GONE);
            holder.container_nguoi_nhan.setVisibility(View.VISIBLE);

            //holder.tvTenNhan.setText(NhanTins.get(position).getNickName());
        }

    }

    @Override
    public int getItemCount() {
        return NhanTins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

    LinearLayout container_nguoi_nhan;
    TextView tvNhan;
    TextView tvGui;
    TextView tvTenNhan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNhan= itemView.findViewById(R.id.tvNhan);
            tvGui= itemView.findViewById(R.id.tvGui);
            tvTenNhan= itemView.findViewById(R.id.tvTenNhan);
            container_nguoi_nhan=itemView.findViewById(R.id.container_nguoi_nhan);

        }
    }
}
