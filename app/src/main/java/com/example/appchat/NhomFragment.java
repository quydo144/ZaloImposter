package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NhomFragment extends Fragment {
    View view;
    Button btnTaoNhom;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_nhom, container, false);
        init_Data();
        taoNhom_Click();

        return view;
    }

    protected void taoNhom_Click() {
        btnTaoNhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), TaoNhomActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void init_Data() {

        btnTaoNhom= view.findViewById(R.id.btnTaoNhom);
    }
}
