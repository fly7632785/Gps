package com.jafir.gps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
    @BindView(R.id.imei)
    TextView mImei;
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
        mImei.setText("imei:" + DeviceUtil.getDeviceId(this));
    }

    @OnClick(R.id.login)
    public void login() {
        RetrofitManager.getInstance()
                .mainService()
                .login(DeviceUtil.getDeviceId(this))
                .compose(ReactivexCompat.singleThreadSchedule())
                .as(bindLifecycle())
                .subscribe(loginResult -> {
                    if (loginResult.getCode() == 200) {
                        startActivity(new Intent(LoginActivity.this, GpsActivity.class));
                        finish();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e(TAG, "login err:" + throwable);
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                });
    }
}
