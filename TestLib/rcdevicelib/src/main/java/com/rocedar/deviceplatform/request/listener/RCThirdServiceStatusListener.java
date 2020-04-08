package com.rocedar.deviceplatform.request.listener;

/**
 * @author liuyi
 * @date 2017/4/26
 * @desc 第三方服务状态查询监听
 * @veison V3.4.00(新增)
 */

public interface RCThirdServiceStatusListener {
    /**
     * 请求成功
     * @param status 服务状态，2为已激活
     */
    void getDataSuccess(int status);

    /**
     * 请求失败
     * @param status
     * @param msg
     */
    void getDataError(int status, String msg);
}
