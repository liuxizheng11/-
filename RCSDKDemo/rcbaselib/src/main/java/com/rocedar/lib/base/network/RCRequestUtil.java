package com.rocedar.lib.base.network;

import android.content.Context;
import android.os.Build;

import com.rocedar.lib.base.config.Config;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.network.unit.NetWorkUtil;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.unit.RCUtilEncode;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by phj on 2016/12/17.
 * <p>
 * 网络请求相关的工具类
 * <p>
 * v1.0.0
 */

public abstract class RCRequestUtil {

    private static List<Integer> interceptorCodeConfig = new ArrayList<>();

    public static void setCodeConfig(int errorCode) {
        RCRequestUtil.interceptorCodeConfig.add(errorCode);
    }

    public static void setCodeConfig(int... errorCode) {
        for (int e : errorCode) {
            RCRequestUtil.interceptorCodeConfig.add(e);
        }
    }

    private static String TAG = "RC网络请求";


    private static Map<String, Object> headTemp = new HashMap<>();

    public static void setHeadTemp(String key, Object vaule) {
        RCRequestUtil.headTemp.put(key, vaule);
    }

    /**
     * 网络请求头
     *
     * @param context
     * @param sign    签名计算后到KEY
     * @return
     */
    public static Map<String, String> headInfoApp(final Context context, final String sign
            , Map<String, String> headers) {
        if (headers == null)
            headers = new HashMap<>();
        headers.put("app", RCSDKManage.getInstance().getAPPTAG());//app标识
        headers.put("sign", sign);//请求签名
        headers.put("Imei", RCAndroid.getImei(context));//设备IMEI号
        headers.put("os", "1");//操作系统类型
        headers.put("os-name", Build.MODEL.replace(" ", ""));//操作系统名称
        headers.put("os-version", Build.VERSION.RELEASE.replace(" ", ""));//操作系统版本
        headers.put("p-version", Config.p_version);//操作系统版本
        headers.put("app-version", RCAndroid.getVerNumber(context) + "");//应用版本号，如：2400
        headers.put("network", NetWorkUtil.networkAvailable(context));//网络环境
        headers.put("corporation", NetWorkUtil.getSimOperatorInfo(context));//网络运营商，如：中国联通
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        for (String key : headTemp.keySet()) {
            headers.put(key, headTemp.get(key).toString());
        }
        headTemp.clear();
        return headers;
    }


    public static Map<String, String> headInfoPlatform(final Context context, final String sign) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("p-app-id", RCSDKManage.getInstance().getAPPTAG());//app标识
        headers.put("p-app-package", RCUtilEncode.getMD5StrLower32(context.getPackageName()));//app标识
        headers.put("p-request-method", Config.NETWORK_METHOD.name());//请求签名
        headers.put("p-request-secret", getSDKSecret());//SDK请求校验码
        headers.put("p-version", Config.p_version);//平台版本号
        headers.put("p-sign", sign);//请求签名
        headers.put("p-imei", RCAndroid.getImei(context));//设备IMEI号
        headers.put("p-os", "1");//操作系统类型
        headers.put("p-os-name", Build.MODEL.replace(" ", ""));//操作系统名称
        headers.put("p-os-version", Build.VERSION.RELEASE.replace(" ", ""));//操作系统版本
        headers.put("app-version", RCAndroid.getVerNumber(context) + "");//应用版本号，如：2400
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        for (String key : headTemp.keySet()) {
            headers.put(key, headTemp.get(key).toString());
        }
        headTemp.clear();
        return headers;
    }


    public static final int OK = 0;
    public static final int NO_STATUS = -1;
    public static final int OTHER = -2;
    public static final int INTERCEPTOR = -3;
    public static final int LOGIN_ERROR = -9;


    /**
     * 网络请求结果数据拦截
     *
     * @param object  数据
     * @param context
     * @return
     */
    public static int DataProcessing(JSONObject object,
                                     final Context context) {
        if (object.has("status")) {
            try {
                switch (object.getInt("status")) {
                    case IRequestCode.STATUS_CODE_OK:
                        RCLog.i(TAG,
                                "数据请求成功：" + object.toString());
                        return OK;
                    case IRequestCode.STATUS_CODE_Operating_limit:
                        return OTHER;
                    case IRequestCode.STATUS_CODE_TOKEN_OVERDUE:
                    case IRequestCode.STATUS_CODE_TOKEN_INVALID:
                        doErrorListener(context, IRCRequestCode.STATUS_APP_CODE_TOKEN_OVERDUE
                                , object.optString("msg"));
                        return LOGIN_ERROR;
                    default:
                        for (int i = 0; i < interceptorCodeConfig.size(); i++) {
                            if (interceptorCodeConfig.get(i) == object.getInt("status")) {
                                if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                                    RCSDKManage.getInstance().getRequestDataErrorLister().error(context,
                                            object.optInt("status"), object.optString("msg"));
                                }
                                return INTERCEPTOR;
                            }
                        }
                        if (Config.debug)
                            RCToast.Center(context, object.optInt("status")
                                    + "->" + object.optString("msg"), false);
                        else {
                            RCToast.Center(context, object.optString("msg"), false);
                        }
                        return OTHER;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return NO_STATUS;
    }


    private static String lastErrorContextName = "";
    private static long lastErrorTime = 0;


    public static void doErrorListener(Context context, int status, String msg) {
        if (!lastErrorContextName.equals(context.getClass().getName()) ||
                new Date().getTime() - lastErrorTime > 10 * 1000l) {
            lastErrorContextName = context.getClass().getName();
            lastErrorTime = new Date().getTime();
            if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                RCSDKManage.getInstance().getRequestDataErrorLister().error(context,
                        status, msg);
            }
        }
    }

    /**
     * MD5(APP_ID  + P_TOKEN + SDK_SECRET +  CURRENT_DATE)
     */
    private static String getSDKSecret() {
        return RCUtilEncode.getMD5StrLower32(
                RCSDKManage.getInstance().getAPPTAG() + RCSPUserInfo.getLastSDKToken()
                        + Config.SDKSECRET + RCDateUtil.getFormatNow("yyyyMMdd"));
    }


}
