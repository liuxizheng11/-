package com.rocedar.sdk.shop.app;

import android.app.Activity;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.rocedar.lib.base.unit.RCHandler;


public class JsToActivity {

    private RCHandler handle;
    private Activity webViewActivity;
    private View view;

    public JsToActivity(RCHandler handle, Activity context, View view) {
        this.handle = handle;
        this.webViewActivity = context;
        this.view = view;
    }

    // 1. 关闭
    @JavascriptInterface
    public void closePage() {
        handle.post(new Runnable() {

            @Override
            public void run() {
                webViewActivity.finish();
            }
        });
    }




}
