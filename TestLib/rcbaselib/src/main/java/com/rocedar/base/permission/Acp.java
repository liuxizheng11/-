package com.rocedar.base.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hupei on 2016/4/26.
 */
class Acp {


    private static Acp mInstance;
    private AcpManager mAcpManager;

    public static Acp getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Acp(context);
        }
        return mInstance;
    }

    private Acp(Context context) {
        mAcpManager = new AcpManager(context.getApplicationContext());
    }

    public void request(AcpOptions options, AcpListener acpListener) {
        if (options == null) new RuntimeException("AcpOptions is null...");
        if (acpListener == null) new RuntimeException("AcpListener is null...");
        mAcpManager.request(options, acpListener);
    }

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mAcpManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAcpManager.onActivityResult(requestCode, resultCode, data);
    }

    void requestPermissions(Activity activity) {
        mAcpManager.requestPermissions(activity);
    }
}
