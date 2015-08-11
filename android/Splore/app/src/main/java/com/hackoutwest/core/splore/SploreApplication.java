package com.hackoutwest.core.splore;

import android.app.ActionBar;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

/**
 * Created by root on 2015-08-11.
 */
public class SploreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Location", "Application onCreate");


        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            Log.d("Location", "Open for first time");
            // do the thing for the first time
            String uniqueID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("USER_ID", uniqueID);
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time your app loads

            Log.d("USER_ID", "ID in SploreApplcation" + " " + settings.getString("USER_ID", "-1"));
        }
    }
}
