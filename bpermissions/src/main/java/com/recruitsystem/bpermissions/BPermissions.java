package com.recruitsystem.bpermissions;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @author 威威君
 * @date 2020/1/14 20:04
 * QQ: 1214585092
 * WeChat:wxid508133793
 * E-mail: 1214585092@qq.com
 * GitHub: https://github.com/huicunjun
 */
public class BPermissions {
    private BPermissionsFragment bfragment;
    private String[] permissions;
    private final String TAG = BPermissions.class.getSimpleName();
    private int REQUESTCODE = 520;

    public BPermissions(FragmentActivity activity) {
        bfragment = buildFragment(activity.getSupportFragmentManager());
    }

    public BPermissions(Fragment fragment) {
        bfragment = buildFragment(fragment.getChildFragmentManager());
    }

    public static boolean checkPermission(Context context,String permission) {
        int i1 = ActivityCompat.checkSelfPermission(context, permission);
        return i1 == PackageManager.PERMISSION_GRANTED;
    }

    private BPermissionsFragment buildFragment(FragmentManager fragmentManager) {
        BPermissionsFragment fragment = (BPermissionsFragment) fragmentManager.findFragmentByTag(TAG);
        boolean t = fragment == null;
        if (t) {
            fragment = new BPermissionsFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commitNow();
        }
        return fragment;
    }

    public void request(CallBack callBack) {
        bfragment.setCallBack(callBack);
    }

    public BPermissions requestEach(String... permissions) {
        String[] strings = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            strings[i] = permissions[i];
        }
        bfragment.requestPermissions(strings, REQUESTCODE);
        return this;
    }

    public void request(String permission, CallBack callBack) {
        bfragment.setCallBack(callBack);
        bfragment.requestPermissions(new String[]{permission}, REQUESTCODE);
    }
}
