package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCDeviceDataDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceMultiDataDetailsListener;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 用户设备数据接口类(请求后台接口)
 * @veison V3.3.30(动吖)
 */

public interface RCDeviceUserDataRequest {

    /**
     * 设备数据详情
     *
     * @param device_id 设备id
     */
    void loadDeviceDataDetails(int device_id, RCDeviceDataDetailsListener listener);

    /**
     * 多角色设备详情
     *
     * @param device_id 设备id
     * @param device_no 设备sn号
     */
    void loadMultiDeviceDataDetails(int device_id, String device_no, RCDeviceMultiDataDetailsListener listener);
}
