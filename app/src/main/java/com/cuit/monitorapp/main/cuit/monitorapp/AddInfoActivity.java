package com.cuit.monitorapp.main.cuit.monitorapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.cuit.monitorapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddInfoActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private ImageView mImageView;
    private Button choose_img_btn;
    private LinearLayout img_parent;
    private Button video_btn;
    private ImageView videoView;

    @android.support.annotation.IdRes
    public int ImageId1 = 1;
    public int ImageID2 = 2;
    public int ImageID3 = 3;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        mImageView = findViewById(R.id.img);
        choose_img_btn = findViewById(R.id.local_choose_btn);
        img_parent = findViewById(R.id.image_parent);
        video_btn = findViewById(R.id.video_btn);
        videoView = findViewById(R.id.video_view);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        choose_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission(choose_img_btn);
                Intent local = new Intent();
                local.setType("image/*");
                local.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(local, 2);
            }
        });

        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission(video_btn);
                Intent local = new Intent();
                local.setType("video/*");
                local.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(local, 3);
            }
        });

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }


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

    public void callPhone(){
        Toast.makeText(getApplicationContext(), "权限来啦~！！！",
                Toast.LENGTH_SHORT).show();
    }


    public void takePhoto() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, 1);
    }


    public ImageView setId(ImageView imageView, int IdCount){
        switch (IdCount){
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
            if(child_count==3) img_parent.removeView(mImageView);
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
                    img_parent.addView(new_view, child_count-1);
                    mImageView.setAdjustViewBounds(true);
                    break;
                case 2:
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Cursor c = cr.query(uri, null, null, null, null);
                        if (c != null){
                            c.moveToFirst();
                            srcPath = c.getString(c.getColumnIndex("_data"));
                            Log.i("save", "onActivityResult: "+srcPath);
                        }

                        //这是获取的图片保存在sdcard中的位置

                        if (srcPath == null){
                            Log.e("user ", "onActivityResult: user xiaomi's method");
                            uri = geturi(this.getIntent(), uri);
                        }
                        System.out.println(srcPath+"----------保存路径2");
                    }catch (Exception e){

                        Log.e("cursor", "onActivityResult: cursor error occurred"+e);
                        e.printStackTrace();
                    }
                    new_view.setImageURI(uri);
                    img_parent.addView(new_view, child_count-1);
                    mImageView.setAdjustViewBounds(true);
                    break;
                case 3:
                    Uri uri3 = data.getData();
                    ContentResolver cr3 = this.getContentResolver();
                    try {
                        Cursor c = cr3.query(uri3, null, null, null, null);
                        if (c != null){
                            c.moveToFirst();
                            srcPath = c.getString(c.getColumnIndex("_data"));
                            Log.i("save", "onActivityResult: "+srcPath);
                        }

                        //这是获取的图片保存在sdcard中的位置

                        if (srcPath == null){
                            Log.e("user ", "onActivityResult: user xiaomi's method");
                            uri3 = geturi(this.getIntent(), uri3);
                        }
                        System.out.println(srcPath+"----------保存路径2");
                    }catch (Exception e){

                        Log.e("cursor", "onActivityResult: cursor error occurred"+e);
                        e.printStackTrace();
                    }
                    MediaMetadataRetriever media = new MediaMetadataRetriever();

                    media.setDataSource(videoView.getContext(), uri3);


                    Bitmap bitmap2 = media.getFrameAtTime();
                    videoView.setImageBitmap(bitmap2);

                default:
                    break;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhone();
            } else
            {
                // Permission Denied
                Toast.makeText(AddInfoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // 把绝对路径转换成content开头的URI
    public Uri geturi(android.content.Intent intent, Uri uri) {

        if (uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);

                ContentResolver cr = this.getContentResolver();
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        "(" + MediaStore.Images.ImageColumns.DATA + "=" + "'" + path + "'" + ")", null, null);

                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                    Log.i("gg", "geturi: no uri gived!");
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                        Log.i("urishi", uri.toString());
                    }
                }
            }
        }
        return uri;
    }


    public static String saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "images");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appDir.toString() + fileName;
    }


}