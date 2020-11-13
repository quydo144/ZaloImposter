package com.example.appchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appchat.Models.BanBe;
import com.example.appchat.Models.Message;
import com.example.appchat.Models.NguoiDung;
import com.example.appchat.Retrofit2.APIUtils;
import com.example.appchat.Retrofit2.DataClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ThemBanFragment extends Fragment {
    final String Regex_CheckSoDienThoai = "^0[1-9][0-9]{8}$";
    final int EDIT_CODE_INTENT = 9999;
    EditText txtSDT;
    SharedPreferences preferences;
    ProgressBar prgbr_Loading;
    View view;
    String token, SDT;
    Button btnTim;
    NguoiDung nguoi_dung_infor;
    BanBe ban_be_info;
    Integer status = -1;
    Fragment selectedFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_them_ban, container, false);
        ban_be_info = new BanBe();
        Init_Data();
        GetMaNguoiDung();
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Get_ThongTin_BanBe();
            }
        });
        return view;
    }

    protected void Init_Data() {
        preferences = getActivity().getSharedPreferences("data_dang_nhap", MODE_PRIVATE);
        token = preferences.getString("Token_DangNhap", "");
        SDT = preferences.getString("SoDienThoai", "");
        btnTim = (Button) view.findViewById(R.id.btnTimBan);
        txtSDT = (EditText) view.findViewById(R.id.etxtSoDienThoai_TimBanBe);
        prgbr_Loading = (ProgressBar) view.findViewById(R.id.prgbr_Loading);
    }

    protected void GetMaNguoiDung(){
        String temp = token;
        DataClient client = APIUtils.getData();
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", ("Bearer " + temp).trim());
        Call<Message> call = client.GetThongTinNguoiDung_bySDT(SDT, map);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        nguoi_dung_infor = response.body().getData();
                        int i = nguoi_dung_infor.getMaNguoiDung();
                        ban_be_info.setMaNguoiDung_Mot(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });

        int MaNguoiDung = preferences.getInt("MaNguoiDung", 0);
        if(MaNguoiDung == 0){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("MaNguoiDung", nguoi_dung_infor.getMaNguoiDung());
            editor.apply();
        }
    }

    protected void Get_ThongTin_BanBe(){
        String temp = token;
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", ("Bearer " + temp).trim());
        String SDT = txtSDT.getText().toString();
        if(!SDT.matches(Regex_CheckSoDienThoai)) {
            Toast.makeText(view.getContext(),"Số Điện Thoại Không Hợp Lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        prgbr_Loading.setVisibility(view.VISIBLE);
        DataClient client = APIUtils.getData();
        Call<Message> call = client.GetThongTinNguoiDung_bySDT(SDT, map);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        nguoi_dung_infor = response.body().getData();
                        if (nguoi_dung_infor != null){
                            ban_be_info.setMaNguoiDung_Hai(nguoi_dung_infor.getMaNguoiDung());
                            CheckTrangThaiBanBe();
                        }
                    }
                    else {
                        prgbr_Loading.setVisibility(view.GONE);
                        Toast.makeText(view.getContext(), "Không tìm thấy", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    protected void CheckTrangThaiBanBe(){
        DataClient client = APIUtils.getData();
        Call<Message> call = client.CheckTrangThaiBanBe(ban_be_info);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()){
                    if (response.body().getSuccess() == 1){
                        int MaNguoiDung = preferences.getInt("MaNguoiDung", 0);
                        if (MaNguoiDung == nguoi_dung_infor.getMaNguoiDung()){
                            selectedFragment = new ThongTinFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, selectedFragment).addToBackStack(selectedFragment.getClass().getSimpleName()).commit();
                        }
                        else {
                            status = response.body().getStatus();
                            if (status == -1){
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(getActivity(), ThongTinChuaKetBanActivity.class);
                                bundle.putSerializable("ThongTinNguoiDung", nguoi_dung_infor);
                                intent.putExtra("Infor", bundle);
                                prgbr_Loading.setVisibility(view.GONE);
                                startActivityForResult(intent, EDIT_CODE_INTENT);
                            }
                            else if (status == 0 && (response.body().getAction() == ban_be_info.getMaNguoiDung_Mot())){
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(getActivity(), ThongTinDaGuiLoiMoiKetBan.class);
                                bundle.putSerializable("ThongTinNguoiDung", nguoi_dung_infor);
                                intent.putExtra("Infor", bundle);
                                prgbr_Loading.setVisibility(view.GONE);
                                startActivityForResult(intent, EDIT_CODE_INTENT);
                            }
                            else if (status == 0 && (response.body().getAction() != ban_be_info.getMaNguoiDung_Mot())){
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(getActivity(), ThongTinChapNhanLoiMoiKetBan.class);
                                bundle.putSerializable("ThongTinNguoiDung", nguoi_dung_infor);
                                intent.putExtra("Infor", bundle);
                                prgbr_Loading.setVisibility(view.GONE);
                                startActivityForResult(intent, EDIT_CODE_INTENT);
                            }
                            else if (status == 1){
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(getActivity(), ThongTinDaKetBanActivity.class);
                                bundle.putSerializable("ThongTinNguoiDung", nguoi_dung_infor);
                                intent.putExtra("Infor", bundle);
                                prgbr_Loading.setVisibility(view.GONE);
                                startActivityForResult(intent, EDIT_CODE_INTENT);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }
}