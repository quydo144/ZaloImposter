package com.example.appchat.Retrofit2;

import android.provider.ContactsContract;

public class APIUtils {
    public static final String baseURL = "http://13.229.207.6:3000/";

    public static final String baseURLUpload = "http://192.168.1.8:6000";   //localhost

    public static DataClient getData(){
        return RetrofitClient.getClient(baseURL).create(DataClient.class);
    }

    public static DataClient getDataUpload(){
        return RetrofitClient.getClient(baseURLUpload).create(DataClient.class);
    }
}
