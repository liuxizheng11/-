package com.rocedar.lib.base.network.unit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.rocedar.lib.base.unit.RCLog;


public class NetWorkUtil {


    private static String TAG = "RC网络请求";


    /**
     * 获取网络状态
     *
     * @param context
     * @return | "wifi"：WIFI | "cellular"：移动网络 | ""：无网络
     */
    public static String networkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE));
            if (connectivity != null) {
                NetworkInfo.State wifi = connectivity.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState();// WIFI网络连接状态
                NetworkInfo.State mobile = connectivity.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE).getState();// 手机网络连接状态
                if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                    RCLog.d(TAG, "当前是WIFI连接");
                    return "wifi";
                } else if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                    RCLog.d(TAG, "当前是数据网络连接");
                    return "cellular";
                }
                if (connectivity.getActiveNetworkInfo() == null || connectivity.getActiveNetworkInfo().isConnected()) {
                    return "unknown";
                } else {
                    return "";
                }
            } else {
                return "unknown";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isHaveNetWork(Context context) {
        if (networkAvailable(context).equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 获取运营商信息
     *
     * @param context
     * @return | 1：中国移动 | 2：中国联通 | 3：中国电信 | 0：错误 |
     */
    public static String getSimOperatorInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString == null) {
            return "0";
        }
        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            //中国移动
            return "1";
        } else if (operatorString.equals("46001")) {
            //中国联通
            return "2";
        } else if (operatorString.equals("46003")) {
            //中国电信
            return "3";
        }
        //error
        return "0";
    }


}
