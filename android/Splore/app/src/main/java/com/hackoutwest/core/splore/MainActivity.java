package com.hackoutwest.core.splore;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hackoutwest.core.splore.util.SystemUiHider;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.transform.Result;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);


        buildGoogleApiClient();
        mGoogleApiClient.connect();

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);

        String UserID = settings.getString("USER_ID", "-1");
        Log.d("USER_ID", "ID in MainActvity" + " " + UserID);

        mWebView = (WebView) findViewById(R.id.webview);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double longitud = intent.getDoubleExtra("LONGITUD", 0);
                double latitude = intent.getDoubleExtra("LATITUDE", 0);
                new LocationTask().execute(latitude, longitud);

            }
        };
        registerReceiver(receiver, new IntentFilter("com.hackoutwest.broadcast.gps.location_change"));

        Intent servIntent = new Intent(this, SploreLocationService.class);
        startService(servIntent);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new SploreWebViewClient());
        mWebView.addJavascriptInterface(new SploreWebInterface(this), "Android");
        mWebView.loadUrl("http://10.47.12.93:3000");

    }

    private String locationToJSON(double lat, double lon) {
        return "{'location':{'lat':"+lat+",'long':"+lon+"}}";
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("Location", mLastLocation.getLatitude()+"");
            Log.d("Location", mLastLocation.getLongitude() + "");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class SploreWebInterface {

        Context mContext;

        /** Instantiate the interface and set the context */
        SploreWebInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String getID() {
            SharedPreferences settings = mContext.getSharedPreferences("PREFS_NAME", 0);
            return settings.getString("USER_ID","-1");
        }

        @JavascriptInterface
        public Location getLocation() {
            return mLastLocation;
        }

        @JavascriptInterface
        public void startLocationService() {
            Intent servIntent = new Intent(mContext, SploreLocationService.class);
            startService(servIntent);
        }
    }

    private class LocationTask extends AsyncTask<Double, Void, Result> {

        @Override
        protected Result doInBackground(Double... params) {

            String jsonLocation = locationToJSON(params[0], params[1]);

            try {
                //url = new URL("http://10.47.12.93:3000/update");
                URL url = new URL("http://www.google.com");
                SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
                String userID = settings.getString("USER_ID", "-1");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                StringBuilder result = new StringBuilder();
                result.append(URLEncoder.encode("userID", "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(userID, "UTF-8"));
                result.append("&");
                result.append(URLEncoder.encode("location", "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(jsonLocation, "UTF-8"));

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(result.toString());
                writer.flush();
                writer.close();
                os.close();

                conn.connect();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

}
