package com.jafir.gps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jafir.gps.service.UploadGpsService;
import com.jafir.gps.util.DeviceUtil;
import com.jafir.gps.util.PrefManager;
import com.jafir.gps.util.ReactivexCompat;
import com.jafir.gps.util.RetrofitManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xdandroid.hellodaemon.DaemonEnv;
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
    @BindView(R.id.imei)
    TextView mImei;

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
                    }else {
                        PrefManager.getInstance(this).setUserId(DeviceUtil.getDeviceId(this));
                        mImei.setText(DeviceUtil.getDeviceId(this));
                    }
                }, e -> {
                });
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
                        PrefManager.getInstance(LoginActivity.this).setUserId(String.valueOf(loginResult.getData().getUid()));
                        PrefManager.getInstance(LoginActivity.this).setToken(String.valueOf(loginResult.getData().getToken()));
                        UploadGpsService.sShouldStopService = false;
                        DaemonEnv.startServiceMayBind(UploadGpsService.class);
                        startActivity(new Intent(this, GpsActivity.class));
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


    @OnClick(R.id.mock_gps)
    public void mockGps() {
        startActivity(new Intent(this, GpsActivity.class));
//        startActivity(new Intent(this, LocationActivity.class));
//        startService(new Intent(this,MockGps1Service.class));
    }


    public void logout() {
        PrefManager.getInstance(this).setUserId("");
        UploadGpsService.stopService();
        stopService(new Intent(this, UploadGpsService.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }

}
