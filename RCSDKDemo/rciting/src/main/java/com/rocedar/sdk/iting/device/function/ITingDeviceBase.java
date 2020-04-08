//package com.rocedar.sdk.iting.device.function;
//
//import android.content.Context;
//
//import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
//
///**
// * 项目名称：瑰柏SDK-商城
// * <p>
// * 作者：phj
// * 日期：2019-10-29 16:35
// * 版本：V1.1.00
// * 描述：瑰柏SDK-服务商品
// * <p>
// * CopyRight©北京瑰柏科技有限公司
// */
//abstract class ITingDeviceBase {
//
//    private String TAG = "ITING-BASE";
//
//    public Context context;
//
//    private ResultCallBack callBack;
//
//    public ITingDeviceBase(Context context, ResultCallBack callBack) {
//        this.context = context;
//        this.callBack = callBack;
//    }
//
//    private String lastFunctionName = "";
//    private long lastFunctionTime = -1;
//
//    public void setLastFunctionName(String lastFunctionName) {
//        this.lastFunctionName = lastFunctionName;
//    }
//
//
//    protected abstract void parsingData(int result, Object[] objects);
//
//
//    protected void parsingError(int result) {
//
//    }
//
//
//}
