package com.recruitsystem.myapplication.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.recruitsystem.myapplication.BHttp;
import com.recruitsystem.myapplication.BY;
import com.recruitsystem.myapplication.R;

import java.util.Random;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        sendViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        final ImageView imageView = root.findViewById(R.id.img);

        new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                  String s = BHttp.get("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&pid=hp&video=0");
                  final BY by = new Gson().fromJson(s, BY.class);
                  int i = new Random().nextInt(6);
                  final String url = by.getImages().get(i).getUrl();
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          int i = new Random().nextInt(9);
                          Glide.with(getActivity()).load("https://cn.bing.com" + url).into(imageView);
                          //   Glide.with(getActivity()).load("https://cn.bing.com//th?id=OHR.AcadiaSunrise_ZH-CN5619713848_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp").into(imageView);
                      }
                  });
              }catch (Exception e){

              }
            }
        });
        return root;
    }
}