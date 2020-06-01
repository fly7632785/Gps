package com.jafir.gps.mocklocation;

import android.app.Activity;
import android.os.Bundle;

import com.jafir.gps.R;
import com.jafir.mockgps.LocationBean;
import com.jafir.mockgps.LocationWidget;

public class LocationActivity extends Activity {
    public static final String INTENT_KEY_LAT = "intent_key_lat";
    public static final String INTENT_KEY_LNG = "intent_key_lng";
    LocationWidget idLocationWidget;
    LocationBean mLocationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_content_view);
        initMockLocationData();
        initView();
    }

    private void initMockLocationData() {
        double latitude;
        double longitude;
        try {
            latitude = getIntent().getDoubleExtra(INTENT_KEY_LAT, 30.0);
            longitude = getIntent().getDoubleExtra(INTENT_KEY_LNG, 105.0);
        } catch (Exception e) {
            latitude = 30.0;
            longitude = 105;
        }
        mLocationBean = new LocationBean();
        mLocationBean.setLatitude(latitude);
        mLocationBean.setLongitude(longitude);
    }

    private void initView() {
        idLocationWidget = (LocationWidget) findViewById(R.id.id_location_wigdet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        idLocationWidget.setMangerLocationData(mLocationBean.getLatitude(), mLocationBean.getLongitude());
        idLocationWidget.startMockLocation();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        idLocationWidget.refreshData();
    }

    @Override
    protected void onPause() {
        idLocationWidget.removeUpdates();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        idLocationWidget.stopMockLocation();
        super.onDestroy();
    }
}