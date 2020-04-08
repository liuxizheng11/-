package com.rocedar.base.webview;

import android.app.Activity;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.rocedar.base.RCHandler;


public class JsFunctionToActivity {

    private RCHandler handle;
    private Activity context;
    private View view;

    public JsFunctionToActivity(RCHandler handle, Activity context, View view) {
        this.handle = handle;
        this.context = context;
        this.view = view;
    }


    // 2. 关闭
    @JavascriptInterface
    public void closePage() {
        handle.post(new Runnable() {

            @Override
            public void run() {
                context.finish();
            }
        });
    }



}
