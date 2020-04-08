package com.rocedar.base.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.RCUmeng;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.network.unit.NetWorkUtil;
import com.rocedar.base.network.unit.Regix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public abstract class RequestData {

    public interface Method {
        int Post = Request.Method.POST;
        int Put = Request.Method.PUT;
        int Get = Request.Method.GET;
        int Delete = Request.Method.DELETE;
    }

    public interface MethodName {
        String Post = "POST";
        String Put = "PUT";
        String Get = "GET";
        String Delete = "DELETE";
    }

    public static String getMethodName(int method) {
        switch (method) {
            case Method.Get:
                return MethodName.Get;
            case Method.Post:
                return MethodName.Post;
            case Method.Delete:
                return MethodName.Delete;
            case Method.Put:
                return MethodName.Put;
        }
        return MethodName.Get;
    }

    private final static int DefaultRetryTime = 60;

    private static String TAG = "RC网络请求";


    public static IResponseData mIResponseDataLast;

    /**
     * 请求 传入的为数据字典对象
     *
     * @param context
     * @param URL           第三方请求URL 默认是APPURL
     * @param method
     * @param iResponseData
     */
    public static void NetWorkGetData(final Context context, final String URL,
                                      final RCBean bean, final int method,
                                      final IResponseData iResponseData) {
        mIResponseDataLast = null;
        //判断是否有网络
        if (NetWorkUtil.networkAvailable(context).equals("")) {
            if (RCBaseManage.getScreenManger().getActivityListCount() > 0)
                RCToast.Center(context, "网络连接失败");
            RCLog.e(TAG, "网络状态->" + NetWorkUtil.networkAvailable(context));
            iResponseData.getDataErrorListener("网络连接失败", RequestCode.STATUS_CODE_NOT_NETWORK);
            return;
        }
        String url = "";
        if (bean != null) {
            mIResponseDataLast = iResponseData;
            //如果URL最后一位为"/",判断action第一位是否为"/"，如果是去掉以免重复
            if (URL.equals(RCBaseConfig.APP_NETWORK_URL) || URL.equals(RCBaseConfig.APP_PT_NETWORK_URL)) {//是请求自己的应用服务器才需要判断，第三方的不用判断
                if (RCBaseConfig.APP_NETWORK_URL.endsWith("/")) {
                    if (bean.getActionName().startsWith("/")) {
                        bean.setActionName(bean.getActionName().substring(1));
                    }
                } else {
                    if (!bean.getActionName().startsWith("/")) {
                        bean.setActionName("/" + bean.getActionName());
                    }
                }
            }
            RCLog.d(TAG, "调用网络请求，请求的接口为->> " + bean.getActionName());
            if (method == Method.Delete || method == Method.Get || method == Method.Put) {
                Regix<RCBean> regix = new Regix<RCBean>();
                if (URL.equals(RCBaseConfig.APP_NETWORK_URL)) {
                    url = URL + RCUtilEncode.reEnCodeUrlInfo(regix.geturl(bean));
                } else {
                    url = URL + RCUtilEncode.reEnCodeUrlInfo(regix.geturl(bean));
                }
                RCUmeng.umengEvent(context, getMethodName(method) + bean.actionName.replace("/", ""),
                        regix.getdates(bean));
            } else {
                Regix<RCBean> regix = new Regix<RCBean>();
                RCUmeng.umengEvent(context, getMethodName(method) + bean.actionName.replace("/", ""),
                        regix.getdates(bean));
                if (URL.equals(RCBaseConfig.APP_NETWORK_URL))
                    url = URL + bean.getActionName();
                else
                    url = URL + bean.getActionName();
            }
        } else {
            url = URL;
        }
        RCLog.d(TAG, "请求的URL为->>" + url);
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        mIResponseDataLast = null;
                        RCLog.i(TAG,
                                "收到返回的数据：\n"
                                        + "请求的方法为：" + ((bean != null && bean.getActionName() != null) ?
                                        bean.getActionName() : URL)
                                        + "\n" +
                                        "返回的数据为－>" + arg0);
                        JSONObject object;
                        try {
                            object = new JSONObject(arg0);
                            if (RequestUtil.DataProcessing(object, context) ||
                                    (!URL.equals(RCBaseConfig.APP_NETWORK_URL) && !URL.equals(RCBaseConfig.APP_PT_NETWORK_URL))) {
                                iResponseData.getDataSucceedListener(object);
                            } else {
                                RCLog.d(TAG, "请求数据状态错误：" + bean != null ? bean.getActionName() : URL);
                                iResponseData.getDataErrorListener(object.getString("msg"), object.optInt("status"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (method == RequestData.Method.Delete || method == RequestData.Method.Get ||
                        method == RequestData.Method.Put) {
                    return new HashMap<>();
                } else {
                    if (bean != null) {
                        Regix<RCBean> regix = new Regix<>();
                        return regix.getdates(bean);
                    }
                    return new HashMap<>();
                }

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (URL.equals(RCBaseConfig.APP_NETWORK_URL) || URL.equals(RCBaseConfig.APP_PT_NETWORK_URL)) {
                    Regix<RCBean> regix = new Regix<>();
                    return RequestUtil.headInfo(context, regix.getSignCode(bean
                            , getMethodName(method)), bean.getBaseUserId());
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                    return map;
                }
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryTime * 1000,
                0, 1.0f));
        RCBaseManage.getInstance().addToRequestQueue(request,
                bean != null ?
                        bean.getActionName() : url);
    }

    /**
     * APP 接口调用方法
     *
     * @param context
     * @param t             BEAN对象
     * @param method        请求类型
     * @param iResponseData 回调
     */
    public static void NetWorkGetData(final Context context,
                                      final RCBean t, final int method,
                                      final IResponseData iResponseData) {
        if (null != t.getBaseNetUrl() && !t.getBaseNetUrl().equals("")) {
            String url = t.getBaseNetUrl();
            t.setBaseNetUrl(null);
            NetWorkGetData(context, url, t, method, iResponseData);
        } else {
            NetWorkGetData(context, RCBaseConfig.APP_NETWORK_URL, t, method, iResponseData);
        }
    }


    /**
     * 第三方请求调用方法
     *
     * @param context
     * @param URL           第三方请求URL
     * @param method        请求类型
     * @param iResponseData 回调
     */
    public static void NetWorkGetData(final Context context, final String URL, final int method,
                                      final IResponseData iResponseData) {
        NetWorkGetData(context, URL, null, method, iResponseData);
    }

    /**
     * 取消请求
     *
     * @param tag
     */
    public static void CancleNetWork(String tag) {
        RCLog.d("netWork->", "NETWORK->取消发送请求－发送数据-" + tag);
        RCBaseManage.getInstance().cancelPendingRequests(tag);
    }


}
