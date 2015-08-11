package com.hackoutwest.core.splore;

import android.app.Application;
import android.util.Log;

/**
 * Created by root on 2015-08-11.
 */
public class SploreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Location", "Application onCreate");
    }
}
