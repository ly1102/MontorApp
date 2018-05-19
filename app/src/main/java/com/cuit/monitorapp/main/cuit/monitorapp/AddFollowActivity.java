package com.cuit.monitorapp.main.cuit.monitorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.cuit.monitorapp.R;

public class AddFollowActivity extends AppCompatActivity {
    String name;
    String gender;
    String identify;
    String qq;
    String wechat;
    String email;
    String phone;
    AVObject follow;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        follow = getIntent().getParcelableExtra("follow");
        if (follow != null) {
            System.out.println(follow.toString());
        }
        setContentView(R.layout.activity_add_follow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RadioGroup radioGroup = findViewById(R.id.gender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                gender = radioButton.getText().toString();
            }
        });
        if (follow != null) {
            ((EditText) findViewById(R.id.name)).setText(follow.getString("name"));
            ((EditText) findViewById(R.id.identify)).setText(follow.getString("identify"));
            ((EditText) findViewById(R.id.qq)).setText(follow.getString("qq"));
            ((EditText) findViewById(R.id.wechat)).setText(follow.getString("wechat"));
            ((EditText) findViewById(R.id.phone)).setText(follow.getString("phone"));
            ((EditText) findViewById(R.id.email)).setText(follow.getString("email"));
            if (follow.getString("gender").equals("女")) {
                ((RadioButton) findViewById(R.id.woman)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.man)).setChecked(true);
            }
        }

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ((EditText) findViewById(R.id.name)).getText().toString();
                identify = ((EditText) findViewById(R.id.identify)).getText().toString();
                qq = ((EditText) findViewById(R.id.qq)).getText().toString();
                wechat = ((EditText) findViewById(R.id.wechat)).getText().toString();
                phone = ((EditText) findViewById(R.id.phone)).getText().toString();
                email = ((EditText) findViewById(R.id.email)).getText().toString();
                if (follow == null) {
                    follow = new AVObject("M_Follow");
                }
                follow.put("name", name);
                follow.put("gender", gender);
                follow.put("identify", identify);
                follow.put("qq", qq);
                follow.put("wechat", wechat);
                follow.put("phone", phone);
                follow.put("email", email);
                Auth auth = (Auth) getApplication();
                String user = auth.getObjectId();
                follow.put("observer", AVObject.createWithoutData("User_obj", "5afd8c8167f356003863ca79"));
                follow.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(AddFollowActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddFollowActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                        AddFollowActivity.this.finish();
                    }
                });
            }
        });
    }

}
