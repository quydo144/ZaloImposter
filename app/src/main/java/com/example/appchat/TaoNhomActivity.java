package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appchat.Models.MessageNhom;
import com.example.appchat.Models.ThanhVien;
import com.example.appchat.Retrofit2.APIUtils;
import com.example.appchat.Retrofit2.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaoNhomActivity extends AppCompatActivity {

    ImageButton btnBackNhom, btnDongYTaoNhom;
    TextView tenNhom;
    String idGroup = "";
    SharedPreferences preferences;

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
        preferences = getSharedPreferences("data_dangnhap", MODE_PRIVATE);
        btnBackNhom = (ImageButton) findViewById(R.id.btnBack_FragmentNhom);
        btnDongYTaoNhom = (ImageButton) findViewById(R.id.btnXac_Nhan_Tao_Nhom);
        tenNhom = (TextView) findViewById(R.id.txtDatTenNhom);
    }

    private void CreateGroup(){
        btnDongYTaoNhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClient client = APIUtils.getDataLocal();
                ThanhVien tv = new ThanhVien();
                tv.setTenNhom(tenNhom.getText().toString());
                int maNguoiDung = preferences.getInt("MaNguoiDung", 0);
                tv.setTruongNhom(maNguoiDung);
                Call<MessageNhom> call = client.CreateGroup(tv);
                call.enqueue(new Callback<MessageNhom>() {
                    @Override
                    public void onResponse(Call<MessageNhom> call, Response<MessageNhom> response) {
                        if (response.isSuccessful())
                            if (response.body().getSuccess() == 1){
                                idGroup = response.body().getMaNhom();
                                Toast.makeText(TaoNhomActivity.this, "Tạo nhóm thành công", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<MessageNhom> call, Throwable t) {

                    }
                });
            }
        });
    }
}