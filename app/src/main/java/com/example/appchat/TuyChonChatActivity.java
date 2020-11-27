package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TuyChonChatActivity extends AppCompatActivity {

    ImageButton btnBack_Chat_Don;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuy_chon_chat);

        btnBack_Chat_Don= findViewById(R.id.btnBack_tuy_chon_chat_don);

        btnBack_Chat_Don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}