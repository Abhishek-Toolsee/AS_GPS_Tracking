package com.example.libra.as_gps_tracking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class GPSTracker extends Service {

    private static final String TAG = "AS_GPS_Tracking ";
    private static final int iLOCATION_INTERVAL = 1000;
    private static final float i_LOCATION_DISTANCE = 0;

    protected LocationManager _objLocationManager = null;

    public class LocationListener implements android.location.LocationListener{

        Location _objLastLocation;

        public LocationListener(String sProvider){
            Log.e(TAG, "LocationListener: " + sProvider);
            _objLastLocation = new Location(sProvider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            _objLastLocation.set(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }
    }

    LocationListener[] objLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initLocationManager();
        try{
            _objLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, iLOCATION_INTERVAL, i_LOCATION_DISTANCE,
                    objLocationListeners[1]);
        }
        catch (SecurityException ex){
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
        catch(IllegalArgumentException ex){
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try{
            _objLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, iLOCATION_INTERVAL, i_LOCATION_DISTANCE,
                    objLocationListeners[0]);
        }
        catch (SecurityException ex){
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
        catch(IllegalArgumentException ex){
            Log.d(TAG, "gps provider does not exist, " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if(_objLocationManager != null){
            for(int i = 0; i < objLocationListeners.length; i++){
                try{
                    _objLocationManager.removeUpdates(objLocationListeners[i]);
                }catch (Exception ex){
                    Log.i(TAG, "fail of remove location listeners, ignore", ex);
                }
            }
        }
    }

    private void initLocationManager(){
        Log.e(TAG, "initLocationManager");
        if(_objLocationManager == null){
            _objLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
