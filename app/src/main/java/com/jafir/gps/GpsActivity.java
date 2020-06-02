package com.jafir.gps;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.jafir.gps.service.MockGpsService;
import com.jafir.gps.service.UploadGpsService;
import com.jafir.gps.util.ConvertUtil;
import com.jafir.gps.util.PrefManager;
import com.jafir.mockgps.MockLocationManager;
import com.xdandroid.hellodaemon.AbsWorkService;
import com.xdandroid.hellodaemon.DaemonEnv;
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
    @BindView(R.id.develop)
    View mViewDevelop;
    private double mMockLat, mMockLng;
    private Marker mMarker;
    private MockLocationManager mMockLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        mMapView.onCreate(savedInstanceState);
        initMap();
        initMockGps();
    }

    private void initMockGps() {
        if (mMockLocationManager == null) {
            mMockLocationManager = new MockLocationManager();
            mMockLocationManager.initService(getApplicationContext());
            mMockLocationManager.startThread();
        }
        if (!mMockLocationManager.getUseMockPosition(this)) {
            mViewDevelop.setVisibility(View.VISIBLE);
        }
    }

    private void initMap() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        AMap map = mMapView.getMap();
        map.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        map.getUiSettings().setMyLocationButtonEnabled(true); //显示默认的定位按钮
        map.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        map.moveCamera(CameraUpdateFactory.zoomTo(16));
        map.setOnMapClickListener(latLng -> {
            Log.d(TAG, "mapCLick:" + latLng.latitude + "\t" + latLng.longitude);
            mMockLat = latLng.latitude;
            mMockLng = latLng.longitude;
            if (mMarker != null) {
                mMarker.remove();
            }
            mMarker = map.addMarker(new MarkerOptions().position(latLng).title("模拟位置").snippet("default"));
        });
        map.setOnMyLocationChangeListener(location -> Log.d(TAG, "onMyLocationChange:" + location.getLatitude() + "\t" + location.getLongitude()));
    }


    @OnClick(R.id.to_develop)
    public void to_develop() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            this.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "无法跳转到开发者选项,请先确保您的设备已处于开发者模式", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @OnClick(R.id.stop_mock_gps)
    public void StopMockGps() {
        MockGpsService.stopService();
        stopService(new Intent(this, MockGpsService.class));
        finish();
    }

    @OnClick(R.id.mock_gps)
    public void mockGps() {
        //高德坐标转gps进行模拟
        LatLng latLng = ConvertUtil.toWGS84Point(mMockLat, mMockLng);
        double lat = latLng.latitude;
        double lng = latLng.longitude;
        if (mMockLat == 0 || mMockLng == 0) {
            Toast.makeText(this, "请先在地图上选点", Toast.LENGTH_SHORT).show();
            return;
        }
//        Intent intent = new Intent(this, LocationActivity.class);
//        intent.putExtra(LocationActivity.INTENT_KEY_LAT, lat);
//        intent.putExtra(LocationActivity.INTENT_KEY_LNG, lng);
//        startActivity(intent);


        MockGpsService.sShouldStopService = false;
        Intent intent = new Intent(this, MockGpsService.class);
        intent.putExtra(LocationActivity.INTENT_KEY_LAT, lat);
        intent.putExtra(LocationActivity.INTENT_KEY_LNG, lng);
        startService(intent);

//        LocationDialog.Builder builder = new LocationDialog.Builder(this);
//        builder.setLatitude(lat);
//        builder.setLongitude(lng);
//        builder.setPositiveButton("知道了", (dialog, which) -> dialog.dismiss());
//        builder.create().show();

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
        if (mMockLocationManager.getUseMockPosition(this)) {
            mViewDevelop.setVisibility(View.GONE);
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
    }

}
