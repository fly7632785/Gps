package com.jafir.gps;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.amap.api.location.APSService;
import com.xdandroid.hellodaemon.DaemonEnv;

/**
 * created by jafir on 2020-05-20
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startAlarm();
        DaemonEnv.initialize(this, UploadGpsService.class, 3 * 60 * 1000);
    }

    public void startAlarm() {
        /**
         首先获得系统服务
         */
        AlarmManager am = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        //设置闹钟的意图，我这里是去调用一个服务，该服务功能就是获取位置并且上传
        Intent intent = new Intent(this, APSService.class);
        PendingIntent pendSender = PendingIntent.getService(this, 0, intent, 0);
        am.cancel(pendSender);
        //AlarmManager.RTC_WAKEUP 这个参数表示系统会唤醒进程；我设置的间隔时间是10分钟
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 60 * 1000, pendSender);
    }
}
