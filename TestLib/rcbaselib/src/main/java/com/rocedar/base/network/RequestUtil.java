package com.rocedar.base.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.rocedar.base.RCAndroid;
import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCDeveloperConfig;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.network.unit.NetWorkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phj on 2016/12/17.
 * <p>
 * 网络请求相关的工具类
 * <p>
 * v1.0.0
 */

public abstract class RequestUtil {


    private static String TAG = "RC网络请求";


    private static Map<String, Object> headTemp = new HashMap<>();

    public static void setHeadTemp(String key, Object vaule) {
        RequestUtil.headTemp.put(key, vaule);
    }

    /**
     * 网络请求头
     *
     * @param context
     * @param sign    签名计算后到KEY
     * @param userId  用户ID
     * @return Map<String, String>
     */
    public static Map<String, String> headInfo(final Context context, final String sign, String userId) {
        String channel = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("app", RCBaseConfig.APPTAG);//app标识
        headers.put("sign", sign);//请求签名
        headers.put("uid", userId);//用户ID
        headers.put("Imei", RCAndroid.getImei(context));//设备IMEI号
        headers.put("os", "1");//操作系统类型
        headers.put("os-name", Build.MODEL.replace(" ", ""));//操作系统名称
        headers.put("os-version", Build.VERSION.RELEASE.replace(" ", ""));//操作系统版本
        headers.put("p-version", RCBaseConfig.version);//操作系统版本
        headers.put("app-version", RCAndroid.getVerNumber(context) + "");//应用版本号，如：2400
        headers.put("network", NetWorkUtil.networkAvailable(context));//网络环境
        headers.put("corporation",
                NetWorkUtil.getSimOperatorInfo(context));//网络运营商，如：中国联通
        headers.put("channel", channel);//渠道
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        if (RCBaseConfig.APPTAG.equals(RCBaseConfig.APPTAG_N3)) {
            if (headTemp.containsKey("cid"))
                headers.put("cid", headTemp.get("cid").toString());
            if (headTemp.containsKey("uid"))
                headers.put("uid", headTemp.get("uid").toString());
            headTemp.clear();
        }
        return headers;
    }


    /**
     * 网络请求结果数据拦截
     *
     * @param object  数据
     * @param context
     * @return
     */
    public static boolean DataProcessing(JSONObject object,
                                         final Context context) {
        if (object.has("status")) {
            try {
                switch (object.getInt("status")) {
                    case RequestCode.STATUS_CODE_OK:
                        RCLog.i(TAG,
                                "数据请求成功：" + object.toString());
                        return true;
                    case RequestCode.STATUS_CODE_Operating_limit:
                        return false;
                    case RequestCode.STATUS_CODE_LOGIN_OUT:
                    case RequestCode.STATUS_CODE_LOGIN_ERROR:
                    case RequestCode.STATUS_CODE_LOGIN_OUT_FORCE:
                        if (RCBaseManage.getInstance().getRequestDataErrorLister() != null) {
                            RCBaseManage.getInstance().getRequestDataErrorLister().error(
                                    object.getInt("status"), object.optString("msg")
                            );
                        }
                        return false;
                    default:
                        if (RCDeveloperConfig.isDebug)
                            RCToast.Center(context, object.optInt("status") + "->" + object.optString("msg"), false);
                        else {
                            RCToast.Center(context, object.optString("msg"), false);
                        }
                        return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
