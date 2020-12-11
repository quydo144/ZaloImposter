package com.example.appchat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Models.NhanTin;
import com.example.appchat.R;

import java.util.ArrayList;

public class NhanTinAdapter extends RecyclerView.Adapter<NhanTinAdapter.ViewHolder> {

    Context context;
    ArrayList<NhanTin> NhanTins;
    String tenNguoiGui;

    public NhanTinAdapter(Context context, ArrayList<NhanTin> nhanTins, String ten){
        this.context= context;
        NhanTins= nhanTins;
        this.tenNguoiGui = ten;
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
            if (NhanTins.get(position).getType_message().equals("image")){
                if (NhanTins.get(position).getBase64()){
                    byte[] decodedByteArray = Base64.decode(NhanTins.get(position).getTinGui(), Base64.NO_WRAP);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                    holder.img_Gui.setVisibility(View.VISIBLE);
                    Glide.with(context).load(decodedBitmap).into(holder.img_Gui);
                }
                else {
                    holder.img_Gui.setVisibility(View.VISIBLE);
                    Glide.with(context).load(NhanTins.get(position).getTinGui()).into(holder.img_Gui);
                }
                holder.container_nguoi_nhan.setVisibility(View.GONE);
                holder.tvGui.setVisibility(View.GONE);
            }
            else {
                holder.tvGui.setText(NhanTins.get(position).getTinGui());
                holder.container_nguoi_nhan.setVisibility(View.GONE);
                holder.tvGui.setVisibility(View.VISIBLE);
                holder.img_Gui.setVisibility(View.GONE);
            }
        }

        if(NhanTins.get(position).getTinGui() == ""){
            holder.container_nguoi_nhan.setVisibility(View.VISIBLE);
            holder.tvTenNhan.setText(tenNguoiGui);
            holder.tvGui.setVisibility(View.GONE);
            if (NhanTins.get(position).getType_message().equals("image")){
                holder.tvNhan.setVisibility(View.GONE);
                holder.img_Nhan.setVisibility(View.VISIBLE);
                holder.img_Gui.setVisibility(View.GONE);
                Glide.with(context).load(NhanTins.get(position).getTinNhan()).into(holder.img_Nhan);
            }
            else {
                holder.tvNhan.setText(NhanTins.get(position).getTinNhan());
                holder.tvNhan.setVisibility(View.VISIBLE);
                holder.img_Nhan.setVisibility(View.GONE);
                holder.img_Gui.setVisibility(View.GONE);
            }
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
    ImageView img_Gui, img_Nhan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNhan= itemView.findViewById(R.id.tvNhan);
            tvGui= itemView.findViewById(R.id.tvGui);
            tvTenNhan= itemView.findViewById(R.id.tvTenNhan);
            container_nguoi_nhan=itemView.findViewById(R.id.container_nguoi_nhan);
            img_Gui = itemView.findViewById(R.id.imageView_anhGui);
            img_Nhan = itemView.findViewById(R.id.imageView_anhNhan);
        }
    }
}
