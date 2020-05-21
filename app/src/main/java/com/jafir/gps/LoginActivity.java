package com.jafir.gps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xdandroid.hellodaemon.IntentWrapper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by jafir on 2020-05-20
 */
public class LoginActivity extends FrameActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.username)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    View mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (PrefManager.getInstance(this).isFirst()) {
            IntentWrapper.whiteListMatters(this, "为了更好的实时定位，最好把应用加入您手机的白名单");
            PrefManager.getInstance(this).setFirst();
        }

        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .as(bindLifecycle())
                .subscribe(b -> {
                    if (!b) {
                        finish();
                    }
                }, e -> {
                });

        if (!TextUtils.isEmpty(PrefManager.getInstance(this).userId())) {
            Toast.makeText(LoginActivity.this, "已登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, GpsActivity.class));
            finish();
        }
    }

    @OnClick(R.id.login)
    public void login() {
        String username = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        RetrofitManager.getInstance()
                .mainService()
                .login(username, password)
                .compose(ReactivexCompat.singleThreadSchedule())
                .as(bindLifecycle())
                .subscribe(loginResult -> {
                    if (loginResult.getCode() == 200) {
                        PrefManager.getInstance(LoginActivity.this).setUserId(String.valueOf(loginResult.getData().getUser_id()));
                        startActivity(new Intent(LoginActivity.this, GpsActivity.class));
                        finish();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    } else {
                        PrefManager.getInstance(LoginActivity.this).setUserId("");
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e(TAG, "login err:" + throwable);
                    PrefManager.getInstance(LoginActivity.this).setUserId("");
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                });
    }
}
