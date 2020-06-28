package com.jafir.gps;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.google.gson.Gson;
import com.xdandroid.hellodaemon.AbsWorkService;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * created by jafir on 2020-05-20
 */
public class KeepLiveService extends AbsWorkService {
    private static final String TAG = KeepLiveService.class.getSimpleName();
    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;
    public static Disposable sDisposable;

    int shouldCount;
    int actualCount;


    @Override
    public void onCreate() {
        super.onCreate();
        initGps();
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = amapLocation -> {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                Log.d("mapLocation", amapLocation.toString());
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                df.format(date);
                Log.d(TAG, String.format("经度：%s\t纬度：%s\t地址：%s\n%s\n应上传次数%d\n实上传次数%d", amapLocation.getLongitude(), amapLocation.getLatitude(), amapLocation.getAddress(), df.format(date), shouldCount, actualCount));
                upload(amapLocation.getLongitude(), amapLocation.getLatitude());
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    };

    private void upload(double longitude, double latitude) {

        LatLng latLng = ConvertUtil.toWGS84Point(latitude, longitude);

        shouldCount++;
        GpsRequestModel requestModel = new GpsRequestModel();
        requestModel.setImei(DeviceUtil.getDeviceId(getApplicationContext()));
        requestModel.setData(new GpsRequestModel.DataBean(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude)));
        String token = PrefManager.getInstance(getApplicationContext()).getToken();
        RetrofitManager.getInstance()
                .mainService()
                .gps(token, requestModel)
                .compose(ReactivexCompat.singleThreadSchedule())
                .subscribe(result -> {
                    if (result.getCode() == 0) {
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        Long interval = Long.valueOf(result.getData().getReportFrequency());
                        long milliseconds = interval * 60 * 1000;
                        mLocationOption.setInterval(milliseconds);
                        mLocationClient.setLocationOption(mLocationOption);
                        Log.d(TAG, "service upload success:" + new Gson().toJson(result));
                        actualCount++;
                    } else if (result.getCode() == 401) {
                        relogin();
                    }
                }, e -> {
                    if (e instanceof HttpException) {
                        if (((HttpException) e).code() == 401) {
                            relogin();
                        }
                    }
                    Log.e(TAG, "service upload err:" + e.getMessage());
                });
    }

    private void relogin() {
        RetrofitManager.getInstance().mainService().login(new LoginRequest("admin", "123456"))
                .compose(ReactivexCompat.singleThreadSchedule())
                .subscribe(result -> {
                    if (result.getCode() == 0) {
                        String token = result.getData().getToken();
                        PrefManager.getInstance(getApplicationContext()).setToken(token);
                    }
                }, e -> {
                    Log.e("login", "service login err:" + e.getMessage());
                });
    }


    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    private void initGps() {
//初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(5000);

        mLocationClient.setLocationOption(mLocationOption);

        //启动定位
        mLocationClient.startLocation();
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

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        Log.i(TAG, "startWork");
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            Log.i(TAG, "startLocation");
            mLocationClient.startLocation();
        } else if (mLocationClient == null) {
            initGps();
        }
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        Log.i(TAG, "stopWork");
        stopService();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        Log.i(TAG, "onServiceKilled");
    }
}
