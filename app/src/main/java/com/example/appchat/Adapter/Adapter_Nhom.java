package com.example.appchat.Adapter;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Models.NguoiDung;
import com.example.appchat.R;

import java.util.ArrayList;

public class Adapter_Nhom extends RecyclerView.Adapter<Adapter_Nhom.MyViewHolderNhom> {
    MyViewHolderNhom holderNhom;
    Context context;
    ArrayList<NguoiDung> itemNguoiDung;
    ArrayList<NguoiDung> checkedNguoiDung= new ArrayList<>();
    SharedPreferences preferences;
    boolean statusBanBe;
    OnMultiClickCheckBoxListener onListener;

        public void setOnMultiClickCheckBoxListener(OnMultiClickCheckBoxListener onListener){
            this.onListener= onListener;
        }

        public Adapter_Nhom (Context context, ArrayList<NguoiDung> lstNguoiDung, Boolean statusBanBe){
            preferences= context.getSharedPreferences("data_dang_nhap", Context.MODE_PRIVATE);

            this.context= context;
            this.itemNguoiDung= lstNguoiDung;
            this.statusBanBe= statusBanBe;
        }

    @NonNull
    @Override
    public MyViewHolderNhom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(context).inflate(R.layout.item_recycle_thembanchat, parent, false);
            holderNhom = new MyViewHolderNhom(itemView, onListener);
        return holderNhom;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderNhom holder, int position) {
        this.setOnMultiClickCheckBoxListener(new OnMultiClickCheckBoxListener() {
            @Override
            public void onMultiClickCheckBox(View view, int position) {
                CheckBox chk= (CheckBox) view;

                if (chk.isChecked()){
                    checkedNguoiDung.add(itemNguoiDung.get(position));
                } else if (!chk.isChecked()){
                    checkedNguoiDung.remove(itemNguoiDung.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemNguoiDung.size();
    }

    public class MyViewHolderNhom extends RecyclerView.ViewHolder implements View.OnClickListener{
        CheckBox checkBoxThemBan;

        public MyViewHolderNhom(@NonNull View itemView, OnMultiClickCheckBoxListener onListener) {
            super(itemView);
            this.checkBoxThemBan= itemView.findViewById(R.id.checkBox_themBanVaoNhom);
        }

        @Override
        public void onClick(View v) {
            onListener.onMultiClickCheckBox(v, getAdapterPosition());
        }
    }
}
