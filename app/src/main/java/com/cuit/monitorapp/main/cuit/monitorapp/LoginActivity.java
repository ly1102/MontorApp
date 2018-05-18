package com.cuit.monitorapp.main.cuit.monitorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cuit.monitorapp.R;
import com.cuit.monitorapp.main.cuit.monitorapp.logreg.ActivityCollector;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String name;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCollector.addActivity(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        EditText editTextPasswd = (EditText) findViewById(R.id.passwd);
        Button buttonLogin = (Button) findViewById(R.id.login);

        final String name = getMacAddress(this);
        final String password = editTextPasswd.getText().toString();

        TextView nameTextView = (TextView) findViewById(R.id.username);
        nameTextView.setText(name);


        TextView signupLink = (TextView) findViewById(R.id.signup_link);
        String text = "你还没有账号吗？click here";

        SpannableString ss = new SpannableString(text);

        signupLink.setText(ss);

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        }, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyUser(name,password)) {
                    editor.putBoolean("lgoin", true);
                    Intent intent = new Intent(LoginActivity.this, SetGestureActivity.class);
                    if (!TextUtils.isEmpty(preferences.getString("gesture", ""))) {
                        intent.putExtra("activityNum", 0);
                    } else {
                        intent.putExtra("activityNum", 1);
                    }
                    editor.commit();
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "login falid!", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private boolean verifyUser(String name, String passwd) {

        return true;
    }

    private String getMacAddress(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        if (wi == null || wi.getMacAddress() == null) {
            return null;
        }

        if ("02:00:00:00:00:00".equals(wi.getMacAddress().trim())) {
            return null;
        } else {
            return wi.getMacAddress().trim();
        }
    }
}
