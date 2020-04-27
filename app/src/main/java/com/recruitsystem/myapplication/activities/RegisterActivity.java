package com.recruitsystem.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.recruitsystem.myapplication.Main2Activity;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.data.model.UserLoginStatus;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";//验证密码是否有特殊符号或长度不满6位
    private TextInputEditText mRegisterPhoneNumber;
    private TextInputEditText mRegisterPassword;
    private Button mCancelRegister,mComfirmRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView() {
        mRegisterPhoneNumber = (TextInputEditText) findViewById(R.id.registerPhoneName);
        mRegisterPhoneNumber.setOnClickListener(this);
        mRegisterPassword = (TextInputEditText) findViewById(R.id.registerPassword);
        mRegisterPassword.setOnClickListener(this);
        mCancelRegister = findViewById(R.id.cancelRegister);
        mCancelRegister.setOnClickListener(this);
        mComfirmRegister = findViewById(R.id.comfirmRegister);
        mComfirmRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.comfirmRegister:
                //获得账号密码
                String phoneNumber = mRegisterPhoneNumber.getText().toString().trim();
                String pass = mRegisterPassword.getText().toString().trim();
                if (phoneNumber == null || "".equals(phoneNumber) || pass == null || "".equals(pass)) {
                    Toast.makeText(this, "账号与密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String number = mRegisterPhoneNumber.getText().toString();
                    boolean judge = isMobile(number);
                    String pa = mRegisterPassword.getText().toString();
                    boolean judge1 = isPassword(pa);
                    Log.d("register","注冊");
                    if (judge == true && judge1 == true) {
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                        Log.d("register","注冊1");
                        saveUserInformationByHttp(phoneNumber,pass);
                        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
                        setting.edit().putBoolean("登录", true).apply();
                        UserLoginStatus.setLoginStatus(true);
                        StaticUserInfo.userPhone = phoneNumber;
                        Intent intent = new Intent(this, Main2Activity.class);
                        startActivity(intent);//启动跳转
                    } else {
                        Toast.makeText(this, "手机号码不法与密码不能有特殊符号", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.cancelRegister:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);//启动跳转
        }
    }
    public void saveUserInformationByHttp(String phoneNumber,String password){
        Log.d("register","注冊2");
        OkHttpUtile.sendOkHttpRequest("http://"+ Resource.ip +":8080/recruitsystem/LoginServlet?action=register"+"&phoneNumber="+phoneNumber+"&password="+password,new okhttp3.Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = response.body().string();

            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息","登录请求失败");
            }
        });
    }
    /**
     * 校验密码
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
    /**
     * 验证手机格式
     */

    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][3458]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
}
