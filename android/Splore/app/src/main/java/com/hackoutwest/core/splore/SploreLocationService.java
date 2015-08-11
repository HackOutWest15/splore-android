package com.hackoutwest.core.splore;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by root on 2015-08-11.
 */
public class SploreLocationService extends Service implements LocationListener {


    @Override
    public void onCreate() {
        Log.d("Service", "Service is created");
        super.onCreate();


    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Service", location.getLatitude() + "");

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "On Start Command called");
        return super.onStartCommand(intent, flags, startId);
    }
}
