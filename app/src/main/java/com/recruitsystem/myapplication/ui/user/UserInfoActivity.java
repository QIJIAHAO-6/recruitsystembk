package com.recruitsystem.myapplication.ui.user;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.recruitsystem.bpermissions.BPermissions;
import com.recruitsystem.bpermissions.CallBack;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.Utils.ToastUtils;
import com.recruitsystem.myapplication.activities.ChangeNameActivity;
import com.recruitsystem.myapplication.activities.ChangeNickNameActivity;
import com.recruitsystem.myapplication.activities.ChangeUserAdvantageActivity;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.Bean.UserBean;
import com.recruitsystem.myapplication.data.Resource;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.ImageCompleteCallback;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.tencent.mmkv.MMKV;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.engine.ImageEngine;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private CircleImageView imgtx;
    private RelativeLayout userInforLayout;
    private RelativeLayout userNameRelayout;
    private RelativeLayout userNickNameRelayout;
    private RelativeLayout userSexRelayout;
    private RelativeLayout userAdvantageRelayout;
    private RelativeLayout userBornDateRelayout;
    private TextView mUserName;
    private TextView mUserNickName;
    private TextView mUserAdvantage;
    private TextView showBornDate;
    private String TAG = getClass().getSimpleName();
    final int DATE_DIALOG = 1;
     int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //先初始化，否则会有问题
        MMKV.initialize(this);
        MMKV kv = MMKV.defaultMMKV();
           LocalMedia localMedia  = kv.decodeParcelable("string", LocalMedia.class);
        if (localMedia!=null) {
            CircleImageView imageView = findViewById(R.id.imgtx);
            Glide.with(this).load(localMedia.getPath()).into(imageView);
        }
        //发起网络请求
        getUserInfo(StaticUserInfo.userPhone);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgtx = (CircleImageView) findViewById(R.id.imgtx);
        imgtx.setOnClickListener(this);
        userInforLayout = (RelativeLayout) findViewById(R.id.userInforLayout);
        userInforLayout.setOnClickListener(this);
        userNameRelayout = findViewById(R.id.userNameRelayout);
        userNameRelayout.setOnClickListener(this);
        userNickNameRelayout = findViewById(R.id.userNickNameRelayout);
        userNickNameRelayout.setOnClickListener(this);
        userSexRelayout = findViewById(R.id.userSexRelayout);
        userSexRelayout.setOnClickListener(this);
        mUserName = findViewById(R.id.userName);
        mUserNickName = findViewById(R.id.userNickName);
        userAdvantageRelayout = findViewById(R.id.userAdvantageRelayout);
        userAdvantageRelayout.setOnClickListener(this);
        mUserAdvantage = findViewById(R.id.userAdvantage);

        userBornDateRelayout = findViewById(R.id.userBornDateRelayout);
        userBornDateRelayout.setOnClickListener(this);
        showBornDate = findViewById(R.id.showBornDate);
    }

    int t = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userInforLayout:
                replaceImage();
                break;
            case R.id.imgtx:
                replaceImage();
                break;
            case R.id.userNameRelayout:
                Intent intentUserName = new Intent(UserInfoActivity.this, ChangeNameActivity.class);
                startActivityForResult(intentUserName,1);
                break;
            case R.id.userNickNameRelayout:
                Intent intentUserNickName = new Intent(UserInfoActivity.this, ChangeNickNameActivity.class);
                startActivityForResult(intentUserNickName,2);
                break;
            case R.id.userSexRelayout:
                showDialog();
                break;
            case R.id.userBornDateRelayout:
                getCurrentDate();
                break;
            case R.id.userAdvantageRelayout:
                Intent intentUserAdvantage = new Intent(UserInfoActivity.this, ChangeUserAdvantageActivity.class);
                startActivityForResult(intentUserAdvantage,3);
                break;

        }
    }
    public void getCurrentDate(){
        if( StaticUserInfo.UserBornDateGet == false){
            final Calendar ca = Calendar.getInstance();
            mYear = ca.get(Calendar.YEAR);
            mMonth = ca.get(Calendar.MONTH);
            mDay = ca.get(Calendar.DAY_OF_MONTH);
        }else {
            mYear = StaticUserInfo.mYear;
            mMonth  = StaticUserInfo.mMonth-1;
            mDay = StaticUserInfo.mDay;
            Log.d("showdate",mYear+" "+mMonth+" "+mDay);
        }

        showDialog(DATE_DIALOG);
    }
    //出生日期
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }
    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        showBornDate.setText(new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear+1;
            mDay = dayOfMonth;
            String bornDate = mYear+"-"+mMonth+"-"+mDay;
            Log.d("time",bornDate);
            updateUserBornDate(StaticUserInfo.userPhone,bornDate);
            display();
        }
    };
    public void showDialog(){
        List<String> list = new ArrayList<>();// 性别选择
        list.add("男");
        list.add("女");
        list.add("不告诉你");
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_to_modify_sex_info, null, false);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);
        ListView listView = view.findViewById(R.id.selectSex);
        listView.setAdapter(new BaseAdapter() {
            class ViewHolder {
                public View rootView;
                public TextView show_list_sex;

                public ViewHolder(View rootView) {
                    this.rootView = rootView;
                    show_list_sex = (TextView) rootView.findViewById(R.id.show_list_sex);
                }

            }
            public int getCount() {
                return list.size();
            }

            public Object getItem(int i) {
                return list.get(i);
            }

            public long getItemId(int i) {
                return i;
            }
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_sex_select, viewGroup, false);
                final ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.show_list_sex.setText(list.get(i));
                return view;
            }
        });
        bottomSheetDialog.show();
        TextView cancelModifySex = view.findViewById(R.id.cancel_modify_sex);
        TextView confirmModifySex = view.findViewById(R.id.confirm_modify_sex);
    }
    //发起网络请求，获取用户Bean
    private void updateUserBornDate(String userPhone,String bornDate) {
        OkHttpUtile.sendOkHttpRequest("http://" + Resource.ip + ":8080/recruitsystem/LoginServlet?action=addUserBornDate"+"&phoneNumber="+userPhone+"&bornDate="+bornDate, new okhttp3.Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = response.body().string();
                //LoginStatusResultFromServer.setLoginReturnResult(msg);
                Log.d("TestUserInfo",msg);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息", "登录请求失败");
            }
        });
    }
    //发起网络请求，获取用户Bean
    private void getUserInfo(String userPhone) {
        OkHttpUtile.sendOkHttpRequest("http://" + Resource.ip + ":8080/recruitsystem/LoginServlet?action=getUser"+"&phoneNumber="+userPhone , new okhttp3.Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = response.body().string();
                //LoginStatusResultFromServer.setLoginReturnResult(msg);
                showResponse( msg);
                Log.d("TestUserInfo",msg);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息", "登录请求失败");
            }
        });
    }
    //在子线程里更新数据，否则更新不了
    private void showResponse(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StaticUserInfo.userBean = JSON.parseObject(msg,new TypeReference<UserBean>(){});
                refreshView(StaticUserInfo.userBean );
            }
        });
    }
   public void  refreshView(UserBean userBean ){
        if(userBean.getUserName() != null){
            mUserName.setText(userBean.getUserName());
        }
        if (userBean.getUserBornDate()!=null){
            String date = userBean.getUserBornDate();
            String strs[]  = date.split("-");
            if (strs != null){
                StaticUserInfo.mYear = Integer.parseInt(strs[0]);
                StaticUserInfo.mMonth = Integer.parseInt(strs[1]);
                StaticUserInfo.mDay = Integer.parseInt(strs[2]);
            }
            showBornDate.setText(new StringBuffer().append( StaticUserInfo.mYear).append("-").append(StaticUserInfo.mMonth ).append("-").append(StaticUserInfo.mDay).append(" "));
            Log.d("showresponse",mYear+" "+mMonth+" "+mDay);
            StaticUserInfo.UserBornDateGet = true;
        }
    }
    //执行拍照，从图库里选头像操作
    public void replaceImage(){
        new BPermissions(this)
                .requestEach(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .request(new CallBack() {
                    @Override
                    public void accept(boolean b) {
                        if (!b) {
                            ToastUtils.show(UserInfoActivity.this, "请给予我读写内存卡权限");
                            return;
                        }
                        t++;
                        if (t == 3) {
                            t = 0;
                            View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null, false);
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

                            bottomSheetDialog.setContentView(view);
                            bottomSheetDialog.show();

                            Button takephoto = view.findViewById(R.id.takephoteo);
                            Button camera = view.findViewById(R.id.camera);
                            Button cancel = view.findViewById(R.id.cancel);
                            takephoto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PictureSelector.create(UserInfoActivity.this)
                                            .openCamera(PictureMimeType.ofImage())
                                            .loadImageEngine(new MyImageEngine()) // 请参考Demo GlideEngine.java
                                            .forResult(PictureConfig.REQUEST_CAMERA);
                                    bottomSheetDialog.dismiss();
                                }
                            });
                            camera.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PictureSelector.create(UserInfoActivity.this)
                                            .openGallery(PictureMimeType.ofImage())
                                            .loadImageEngine(new MyImageEngine()) // 请参考Demo GlideEngine.java
                                            .forResult(PictureConfig.CHOOSE_REQUEST);
                                    bottomSheetDialog.dismiss();
                                           /* Matisse.from(UserInfoActivity.this)
                                                    .choose(MimeType.ofImage(), false) //
                                                    // .countable(true)
                                                    .maxSelectable(1) //
                                                    //.capture(true)
                                                    //  .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                                    //.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                                    .thumbnailScale(0.85f) // 缩略图的比例
                                                    .imageEngine(new MyGlideEngine())
                                                    .forResult(666);*/
                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bottomSheetDialog.dismiss();
                                }
                            });
                        }
                    }
                });

    }
    Context context = this;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PictureConfig.REQUEST_CAMERA || requestCode == PictureConfig.CHOOSE_REQUEST) {
                // 图片选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回五种path
                // 1.media.getPath(); 原图path，但在Android Q版本上返回的是content:// Uri类型
                // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路
                //径；注意：.isAndroidQTransform(false);此字段将返回空
                // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                LocalMedia localMedia = selectList.get(0);
                MMKV kv = MMKV.defaultMMKV();
                kv.encode("string", localMedia);
                Glide.with(this).load(localMedia.getPath()).into(imgtx);
                for (LocalMedia media : selectList) {
                    Log.i(TAG, "压缩::" + media.getCompressPath());
                    Log.i(TAG, "原图::" + media.getPath());
                    Log.i(TAG, "裁剪::" + media.getCutPath());
                    Log.i(TAG, "是否开启原图::" + media.isOriginal());
                    Log.i(TAG, "原图路径::" + media.getOriginalPath());
                    Log.i(TAG, "Android Q 特有Path::" + media.getAndroidQToPath());
                }
            }
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {

            }
            if (requestCode == 666 && resultCode == RESULT_OK) {
                List<Uri> uris = Matisse.obtainResult(data);
                List<String> stringList = Matisse.obtainPathResult(data);
                String s = stringList.get(0);
                MMKV kv = MMKV.defaultMMKV();
                kv.encode("string", s);
                Glide.with(this).load(uris.get(0)).into(imgtx);
            }
            //姓名数据返回
            switch (requestCode){
                case 1:
                    if(resultCode==RESULT_OK){
                        String name_save= data.getStringExtra("date_save_name");
                        mUserName.setText(name_save);
                    }
                case 2:
                    if(resultCode==RESULT_OK){
                        String date_save_nick_name= data.getStringExtra("date_save_nick_name");
                        mUserNickName.setText(date_save_nick_name);
                    }
                case 3:
                    if(resultCode==RESULT_OK){
                        String date_save_user_advantage= data.getStringExtra("date_save_user_advantage");
                        mUserAdvantage.setText(date_save_user_advantage);
                    }
            }
        } catch (Exception e) {

        }
    }



    static class MyImageEngine implements com.luck.picture.lib.engine.ImageEngine {

        @Override
        public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, SubsamplingScaleImageView longImageView, ImageCompleteCallback callback) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, SubsamplingScaleImageView longImageView) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void loadAsGifImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        }
    }

    static class MyGlideEngine implements ImageEngine {
        @Override
        public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
            Glide.with(context)
                    .load(uri)
                    .placeholder(placeholder)
                    .override(resize, resize)
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView,
                                     Uri uri) {
            Glide.with(context)
                    .load(uri)
                    .placeholder(placeholder)
                    .override(resize, resize)
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
            Glide.with(context)
                    .load(uri)
                    .override(resizeX, resizeY)
                    .priority(Priority.HIGH)
                    .fitCenter()
                    .into(imageView);
        }

        @Override
        public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
            Glide.with(context)
                    .load(uri)
                    .override(resizeX, resizeY)
                    .priority(Priority.HIGH)
                    .into(imageView);
        }

        @Override
        public boolean supportAnimatedGif() {
            return true;
        }

    }
}
