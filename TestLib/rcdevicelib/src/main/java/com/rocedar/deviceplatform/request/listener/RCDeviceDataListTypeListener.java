package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceDataListTypeDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备数据列表类型请求后台接口返回的数据
 * @veison
 */

public interface RCDeviceDataListTypeListener {

    void getDataSuccess(List<RCDeviceDataListTypeDTO> dtoList);

    void getDataError(int status, String msg);
}
