package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceSnDetailsDTO;

/**
 * @author liuyi
 * @date 2017/2/17
 * @desc SN设备详情的监听
 * @veison V1.0
 */

public interface RCDeviceSNDetailsListener {

    void getDataSuccess(RCDeviceSnDetailsDTO dto);

    void getDataError(int status, String msg);
}
