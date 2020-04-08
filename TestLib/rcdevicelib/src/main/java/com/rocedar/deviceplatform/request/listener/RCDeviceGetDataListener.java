package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceDataListDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备操作请求后台接口返回的数据
 * @veison V1.0
 */

public interface RCDeviceGetDataListener {

    void getDataSuccess(List<RCDeviceDataListDTO> dtoList);

    void getDataError(int status, String msg);
}
