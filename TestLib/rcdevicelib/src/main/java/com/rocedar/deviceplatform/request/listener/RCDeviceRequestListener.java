package com.rocedar.deviceplatform.request.listener;

/**
 * @author liuyi
 * @date 2017/2/9
 * @desc 设备操作请求后台接口返回的状态
 * @veison V1.0
 */

public interface RCDeviceRequestListener {
    /**
     * 请求成功
     */
    void requestSuccess();

    /**
     * 请求失败
     * @param status
     * @param msg
     */
    void requestError(int status, String msg);
}
