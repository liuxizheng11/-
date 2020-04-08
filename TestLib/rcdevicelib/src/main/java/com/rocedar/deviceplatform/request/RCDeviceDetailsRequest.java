package com.rocedar.deviceplatform.request;


import com.rocedar.deviceplatform.request.listener.RCDeviceBlueToothDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceOAuth2DetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceSNDetailsListener;

/**
 * @author liuyi
 * @date 2017/2/16
 * @desc 设备详情接口
 * @veison V1.0
 */

public interface RCDeviceDetailsRequest {
    /**
     * OAuth2设备详情
     *
     * @param listener
     * @param device_id 设备id
     */
    void getOAuth2DeviceDetails(RCDeviceOAuth2DetailsListener listener, int device_id);

    /**
     * 蓝牙设备详情
     *
     * @param listener
     * @param device_id 设备id
     */
    void getBlueToothDeviceDetails(RCDeviceBlueToothDetailsListener listener, int device_id);

    /**
     * SN设备详情
     *
     * @param listener
     * @param device_id 设备id
     */
    void getSNDeviceDetails(RCDeviceSNDetailsListener listener, int device_id);
}
