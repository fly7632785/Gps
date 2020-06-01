package com.jafir.gps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xdandroid.hellodaemon.IntentWrapper;

import butterknife.OnClick;

/**
 * created by jafir on 2020-05-20
 */
public class LoginActivity extends FrameActivity {

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
        PrefManager.getInstance(this).setUserId(DeviceUtil.getDeviceId(this));

        startService(new Intent(this, KeepLiveService.class));

    }

    @OnClick(R.id.mock_gps)
    public void mockGps() {
        startActivity(new Intent(this,GpsActivity.class));
//        startActivity(new Intent(this, LocationActivity.class));
//        startService(new Intent(this,MockGpsService.class));
    }

    @Override
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }

}
