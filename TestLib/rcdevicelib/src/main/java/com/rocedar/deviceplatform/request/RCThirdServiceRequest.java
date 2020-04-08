package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCThirdServiceStatusListener;

/**
 * @author liuyi
 * @date 2017/4/26
 * @desc 第三方服务状态查询接口
 * @veison V3.4.00(新增)
 */

public interface RCThirdServiceRequest {
    /**
     * 查询服务状态
     * @param serviceId 服务id
     */
    void serviceStatusQuery(String serviceId, RCThirdServiceStatusListener listener);
}
