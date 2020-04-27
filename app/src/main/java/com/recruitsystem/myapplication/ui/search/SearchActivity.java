package com.recruitsystem.myapplication.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.data.Bean.JobPostBean;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.data.StaticJobListInfoData;
import com.recruitsystem.myapplication.ui.info.ShowJobPostDetailActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll1;
    private RecyclerView rec;
    private LinearLayout ll2;
    private Spinner sp1;
    private EditText ed;
    public static List<JobPostBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setStatusBarColor(0xffffffff);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为
        setContentView(R.layout.activity_search);
        initView();
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.GONE);
        ed.setSingleLine(true);
        ed.setMaxLines(1);
        ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);
                    getJobPostByKeySearch(ed.getText().toString().trim());
                    /**隐藏软键盘**/
                    View view = getWindow().peekDecorView();
                    hideKeyBoard(view);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.qx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //隐藏键盘
    public static void hideKeyBoard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    Ada ada;

    class Ada extends BaseQuickAdapter<JobPostBean, BaseViewHolder> {

        public Ada(int layoutResId, @Nullable List<JobPostBean> data) {
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
                    Intent intent = new Intent(getApplicationContext(), ShowJobPostDetailActivity.class);
//                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    Activity act = this;

    private void initView() {
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        rec = (RecyclerView) findViewById(R.id.rec);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        sp1 = (Spinner) findViewById(R.id.sp1);
        ed = (EditText) findViewById(R.id.ed);
    }

    @Override
    public void onClick(View v) {

    }
    //在子线程里更新数据，否则更新不了
    private void showResponse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ada = new Ada(R.layout.collectstyle, list);
                rec.setLayoutManager(new LinearLayoutManager(act));
                rec.setAdapter(ada);
            }
        });
    }
    //发起网络请求，根据搜索内容查询所需要的数据
    private void getJobPostByKeySearch(String key ){
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/DownJobPostList?action=search"+"&key="+key,new okhttp3.Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = new String(response.body().string().getBytes("UTF-8"));
                Log.d("123",msg.toString());
               StaticJobListInfoData.bykeysearchList = JSON.parseObject(msg,new TypeReference<ArrayList<JobPostBean>>(){});
                list = StaticJobListInfoData.bykeysearchList;
                showResponse();
                Log.d("searchtest",StaticJobListInfoData.bykeysearchList.size()+"");
                Log.d("search1",StaticJobListInfoData.bykeysearchList.get(0).toString());
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","请求失败");
            }
        });
    }
    /**隐藏软键盘**/
//    View view = getWindow().peekDecorView();
//                    if (view != null) {
//                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    }

}
