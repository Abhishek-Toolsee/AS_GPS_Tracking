package com.example.libra.as_gps_tracking;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

public class GPSTracker extends Service implements LocationListener {

    private final int iUPDATE_LOCATION_TIME = 5000;
    private final int iUPDATE_LOCATION_DISTANCE = 1;
    private final String sMSG_GPS_NOT_ENABLED = "GPS not enabled.";
    private final String sMSG_NETWORK_NOT_ENABLED = "Network not enabled.";
    private final Context _objContext;

    private boolean _isGPSEnabled = false;
    private boolean _isNetworkEnabled = false;
    private boolean _isLocationAvailable = false;

    private Location _objLocation;
    protected LocationManager _objLocationManager;

    public GPSTracker(Context objContext) {
        this._objContext = objContext;
    }

    public Location getLocation() {

        _objLocation = null;
        _objLocationManager = (LocationManager) _objContext.getSystemService(LOCATION_SERVICE);
        _isGPSEnabled = _objLocationManager.isProviderEnabled(_objLocationManager.GPS_PROVIDER);
        _isNetworkEnabled = _objLocationManager.isProviderEnabled(_objLocationManager.NETWORK_PROVIDER);

        try {
            if (ContextCompat.checkSelfPermission(_objContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(_objContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (_isGPSEnabled){
                    if (_objLocation == null) {
                        _objLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, iUPDATE_LOCATION_TIME, iUPDATE_LOCATION_DISTANCE, this);
                        if (_objLocationManager != null) {
                            _objLocation = _objLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

            /* if location is not found from GPS then it will be found from network */
            if (_objLocation == null) {
                if (_isNetworkEnabled){
                    _objLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, iUPDATE_LOCATION_TIME, iUPDATE_LOCATION_DISTANCE, this);
                    if (_objLocationManager != null) {
                        _objLocation = _objLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }
        } finally {

        }
        return _objLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
