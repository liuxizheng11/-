package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/14
 * @desc 查询已绑定的前端链接设备返回的数据
 * @veison V1.0
 */

public interface RCDeviceAlreadyBindListener {

    void getDataSuccess(List<RCDeviceAlreadyBindDTO> dtoList);

    void getDataError(int status, String msg);
}
