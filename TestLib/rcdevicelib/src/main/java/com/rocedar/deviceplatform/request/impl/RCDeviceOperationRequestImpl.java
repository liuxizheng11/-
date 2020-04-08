package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.deviceplatform.request.RCDevDataListType;
import com.rocedar.deviceplatform.request.RCDeviceDataRequest;
import com.rocedar.deviceplatform.request.RCDeviceDetailsRequest;
import com.rocedar.deviceplatform.request.RCDeviceOperationRequest;
import com.rocedar.deviceplatform.request.listener.RCDeviceAlreadyBindListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceBlueToothDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataListTypeListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceFamliyRelationListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceGetDataListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceOAuth2DetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceSNDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/6 下午12:36
 * 版本：V1.0
 * 描述：设备操作请求后台接口的实现类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCDeviceOperationRequestImpl implements RCDeviceOperationRequest, RCDeviceDataRequest,RCDeviceDetailsRequest {
    private static String TAG = "RCDevice_Request";
    private static RCDeviceOperationRequestImpl ourInstance;

    private RCDeviceNetworkRequest rcDeviceNetworkRequest;

    public static RCDeviceOperationRequestImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCDeviceOperationRequestImpl(context);
        return ourInstance;
    }

    private Context mContext;

    private RCDeviceOperationRequestImpl(Context context) {
        this.mContext = context;
        rcDeviceNetworkRequest = new RCDeviceNetworkRequest(mContext);
    }

    @Override
    public void doOAuth2Binding(RCRequestSuccessListener listener, int deviceId, String code) {
        rcDeviceNetworkRequest.requestOAuth2Binding(listener, deviceId, code);
    }

    @Override
    public void doSNBinding(RCDeviceDataDetailsListener listener, int deviceId, String sn, String role) {
        rcDeviceNetworkRequest.requestSNBinding(listener,deviceId,sn,role);
    }

    @Override
    public void doBlueToothBinding(RCRequestSuccessListener listener, int deviceId, String mac) {
        rcDeviceNetworkRequest.requestBlueToothBinding(listener,deviceId,mac);
    }

    @Override
    public void doUnBinding(RCRequestSuccessListener listener, int deviceId, String device_no) {
        rcDeviceNetworkRequest.requestUnBinding(listener, deviceId,device_no);
    }

    @Override
    public void doUploading(RCRequestSuccessListener listener, String data) {
        rcDeviceNetworkRequest.requestUploading(listener,data);
    }

    @Override
    public void getDeviceDataType(RCDeviceDataListTypeListener listener) {
        rcDeviceNetworkRequest.requestGetDataType(listener);
    }

    @Override
    public void getDeviceTypeList(RCDeviceGetDataListener listener, int type_id) {
        rcDeviceNetworkRequest.requestgetDataList(listener, type_id, RCDevDataListType.DEVICE_TYPE_LIST);
    }

    @Override
    public void getDeviceIndicatorList(RCDeviceGetDataListener listener, int indicator_id) {
        rcDeviceNetworkRequest.requestgetDataList(listener, indicator_id, RCDevDataListType.DEVICE_INDICATOR_LIST);
    }

    @Override
    public void getDeviceIndicatorList(RCDeviceGetDataListener listener, int indicator_id, long user_id) {
        rcDeviceNetworkRequest.requestgetDataList(listener, indicator_id,user_id, RCDevDataListType.DEVICE_INDICATOR_LIST);
    }

    @Override
    public void getDeviceUserBindList(RCDeviceGetDataListener listener) {
        rcDeviceNetworkRequest.requestgetDataList(listener, RCDevDataListType.DEVICE_USER_LIST);
    }

    @Override
    public void queryDeviceAlreadyBindList(RCDeviceAlreadyBindListener listener) {
        rcDeviceNetworkRequest.requestGetAlreadyBind(listener);
    }

    @Override
    public void queryFamilyRelationList(RCDeviceFamliyRelationListener listener) {
        rcDeviceNetworkRequest.requestQueryFamilyRelationList(listener);
    }

    @Override
    public void getOAuth2DeviceDetails(RCDeviceOAuth2DetailsListener listener, int device_id) {
        rcDeviceNetworkRequest.requestGetOAuth2Details(listener,device_id);
    }

    @Override
    public void getBlueToothDeviceDetails(RCDeviceBlueToothDetailsListener listener, int device_id) {
        rcDeviceNetworkRequest.requestGetBlueToothDetails(listener,device_id);
    }

    @Override
    public void getSNDeviceDetails(RCDeviceSNDetailsListener listener, int device_id) {
        rcDeviceNetworkRequest.requestGetSNDetails(listener,device_id);
    }
}
