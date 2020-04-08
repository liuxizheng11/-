package com.rocedar.base;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.Logger;
import com.rocedar.base.network.RequestDataErrorLister;


/**
 * Created by phj on 2016/12/16.
 * <p>
 * <p>
 * LIB基础类，请在启动页面或Application中调用init方法
 */
public class RCBaseManage {

    //公网网络请求地址
    public String url_app = "http://www.cubehealthy.com";

    //内网地址
//    public static String url_app = "http://192.168.18.21";


    /* 初始化logger的tag*/
    private static String TAG = "GuiBai";

    private static RCBaseManage ourInstance;

    public static RCBaseManage getInstance() {
        if (ourInstance == null)
            ourInstance = new RCBaseManage();
        return ourInstance;
    }

    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public RCBaseManage init(Context context) {
        this.mContext = context.getApplicationContext();
        RCUmeng.initialize(context);
        Logger.init(TAG);
        return this;
    }

    /**
     * 网络请求异常，需要全局处理的错误异常监听
     */
    public RequestDataErrorLister requestDataErrorLister;

    public void setRequestDataErrorLister(RequestDataErrorLister requestDataErrorLister) {
        this.requestDataErrorLister = requestDataErrorLister;
    }

    public RequestDataErrorLister getRequestDataErrorLister() {
        return requestDataErrorLister;
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
        RCLog.d("Adding request to queue: ", req.getUrl());
        RCLog.d("Adding tag: ", tag);
        getRequestQueue().add(req);
    }


    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        RCLog.d("cancel tag: ", tag.toString());
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /*--------Volley 相关－－－－－－－E*/

}
