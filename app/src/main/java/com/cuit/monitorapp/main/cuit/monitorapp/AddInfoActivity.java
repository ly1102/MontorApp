package com.cuit.monitorapp.main.cuit.monitorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cuit.monitorapp.R;

public class AddInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        Toast.makeText(getApplicationContext(), "默认Toast样式",
                Toast.LENGTH_SHORT).show();
    }
}
