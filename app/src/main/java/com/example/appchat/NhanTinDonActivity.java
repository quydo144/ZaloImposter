package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appchat.Adapter.NhanTinAdapter;
import com.example.appchat.Models.DataMessage;
import com.example.appchat.Models.ItemMessage;
import com.example.appchat.Models.Message;
import com.example.appchat.Models.NhanTin;
import com.example.appchat.Models.Room;
import com.example.appchat.Retrofit2.APIUtils;
import com.example.appchat.Retrofit2.DataClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhanTinDonActivity extends AppCompatActivity {

    Socket client_socket;
    ImageButton btnGui_Chat, btnTuyChon, btnBack_DanhBa, btnGuiTinNhan_File, btnGuiTinNhan_Hinh;
    EditText edtNhanTin;
    TextView tvTenNguoiNhan;
    RecyclerView recycleTinNhan;
    ArrayList<NhanTin> listNhanTin;
    NhanTinAdapter nhanTinAdapter;
    SharedPreferences preferences;
    String ten;
    String id_room = "";
    int id_user_2 = 0;
    int id_user_1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_tin_don);
        Init();
        Back_DanhBa();
        InitSocket();
        CheckRoom();
        SendMessage();
        TuyChon();

    }

    protected void Init() {
        ten = getIntent().getStringExtra("ten");
        preferences = getSharedPreferences("data_dang_nhap", MODE_PRIVATE);
        listNhanTin = new ArrayList<>();
        recycleTinNhan = findViewById(R.id.recycleTinNhanChat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NhanTinDonActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setStackFromEnd(true);
        nhanTinAdapter = new NhanTinAdapter(NhanTinDonActivity.this, listNhanTin, ten);
        recycleTinNhan.setAdapter(nhanTinAdapter);
        recycleTinNhan.setLayoutManager(layoutManager);
        edtNhanTin = (EditText) findViewById(R.id.edtTinNhan_Don);
        btnGui_Chat = (ImageButton) findViewById(R.id.btn_Gui_Tin_Nhan_Don);
        btnTuyChon = findViewById(R.id.btn_Tuy_Chon_Chat_Don);
        btnBack_DanhBa = findViewById(R.id.btnBack_Fragment_Tro_Chuyen);
    }

    protected void InitSocket() {
        try {
            client_socket = IO.socket("http://192.168.1.9:6000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client_socket.disconnect();
        client_socket.connect();
        client_socket.on("SERVER_GUI_TIN_NHAN", NhanTinNhanServer);

    }

    private void CheckTableChat(){
        DataClient client = APIUtils.getDataDemo();
        Room room = new Room();
        room.setId_room(id_room);
        Call<Message> call = client.CreateTable(room);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    private void CheckRoom() {
        String soDienThoai = preferences.getString("SoDienThoai", "");
        String sdt = getIntent().getStringExtra("sdt");
        String id_room_sdt = sdt.concat(soDienThoai);

        id_user_2 = getIntent().getIntExtra("id_user", 0);
        id_user_1 = preferences.getInt("MaNguoiDung", 0);
        Room room = new Room();
        room.setId_user_1(id_user_1);
        room.setId_user_2(id_user_2);
        DataClient client = APIUtils.getData();
        Call<Message> call = client.FindRoomChat(room);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        id_room = response.body().getId_room();
                    } else {
                        room.setId_room(id_room_sdt);
                        AddRoom(room);
                    }
                    JSONObject object = new JSONObject();
                    try {
                        object.put("TenPhong", id_room);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CheckTableChat();
                    LoadTinNhan();
                    client_socket.emit("DANG_KY_PHONG", object);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    private void AddRoom(Room room) {
        DataClient client = APIUtils.getData();
        Call<Message> call = client.AddRoomChat(room);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful())
                    if (response.body().getSuccess() == 1) {
                        id_room = room.getId_room();
                    }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    protected void SendMessage() {
        btnGui_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listNhanTin.add(new NhanTin(edtNhanTin.getText().toString().trim(), ""));
                JSONObject object = new JSONObject();
                int userSend = id_user_1;
                int userReceive = id_user_2;
                try {
                    object.put("userSend", userSend);
                    object.put("userReceive", userReceive);
                    object.put("message", edtNhanTin.getText().toString().trim());
                    object.put("type_message", "text");
                    object.put("tableName", id_room);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                client_socket.emit("CLIENT_GUI_TIN_NHAN", object);
                nhanTinAdapter.notifyDataSetChanged();
                recycleTinNhan.scrollToPosition(listNhanTin.size() - 1);
                edtNhanTin.setText("");
            }
        });
    }

    protected void Back_DanhBa() {
        btnBack_DanhBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void TuyChon() {
        btnTuyChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NhanTinDonActivity.this, TuyChonChatActivity.class);
                startActivity(intent);
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
                        tinNhan_Gui = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    listNhanTin.add(new NhanTin("", tinNhan_Gui));
                    nhanTinAdapter.notifyDataSetChanged();
                    recycleTinNhan.scrollToPosition(listNhanTin.size() - 1);
                }
            });
        }
    };

    private void LoadTinNhan(){
        Room room = new Room();
        room.setId_room(id_room);
        DataClient client = APIUtils.getDataDemo();
        Call<DataMessage> call = client.ScanFirstItemMessage(room);
        call.enqueue(new Callback<DataMessage>() {
            @Override
            public void onResponse(Call<DataMessage> call, Response<DataMessage> response) {
                if (response.isSuccessful()){
                    ArrayList<ItemMessage> data = response.body().getItems();
                    for (ItemMessage item : data){
                        if (item.getUserSend() == id_user_1){
                            listNhanTin.add(new NhanTin(item.getMessage(), ""));
                        }
                        if (item.getUserReceive() == id_user_1){
                            listNhanTin.add(new NhanTin("", item.getMessage()));
                        }
                    }
                    nhanTinAdapter.notifyDataSetChanged();
                    recycleTinNhan.scrollToPosition(listNhanTin.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<DataMessage> call, Throwable t) {

            }
        });
    }
}