package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceMultiDataDetailsDTO;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 多角色设备详情的监听
 * @veison V3.3.30(动吖)
 */

public interface RCDeviceMultiDataDetailsListener {

    void getDataSuccess(RCDeviceMultiDataDetailsDTO dto);

    void getDataError(int status, String msg);
}
