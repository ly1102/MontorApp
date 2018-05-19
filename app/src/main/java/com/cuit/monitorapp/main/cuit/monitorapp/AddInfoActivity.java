package com.cuit.monitorapp.main.cuit.monitorapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.cuit.monitorapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;


public class AddInfoActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private ImageView mImageView, videoView;
    private Button choose_img_btn, located_btn, video_btn, submit_btn;
    private LinearLayout img_parent;
    private EditText longitude_edit, latitude_edit, content_edit;
    private byte[] bitmap1 = null;
    private byte[] bitmap2 = null;
    private byte[] bitmap3 = null;
    private byte[] bitmap4 = null;
    private Boolean send_status = false;
    private String video_path = null, pic1_url = null, pic2_url = null, pic3_url = null, video_url = null, video_pic_url, follower_id, user_id;
    private String longitude_string = null, latitude_string = null;
    private double longitude_double = 0.0, latitude_double = 0.0;
    private ProgressDialog dialog_progress;
    Boolean status1 = null, status2 = null, status3 = null, status4 = null, status5 = null, status6 = null;
    private int progress, bitmap_count=0, video_count=0;
    private String content_text;
    private Date date = new Date();
    @android.support.annotation.IdRes
    public int ImageId1 = 1;
    public int ImageID2 = 2;
    public int ImageID3 = 3;

    private int current_file_count = 0, total_file_count = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            if(msg.what == 1){
                Log.i("upload", "handleMessage: upload pic1");
                if(bitmap_count>1){
                    upload_pic2();
                }else if(video_count != 0){
                    upload_video_pic();
                }else {
                    upload_info();
                }
            }

            if(msg.what == 2){
                Log.i("upload", "handleMessage: upload pic2");
                if(bitmap_count>1){
                    upload_pic3();
                }else if(video_count != 0){
                    upload_video_pic();
                }else {
                    upload_info();
                }
            }
            if(msg.what == 3){
                Log.i("upload", "handleMessage: upload pic3");
                if(video_count != 0){
                    upload_video_pic();
                }else {
                    upload_info();
                }
            }
            if(msg.what == 4){
                Log.i("upload", "handleMessage: upload pic4");
                upload_video();
            }
            if(msg.what == 5){
                Log.i("upload", "handleMessage: upload video");
                upload_info();
            }
            if(msg.what == -1){
                if(dialog_progress.isShowing()){
                    Log.i("close dialog", "handleMessage: close dialog");
                    dialog_progress.dismiss();
                }
                Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_SHORT).show();
                finishActivity(1);
                finish();
            }
            else if(msg.what == -2){
                if(dialog_progress.isShowing()){
                    Log.i("close dialog", "handleMessage: close dialog");
                    dialog_progress.dismiss();
                }
                Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_SHORT).show();
                finishActivity(1);
                finish();
            }
            else {
                if(!dialog_progress.isShowing()){
                    dialog_progress.show();
                }
                dialog_progress.setProgress(msg.arg1);
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "fQeWhHtqk7fB9aVWwm6BAJOF-gzGzoHsz", "uvGmN110krfVFgxP9Cr8B6fv");

        setContentView(R.layout.activity_add_info);
        mImageView = findViewById(R.id.img);
        choose_img_btn = findViewById(R.id.local_choose_btn);
        img_parent = findViewById(R.id.image_parent);
        video_btn = findViewById(R.id.video_btn);
        videoView = findViewById(R.id.video_view);
        located_btn = findViewById(R.id.locate_btn);
        longitude_edit = findViewById(R.id.edit_longitude);
        latitude_edit = findViewById(R.id.edit_latitude);
        ImageButton back_btn = findViewById(R.id.back_btn);
        submit_btn = findViewById(R.id.submit_btn);
        content_edit = findViewById(R.id.content);


        Intent start_intent = new Intent(this, AddInfoActivity.class);
        user_id = start_intent.getStringExtra("user_id");
        follower_id = start_intent.getStringExtra("follower_id");

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        choose_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_img_from_local();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_video(video_btn);
            }
        });

        located_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_location();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = submit_info(submit_btn);
                Log.i("status", "onClick: " + status);
                if (status != null) {
                    Log.i("finish", "onClick: finish");
                    finishActivity(1);
                    Log.i("finish2", "onClick: finish2");
                    finish();
                }
            }
        });


    }

    public void choose_video(View view) {
        permission(video_btn);
        Intent local = new Intent();
        local.setType("video/*");
        local.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(local, 3);
    }

    public void permission(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }
    }

    public void get_locate_permission(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.LOCATION_HARDWARE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.LOCATION_HARDWARE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }
    }

    public void callPhone() {
        Toast.makeText(getApplicationContext(), "权限来啦~！！！",
                Toast.LENGTH_SHORT).show();
    }


    public void takePhoto() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, 1);
    }

    public void choose_img_from_local() {
        permission(choose_img_btn);
        Intent local = new Intent();
        local.setType("image/*");
        local.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(local, 2);
    }

    public void get_location() {
        get_locate_permission(located_btn);
        //获取定位服务
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        assert locationManager != null;
        List<String> list = locationManager.getProviders(true);

        String provider;
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(getApplicationContext(), "请检查网络或GPS是否打开",
                    LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "locate permission denied!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            String string = "纬度为：" + location.getLatitude() + ",经度为："
                    + location.getLongitude();

            longitude_edit.setText(String.valueOf(location.getLongitude()));
            latitude_edit.setText(String.valueOf(location.getLatitude()));
//                    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
        }
    }

    public ImageView setId(ImageView imageView, int IdCount) {
        switch (IdCount) {
            case 1:
                imageView.setId(R.id.ImageId1);
                break;
            case 2:
                imageView.setId(R.id.ImageId2);
                break;
            case 3:
                imageView.setId(R.id.ImageId3);
                break;
        }
        return imageView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String srcPath = null;
        if (resultCode == RESULT_OK) {

            int child_count = img_parent.getChildCount();
            ImageView new_view = new ImageView(img_parent.getContext());
            if (child_count == 3) img_parent.removeView(mImageView);
            new_view = setId(new_view, child_count);
            new_view.setMaxHeight(360);
            new_view.setMaxWidth(270);
            new_view.setAdjustViewBounds(true);
            new_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));


            switch (requestCode) {
                case 1:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    //拿到bitmap，做喜欢做的事情把  ---> 显示 or上传？
                    new_view.setImageBitmap(bitmap);
                    switch (child_count) {
                        case 1:
                            bitmap1 = bitmap_to_bytes(bitmap);
                            break;
                        case 2:
                            bitmap2 = bitmap_to_bytes(bitmap);
                            break;
                        case 3:
                            bitmap3 = bitmap_to_bytes(bitmap);
                            break;
                    }
                    img_parent.addView(new_view, child_count - 1);
                    mImageView.setAdjustViewBounds(true);
                    break;
                case 2:
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Cursor c = cr.query(uri, null, null, null, null);
                        if (c != null) {
                            c.moveToFirst();
                            srcPath = c.getString(c.getColumnIndex("_data"));
                            Log.i("save", "onActivityResult: " + srcPath);
                        }
                        //这是获取的图片保存在sdcard中的位置
                        if (srcPath == null) {
                            Log.e("user ", "onActivityResult: user xiaomi's method");
                            uri = geturi(this.getIntent(), uri, "image");
                        }
                        System.out.println(srcPath + "----------保存路径2");
                    } catch (Exception e) {
                        Log.e("cursor", "onActivityResult: cursor error occurred" + e);
                        e.printStackTrace();
                    }
                    new_view.setImageURI(uri);
                    Bitmap bitmap5 = getBitmapFromUri(uri);
                    if (bitmap5 == null) {
                        Log.e("no bitmap", "onActivityResult: GET file error!!");
                    }
                    switch (child_count) {
                        case 1:
                            bitmap1 = bitmap_to_bytes(bitmap5);
                            break;
                        case 2:
                            bitmap2 = bitmap_to_bytes(bitmap5);
                            break;
                        case 3:
                            bitmap3 = bitmap_to_bytes(bitmap5);
                            break;
                    }
                    img_parent.addView(new_view, child_count - 1);
                    mImageView.setAdjustViewBounds(true);
                    break;
                case 3:
                    Uri uri3 = data.getData();
                    try {
                        assert uri3 != null;
                        srcPath = uri3.getPath();
                        //这是获取的图片保存在sdcard中的位置

                        if (srcPath == null) {
                            Log.e("user ", "onActivityResult: user xiaomi's method");
                            uri3 = geturi(this.getIntent(), uri3, "video");
                        }
                        System.out.println(srcPath + "----------保存路径2");
                    } catch (Exception e) {

                        Log.e("cursor", "onActivityResult: cursor error occurred" + e);
                        e.printStackTrace();
                    }
                    MediaMetadataRetriever media = new MediaMetadataRetriever();

                    media.setDataSource(videoView.getContext(), uri3);

                    srcPath = getPath(this, uri3);
                    video_path = srcPath;
                    Toast.makeText(this, srcPath, Toast.LENGTH_SHORT).show();
                    Bitmap video_poster = media.getFrameAtTime();
                    bitmap4 = bitmap_to_bytes(video_poster);
                    videoView.setImageBitmap(video_poster);

                default:
                    break;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            Log.d("GPS权限", "onRequestPermissionsResult: " + Arrays.toString(grantResults));
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                if (grantResults.length > 1) {
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        Log.i("gps ok", "onRequestPermissionsResult: GPS ok!");
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(AddInfoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public byte[] bitmap_to_bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // 把绝对路径转换成content开头的URI
    public Uri geturi(android.content.Intent intent, Uri uri, String type) {

        if (uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);

                ContentResolver cr = this.getContentResolver();
                if (type.equals("image")) {
                    Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Images.ImageColumns._ID},
                            "(" + MediaStore.Images.ImageColumns.DATA + "=" + "'" + path + "'" + ")", null, null);
                    int index = 0;
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                        // set _id value
                        index = cur.getInt(index);
                    }
                    if (index == 0) {
                        // do nothing
                        Log.i("image gg", "geturi: no uri gived!");
                    } else {
                        Uri uri_temp = Uri
                                .parse("content://media/external/images/media/"
                                        + index);
                        if (uri_temp != null) {
                            uri = uri_temp;
                            Log.i("urishi", uri.toString());
                        }
                    }
                } else {
                    Cursor cur = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Video.VideoColumns._ID},
                            "(" + MediaStore.Video.VideoColumns.DATA + "=" + "'" + path + "'" + ")", null, null);
                    int index = 0;
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        index = cur.getColumnIndex(MediaStore.Video.VideoColumns._ID);
                        // set _id value
                        index = cur.getInt(index);
                    }
                    if (index == 0) {
                        // do nothing
                        Log.i("video gg", "geturi: no uri gived!");
                    } else {
                        Uri uri_temp = Uri
                                .parse("content://media/external/videos/media/"
                                        + index);
                        if (uri_temp != null) {
                            uri = uri_temp;
                            Log.i("urishi", uri.toString());
                        }
                    }
                }

            }
        }
        return uri;
    }


    public String submit_info(View view) {

        bitmap_count = 0;
        video_count = 0;

        content_text = String.valueOf(content_edit.getText());
        longitude_string = String.valueOf(longitude_edit.getText());
        latitude_string = String.valueOf(latitude_edit.getText());

        if (content_text.length() == 0) {
            show_alert("备注信息是必填项！");
            return null;
        }
        if (longitude_string.equals("") || latitude_string.equals("")) {
            show_alert("经纬度是必填项！");
            return null;
        }
        try {
            longitude_double = Double.valueOf(longitude_string);
            latitude_double = Double.valueOf(latitude_string);
        } catch (Exception e) {
            show_alert("经纬度必须是小数或者整数");
            return null;
        }

        if (bitmap1 != null)
            bitmap_count += 1;
        if (bitmap2 != null)
            bitmap_count += 1;
        if (bitmap3 != null)
            bitmap_count += 1;
        if (video_path != null)
            video_count += 1;

        date.getTime();

        total_file_count= current_file_count = bitmap_count + video_count;
        Log.d("nummmmm", "submit_info: "+bitmap_count+"    "+video_count);
        Log.i("waitttttt", "submit_info: wait upload");
        if (bitmap_count != 0 || video_count != 0) {
            dialog_progress = new ProgressDialog(this);
            // 设置进度条风格，风格为圆形，旋转的
            dialog_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // 设置ProgressDialog 标题
            dialog_progress.setTitle("上传中，请稍后");
            // 设置ProgressDialog提示信息
            dialog_progress.setMessage("文件上传中，请稍后...");

            // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
            dialog_progress.setIndeterminate(true);
            dialog_progress.setProgress(1);
            // 设置ProgressDialog 是否可以按退回键取消
            dialog_progress.setCancelable(true);
            // 设置ProgressDialog 的一个Button
            //dialog_progress.setButton("取消");
            // 让ProgressDialog显示
//            Toast.makeText(this, "show progress dialog", Toast.LENGTH_SHORT).show();
            dialog_progress.show();
        } else {
            Toast.makeText(this, "没显示进度条呢", Toast.LENGTH_SHORT).show();
        }

        Log.e("time", "submit_info: " + date.toString() + "---" + String.valueOf(date.getTime()));

        if (bitmap_count == 0 && video_count != 0) {
            upload_video_pic();
        }else{
            upload_pic1();
        }



//        while (true) {
//            Log.i("progress", "submit_info: progress: "+progress);
//            if (bitmap_count == 0 && video_count == 0) break;
//            Boolean video_status = check_video_finish(video_count);
//
//            if (!video_status) return null;
//            if (bitmap_count == 1) {
//                if (status1 != null) {
//                    if (!status1) {
//                        Toast.makeText(this, "上传图片失败！", Toast.LENGTH_SHORT).show();
//                        return null;
//                    } else {
//                        break;
//                    }
//
//                }
//            }
//            if (bitmap_count == 2) {
//                if (status1 != null && status2 != null) {
//                    if (!status1 || !status2) {
//                        Toast.makeText(this, "上传图片失败！", Toast.LENGTH_SHORT).show();
//                        return null;
//                    } else {
//                        break;
//                    }
//
//                }
//            }
//            if (bitmap_count == 3) {
//                if (status1 != null && status2 != null && status3 != null) {
//                    if (!status1 || !status2 || status3) {
//                        Toast.makeText(this, "上传图片失败！", Toast.LENGTH_SHORT).show();
//                        return null;
//                    } else {
//                        break;
//
//                    }
//                }
//            }
//        }

//        if (bitmap_count != 0 || video_count != 0)
//            dialog_progress.dismiss();

        Log.i("waittttt", "submit_info: wait last upload");

        return null;
    }

    public void upload_pic1(){
        dialog_progress.setMessage("正在上传图片1...");
        final AVFile avFile = new AVFile(user_id + follower_id + date.toString() + "pic1", bitmap1);

        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                if (e == null) {
                    pic1_url = avFile.getUrl();
                    status1 = true;
                    Log.i("success save", "done: save pic1 success!");
                } else {
                    status1 = false;
                    Log.i("fail save", "done: save info fail!");
                }

            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                progress = integer;

                Message message = new Message();
                message.what = 99;
                message.arg1 = progress;
                handler.sendMessage(message);
            }
        });
    }

    public void upload_pic2(){
        dialog_progress.setMessage("正在上传图片2...");
        final AVFile avFile = new AVFile(user_id + follower_id + date.toString() + "pic2", bitmap2);

        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
                if (e == null) {
                    pic2_url = avFile.getUrl();
                    status2 = true;
                    Log.i("success save", "done: save pic2 success!");
                } else {
                    status2 = false;
                    Log.i("fail save", "done: save pic2 fail!");
                }

            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                progress = integer;
                Message message = new Message();
                message.what = 99;
                message.arg1 = progress;
                handler.sendMessage(message);
            }
        });
    }

    public void upload_pic3(){
        dialog_progress.setMessage("正在上传图片3...");
        final AVFile avFile = new AVFile(user_id + follower_id + date.toString() + "pic1", bitmap3);

        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
                if (e == null) {
                    pic3_url = avFile.getUrl();
                    status3 = true;
                    Log.i("success save", "done: save pic3 success!");
                } else {
                    status3 = false;
                    Log.i("fail save", "done: save pic3 fail!");
                }

            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                progress = integer;
                Message message = new Message();
                message.what = 99;
                message.arg1 = progress;
                handler.sendMessage(message);
            }
        });
    }

    public void upload_video_pic(){
        dialog_progress.setMessage("正在准备上传视频文件");
        final AVFile avFile0 = new AVFile(user_id + follower_id + date.toString() + "pic1", bitmap3);
        avFile0.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Message message = new Message();
                message.what = 4;
                handler.sendMessage(message);
                if (e == null) {
                    video_pic_url = avFile0.getUrl();
                    status4 = true;
                    Log.i("success save", "done: save video pic success!");
                } else {
                    status4 = false;
                    Log.i("fail save", "done: save video pic fail!");
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                progress = integer;
                Message message = new Message();
                message.what = 99;
                message.arg1 = progress;
                handler.sendMessage(message);
            }
        });
    }

    public void upload_video(){
        dialog_progress.setMessage("正在上传视频...");
        try {
            final AVFile avFile = AVFile.withAbsoluteLocalPath(user_id + date.toString() + ".mp4", video_path);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Message message = new Message();
                    if (e == null) {
                        video_url = avFile.getUrl();
                        status5 = true;
                        message.what = -1;
                        handler.sendMessage(message);
                        Log.i("success save", "done: save video success!");
                    } else {
                        status5 = false;
                        message.what = -2;
                        handler.sendMessage(message);
                        Log.i("fail save", "done: save video fail!");
                    }

                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    progress = integer;
                    Message message = new Message();
                    message.what = 99;
                    message.arg1 = progress;
                    handler.sendMessage(message);
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "找不到video: " + video_path, LENGTH_LONG).show();
            Message message = new Message();
            message.what = -2;
            handler.sendMessage(message);
        }
    }

    public void upload_info(){
        dialog_progress.setMessage("正在上传全部信息...");
        Date date = new Date();
        date.getTime();

        AVObject new_info = new AVObject("records");
        new_info.put("user_id", "123456");
        new_info.put("follower_id", "123456");
        new_info.put("content", content_text);
        new_info.put("longitude", longitude_double);
        new_info.put("latitude", latitude_double);
        new_info.put("ctime", date.getTime());
        if (pic1_url != null)
            new_info.put("pic1", pic1_url);
        if (pic2_url != null)
            new_info.put("pic2", pic2_url);
        if (pic3_url != null)
            new_info.put("pic3", pic3_url);
        if (video_url != null) {
            new_info.put("video", video_url);
            new_info.put("video_pic", video_pic_url);
        }

        new_info.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    status6 = true;
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                    Log.e("upload success!", "done: upload info success!");
                } else {
                    Log.e("upload error!", "done: upload info error!");
                    Message message = new Message();
                    message.what = -2;
                    handler.sendMessage(message);
                    status6 = false;
                }
            }
        });
    }

    public void show_alert(String message) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage(message).setPositiveButton("确定", null).show();
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap_file = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(), uri);
            return bitmap_file;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

}


