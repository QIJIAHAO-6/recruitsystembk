package com.recruitsystem.myapplication.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpUtile {

    private static OkHttpClient sClient;
    private static Request sRequest;

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        sClient = new OkHttpClient();
        sRequest = new Request.Builder().url(address).build();
        sClient.newCall(sRequest).enqueue(callback);
    }
}
