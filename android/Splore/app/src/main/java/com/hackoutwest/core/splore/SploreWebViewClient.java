package com.hackoutwest.core.splore;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by root on 2015-08-11.
 */
public class SploreWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        return false;
    }
}
