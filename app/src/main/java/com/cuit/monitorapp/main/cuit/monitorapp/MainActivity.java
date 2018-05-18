package com.cuit.monitorapp.main.cuit.monitorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cuit.monitorapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();

        if (isLogin()) {
            intent.setClass(this, SetGestureActivity.class);
            intent.putExtra("activityNum", 0);
            startActivity(intent);
        } else {
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    private boolean isLogin() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean login = sharedPreferences.getBoolean("login", false);

        return login;
    }
}
