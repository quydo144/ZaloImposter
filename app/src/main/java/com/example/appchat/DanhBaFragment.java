package com.example.appchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Adapter.MyAdapter;
import com.example.appchat.Helper.MyButton;
import com.example.appchat.Helper.MyButtonClickListener;
import com.example.appchat.Helper.SwipeHelper;
import com.example.appchat.Models.BanBe;
import com.example.appchat.Models.Message;
import com.example.appchat.Models.NguoiDung;
import com.example.appchat.Retrofit2.APIUtils;
import com.example.appchat.Retrofit2.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DanhBaFragment extends Fragment {

    ArrayList<NguoiDung> lstUser = new ArrayList<>();
    View view;
    RecyclerView recyclerView;
    MyAdapter adapter;
    LinearLayoutManager layoutManager;
    SharedPreferences preferences;
    Button btnLoiMoiKetBan;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_danh_ba,container,false);
        Init();
        GetDanhSachBan();
        SwipeHelper();
        LoiMoiKetBan_Click();
        lstUser.clear();
        return view;

    }

    protected void Init(){
        preferences = getActivity().getSharedPreferences("data_dang_nhap", MODE_PRIVATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleDanhSachBan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        btnLoiMoiKetBan = (Button) view.findViewById(R.id.btnLoiMoiKetBan);
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
        SwipeHelper swipeHelper = new SwipeHelper(view.getContext(), recyclerView, 150) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(view.getContext(),"", 0, R.drawable.ic_baseline_delete_24, Color.parseColor("#FF3c30"), new MyButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        dialog.setTitle("Thông báo");
                        dialog.setMessage("Bạn có muốn xoá hay không?");

                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                                                Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    protected void ShowDanhSach(){
        adapter = new MyAdapter(view.getContext(), lstUser);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    protected void GetDanhSachBan(){
        int MaNguoiDung = preferences.getInt("MaNguoiDung" , 0);
        DataClient client = APIUtils.getData();
        BanBe banBe = new BanBe();
        banBe.setMaNguoiDung_Mot(MaNguoiDung);
        Call<Message> call = client.GetListFriend(banBe);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        for (NguoiDung user : response.body().getDanhsach()){
                            lstUser.add(user);
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
