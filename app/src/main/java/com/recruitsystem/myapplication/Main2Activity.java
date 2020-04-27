package com.recruitsystem.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.data.Bean.JobPostBean;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.JobPostStaticList;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.data.StaticJobListInfoData;
import com.recruitsystem.myapplication.ui.search.SearchActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav1,
                R.id.nav2,
                R.id.nav3,
                R.id.nav4
        )
                .build();
        NavController navController = Navigation.findNavController(this, R.id.bottom_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        getJobPostListsAll();
        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
        StaticUserInfo.userPhone =  setting.getString("userPhone","");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.searchview) {
            startActivity(new Intent(Main2Activity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void getJobPostListsAll(){
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/DownJobPostList?action=all",new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = new String(response.body().string().getBytes("UTF-8"));
                Log.d("消息main",msg.toString());
                StaticJobListInfoData.list = JSON.parseObject(msg,new TypeReference<ArrayList<JobPostBean>>(){});
                JobPostStaticList.setList(StaticJobListInfoData.list);
                Log.d("消息maincount",StaticJobListInfoData.list.size()+"");
                Log.d("消息",StaticJobListInfoData.list.get(0).toString());
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息main","请求失败");
            }
        });
    }

}
