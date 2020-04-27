package com.recruitsystem.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.recruitsystem.myapplication.Main2Activity;
import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.Utils.ToastUtile;
import com.recruitsystem.myapplication.data.Bean.LoginStatusResultFromServer;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.Resource;
import com.recruitsystem.myapplication.data.model.UserLoginStatus;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText mLoginPhoneNumber;
    private TextInputEditText mLoginPassword;
    private Button mLogin,mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }
    private void initView() {
        mLoginPhoneNumber = (TextInputEditText) findViewById(R.id.loginPhoneName);
        mLoginPhoneNumber.setOnClickListener(this);
        mLoginPassword = (TextInputEditText) findViewById(R.id.loginPassword);
        mLoginPassword.setOnClickListener(this);
        mLogin = findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        mRegister = findViewById(R.id.register);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login:
                login();
                break;
            case R.id.register:
                Log.d("R","点击了注册");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void login() {
        if (mLoginPhoneNumber.getText().toString().equals("")) {
          ToastUtile.showMsg(this,"请输入账号");
            return;
        }
        if (mLoginPhoneNumber.getText().toString().equals("")) {
            ToastUtile.showMsg(this, "请输入密码");
            return;
        }
        String phoneNumber = mLoginPhoneNumber.getText().toString().trim();
        String password = mLoginPassword.getText().toString().trim();

        OkHttpUtile.sendOkHttpRequest("http://" + Resource.ip + ":8080/recruitsystem/LoginServlet?action=login" + "&phoneNumber=" + phoneNumber + "&password=" + password, new okhttp3.Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = response.body().string();
                //LoginStatusResultFromServer.setLoginReturnResult(msg);
                LoginStatusResultFromServer.setLoginReturnResult(Integer.parseInt(msg));
                Log.d("Test1",msg);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息", "登录请求失败");
            }
        });
        //先让网络请求先完成
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Test1",LoginStatusResultFromServer.getLoginReturnResult()+"这是");
        if(LoginStatusResultFromServer.getLoginReturnResult() == 101){
            SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
            setting.edit().putBoolean("登录", true).apply();
            setting.edit().putString("userPhone",phoneNumber).apply();
            UserLoginStatus.setLoginStatus(true);
            StaticUserInfo.userPhone = phoneNumber;
            Intent intent1 = new Intent(LoginActivity.this, Main2Activity.class);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(intent1);
        }else{
            Intent intent2 = new Intent(LoginActivity.this, LoginActivity.class);
            Toast.makeText(LoginActivity.this, "手机号或密码输入不正确", Toast.LENGTH_SHORT).show();
            startActivity(intent2);
        }

    }

}
