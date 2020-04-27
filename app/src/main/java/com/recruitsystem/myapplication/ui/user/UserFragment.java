package com.recruitsystem.myapplication.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.Utils.ToastUtile;
import com.recruitsystem.myapplication.activities.AboutActivity;
import com.recruitsystem.myapplication.activities.AccountAndSecurityActivity;
import com.recruitsystem.myapplication.activities.HelpAndFeedbackActivity;
import com.recruitsystem.myapplication.activities.LoginActivity;
import com.recruitsystem.myapplication.activities.PostResumeActivity;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.data.StaticJobListInfoData;
import com.recruitsystem.myapplication.data.model.UserLoginStatus;
import com.recruitsystem.myapplication.ui.collections.CollectionsActivity;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment implements View.OnClickListener {

    private TextView mExitLogin;
    private CircleImageView mCircleImageView;
    private LinearLayout mUserCollection;
    private TextView mUserCollectionCount;
    private TextView mAccountAndSecurity;
    private TextView mPostResume;
    private TextView mHeplAndFeedback;
    private TextView mCheckUpdate;
    private TextView mAbout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            }
        });
        getCollectionCount(StaticUserInfo.userPhone);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initView(getView());
        loadImgtx();

    }

    public void initView(View view) {
        mExitLogin = view.findViewById(R.id.exitLogin);
        mExitLogin.setOnClickListener(this);
        mCircleImageView = view.findViewById(R.id.imgtx);
        mCircleImageView.setOnClickListener(this);
        mUserCollection = view.findViewById(R.id.userCollection);
        mUserCollection.setOnClickListener(this);
        mUserCollectionCount = view.findViewById(R.id.userCollectionCount);
        if (StaticJobListInfoData.collectionCount != null)
        mUserCollectionCount.setText(StaticJobListInfoData.collectionCount);
        mAccountAndSecurity = view.findViewById(R.id.accountAndSecurity);
        mAccountAndSecurity.setOnClickListener(this);

        mHeplAndFeedback = view.findViewById(R.id.helpAndFeedback);
        mHeplAndFeedback.setOnClickListener(this);


        mCheckUpdate = view.findViewById(R.id.checkUpdate);
        mCheckUpdate.setOnClickListener(this);

        mAbout = view.findViewById(R.id.about);
        mAbout.setOnClickListener(this);

        mPostResume = view.findViewById(R.id.postResume);
        mPostResume.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgtx:
                startActivityForResult(new Intent(getActivity(), UserInfoActivity.class), 666);
                break;
            case R.id.userCollection:
                startActivity(new Intent(getActivity(), CollectionsActivity.class));
                break;
            case R.id.exitLogin:
                proceessExitLogin();
                break;
            case R.id.accountAndSecurity:
                Intent intent = new Intent(getActivity(), AccountAndSecurityActivity.class);
                startActivity(intent);
                break;
            case R.id.helpAndFeedback:
                startActivity(new Intent(getActivity(), HelpAndFeedbackActivity.class));
                break;
            case R.id.postResume:
                startActivity(new Intent(getActivity(), PostResumeActivity.class));
                break;
            case R.id.checkUpdate:
                processCheckUpdate();
                break;
            case R.id.about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;


        }
    }
    public void processCheckUpdate(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ToastUtile.showMsg(getContext(),"当前已是最新版本");
    }

    public void proceessExitLogin(){
        SharedPreferences setting = getActivity().getSharedPreferences("setting", MODE_PRIVATE);
        setting.edit().putBoolean("登录", false).apply();
        setting.edit().putString("userPhone", "").apply();
        UserLoginStatus.setLoginStatus(false);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
    private void loadImgtx() {
        try {
            //先初始化，否则会有问题
            MMKV.initialize(getActivity());
            MMKV kv = MMKV.defaultMMKV();
            LocalMedia localMedia = kv.decodeParcelable("string", LocalMedia.class);
            if (localMedia != null) {
                CircleImageView imageView = getView().findViewById(R.id.imgtx);
                Glide.with(this).load(localMedia.getPath()).into(imageView);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadImgtx();
    }

    //发起网络请求，获取用户收藏的工作数量
    private void getCollectionCount(String phoneNumber) {
        OkHttpUtile.sendOkHttpRequest("http://" + Resource.ip + ":8080/recruitsystem/Collections?action=getCollectionCount"+"&phoneNumber="+phoneNumber, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = new String(response.body().string().getBytes("UTF-8"));
                StaticJobListInfoData.collectionCount = msg;
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息", "请求失败");
            }
        });
    }
}