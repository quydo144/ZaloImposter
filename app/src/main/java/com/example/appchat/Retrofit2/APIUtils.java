package com.example.appchat.Retrofit2;

import android.provider.ContactsContract;

public class APIUtils {
    public static final String baseURL = "http://13.229.207.6:3000/";

    public static DataClient getData(){
        return RetrofitClient.getClient(baseURL).create(DataClient.class);
    }
}
