package com.hackoutwest.core.splore;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hackoutwest.core.splore.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LocationManager mLocationManager;
    private Location mLastLocation;
    private String mLocationProvide;
    private GoogleApiClient mGoogleApiClient;

    private TextView mFullScreenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Location", "onCreate Main");
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
        mGoogleApiClient.connect();

        mFullScreenText = (TextView) findViewById(R.id.fullscreen_content);


    }

    private void initLocation() {
        mLocationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);

    }

    protected synchronized void buildGoogleApiClient() {
        Log.d("Location", "Build google api");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Location", "onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("Location", mLastLocation.getLatitude()+"");
            Log.d("Location", mLastLocation.getLongitude()+"");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
