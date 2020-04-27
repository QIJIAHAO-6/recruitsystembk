package com.recruitsystem.myapplication.ui.collections;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.data.Bean.JobCollectedBean;
import com.recruitsystem.myapplication.data.Bean.JobPostBean;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.data.StaticJobListInfoData;
import com.recruitsystem.myapplication.ui.info.ShowJobPostDetailActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CollectionsActivity extends AppCompatActivity {

    private RecyclerView rec;
    private SwipeRefreshLayout sr;
    public List<JobPostBean> allJobPostList;
    public static List<JobCollectedBean> JobPostIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        Toolbar toolbar = findViewById(R.id.tt);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sr.setRefreshing(false);
                    }
                }, 1000);
                getCollectionList(StaticUserInfo.userPhone);
            }
        });
        getCollectionList(StaticUserInfo.userPhone);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        List<ZPBean> list = new ArrayList<>();
        initData();
//        list = StaticData.list;
        ada = new Ada(R.layout.collectstyle, StaticJobListInfoData.collectedList);
        rec.setLayoutManager(new LinearLayoutManager(context));
        rec.setAdapter(ada);
    }

    Ada ada;

    class Ada extends BaseQuickAdapter<JobPostBean, BaseViewHolder> {

        public Ada(int layoutResId, List<JobPostBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, JobPostBean jobPostBean) {
            try {
               baseViewHolder.setText(R.id.collectJobName,jobPostBean.getPostName() );
               baseViewHolder.setText(R.id.collectJobCompany, jobPostBean.getCompany());
               baseViewHolder.setText(R.id.collectPostSalary, jobPostBean.getSalaryRange());
//               baseViewHolder.setText(R.id.collectPostLimit, jobPostBean.getPostLimitation());
            } catch (Exception e) {

            }
            baseViewHolder.getView(R.id.root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    int position = baseViewHolder.getAdapterPosition();
//                    bundle.putInt("position",position);
                    ShowJobPostDetailActivity.jobPostBean = jobPostBean;
                    Intent intent = new Intent(context, ShowJobPostDetailActivity.class);
//                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    Context context = this;
    public void initData(){
        allJobPostList = StaticJobListInfoData.list;
        if(StaticJobListInfoData.newCollectedCount > StaticJobListInfoData.oldCollectedCount) {
            for(int i = 0; i < JobPostIdList.size(); i++){
                JobPostBean jobPostBean= allJobPostList.get(JobPostIdList.get(i).getCollectedId());
                if (judge(jobPostBean)) {
                    StaticJobListInfoData.collectedList.add(jobPostBean);
                }
            }
            StaticJobListInfoData.oldCollectedCount = StaticJobListInfoData.newCollectedCount;
        }
    }
    public boolean judge(JobPostBean jobPostBean){
        for(int i = 0 ;i < StaticJobListInfoData.collectedList.size();i++){
            if(StaticJobListInfoData.collectedList.get(i).getPostId() == jobPostBean.getPostId()){
                return false;
            }
        }
          return true;
    }
    private void initView() {
        rec = (RecyclerView) findViewById(R.id.rec);
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
    }
    //发起网络请求，获取用户收藏的工作编号
    private void getCollectionList(String userPhone){
        Log.d("addcollection","test");
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/Collections?action=getCollection"+"&phoneNumber="+userPhone,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = new String(response.body().string().getBytes("UTF-8"));
                Log.d("消息",msg.toString());
                showResponse(msg);
                Log.d("消息Collection",JobPostIdList.toString());
                Log.d("collectionCount",JobPostIdList.size()+"");
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","请求失败");
            }
        });
    }
    //在子线程里更新数据，否则更新不了
    private void showResponse(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JobPostIdList  = JSON.parseObject(msg,new TypeReference<ArrayList<JobCollectedBean>>(){});
                StaticJobListInfoData.newCollectedCount =JobPostIdList.size();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initData();
                ada = new Ada(R.layout.collectstyle, StaticJobListInfoData.collectedList);
                rec.setLayoutManager(new LinearLayoutManager(context));
                rec.setAdapter(ada);
            }
        });
    }
}
