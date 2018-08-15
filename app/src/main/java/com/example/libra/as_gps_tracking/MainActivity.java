package com.example.libra.as_gps_tracking;

import android.Manifest;
import android.content.Intent;
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView _txtvLongitude;
    private TextView _txtvLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _txtvLongitude = (TextView) findViewById(R.id.txtvLongitude);
        _txtvLatitude = (TextView) findViewById(R.id.txtvLatitude);

        Log.d("-Start", "xxx");
        startService( new Intent(this, GPSTracker.class));
        Log.d("-end", "zzz");
    }
}
