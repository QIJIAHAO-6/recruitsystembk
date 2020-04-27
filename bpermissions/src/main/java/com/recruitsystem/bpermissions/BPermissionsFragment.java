package com.recruitsystem.bpermissions;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author 威威君
 * @date 2020/1/14 20:07
 * QQ: 1214585092
 * WeChat:wxid508133793
 * E-mail: 1214585092@qq.com
 * GitHub: https://github.com/huicunjun
 */
public class BPermissionsFragment extends Fragment {
    CallBack callBack;
    private int REQUESTCODE = 520;

    public static BPermissionsFragment newInstance() {
        Bundle args = new Bundle();
        BPermissionsFragment fragment = new BPermissionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUESTCODE) {
            if (permissions .length>0 && grantResults .length>0) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        callBack.accept(true);
                    } else {
                        callBack.accept(false);
                    }
                }
            }
        }
    }
}
