package com.rocedar.lib.base.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.network.dto.NetworkGetDataDTO;
import com.rocedar.lib.base.network.unit.NetWorkUtil;
import com.rocedar.lib.base.network.unit.Regix;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCUtilEncode;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/21 下午5:12
 * 版本：V1.0.00
 * 描述：瑰柏SDK-网络请求
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCRequestNetwork {

    private static String TAG = "RC网络请求";


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


    /**
     * 第三方请求调用方法
     *
     * @param context
     * @param URL           第三方请求URL
     * @param method        请求类型
     * @param iResponseData 回调
     */
    public static void thirdRequest(final Context context, final String URL, final int method,
                                    final IResponseData iResponseData) {
        sendRequestThrid(context, URL, method, iResponseData, null);
    }

    /**
     * 第三方请求调用方法
     *
     * @param context
     * @param URL           第三方请求URL
     * @param method        请求类型
     * @param iResponseData 回调
     */
    public static void thirdRequest(final Context context, final String URL, final int method,
                                    final IResponseData iResponseData, Map<String, String> headInfo) {
        sendRequestThrid(context, URL, method, iResponseData, headInfo);
    }


    /*** 接口调用方法
     *
     * @param context
     * @param t             BEAN对象
     * @param method        请求类型
     * @param iResponseData 回调
     */
    public static void NetWorkGetData(final Context context, final RCBean t, final int method,
                                      final IResponseData iResponseData) {
        NetworkGetDataDTO dataDTO = getNetworkDTO(context, t, method, iResponseData, null);
        sendRequest(context, dataDTO, false);
    }

    /*** APP 接口调用方法
     *
     * @param context
     * @param t             BEAN对象
     * @param method        请求类型
     * @param iResponseData 回调
     */
    public static void NetWorkGetData(final Context context, final RCBean t, final int method,
                                      final IResponseData iResponseData, Map<String, String> headInfo) {
        NetworkGetDataDTO dataDTO = getNetworkDTO(context, t, method, iResponseData, headInfo);
        sendRequest(context, dataDTO, false);
    }


    /**
     * 取消请求
     *
     * @param tag
     */
    public static void CancleNetWork(String tag) {
        RCLog.d("netWork->", "NETWORK->取消发送请求－发送数据-" + tag);
        RCSDKManage.getInstance().cancelPendingRequests(tag);
    }


    private static NetworkGetDataDTO getNetworkDTO(
            Context context, RCBean bean, int method,
            IResponseData iResponseData, Map<String, String> headInfo) {
        NetworkGetDataDTO dataDTO = new NetworkGetDataDTO();
        dataDTO.setBean(bean);
        dataDTO.setContextName(context.getClass().getName());
        dataDTO.setiResponseData(iResponseData);
        dataDTO.setMethod(method);
        dataDTO.setHeadInfo(headInfo);
        return dataDTO;
    }


    private static String getMethodName(int method) {
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


    //存储没有请求失败的网络请求参数
    private static List<NetworkGetDataDTO> networkGetDataDTOList = new ArrayList<>();

    /**
     * 发送网络请求
     *
     * @param context
     * @param networkGetDataDTO
     * @param isRefresh
     */
    private static void sendRequest(final Context context, final NetworkGetDataDTO networkGetDataDTO,
                                    final boolean isRefresh) {
        //判断是否有网络
        if (NetWorkUtil.networkAvailable(context).equals("")) {
            RCLog.e(TAG, "网络状态->" + NetWorkUtil.networkAvailable(context));
            networkGetDataDTO.getiResponseData().
                    getDataErrorListener("网络连接失败", IRequestCode.STATUS_CODE_NOT_NETWORK);
            return;
        }
        //如果是平台的请求，并且没有token
        if (networkGetDataDTO.getBean() != null && networkGetDataDTO.getBean().networkMethod == NetworkMethod.SDK) {
            if (RCSPUserInfo.getLastSDKToken().equals("") && networkGetDataDTO.getBean().isCheckToken()) {
                //通知接听者没有登陆
                RCRequestUtil.doErrorListener(context, IRequestCode.STATUS_CODE_LOGIN_OUT, IRequestCode.STATUS_MSG_LOGIN_OUT);
                if (!isRefresh)
                    networkGetDataDTOList.add(networkGetDataDTO);
                networkGetDataDTO.getiResponseData().
                        getDataErrorListener(IRequestCode.STATUS_MSG_LOGIN_OUT, IRequestCode.STATUS_CODE_LOGIN_OUT);
                changeErrorInfoLayout(true);
                return;
            }
        }
        String url = "";
        if (networkGetDataDTO.getBean() != null) {
            //如果URL最后一位为"/",判断action第一位是否为"/"，如果是去掉以免重复
            if (networkGetDataDTO.getBean().getBaseNetUrl().endsWith("/")) {
                if (networkGetDataDTO.getBean().getActionName().startsWith("/")) {
                    networkGetDataDTO.getBean().setActionName(
                            networkGetDataDTO.getBean().getActionName().substring(1));
                }
            } else {
                if (!networkGetDataDTO.getBean().getActionName().startsWith("/")) {
                    networkGetDataDTO.getBean().setActionName(
                            "/" + networkGetDataDTO.getBean().getActionName());
                }
            }

            if (networkGetDataDTO.getMethod() == Method.Delete ||
                    networkGetDataDTO.getMethod() == Method.Get ||
                    networkGetDataDTO.getMethod() == Method.Put) {
                Regix<RCBean> regix = new Regix<RCBean>();
                url = networkGetDataDTO.getBean().getBaseNetUrl() +
                        RCUtilEncode.reEnCodeUrlInfo(regix.geturl(networkGetDataDTO.getBean()));
            } else {
                url = networkGetDataDTO.getBean().getBaseNetUrl() + networkGetDataDTO.getBean().getActionName();
            }
        } else {
            RCLog.e(TAG, "请求的信息为空");
            return;
        }
        url = url.replaceAll("#", "%23");
        RCLog.d(TAG, "请求的URL为->>" + url);
        StringRequest request = new StringRequest(networkGetDataDTO.getMethod(), url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        RCLog.i(TAG, "" +
                                "收到返回的数据：\n" +
                                "请求的方法为：" + networkGetDataDTO.getBean().getActionName() + "\n" +
                                "返回的数据为－>" + arg0);
                        JSONObject object;
                        try {
                            object = new JSONObject(arg0);
                            if (object.has("result") && object.get("result").toString().equals("null")) {
                                object.put("result", new JSONObject());
                            }
                            if (RCRequestUtil.DataProcessing(object, context) == RCRequestUtil.OK) {
                                networkGetDataDTO.getiResponseData().getDataSucceedListener(object);
                                changeErrorInfoLayout(false);
                                if (isRefresh) {
                                    networkGetDataDTOList.remove(networkGetDataDTO);
                                    if (checkAllGetData(context)) {
                                        showHandler(false);
                                    }
                                }
                            } else if (RCRequestUtil.DataProcessing(object, context) == RCRequestUtil.LOGIN_ERROR) {
                                //如果收到error的消息处理
                                if (object.getInt("status") == IRequestCode.STATUS_CODE_TOKEN_INVALID) {
                                    if (object.has("result"))
                                        networkGetDataDTO.getiResponseData().getDataSucceedListener(object);
                                } else {
                                    RCSPUserInfo.setLastSDKToken("");
                                    if (!isRefresh)
                                        networkGetDataDTOList.add(networkGetDataDTO);
                                    networkGetDataDTO.getiResponseData().getDataErrorListener(
                                            object.getString("msg"), object.optInt("status"));
                                    changeErrorInfoLayout(true);
                                }
                                if (isRefresh) {
                                    showHandler(false);
                                }
                            } else {
                                networkGetDataDTO.getiResponseData().getDataErrorListener(
                                        object.getString("msg"), object.optInt("status"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (networkGetDataDTO.getMethod() == RCRequestNetwork.Method.Delete ||
                        networkGetDataDTO.getMethod() == RCRequestNetwork.Method.Get ||
                        networkGetDataDTO.getMethod() == RCRequestNetwork.Method.Put) {
                    return new HashMap<>();
                } else {
                    if (networkGetDataDTO.getBean() != null) {
                        Regix<RCBean> regix = new Regix<>();
                        return regix.getdates(networkGetDataDTO.getBean());
                    }
                    return new HashMap<>();
                }

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (networkGetDataDTO.getBean() != null) {
                    Regix<RCBean> regix = new Regix<>();
                    if (networkGetDataDTO.getBean().networkMethod == NetworkMethod.SDK) {
                        return RCRequestUtil.headInfoPlatform(context, regix.getSignCode(networkGetDataDTO.getBean()
                                , getMethodName(networkGetDataDTO.getMethod())));
                    } else {
                        return RCRequestUtil.headInfoApp(context, regix.getSignCode(networkGetDataDTO.getBean()
                                , getMethodName(networkGetDataDTO.getMethod())), networkGetDataDTO.getHeadInfo());
                    }
                }
                return new HashMap<>();

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryTime * 1000,
                0, 1.0f));
        RCSDKManage.getInstance().addToRequestQueue(request, networkGetDataDTO.getBean().getActionName());
    }


    /**
     * 请求第三方链接
     *
     * @param context
     * @param url
     */
    private static void sendRequestThrid(final Context context, final String url, final int method,
                                         final IResponseData iResponseData, final Map<String, String> headMap) {
        //判断是否有网络
        if (NetWorkUtil.networkAvailable(context).equals("")) {
            RCLog.e(TAG, "网络状态->" + NetWorkUtil.networkAvailable(context));
            iResponseData.getDataErrorListener("网络连接失败", IRequestCode.STATUS_CODE_NOT_NETWORK);
            return;
        }
        RCLog.d(TAG, "请求的URL为(第三方URL)->>" + url);
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        RCLog.i(TAG, "收到返回的数据(第三方URL)：" + url + "\n返回的数据为－>" + arg0);
                        JSONObject object;
                        try {
                            object = new JSONObject(arg0);
                            iResponseData.getDataSucceedListener(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iResponseData.getDataErrorListener("", -1);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (method == RCRequestNetwork.Method.Delete ||
                        method == RCRequestNetwork.Method.Get ||
                        method == RCRequestNetwork.Method.Put) {
                    return new HashMap<>();
                } else {
                    return new HashMap<>();
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headMapTemp = headMap;
                if (headMapTemp == null)
                    headMapTemp = new HashMap<>();
                headMapTemp.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                return headMapTemp;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryTime * 1000,
                0, 1.0f));
        RCSDKManage.getInstance().addToRequestQueue(request, url);
    }


    /**
     * 刷新请求（根据传过了的上下文取出存储的请求参数进行重新请求）
     *
     * @param context
     * @return
     */
    public static boolean refreshGetData(Context context) {
        //如果是平台的请求，并且没有token
        if (RCSPUserInfo.getLastSDKToken().equals("")) {
            //通知接听者没有登陆
            if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                RCSDKManage.getInstance().getRequestDataErrorLister().error(context,
                        IRCRequestCode.STATUS_APP_CODE_TOKEN_OVERDUE, IRequestCode.STATUS_MSG_LOGIN_OUT
                );
            }
            changeErrorInfoLayout(true);
            return false;
        }
        //便利请求失败的网络请求集合，找出指定上下文对象一致的请求重新发送请求
        for (int i = 0; i < networkGetDataDTOList.size(); i++) {
            if (networkGetDataDTOList.get(i).getContextName().equals(context.getClass().getName())) {
                networkGetDataDTOList.get(i).getBean().setP_token(RCSPUserInfo.getLastSDKToken());
                if (null != networkGetDataDTOList.get(i).getBean().getBaseNetUrl()
                        && !networkGetDataDTOList.get(i).getBean().getBaseNetUrl().equals("")) {
                    sendRequest(context, networkGetDataDTOList.get(i), true);
                } else {
                    sendRequest(context, networkGetDataDTOList.get(i), true);
                }
            }
        }
        showHandler(true);
        return true;
    }


    public static void cleanDetDataDTO(Context context) {
        for (int i = 0; i < networkGetDataDTOList.size(); i++) {
            if (networkGetDataDTOList.get(i).getContextName().equals(context.getClass().getName())) {
                networkGetDataDTOList.remove(i);
                i--;
            }
        }
    }


    /**
     * 检查指定上下文对应的网络请求是否全部完成（用于handler的显示隐藏）
     *
     * @param context
     * @return
     */
    private static boolean checkAllGetData(Context context) {
        for (int i = 0; i < networkGetDataDTOList.size(); i++) {
            if (networkGetDataDTOList.get(i).getContextName().equals(context.getClass().getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示或隐藏请求失败页面
     *
     * @param isShow 是否显示
     */
    private static void changeErrorInfoLayout(boolean isShow) {
        if (RCSDKManage.getScreenManger().currentActivity() instanceof RCBaseActivity) {
            if (isShow) {
                ((RCBaseActivity) RCSDKManage.getScreenManger().currentActivity()).showNetworkErrorInfo();
            } else {
                ((RCBaseActivity) RCSDKManage.getScreenManger().currentActivity()).goneNetworkErrorInfo();
            }
        }
    }

    /**
     * 显示或隐藏请求时的handler对象
     *
     * @param isShow 是否显示
     */
    private static void showHandler(boolean isShow) {
        if (RCSDKManage.getScreenManger().currentActivity() instanceof RCBaseActivity) {
            if (isShow) {
                ((RCBaseActivity) RCSDKManage.getScreenManger().currentActivity()).mRcHandler.sendMessage(RCHandler.START);
            } else {
                ((RCBaseActivity) RCSDKManage.getScreenManger().currentActivity()).mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }
    }


}
