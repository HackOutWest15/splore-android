package com.hackoutwest.core.splore;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hackoutwest.core.splore.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

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
        Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intiLocationUpdates();
            }
        });


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new SploreWebViewClient());
        mWebView.addJavascriptInterface(new SploreWebInterface(this), "Android");
        mWebView.loadUrl("http://10.47.12.93:3000");

        testLocationService();

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

    private void testLocationService() {
        Intent servIntent = new Intent(this, SploreLocationService.class);
        startService(servIntent);
    }

    private void intiLocationUpdates() {
        SploreLocationService locationService = new SploreLocationService();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationService);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationService);

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
            Intent servIntent = new Intent("com.hackoutwest.core.LONGRUNSERVICE");
            startService(servIntent);
            SploreLocationService locationService = new SploreLocationService();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationService);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationService);
        }
    }

}
