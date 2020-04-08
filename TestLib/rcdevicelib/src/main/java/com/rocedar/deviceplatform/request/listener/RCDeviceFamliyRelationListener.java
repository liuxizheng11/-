package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.data.RCDeviceFamilyRelationDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/18
 * @desc 查询家人关系列表请求后台接口返回的数据
 * @veison
 */

public interface RCDeviceFamliyRelationListener {

    void getDataSuccess(List<RCDeviceFamilyRelationDTO> dtoList);

    void getDataError(int status, String msg);
}
