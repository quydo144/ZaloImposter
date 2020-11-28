package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appchat.Adapter.MyAdapter;
import com.example.appchat.Adapter.OnItemClickListener;
import com.example.appchat.Helper.MyButton;
import com.example.appchat.Helper.MyButtonClickListener;
import com.example.appchat.Helper.SwipeHelper;
import com.example.appchat.Models.BanBe;
import com.example.appchat.Models.Message;
import com.example.appchat.Models.NguoiDung;
import com.example.appchat.Retrofit2.APIUtils;
import com.example.appchat.Retrofit2.DataClient;
import com.example.appchat.Socket.SocketClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachLoiMoiKetBanActivity extends AppCompatActivity {

    ArrayList<NguoiDung> lstUser = new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter adapter;
    LinearLayoutManager layoutManager;
    SharedPreferences preferences;
    ImageButton btnBack_LoiMoiKetBan;
    SwipeRefreshLayout refresh_loimoiketban;
    SocketClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_loi_moi_ket_ban);

        Init();
        ShowDanhSach();
        GetDanhSachCho();
        SwipeHelper();
        SwipeRefresh();
        Back();
        mClient = new SocketClient();
    }

    protected void Init() {
        btnBack_LoiMoiKetBan = (ImageButton) findViewById(R.id.btnBack_LoiMoiKetBan);
        preferences = getSharedPreferences("data_dang_nhap", MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.recycleDanhSachCho);
        refresh_loimoiketban = (SwipeRefreshLayout) findViewById(R.id.refresh_loimoiketban);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    protected void Back() {
        btnBack_LoiMoiKetBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void SwipeRefresh() {
        refresh_loimoiketban.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lstUser.clear();
                GetDanhSachCho();
                refresh_loimoiketban.setRefreshing(false);
            }
        });
    }

    protected void SwipeHelper() {
        SwipeHelper swipeHelper = new SwipeHelper(DanhSachLoiMoiKetBanActivity.this, recyclerView, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {

                buffer.add(new MyButton(DanhSachLoiMoiKetBanActivity.this, "Từ chối", 0, R.drawable.ic_baseline_delete_24, Color.parseColor("#FF3c30"), new MyButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DanhSachLoiMoiKetBanActivity.this);
                        dialog.setTitle("Thông báo");
                        dialog.setMessage("Bạn có muốn xóa lời mời này không?");

                        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int MaNguoiDung = preferences.getInt("MaNguoiDung", 0);
                                DataClient client = APIUtils.getData();
                                BanBe banBe = new BanBe();
                                banBe.setMaNguoiDung_Mot(MaNguoiDung);
                                banBe.setMaNguoiDung_Hai(lstUser.get(pos).getMaNguoiDung());
                                lstUser.remove(pos);
                                Call<Message> call = client.DeleteRequestFriend(banBe);
                                call.enqueue(new Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getSuccess() == 1) {
                                                ShowDanhSach();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Message> call, Throwable t) {

                                    }
                                });
                            }
                        });

                        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ShowDanhSach();
                            }
                        });

                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
                    }
                }));
            }
        };
    }

    protected void ShowDanhSach() {
        adapter = new MyAdapter(this, lstUser, true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BanBe banBe = new BanBe();
                banBe.setMaNguoiDung_Mot(preferences.getInt("MaNguoiDung", 0));
                banBe.setMaNguoiDung_Hai(lstUser.get(position).getMaNguoiDung());
                DataClient dataClient = APIUtils.getData();
                Call<Message> call = dataClient.AcceptRequestFriend(banBe);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()){
                            if (response.body().getSuccess() == 1){
                                JSONObject object = new JSONObject();

                                try {
                                    object.put("MaNguoiDung_Hai", lstUser.get(position).getMaNguoiDung());
                                    object.put("SoDienThoai", lstUser.get(position).getSoDienThoai());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mClient.getmClient().emit("DaXacNhanLoiMoiKetBan", object);

                                //Cập Nhật Lại Danh Sách Danh Bạ
                                SharedPreferences preferences = getSharedPreferences("data_danh_ba", MODE_PRIVATE);
                                String ListUser = preferences.getString("ListUser", "");
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<NguoiDung>>() {}.getType();
                                ArrayList<NguoiDung> temp = gson.fromJson(ListUser, type);

                                temp.add(lstUser.get(position));
                                SharedPreferences.Editor editor = preferences.edit();
                                String json = gson.toJson(temp);
                                editor.putString("ListUser", json);
                                editor.commit();

                                lstUser.remove(lstUser.get(position));
                                Toast.makeText(getApplicationContext(), "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                                ShowDanhSach();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {

                    }
                });
            }
        });
    }

    protected void GetDanhSachCho() {
        int MaNguoiDung = preferences.getInt("MaNguoiDung", 0);
        DataClient client = APIUtils.getData();
        BanBe banBe = new BanBe();
        banBe.setMaNguoiDung_Mot(MaNguoiDung);
        Call<Message> call = client.GetListRequestFriend(banBe);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        for (NguoiDung user : response.body().getDanhsach()) {
                            if (user.isStatus()) {
                                lstUser.add(user);
                            }
                        }
                        ShowDanhSach();
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }
}
