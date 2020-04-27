package com.recruitsystem.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.recruitsystem.myapplication.R;
import com.recruitsystem.myapplication.Utils.OkHttpUtile;
import com.recruitsystem.myapplication.data.Bean.StaticUserInfo;
import com.recruitsystem.myapplication.data.Resource;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class ChangeNameActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLinearImageViewBack;
    private LinearLayout mLinearImageViewSave;
    private EditText mEditName;
    private TextView mTextViewNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        initData();
    }
    public  void initData(){
        mLinearImageViewBack = findViewById(R.id.linearImageViewBack);
        mLinearImageViewBack.setOnClickListener(this);
        mLinearImageViewSave = findViewById(R.id.linearImageViewSave);
        mLinearImageViewSave.setOnClickListener(this);
        mEditName = findViewById(R.id.editName);
        if (StaticUserInfo.userBean != null)
        mEditName.setText(StaticUserInfo.userBean.getUserName());
        mTextViewNum = findViewById(R.id.textViewNum);
        if (StaticUserInfo.userBean != null)
        mTextViewNum.setText(StaticUserInfo.userBean.getUserName().length()+"/10");
        editTextProcess();
    }
    //输入中文出现带下划线拼音处理
    public void editTextProcess(){
        //输入中文出现带下划线拼音处理
        InputFilter iF = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                SpannableString ss = new SpannableString(source);
                Object[] spans = ss.getSpans(0, ss.length(), Object.class);
                if(spans != null) {
                    for(Object span : spans) {
                        if(span instanceof UnderlineSpan) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        mEditName.setFilters(new InputFilter[]{iF});
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTextViewNum.setText( (editable.length())+"/10");
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearImageViewBack:
                finish();
                break;
            case R.id.linearImageViewSave:
                addName();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                processReturnData();
                break;
        }
    }
   public void processReturnData(){
       String date =mEditName.getText().toString().trim();
       Intent intent_save_name = new Intent();
       intent_save_name.putExtra("date_save_name",date);
       setResult(RESULT_OK,intent_save_name);
       Toast.makeText(this,"更改成功",Toast.LENGTH_SHORT).show();
       finish();
   }

    public void addName(){
        String name = mEditName.getText().toString().trim();
        addUserName(name,StaticUserInfo.userPhone);
    }
    //发起网络请求，获取用户Bean
    private void addUserName(String userName,String userPhone) {
        OkHttpUtile.sendOkHttpRequest("http://" + Resource.ip + ":8080/recruitsystem/LoginServlet?action=addUserName"+"&phoneNumber="+userPhone+"&userName="+userName, new okhttp3.Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String msg = response.body().string();
                //LoginStatusResultFromServer.setLoginReturnResult(msg);
                Log.d("TestAddUserName",msg);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("消息", "登录请求失败");
            }
        });
    }
}
