package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.example.appchat.Adapter.NhanTinAdapter;
import com.example.appchat.Models.NhanTin;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class NhanTinDonActivity extends AppCompatActivity {

    Socket client_socket;
    ImageButton btnGui_Chat, btnTuyChon;
    EditText edtNhanTin;
    Button btnBack;
    TextView tvTenNguoiNhan;
    RecyclerView rvTinNhan;
    ArrayList<NhanTin> NhanTins;
    NhanTinAdapter nhanTinAdapter;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_tin_don);

        NhanTins = new ArrayList<>();

        rvTinNhan = findViewById(R.id.recycleTinNhanChat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NhanTinDonActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setStackFromEnd(true);
        nhanTinAdapter = new NhanTinAdapter(NhanTinDonActivity.this, NhanTins);
        rvTinNhan.setAdapter(nhanTinAdapter);
        rvTinNhan.setLayoutManager(layoutManager);

        SharedPreferences preferences = getSharedPreferences("data_dang_nhap",MODE_PRIVATE);
        String soDienThoai = preferences.getString("SoDienThoai","");
        String sdt = getIntent().getStringExtra("sdt");

        String tenPhong = sdt.concat(soDienThoai);
        String ten = getIntent().getStringExtra("ten");
        Log.d("123", "onCreate: tenPhong" +tenPhong);

        JSONObject object = new JSONObject();
        try{
            object.put("NickName",ten);
            object.put("TenPhong","123");
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            client_socket = IO.socket("http://54.179.131.22:3002");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client_socket.connect();
        client_socket.on("SERVER_GUI_TIN_NHAN", NhanTinNhanServer);
        client_socket.emit("DANG_KY_PHONG", object);

        edtNhanTin = (EditText) findViewById(R.id.edtTinNhan_Don);
        btnGui_Chat = (ImageButton) findViewById(R.id.btn_Gui_Tin_Nhan_Don);

        btnGui_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NhanTins.add(new NhanTin(edtNhanTin.getText().toString().trim(), ""));
                JSONObject object = new JSONObject();
                try {
                    object.put("NickName", ten);
                    object.put("NoiDung", edtNhanTin.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                client_socket.emit("CLIENT_GUI_TIN_NHAN", object);
                nhanTinAdapter.notifyDataSetChanged();
                rvTinNhan.scrollToPosition(NhanTins.size() - 1);

                edtNhanTin.setText("");
            }
        });
    }

    private Emitter.Listener NhanTinNhanServer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    String tinNhan_Server = "";
                    String nickName_Sender = "";
                    String tinNhan_Gui = "";
                    String tinNhan_Nhan = "";

                    try {
                        tinNhan_Gui = object.getString("NoiDung");
                        nickName_Sender = object.getString("NickName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NhanTins.add(new NhanTin(tinNhan_Nhan, tinNhan_Gui));
                    nhanTinAdapter.notifyDataSetChanged();
                    rvTinNhan.scrollToPosition(NhanTins.size() - 1);
                }
            });
        }
    };

}