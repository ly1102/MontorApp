package com.cuit.monitorapp.main.cuit.monitorapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cuit.monitorapp.R;

public class AddInfoActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 111;

    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private Button gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        Toast.makeText(getApplicationContext(), "默认Toast样式",
                Toast.LENGTH_SHORT).show();
        mImageView1 = findViewById(R.id.img1);
        mImageView2 = findViewById(R.id.img2);
        mImageView3 = findViewById(R.id.img3);
        gps = findViewById(R.id.gps_btn);

        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(1);
            }
        });
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(2);
            }
        });
        mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(3);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AddInfoActivity.this, DingweiActivity.class);
                startActivity(intent);
            }
        });


    }
    public void takePhoto(int index) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //拿到bitmap，做喜欢做的事情把  ---> 显示 or上传？

                mImageView1.setImageBitmap(bitmap);

                //upload
            }
        }


    }

}