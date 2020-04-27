package com.recruitsystem.myapplication.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.StaticData;
import com.recruitsystem.myapplication.Utils.ToastUtils;
import com.recruitsystem.myapplication.ZPBean;

public class ZPInfoActivity extends AppCompatActivity {
    public static ZPBean zpBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_job_post_details);
    }

    public void back(View view) {
        finish();
    }

    public void sc(View view) {
      if (zpBean!=null){
          StaticData.list.add(zpBean);
          ToastUtils.show(this, "已收藏  ");
      }
    }

    public void fx(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(zpBean));
        shareIntent = Intent.createChooser(shareIntent, "分享");
        startActivity(shareIntent);
    }
}
