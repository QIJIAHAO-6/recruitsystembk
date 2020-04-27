package com.recruitsystem.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.recruitsystem.myapplication.R;

public class HelpAndFeedbackActivity extends AppCompatActivity {
    private LinearLayout mLinearImageViewBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);
        mLinearImageViewBack = findViewById(R.id.linearImageViewBack);
        mLinearImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
