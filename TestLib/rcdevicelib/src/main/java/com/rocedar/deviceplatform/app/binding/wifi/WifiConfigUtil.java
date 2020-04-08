package com.rocedar.deviceplatform.app.binding.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


/**
 * Created by phj on 2016/11/14.
 */

public class WifiConfigUtil {


    /**
     * 判断是否是wifi链接
     *
     * @param context 上下文
     * @return 是否是wifi链接
     */
    public static boolean isWifiEnabled(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }


    /**
     * 获取Wifi 名称
     *
     * @param context
     * @return Wifi名称
     */
    public static String getConnectWifiName(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : "";
        return wifiId;
    }

//    private static NewDialog newDialog;

    /**
     * 判断是否连接wifi，并提示打开设置
     *
     * @param context
     */
    public static void goWifiSetting(final Context context) {
//        if (!isWifiEnabled(context)) {
//            newDialog = new NewDialog(context, new String[]{"您未连接WIFI，是否打开设置？", "", ""}, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openWifiSetting(context);
//                    newDialog.dismiss();
//                }
//            }, null, false);
//            newDialog.show();
//        }
    }


    /**
     * 打开wifi设置
     *
     * @param context
     */
    public static void openWifiSetting(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }


}
