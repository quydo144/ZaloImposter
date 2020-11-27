package com.example.appchat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appchat.Adapter.MyAdapter;
import com.example.appchat.Helper.MyButton;
import com.example.appchat.Helper.MyButtonClickListener;
import com.example.appchat.Helper.SwipeHelper;
import com.example.appchat.Models.BanBe;
import com.example.appchat.Models.Message;
import com.example.appchat.Models.NguoiDung;
import com.example.appchat.Retrofit2.APIUtils;
import com.example.appchat.Retrofit2.DataClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DanhBaFragment extends Fragment {
    int CountSwipe = 1;

    ArrayList<NguoiDung> lstUser = new ArrayList<>();
    View view;
    RecyclerView recyclerView;
    MyAdapter adapter;
    LinearLayoutManager layoutManager;
    SharedPreferences preferences;
    Button btnLoiMoiKetBan;
    SwipeRefreshLayout refreshLayout;
    ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_danh_ba,container,false);

        Init();
        GetDanhSachBan();
        SwipeHelper();
        LoiMoiKetBan_Click();
        SwipeRefreshLayout();
        lstUser.clear();

        return view;
    }

    /*
    private void GuiThongBaoDangKyThongTinDanhBa(){
        SharedPreferences preferences = preferences = getActivity().getSharedPreferences("data_dang_nhap", MODE_PRIVATE);
        String SoDienThoai = preferences.getString("SoDienThoai", "");

        JSONObject object = new JSONObject();
        try {
            object.put("SoDienThoai", SoDienThoai);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mClient.emit("DangKyThongBaoDanhBa", object);
    }*/

    protected void Init(){
        preferences = getActivity().getSharedPreferences("data_dang_nhap", MODE_PRIVATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleDanhSachBan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        btnLoiMoiKetBan = (Button) view.findViewById(R.id.btnLoiMoiKetBan);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_danhba);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    protected void SwipeRefreshLayout(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lstUser.clear();
                GetDanhSachBan();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    protected void LoiMoiKetBan_Click(){
        btnLoiMoiKetBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachLoiMoiKetBanActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void SwipeHelper(){
        SwipeHelper swipeHelper = new SwipeHelper(view.getContext(), recyclerView, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                MyButton temp = new MyButton(view.getContext(),"", 0, R.drawable.ic_baseline_delete_24, Color.parseColor("#FF3c30"), new MyButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        dialog.setTitle("Thông báo");
                        dialog.setMessage("Bạn có muốn xoá hay không?");

                        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int MaNguoiDung = preferences.getInt("MaNguoiDung" , 0);
                                DataClient client = APIUtils.getData();
                                BanBe banBe = new BanBe();
                                banBe.setMaNguoiDung_Mot(MaNguoiDung);
                                banBe.setMaNguoiDung_Hai(lstUser.get(pos).getMaNguoiDung());
                                lstUser.remove(pos);
                                Call<Message> call = client.DeleteRequestFriend(banBe);
                                call.enqueue(new Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {
                                        if (response.isSuccessful()){
                                            if (response.body().getSuccess() == 1){
                                                Toast.makeText(view.getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
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
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                });
                buffer.add(temp);
            }
        };
    }

    protected void ShowDanhSach(){
        SharedPreferences preferences = getActivity().getSharedPreferences("data_danh_ba", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(lstUser);
        editor.putString("ListUser", json);
        editor.commit();

        adapter = new MyAdapter(view.getContext(), lstUser, false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        JSONObject object = new JSONObject();
//        try {
//            object.put("ThongBao", json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        mClient.emit("ThongBaoHienThiDanhBa", object);
    }

    protected void GetDanhSachBan(){
        progressBar.setVisibility(View.VISIBLE);
        int MaNguoiDung = preferences.getInt("MaNguoiDung" , 0);
        DataClient client = APIUtils.getData();
        BanBe banBe = new BanBe();
        banBe.setMaNguoiDung_Mot(MaNguoiDung);
        Call<Message> call = client.GetListFriend(banBe);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    if (response.body().getSuccess() == 1) {
                        for (NguoiDung user : response.body().getDanhsach()){
                            if (user.isStatus()){
                                lstUser.add(user);
                            }
                        }
                        ShowDanhSach();
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
