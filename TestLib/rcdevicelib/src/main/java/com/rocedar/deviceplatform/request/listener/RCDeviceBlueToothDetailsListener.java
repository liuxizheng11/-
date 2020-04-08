package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceBlueToothDetailsDTO;

/**
 * @author liuyi
 * @date 2017/2/16
 * @desc 蓝牙设备详情的监听
 * @veison V1.0
 */

public interface RCDeviceBlueToothDetailsListener {

    void getDataSuccess(RCDeviceBlueToothDetailsDTO dto);

    void getDataError(int status, String msg);
}
