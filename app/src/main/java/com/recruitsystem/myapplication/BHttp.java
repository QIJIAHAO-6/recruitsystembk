package com.recruitsystem.myapplication;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class BHttp {
    static OkHttpClient okHttpClient = new OkHttpClient();

    public static String get(String s) {
        try {
            return okHttpClient.newCall(new Request.Builder().url(s).get().build()).execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void getBG(final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
             try {
                 String s = BHttp.get("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&pid=hp&video=0");
                 final BY by = new Gson().fromJson(s, BY.class);
                 int i = new Random().nextInt(6);
                 final String url = by.getImages().get(i).getUrl();
                 new Handler(Looper.getMainLooper()).post(new Runnable() {
                     @Override
                     public void run() {
                         callBack.get("https://cn.bing.com" + url);
                     }
                 });
             }catch (Exception e){

             }
            }
        }).start();
    }

    public interface CallBack {
        void get(String s);
    }
}
