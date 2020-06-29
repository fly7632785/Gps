package com.jafir.gps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xdandroid.hellodaemon.IntentWrapper;

/**
 * created by jafir on 2020-05-20
 */
public class LoginActivity extends FrameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                    } else {
                        init();

                    }
                }, e -> {
                });

    }

    private void init() {
        RetrofitManager.getInstance().mainService().login(new LoginRequest("admin","123456"))
                .compose(ReactivexCompat.singleThreadSchedule())
                .subscribe(result -> {
                    if (result.getCode() == 0) {
                        String token = result.getData().getToken();
                        PrefManager.getInstance(LoginActivity.this).setToken(token);
                    }
                }, e -> {
                    Log.e("login", "service login err:" + e.getMessage());
                });
        startService(new Intent(this, KeepLiveService.class));
    }
}
