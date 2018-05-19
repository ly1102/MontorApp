package com.cuit.monitorapp.main.cuit.monitorapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cuit.monitorapp.R;

import java.util.ArrayList;
import java.util.List;

public class DingweiActivity extends Activity implements BDLocationListener {
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    public LocationClient mLocationClient = null;
    private boolean isFirstLoc = true;
    private String follower_id="123456";
    public ArrayList<MarkerInfoUtil> marker = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                    ArrayList list = msg.getData().getStringArrayList("list");

                assert list != null;
                for (int i=0; i<list.size(); i=i+2) {

                        String latitude = (String) list.get(i);
                        String longitude = (String) list.get(i+1);
                        Log.i("tag111111", "done: " + latitude + "   " + longitude);
                        try {
                            if(latitude != null && longitude != null){
                                double latitude_d = Double.valueOf(String.valueOf(latitude));
                                double longitude_d = Double.valueOf(String.valueOf(longitude));
                                MarkerInfoUtil markerInfoUtil = new MarkerInfoUtil(latitude_d, longitude_d);
                                Log.i("fff", "handleMessage: "+markerInfoUtil);
                                marker.add(markerInfoUtil);
                            }

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                if(marker.size() > 0)
                    addOverlay(marker);
            }else
                Toast.makeText(getApplicationContext(), "获取用户信息失败", Toast.LENGTH_LONG).show();
            if(msg.what == 1){
                Toast.makeText(getApplicationContext(), "获取到"+msg.arg1+"条位置信息", Toast.LENGTH_LONG).show();
            }
        }
    };

    class MyThread extends Thread {
        @Override
        public void run() {
            AVQuery<AVObject> avQuery = new AVQuery<>("records");
            avQuery.whereEqualTo("follower_id", follower_id);
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e != null){
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                    if (e == null) {
                        Message message = new Message();
                        message.what = 0;
                        message.arg1 = list.size();
                        Bundle bundle = new Bundle();
                        ArrayList<String> list1 = new ArrayList<>();

                        for (AVObject avObject : list) {
//                            Log.i("jjjjj", "done: "+avObject.get("content"));
                            Number latitude = avObject.getNumber("latitude");
                            Number longitude = avObject.getNumber("longitude");
//                            Log.i("tag", "done: " + latitude + "   " + longitude);
                            try {
                                if(latitude != null && longitude != null){
//                                    double latitude_d = Double.valueOf(String.valueOf(latitude));
//                                    double longitude_d = Double.valueOf(String.valueOf(longitude));
                                    list1.add(String.valueOf(latitude));
                                    list1.add(String.valueOf(longitude));
                                }

                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                        bundle.putStringArrayList("list", list1);
                        message.setData(bundle);
                        mHandler.sendMessage(message);

                    } else {
                        Log.e("tag", "done: failed get" );

                    }

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_dingwei);
        AVOSCloud.initialize(this, "fQeWhHtqk7fB9aVWwm6BAJOF-gzGzoHsz", "uvGmN110krfVFgxP9Cr8B6fv");

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
        // user_id = ((Auth)getApplication()).getObjectId();
        String user_id = "123456";
        follower_id = intent.getStringExtra("follower_id");
        Log.i("location follower id", "onCreate: "+follower_id);
        mMapView = findViewById(R.id.bmapView);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(this);
        initLocation();
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
        MyThread thread = new MyThread();
        thread.start();

        //ArrayList<MarkerInfoUtil> marker = new ArrayList<MarkerInfoUtil>();
        //marker.add(new MarkerInfoUtil(30,103));
    }

    private void addOverlay(List<MarkerInfoUtil> marker) {
        //清空地图
        mBaiduMap.clear();
        //创建marker的显示图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        LatLng latLng = null;
        Marker marker1;
        OverlayOptions options;
        for (MarkerInfoUtil info : marker) {
            //获取经纬度
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            options = new MarkerOptions()
                    .position(latLng)//设置位置
                    .icon(bitmap)//设置图标样式
                    .zIndex(9) // 设置marker所在层级
                    .draggable(true); // 设置手势拖拽;
            //添加marker
            marker1 = (Marker) mBaiduMap.addOverlay(options);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
        }
        //将地图显示在最后一个marker的位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {

        MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
            mBaiduMap.animateMapStatus(update);
            isFirstLoc = false;
            Toast.makeText(getApplicationContext(), location.getAddrStr(), Toast.LENGTH_SHORT).show();
        }
    }
}

