package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceOAuth2DetailsDTO;

/**
 * @author liuyi
 * @date 2017/2/16
 * @desc OAuth2设备详情的监听
 * @veison V1.0
 */

public interface RCDeviceOAuth2DetailsListener {

    void getDataSuccess(RCDeviceOAuth2DetailsDTO dto);

    void getDataError(int status, String msg);
}
