package com.example.appchat.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView nameUser;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        nameUser = (TextView) itemView.findViewById(R.id.nameUser);


    }
}
