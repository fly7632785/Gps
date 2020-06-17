package com.jafir.gps.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * created by jafir on 2020-05-20
 */
public class PrefManager {

    private static volatile PrefManager mInstance;
    SharedPreferences sharedPreferences;


    private PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences("jafir", Context.MODE_PRIVATE);
    }

    public static PrefManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PrefManager.class) {
                if (mInstance == null) {
                    mInstance = new PrefManager(context);
                }
            }
        }
        return mInstance;
    }


    public boolean isFirst() {
        return sharedPreferences.getBoolean("isFirst", true);
    }

    public void setFirst() {
        sharedPreferences.edit().putBoolean("isFirst", false).apply();
    }

    public String userId() {
        return sharedPreferences.getString("userId", "");
    }

    public void setUserId(String userId) {
        sharedPreferences.edit().putString("userId", userId).apply();
    }

    public void setToken(String valueOf) {
        sharedPreferences.edit().putString("token", valueOf).apply();
    }
    public String getToken() {
        return sharedPreferences.getString("token", "");
    }
}
