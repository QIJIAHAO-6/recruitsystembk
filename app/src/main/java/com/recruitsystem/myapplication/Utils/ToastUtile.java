package com.recruitsystem.myapplication.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtile {
    public  static Toast mToast;//定义一个公共类，
    public static  void showMsg(Context context,String msg)
    {
        if(mToast == null)//会刷新之前的，只完整显示最后一个
        {
            mToast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
        }
        else//如果不为空，显示同一个输出
        {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
