package com.rocedar.lib.base.manage;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.rocedar.lib.base.config.Config;
import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.network.IRCDataErrorLister;
import com.rocedar.lib.base.network.NetworkMethod;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.crash.AppUncaughtExceptionHandler;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/16 下午3:35
 * 版本：V1.1.00
 * 描述：瑰柏SDK-初始化
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCSDKManage {

    /* 初始化logger的tag*/
    private static String TAG = "ROCEDAR_SDK";

    private static RCSDKManage ourInstance;
    private Context mContext;

    //APPKey
    private String APP_SECRET = "";
    //APP标识
    private String APP_TAG = "";
    /**
     * 网络请求异常，需要全局处理的错误异常监听
     */
    private IRCDataErrorLister requestDataErrorLister;

    /* 是否支持滑动返回*/
    public static boolean hasSwipeBack = false;


    public static RCSDKManage getInstance() {
        if (ourInstance == null)
            ourInstance = new RCSDKManage();
        return ourInstance;
    }


    public RCSDKManage() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public Context getContext() {
        return mContext;
    }

    public RCSDKManage init(Context context) {
        init(context, null);
        return this;
    }

    public RCSDKManage init(Context context, IRCDataErrorLister requestDataErrorLister) {
        this.mContext = context.getApplicationContext();
        this.requestDataErrorLister = requestDataErrorLister;
        RCBaseConfig.setNetWorkConfig(NetworkMethod.SDK, Config.P_NETWORK_URL);
        // 必须在 Application 的 onCreate 方法中执行来初始化滑动返回
        BGASwipeBackHelper.init((Application) context.getApplicationContext(), null);
        return this;
    }

    public RCSDKManage setRequestDataErrorLister(IRCDataErrorLister requestDataErrorLister) {
        this.requestDataErrorLister = requestDataErrorLister;
        return this;
    }

    public RCSDKManage setAppKey(String appSecret) {
        this.APP_SECRET = appSecret;
        return this;
    }

    public RCSDKManage setAppTag(String appTag) {
        this.APP_TAG = appTag;
        return this;
    }

    public String getAppKey() {
        if (APP_SECRET == null || APP_SECRET.equals(""))
            APP_SECRET = RCAndroid.getMetaData(mContext, "ROCEDAR_APPSECRET");
        if (APP_SECRET == null || APP_SECRET.equals("")) {
            RCLog.e("请在AndroidManifest.xml中设置ROCEDAR_APPSECRET");
            Log.e("rocedar", "请在AndroidManifest.xml中设置ROCEDAR_APPSECRET");
        }
        return APP_SECRET;
    }


    public String getAPPTAG() {
        if (APP_TAG == null || APP_TAG.equals(""))
            APP_TAG = RCAndroid.getMetaData(mContext, "ROCEDAR_APPID");
        if (APP_TAG == null || APP_TAG.equals("")) {
            RCLog.e("");
            Log.e("rocedar", "请在AndroidManifest.xml中设置ROCEDAR_APPID");
        }
        return APP_TAG;
    }

    public boolean setToken(String token) {
        return RCSPUserInfo.setLastSDKToken(token);
    }


    public IRCDataErrorLister getRequestDataErrorLister() {
        return requestDataErrorLister;
    }

    /**
     * 设置平台网络请求类型
     *
     * @param isTestMethod true为测试环境
     */
    public static void setNetworkMethod(boolean isTestMethod) {
        if (isTestMethod) {
            Config.BASE_URL = "develop.rocedar.com:8005";
            Config.P_NETWORK_URL = "http://" + Config.BASE_URL;
            RCBaseConfig.setNetWorkConfig(NetworkMethod.SDK, Config.P_NETWORK_URL);
        }
    }

    /**
     * 设置是否捕捉异常
     *
     * @param isCrash
     */
    public static void setCrash(boolean isCrash) {
        if (isCrash) {
            // 捕捉异常
            AppUncaughtExceptionHandler crashHandler = AppUncaughtExceptionHandler.getInstance();
            crashHandler.init(RCSDKManage.getInstance().mContext);
        }
    }


    public static void setDebug(boolean mIsDebug) {
        setNetworkMethod(mIsDebug);
        setCrash(mIsDebug);
    }

    public static boolean getDebug() {
        return Config.debug;
    }

    public static void setHasSwipeBack(boolean hasSwipeBack) {
        RCSDKManage.hasSwipeBack = hasSwipeBack;
    }

    /*--------Volley 相关－－－－－－－S*/


    /**
     * volley 网络请求队列
     */
    private RequestQueue mRequestQueue;

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified then
     * it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "FZ" : tag);
        getRequestQueue().add(req);
    }


    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /*--------Volley 相关－－－－－－－E*/


    /**
     * activity管理类对象
     */
    private static ScreenManage screenManage;

    /**
     * @return activity 管理者对象
     */
    public static ScreenManage getScreenManger() {
        if (screenManage == null) {
            screenManage = ScreenManage.getScreenManger();
        }
        return screenManage;
    }

}
