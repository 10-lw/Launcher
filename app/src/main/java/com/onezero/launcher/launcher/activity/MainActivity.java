package com.onezero.launcher.launcher.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.onezero.launcher.launcher.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //here to avoid launcher activity resume twice when press home button
        Intent intent = (Intent)getIntent().clone();
        intent.setClass(MainActivity.this, Launcher.class);
        startActivity(intent);
        finish();
    }
}
