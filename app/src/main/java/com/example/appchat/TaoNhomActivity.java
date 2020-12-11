package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.appchat.Adapter.Adapter_Nhom;
import com.example.appchat.Adapter.MyAdapter;
import com.example.appchat.Adapter.OnMultiClickCheckBoxListener;
import com.example.appchat.Models.BanBe;
import com.example.appchat.Models.Message;
import com.example.appchat.Models.NguoiDung;
import com.example.appchat.Retrofit2.APIUtils;
import com.example.appchat.Retrofit2.DataClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaoNhomActivity extends AppCompatActivity {

    EditText txtSDT;
    String SDT;
    SharedPreferences preferences;
    Adapter_Nhom adapter_nhom;
    View view;
    LinearLayoutManager layoutManager;
    NguoiDung nguoi_dung_infor;
    ArrayList<NguoiDung> lstUser = new ArrayList<>();
    BanBe ban_be_info;
    Integer status = -1;
    RecyclerView recycleDSNhom_Tao;

    ImageButton btnBackNhom, btnDongYTaoNhom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_nhom);
        ban_be_info = new BanBe();
        lstUser.clear();
        init_Data();

        TextChange();
        GetDanhSachBan();
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
        preferences = getSharedPreferences("data_dang_nhap", MODE_PRIVATE);

        btnBackNhom= findViewById(R.id.btnXac_Nhan_Tao_Nhom);
        btnBackNhom= findViewById(R.id.btnBack_FragmentNhom);

        SDT = preferences.getString("SoDienThoai", "");

        recycleDSNhom_Tao = (RecyclerView) findViewById(R.id.recycleDSNhom_Tao);
        layoutManager = new LinearLayoutManager(this);
        recycleDSNhom_Tao.setLayoutManager(layoutManager);

        txtSDT = findViewById(R.id.txtTimSoDienThoai);
    }

    protected void ShowDanhSach(){
        adapter_nhom = new Adapter_Nhom(this, lstUser, false);

        recycleDSNhom_Tao.setAdapter(adapter_nhom);
        adapter_nhom.notifyDataSetChanged();
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

            }
        });
    }

    protected void TextChange(){
        txtSDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<NguoiDung> lstUserTemp = new ArrayList<>();
                for (NguoiDung user : lstUser){
                    if (user.getSoDienThoai().contains(s)){
                        lstUserTemp.add(user);
                    }
                }
                adapter_nhom = new Adapter_Nhom(view.getContext(), lstUserTemp, false);
                recycleDSNhom_Tao.setAdapter(adapter_nhom);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}