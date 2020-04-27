package com.recruitsystem.myapplication.ui.boss;

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
import com.recruitsystem.myapplication.AddActivity;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.data.Bean.JobPostBean;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.data.StaticJobListInfoData;
import com.recruitsystem.myapplication.ui.info.ShowJobPostDetailActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BossFragment extends Fragment {
    public static List<JobPostBean> list = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        return root;
    }


    ListView listView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getView().findViewById(R.id.lv);

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
                getJobPostLists("boss");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        getJobPostLists("boss");
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
                public ViewHolder(View rootView) {
                    this.rootView = rootView;
                    this.t1 = (TextView) rootView.findViewById(R.id.t1);
                    this.t2 = (TextView) rootView.findViewById(R.id.t2);
                    this.t3 = (TextView) rootView.findViewById(R.id.t3);
                    this.t4 = (TextView) rootView.findViewById(R.id.t4);
                }

            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_boss_cardview, parent, false);
                final ViewHolder viewHolder = new ViewHolder(convertView);
                final JobPostBean jobPostBean = list.get(position);
                viewHolder.t1.setText("" + jobPostBean.getPostName() );
                viewHolder.t2.setText("" + jobPostBean.getSalaryRange());
                viewHolder.t3.setText("" + jobPostBean.getPostLimitation());
                viewHolder.t4.setText("" + jobPostBean.getPostDescription());
//                viewHolder.t4.setVisibility(View.GONE);
                return convertView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position",i);
                ShowJobPostDetailActivity.jobPostBean = list.get(i-1);
                Intent intent = new Intent(getActivity(), ShowJobPostDetailActivity.class);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    //发起网络请求请求jobpost数据填充
    private void getJobPostLists(String requestType){
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/DownJobPostList?action="+requestType,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = new String(response.body().string().getBytes("UTF-8"));
                Log.d("消息boss",msg.toString());
                StaticJobListInfoData.bossList = JSON.parseObject(msg,new TypeReference<ArrayList<JobPostBean>>(){});
                if (StaticJobListInfoData.bossList != null)
                    list = StaticJobListInfoData.bossList;
                Log.d("消息boss0",StaticJobListInfoData.bossList .size()+"");
                Log.d("消息boss1",StaticJobListInfoData.bossList .get(0).toString());
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","请求失败");
            }
        });
    }
}