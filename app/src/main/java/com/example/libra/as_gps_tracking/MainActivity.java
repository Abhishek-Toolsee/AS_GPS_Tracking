package com.example.libra.as_gps_tracking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //region Private declarations

    private TextView _txtvLongitude;
    private TextView _txtvLatitude;
    private TextView _txtvInfo;
    private Button _btnStartService;
    private Button _btnStopService;
    private BroadcastReceiver _objBroadcastReceiver;

    //endregion

    //region Public methods

    /**
     * Handles user's response when permissions are needed.
     * @param requestCode   Request code to identify the requested permission.
     * @param permissions   Permissions needed.
     * @param grantResults  Results of the permission (Granted or Not Granted).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enableButtons();
            }
            else{
                checkRuntimePermissions();
            }
        }
    }

    //endregion

    //region Protected methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _txtvLongitude = (TextView) findViewById(R.id.txtvLongitude);
        _txtvLatitude = (TextView) findViewById(R.id.txtvLatitude);
        _txtvInfo = (TextView) findViewById(R.id.txtvInfo);
        _btnStartService = (Button) findViewById(R.id.btnStartService);
        _btnStopService = (Button) findViewById(R.id.btnStopService);

        if(!checkRuntimePermissions())
            enableButtons();
    }


    /**
     * Register the broadcaster correctly.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(_objBroadcastReceiver == null){
            _objBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    _txtvLongitude.setText(String.format("%s%f", getString(R.string.sTextviewLongitude),
                            intent.getExtras().get(getString(R.string.sIntentKeyLongitude))));

                    _txtvLatitude.setText(String.format("%s%f", getString(R.string.sTextviewLatitude),
                            intent.getExtras().get(getString(R.string.sIntentKeyLatitude))));
                }
            };
        }

        registerReceiver(_objBroadcastReceiver, new IntentFilter(getString(R.string.sIntentFilterLocationUpdate)));
    }

    /**
     * Unregister broadcaster
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(_objBroadcastReceiver != null)
            unregisterReceiver(_objBroadcastReceiver);
    }

    //endregion

    //region Private methods

    /**
     * Check the permissions during runtime when needed.
     * Check is done for SDK above 23.
     * @return  true if granted, false if not granted
     */
    private boolean checkRuntimePermissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    /**
     * Enables the buttons if permissions are granted.
     */
    private void enableButtons() {
        _btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(getApplicationContext(), GPSTracker.class);
                startService(objIntent);
            }
        });

        _btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(getApplicationContext(), GPSTracker.class);
                stopService(objIntent);
            }
        });
    }

    //endregion
}
