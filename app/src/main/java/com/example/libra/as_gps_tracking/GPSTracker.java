package com.example.libra.as_gps_tracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

/**
 * Class for running GPS in background
 */
public class GPSTracker extends Service {

    //region Private declarations

    private static final int iLOCATION_INTERVAL = 5000;
    private static final int iLOCATION_DISTANCE = 5000;
    private LocationListener _objLocationListener;
    private LocationManager _objLocationManager;

    //endregion

    //region Public methods

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        _objLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent objIntent = new Intent(getString(R.string.sIntentFilterLocationUpdate));
                objIntent.putExtra(getString(R.string.sIntentKeyLongitude), location.getLongitude());
                objIntent.putExtra(getString(R.string.sIntentKeyLatitude), location.getLatitude());
                sendBroadcast(objIntent);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent objIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                objIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(objIntent);
            }
        };

        _objLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        _objLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, iLOCATION_INTERVAL, iLOCATION_DISTANCE, _objLocationListener);
    }

    /**
     * Destroy listener when service is destroyed to avoid memory leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(_objLocationManager != null)
            _objLocationManager.removeUpdates(_objLocationListener);
    }

    //endregion
}
