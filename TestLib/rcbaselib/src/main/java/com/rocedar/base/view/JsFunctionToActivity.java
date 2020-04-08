//package com.rocedar.base.view;
//
//import android.app.Activity;
//import android.view.View;
//import android.webkit.JavascriptInterface;
//
//import com.rocedar.base.RCHandler;
//import com.rocedar.base.RCTPJump;
//
//
//public class JsFunctionToActivity {
//
//    private RCHandler handle;
//    private Activity context;
//    private View view;
//
//    public JsFunctionToActivity(RCHandler handle, Activity context, View view) {
//        this.handle = handle;
//        this.context = context;
//        this.view = view;
//    }
//
//
//    // 2. 关闭
//    @JavascriptInterface
//    public void closePage() {
//        handle.post(new Runnable() {
//
//            @Override
//            public void run() {
//                context.finish();
//            }
//        });
//    }
//
//    // 3.1 打开协议
//    @JavascriptInterface
//    public void jump(final String url, final String action) {
//        handle.post(new Runnable() {
//
//            @Override
//            public void run() {
//                RCTPJump.ActivityJump(context, url, action);
//            }
//        });
//    }
//
//    // 3.2  打开协议
//    @JavascriptInterface
//    public void jump(final String url) {
//        handle.post(new Runnable() {
//
//            @Override
//            public void run() {
//                RCTPJump.ActivityJump(context, url, UrlConfing.HTMLACTION_OTHER);
//            }
//        });
//    }
//
//
//}
