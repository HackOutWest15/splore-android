package com.hackoutwest.core.splore;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.Result;

/**
 * Created by root on 2015-08-11.
 */
public class SploreLocationService extends Service  {

    public static URL url;
    public static String userID;
    public Intent intent;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "Service is created");
        intent = new Intent("com.hackoutwest.broadcast.gps.location_change");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "On Start Command called");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, listener);

        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);
        Notification not = new Notification(R.mipmap.ic_launcher, "Location", System.currentTimeMillis());
        not.setLatestEventInfo(this,"Splore is running", "Splore is helping you to explore the musical world!", pendingIntent);
        startForeground(1337, not);

        return(START_NOT_STICKY);

    }



    public class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {

            intent.putExtra("LONGITUD", location.getLongitude());
            intent.putExtra("LATITUDE", location.getLatitude());
            sendBroadcast(intent);

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
    }
}
