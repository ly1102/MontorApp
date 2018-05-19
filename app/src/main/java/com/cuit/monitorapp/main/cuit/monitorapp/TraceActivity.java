package com.cuit.monitorapp.main.cuit.monitorapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.cuit.monitorapp.R;
import com.cuit.monitorapp.main.cuit.monitorapp.adapter.TraceListAdapter;
import com.cuit.monitorapp.main.cuit.monitorapp.model.Trace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TraceActivity extends AppCompatActivity {
    private RecyclerView rvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    private Button btnAdd;
    private Button btnShow;
    private String user_id;
    private String follower_id;
    // private long longitude;
    // private long latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        AVOSCloud.initialize(this,"fQeWhHtqk7fB9aVWwm6BAJOF-gzGzoHsz","uvGmN110krfVFgxP9Cr8B6fv");

        // 测试连接
        // AVObject testObject = new AVObject("TestObject");
        // testObject.put("words","Hello");
        // testObject.saveInBackground(new SaveCallback() {
        //     @Override
        //     public void done(AVException e) {
        //         if(e == null){
        //             Log.d("saved","success!");
        //         }
        //     }
        // });

        Intent intent = getIntent();
        try{
            user_id = ((Auth)getApplication()).getObjectId();
        }catch (Exception e){
            user_id = "123456";
        }

        // user_id = "123456";
        follower_id = intent.getStringExtra("follower_id");
        // follower_id = "123456";
        Log.i("trace back follower id", "onCreate: "+follower_id);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TraceActivity.this, AddInfoActivity.class);
                // String user_oid = "1"; //暂时为1,dao还没写
                // String followers_oid = "1";
                intent.putExtra("user_oid", user_id);
                intent.putExtra("follower_id", follower_id);
                startActivityForResult(intent, 0);
            }
        });

        btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TraceActivity.this, DingweiActivity.class);
                intent.putExtra("follower_id", follower_id);
                startActivityForResult(intent, 1);

            }
        });
        rvTrace = (RecyclerView) findViewById(R.id.rvTrace);
        initData(follower_id, user_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (requestCode == 0 && resultCode == 0) { //添加页面传回的
        initData(follower_id, user_id);
        // }

        // if (requestCode == 1 && resultCode == 0) {//可以不要?
        //     // Bundle bundle = data.getExtras();
        //     // 茶数据库
        // }
    }

    private void initData(String follower_id, String user_id) { //茶数据库
        AVQuery<AVObject> query = new AVQuery<>("records");
        // query.whereEqualTo("follower_id", AVObject.createWithoutData("records", follower_id));
        query.whereEqualTo("user_id", user_id);
        query.whereEqualTo("follower_id", follower_id);
        query.orderByDescending("ctime");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject obj : list) {
                    int ctime = obj.getInt("ctime");
                    String content = obj.getString("content");
                    System.out.println(content);
                    String picUrl = obj.getString("pic1");
                    System.out.println(picUrl);
                    String videoUrl = obj.getString("video");

                    Calendar c= Calendar.getInstance();
                    long millis = new Long(ctime).longValue()*1000;
                    c.setTimeInMillis(millis);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = sdf.format(c.getTime());
                    // System.out.println(time + content);
                    traceList.add(new Trace(time, content, picUrl, videoUrl));
                    // System.out.println(traceList);
                }
                adapter = new TraceListAdapter(TraceActivity.this, traceList);
                // System.out.println(adapter.getItemCount());
                rvTrace.setLayoutManager(new LinearLayoutManager(TraceActivity.this));
                rvTrace.setAdapter(adapter);
            }
        });

        // String cql = "select ctime,content from records where follower_id = "+follower_id+" order by ctime desc";
        // AVQuery.doCloudQueryInBackground(cql, new CloudQueryCallback<AVCloudQueryResult>() {
        //     @Override
        //     public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
        //         if (e != null) {
        //             System.out.println("error");
        //         }
        //         List<AVObject> list = (List<AVObject>) avCloudQueryResult.getResults();
        //         for (AVObject obj : list) {
        //             int ctime = obj.getInt("ctime");
        //             String content = obj.getString("content");
        //             Calendar c= Calendar.getInstance();
        //             long millis = new Long(ctime).longValue()*1000;
        //             c.setTimeInMillis(millis);
        //             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //             String time = sdf.format(c.getTime());
        //             System.out.println(time + content);
        //             traceList.add(new Trace(time, content));
        //         }
        //     }
        // });

        // 模拟
        // traceList.add(new Trace("2016-05-25 14:13:00", "adfgshs", R.drawable.aaa));
        // traceList.add(new Trace("2016-05-25 13:01:04", "dfgbrgbgrbrsf"));
        // traceList.add(new Trace("2016-05-25 12:19:47", "fdbvgsdbfd]"));
        // traceList.add(new Trace("2016-05-25 11:12:44", "zdfbadgbgbdabxdf"));
        // traceList.add(new Trace("2016-05-24 03:12:12", "sgdhrsgfbgszbn"));
        // traceList.add(new Trace("2016-05-23 21:06:46", "gfdszbfgbsfgb"));
        // traceList.add(new Trace("2016-05-23 18:59:41", "sgdfbzsgbnfsgnb"));
        // traceList.add(new Trace("2016-05-23 18:35:32", "adfvdafvadfbadfb"));
        // traceList.add(new Trace("2016-05-25 13:01:04", "fDSvaddgfhsrfyhyhr"));
        // //
        // adapter = new TraceListAdapter(this, traceList);
        // // System.out.println(adapter.getItemCount());
        // rvTrace.setLayoutManager(new LinearLayoutManager(this));
        // rvTrace.setAdapter(adapter);
    }
}