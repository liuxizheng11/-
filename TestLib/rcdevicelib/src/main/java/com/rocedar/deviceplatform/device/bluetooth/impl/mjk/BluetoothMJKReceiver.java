package com.rocedar.deviceplatform.device.bluetooth.impl.mjk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class BluetoothMJKReceiver extends BroadcastReceiver {

    public IBroadcastData iBroadcastData;



    public BluetoothMJKReceiver(IBroadcastData iBroadcastData) {
        this.iBroadcastData = iBroadcastData;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String data = intent.getStringExtra("signal");
        if (iBroadcastData != null && !TextUtils.isEmpty(data)) {
            iBroadcastData.getData(data);
        }
        String error = intent.getStringExtra("error");
        int status = intent.getIntExtra("status", 0);
        if (!TextUtils.isEmpty(error) && iBroadcastData != null && !error.equals("")) {
            iBroadcastData.error(status, error);
        }

    }

    public interface IBroadcastData {
        void getData(String data);//步数传过来

        void error(int status, String msg);//连接错误
    }

}
