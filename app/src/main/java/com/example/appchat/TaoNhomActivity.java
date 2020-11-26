package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TaoNhomActivity extends AppCompatActivity {

    ImageButton btnBackNhom, btnDongYTaoNhom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_nhom);
        init_Data();
        backFragmentNhom_Click();
    }

    protected void backFragmentNhom_Click() {
        btnBackNhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void init_Data() {
        btnBackNhom= findViewById(R.id.btnBack_FragmentNhom);
    }
}