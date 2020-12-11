package com.example.appchat.Retrofit2;

import android.provider.ContactsContract;

public class APIUtils {
    public static final String baseURL = "http://13.229.207.6:3000/";

    public static final String baseURLLocal = "http://192.168.2.213:6000";   //localhost

    public static DataClient getData(){
        return RetrofitClient.getClient(baseURL).create(DataClient.class);
    }

    public static DataClient getDataLocal(){
        return RetrofitClient.getClient(baseURLLocal).create(DataClient.class);
    }
}
