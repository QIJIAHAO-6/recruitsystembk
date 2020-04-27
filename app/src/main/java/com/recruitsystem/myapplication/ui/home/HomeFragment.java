package com.recruitsystem.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.recruitsystem.myapplication.AddActivity;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.Utils.ToastUtils;
import com.recruitsystem.myapplication.ZPBean;
import com.recruitsystem.myapplication.adapter.ImageAdapter;
import com.recruitsystem.myapplication.data.Bean.JobPostBean;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.ui.info.ShowJobPostDetailActivity;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    public static List<JobPostBean> lists = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    ListView listView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getView().findViewById(R.id.lv);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_banner, null, false);

        //分类的点击事件
        TextView[] textViews = {inflate.findViewById(R.id.internet),
                inflate.findViewById(R.id.sale),
                inflate.findViewById(R.id.design),
                inflate.findViewById(R.id.manage),
                inflate.findViewById(R.id.educate),
                inflate.findViewById(R.id.finance),
                inflate.findViewById(R.id.common),
        };
        String classfyName[] = {"internet","sale","design","manage","educate","finance","commom"};
        for (int i = 0; i < textViews.length; i++) {
            int finalI = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < textViews.length; j++) {
                        textViews[j].setTextColor(0xff000000);
                    }
                    textViews[finalI].setTextColor(0xffFFA74B);
//                    ToastUtils.show(getActivity(),"点击了" + textViews[finalI].getText().toString());
                    getJobPostLists(classfyName[finalI]);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initData();
                }
            });
        }
        Banner banner = inflate.findViewById(R.id.banner);
        List<String> list = new ArrayList<>();
        list.add("https://cn.bing.com/th?id=OHR.FlowingClouds_ZH-CN0721854476_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp");
        list.add("https://cn.bing.com/th?id=OHR.WallaceFF_ZH-CN0633742587_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp");
        list.add("http://h1.ioliu.cn/bing/SpectralTarsiers_ZH-CN1108590907_1920x1080.jpg?imageslim");
        list.add("http://h1.ioliu.cn/bing/PBWhaleBones_ZH-CN5771331489_1920x1080.jpg?imageslim");

        banner.setAdapter(new ImageAdapter(list));
        banner.setOrientation(Banner.HORIZONTAL)
                .setIndicator(new CircleIndicator(getContext()))
                .setUserInputEnabled(true)
                .start();
        listView.addHeaderView(inflate);
        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                0xff000000
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
                initData();
            }
        });

        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddActivity.class), 111);
            }
        });
        getJobPostLists("internet");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initData();
    }

    private void initData() {
        listView.setAdapter(new BaseAdapter() {
            class ViewHolder {
                public View rootView;
                public TextView t1;
                public TextView t2;
                public TextView t3;
                public TextView t4;
                public TextView q1;
                public TextView q2;

                public ViewHolder(View rootView) {
                    this.rootView = rootView;
                    this.t1 = (TextView) rootView.findViewById(R.id.t1);
                    this.t2 = (TextView) rootView.findViewById(R.id.t2);
                    this.t3 = (TextView) rootView.findViewById(R.id.t3);
                    this.t4 = (TextView) rootView.findViewById(R.id.t4);
                    this.q1 = (TextView) rootView.findViewById(R.id.q1);
                    this.q2 = (TextView) rootView.findViewById(R.id.q2);
                }

            }

            @Override
            public int getCount() {
                return lists.size();
            }

            @Override
            public Object getItem(int position) {
                return lists.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_home, parent, false);
                final ViewHolder viewHolder = new ViewHolder(convertView);
                final JobPostBean jobPostBean = lists.get(position);
                viewHolder.t1.setText("" + jobPostBean.getPostName() );
                viewHolder.t2.setText("" + jobPostBean.getCompany());
                viewHolder.t3.setText("" + jobPostBean.getSalaryRange());
                //   viewHolder.t4.setText("" + ZPBean.q1);
                viewHolder.t4.setVisibility(View.GONE);

                viewHolder.q1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(jobPostBean));
                        shareIntent = Intent.createChooser(shareIntent, "分享");
                        startActivity(shareIntent);
                    }
                });

                viewHolder.q2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //StaticData.list.add(ZPBean);
                        addCollection(StaticUserInfo.userPhone,jobPostBean.getPostId());
                        ToastUtils.show(getContext(), "已收藏"+StaticUserInfo.userPhone+"  "+jobPostBean.getPostId());
                    }
                });
                //法二
//                convertView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ZPInfoActivity.zpBean = list.get(position);
//                        startActivity(new Intent(getActivity(), ZPInfoActivity.class));
//                    }
//                });
                return convertView;
            }
        });
        Log.d("Test","测试点击事件");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position",i);
                ShowJobPostDetailActivity.jobPostBean = lists.get(i-1);
                Intent intent = new Intent(getActivity(), ShowJobPostDetailActivity.class);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转详情    可自行传递参数
                startActivity(new Intent(getActivity(), ZPInfoActivity.class));
            }
        });*/
    }

    //发起网络请求请求jobpost数据填充
    private void getJobPostLists(String requestType){
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/DownJobPostList?action="+requestType,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = new String(response.body().string().getBytes("UTF-8"));
                Log.d("消息",msg.toString());
                lists  = JSON.parseObject(msg,new TypeReference<ArrayList<JobPostBean>>(){});
                Log.d("消息0",lists.size()+"");
                Log.d("消息",lists.get(0).toString());
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","请求失败");
            }
        });
    }
    //发起网络请求，存取用户收藏的工作编号
    private void addCollection(String userPhone,Integer jobId){
        Log.d("addcollection","test");
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/Collections?action=addCollection"+"&phoneNumber="+userPhone+"&jobId="+jobId,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","请求失败");
            }
        });
    }
        public static List<ZPBean> list = new ArrayList<>();
}