package com.cuit.monitorapp.main.cuit.monitorapp;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.DeleteCallback;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.widget.Toolbar;
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

public class HomeActivity extends AppCompatActivity {
    private static final int REFRESH_DATA = 0x100;
    private static final int DELETE_DATA = 0x101;
    private FollowListAdapter adapter;
    private Handler mHandler = new MyHandler(HomeActivity.this);
    private LinkedList<AVObject> dataSource = new LinkedList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private class MyHandler extends Handler {
        private final WeakReference<HomeActivity> mActivity;

        public MyHandler(HomeActivity activity) {
            super();
            mActivity = new WeakReference<HomeActivity>(activity);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_add) {
                    startActivity(new Intent(HomeActivity.this, AddFollowActivity.class));
                }
                return true;
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
        ListView listView = findViewById(R.id.follow_list);
        adapter = new FollowListAdapter(HomeActivity.this, R.layout.main_list_item, dataSource);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AVObject avobject = adapter.getItem(i);
                assert avobject != null;
                String id = avobject.getObjectId();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TraceActivity.class);
                intent.putExtra("follower_id", id);
                startActivity(intent);
            }
        });
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 0, 0, "编辑");
                contextMenu.add(0, 1, 0, "删除");
            }

        });
        requestData();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        System.out.println("click");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(HomeActivity.this, AddFollowActivity.class);
                intent.putExtra("follow", adapter.getItem(info.position));
                startActivity(intent);
                break;
            case 1:
                delete(info.position);
                break;
            default:
                break;
        }
        return true;
    }

    private void delete(final int index) {
        AVObject del = AVObject.createWithoutData("M_Follow", adapter.getItem(index).getObjectId());
        del.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    dataSource.remove(index);
                    mHandler.sendEmptyMessage(DELETE_DATA);
                }
            }
        });
    }

    private void requestData() {
        AVQuery<AVObject> query = new AVQuery<>("M_Follow");
        query.whereEqualTo("observer", AVObject.createWithoutData("User_obj", "5afd8c8167f356003863ca79"));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                dataSource.clear();
                dataSource.addAll(list);
                mHandler.sendEmptyMessage(REFRESH_DATA);
            }
        });
    }
}

