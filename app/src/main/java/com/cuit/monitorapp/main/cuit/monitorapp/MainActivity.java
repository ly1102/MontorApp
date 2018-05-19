package com.cuit.monitorapp.main.cuit.monitorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.cuit.monitorapp.R;
import com.cuit.monitorapp.main.cuit.monitorapp.AddInfoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", "mainActivity");
                intent.setClass(MainActivity.this, AddInfoActivity.class);
                startActivity(intent);
            }
        });

        // 测试 SDK 是否正常工作的代码
        // AVObject testObject = new AVObject("TestObject");
        // testObject.put("words","Hello World!");
        // testObject.saveInBackground(new SaveCallback() {
        //     @Override
        //     public void done(AVException e) {
        //         if(e == null){
        //             Log.d("saved","success!");
        //         }
        //     }
        // });
    }

}
