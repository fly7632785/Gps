package com.jafir.gps;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jafir.mockgps.MockLocationManager;
import com.xdandroid.hellodaemon.AbsWorkService;

import androidx.annotation.Nullable;

/**
 * created by jafir on 2020-05-20
 */
public class MockGpsService extends AbsWorkService {
    private static final String TAG = MockGpsService.class.getSimpleName();
    public static final String INTENT_KEY_LAT = "intent_key_lat";
    public static final String INTENT_KEY_LNG = "intent_key_lng";
    //是否 任务完成, 不再需要服务运行?
    public static  boolean sShouldStopService;
    private MockLocationManager mMockLocationManager;


    @Override
    public void onCreate() {
        super.onCreate();
        sShouldStopService = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sShouldStopService = false;
        return super.onStartCommand(intent, flags, startId);
    }

    public static void stopService() {
        //我们现在不再需要服务运行了, 将标志位置为 true
        sShouldStopService = true;
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();
    }

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

    public void setMangerLocationData(double lat, double lon) {
        if (mMockLocationManager != null) {
            mMockLocationManager.setLocationData(lat, lon);
        }
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        Log.i(TAG, "startWork");
        if (mMockLocationManager == null) {
            mMockLocationManager = new MockLocationManager();
            mMockLocationManager.initService(getApplicationContext());
            mMockLocationManager.startThread();
        }

        if (mMockLocationManager.getUseMockPosition(getApplicationContext())) {
            startMockLocation();
            double lat = intent.getDoubleExtra(INTENT_KEY_LAT, 0);
            double lng = intent.getDoubleExtra(INTENT_KEY_LNG, 0);
            setMangerLocationData(lat, lng);
        }
    }

    public void startMockLocation() {
        if (mMockLocationManager != null) {
            mMockLocationManager.bRun = true;
        }
    }

    public void stopMockLocation() {
        if (mMockLocationManager != null) {
            mMockLocationManager.bRun = false;
            mMockLocationManager.stopMockLocation();
        }
    }


    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        Log.i(TAG, "stopWork");
        stopMockLocation();
        stopService();
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        Log.i(TAG, "onServiceKilled");
        stopMockLocation();
    }
}
