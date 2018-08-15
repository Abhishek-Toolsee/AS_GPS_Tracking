package com.example.libra.as_gps_tracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //region Private declarations

    private LocationManager _objLocationManager;
    private LocationListener _objLocationListener;
    private TextView _txtvLongitude;
    private TextView _txtvLatitude;
    private Location _objLocation;
    private GPSTracker _objGPSTracker;

    //endregion

    //region Protected methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _txtvLongitude = (TextView) findViewById(R.id.txtvLongitude);
        _txtvLatitude = (TextView) findViewById(R.id.txtvLatitude);
        _objGPSTracker = new GPSTracker(getApplicationContext());
        _objLocation = _objGPSTracker.getLocation();

        _objLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        _objLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                try {
                    if (location == null)
                        return;

                    Log.d("Location: ", location.toString());
                    _txtvLongitude.setText(String.format("%s%f", getString(R.string.sLongitude), location.getLongitude()));
                    _txtvLatitude.setText(String.format("%s%f", getString(R.string.sLatitude), location.getLatitude()));

                } catch (Exception ex) {

                }
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
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            _objLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _objLocationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                _objLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _objLocationListener);
            }
        }
    }

    //endregion
}
