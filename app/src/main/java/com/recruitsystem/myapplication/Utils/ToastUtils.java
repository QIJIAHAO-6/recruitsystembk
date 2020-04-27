package com.recruitsystem.myapplication.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.recruitsystem.myapplication.R;


public class ToastUtils {
    static Toast toast;
    public static void show(Context context, String s) {
         toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        View v = LayoutInflater.from(context).inflate(R.layout.toast, null, false);
        toast.setView(v);
        TextView textView = v.findViewById(R.id.t);

        textView.setText(s);
       // toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
