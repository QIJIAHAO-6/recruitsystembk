package com.recruitsystem.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.data.Bean.JobPostBean;
import com.recruitsystem.myapplication.data.Resource;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText e1;
    private TextInputEditText e2;
    private TextInputEditText e3;
    private TextInputEditText e4;
    private TextInputEditText e5;
    private TextInputEditText e6;
    private Button mButtonCancel;
    private Button mButtonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        setTitle("上传招聘职位");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                cancelProcess();
                break;
            case R.id.confirm:
                confirmProcess();
                break;
    }
    }
    public void cancelProcess(){
        setResult(111);
        finish();
    }
    public void confirmProcess(){
        JobPostBean jobPostBean =  new JobPostBean();
        jobPostBean.setPostName(e1.getText().toString().trim());
        jobPostBean.setSalaryRange(e2.getText().toString().trim());
        jobPostBean.setCompany(e3.getText().toString().trim());
        jobPostBean.setPostType(e4.getText().toString().trim());
        jobPostBean.setPostLimitation(e5.getText().toString().trim());
        jobPostBean.setPostDescription(e6.getText().toString().trim());
        jobPostBean.setPostReleaseType("boss");
        Log.d("add",jobPostBean.toString());
        addJobPost(jobPostBean);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setResult(111);
        finish();
    }

    private void initView() {
        e1 = (TextInputEditText) findViewById(R.id.e1);
        e2 = (TextInputEditText) findViewById(R.id.e2);
        e3 = (TextInputEditText) findViewById(R.id.e3);
        e4 = (TextInputEditText) findViewById(R.id.e4);
        e5 = (TextInputEditText) findViewById(R.id.e5);
        e6 = (TextInputEditText) findViewById(R.id.e6);
        mButtonCancel = findViewById(R.id.cancel);
        mButtonCancel.setOnClickListener(this);
        mButtonConfirm = findViewById(R.id.confirm);
        mButtonConfirm.setOnClickListener(this);
    }
    //发起网络请求请求jobpost数据填充
    private void addJobPost(JobPostBean jobPostBean){
        Log.d("add","123");
        String postN = jobPostBean.getPostName();
        String postS = jobPostBean.getSalaryRange();
        String postC = jobPostBean.getCompany();
        String postPT = jobPostBean.getPostType();
        String postPL= jobPostBean.getPostLimitation();
        String postD = jobPostBean.getPostDescription();
        String postRT = jobPostBean.getPostReleaseType();
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/AddJobPostServlet?action=addJobPostFromBoss"+"&postN="+postN+"&postS="+postS+"&postC="+postC+"&postPT="+postPT+"&postPL="+postPL+"&postD="+postD+"&postRT="+postRT,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","请求失败");
            }
        });
    }

}
