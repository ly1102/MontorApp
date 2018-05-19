package com.cuit.monitorapp.main.cuit.monitorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.cuit.monitorapp.R;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REFRESH_DATA = 0x100;
    private static final int DELETE_DATA = 0x101;
    private FollowListAdapter adapter;
    private Handler mHandler = new MyHandler(this);
    private LinkedList<AVObject> dataSource = new LinkedList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            super();
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_DATA:
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case DELETE_DATA:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVOSCloud.initialize(this, "fQeWhHtqk7fB9aVWwm6BAJOF-gzGzoHsz", "uvGmN110krfVFgxP9Cr8B6fv");
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//        intent.getStringExtra("");
        Log.i("   fff", "onCreate: create main");
        Intent intent = new Intent();

        if (isLogin()) {
            intent.setClass(this, SetGestureActivity.class);
            intent.putExtra("activityNum", 0);
            startActivityForResult(intent, 1);
        } else {
            intent.setClass(this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }

    }


    private boolean isLogin() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean("login", false);
    }

}
