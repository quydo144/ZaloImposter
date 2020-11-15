package com.example.appchat.Retrofit2;

import com.example.appchat.Models.BanBe;
import com.example.appchat.Models.Message;
import com.example.appchat.Models.NguoiDung;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataClient {

    @POST("/")
    Call<Message> ThemNguoiDung(@Body NguoiDung nguoiDung);

    @POST("/dangnhap")
    Call<Message> DangNhap(@Body NguoiDung nguoiDung);

    @GET("/sdt={sdt}")
    Call<Message> GetThongTinNguoiDung_bySDT(@Path("sdt") String sdt, @HeaderMap Map<String, String> token);

    @PATCH("/")
    Call<Message> SuaThongTin(@Body NguoiDung nguoiDung, @HeaderMap Map<String, String> token);

    @POST("/checksdt")
    Call<Message> CheckSoDienThoai(@Body NguoiDung nguoiDung);

    @PATCH("/updatepass")
    Call<Message> UpdatePassword(@Body NguoiDung nguoiDung);

    @POST("/getTrangThaiFriend")
    Call<Message> CheckTrangThaiBanBe(@Body BanBe banBe);

    @POST("/sendRequestAddFriend")
    Call<Message> SendRequestAddFriend(@Body BanBe banBe);

    @POST("/deleterequest")
    Call<Message> DeleteRequestFriend(@Body BanBe banBe);

    @POST("/acceptrequest")
    Call<Message> AcceptRequestFriend(@Body BanBe banBe);

    @POST("/getListFriend")
    Call<Message> GetListFriend(@Body BanBe banBe);

    @POST("/getlistrequestfriend")
    Call<Message> GetListRequestFriend(@Body BanBe banBe);
}
