package com.recruitsystem.myapplication.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.Utils.ToastUtils;
import com.recruitsystem.myapplication.data.Bean.JobPostBean;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.JobPostStaticList;
import com.recruitsystem.myapplication.data.Resource;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class ShowJobPostDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mPostName;
    private TextView mPostSalary;
    private TextView mPostCompany;
    private TextView mPostDescription;
    private LinearLayout mBack;
    private LinearLayout mShare;
    private LinearLayout mCollection;
    public static JobPostBean jobPostBean;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_job_post_details);
        initView();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.collection:
                setJobPostCollection(jobPostBean);
                ToastUtils.show(this, "已收藏  ");

                break;
            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(jobPostBean));
                shareIntent = Intent.createChooser(shareIntent, "分享");
                startActivity(shareIntent);
        }
    }
    public void initView(){
        mPostName = findViewById(R.id.postName);
        mPostSalary = findViewById(R.id.postSalary);
        mPostCompany = findViewById(R.id.postCompany);
        mPostDescription = findViewById(R.id.postDescription);
        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(this);

        mShare = findViewById(R.id.share);
        mShare.setOnClickListener(this);

        mCollection = findViewById(R.id.collection);
        mCollection.setOnClickListener(this);

//        Bundle bundle = getIntent().getExtras();
//        int index = bundle.getInt("position")-1;
//        jobPostBean = JobPostStaticList.getList().get(index);
        mPostName.setText(jobPostBean.getPostName());
        mPostSalary.setText(jobPostBean.getSalaryRange());
        mPostCompany.setText(jobPostBean.getCompany());
        mPostDescription.setText(jobPostBean.getPostDescription());
    }
    //发起网络请求添加用户收藏工作信息的id
    private void setJobPostCollection(JobPostBean jobPostBean){
        int postId = jobPostBean.getPostId();
        String userPhone = StaticUserInfo.userPhone;
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/DownJobPostList",new okhttp3.Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = new String(response.body().string().getBytes("UTF-8"));
                Log.d("消息",msg.toString());
               // list1 = JSON.parseObject(msg,new TypeReference<ArrayList<JobPostBean>>(){});
             //   JobPostStaticList.setList(list1);
                Log.d("消息0",JobPostStaticList.list.size()+"");
               // Log.d("消息",list1.get(0).toString());
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","请求失败");
            }
        });
    }
}
