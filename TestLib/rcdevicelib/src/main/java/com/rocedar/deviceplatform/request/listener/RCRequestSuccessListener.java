package com.rocedar.deviceplatform.request.listener;

/**
 * @author liuyi
 * @date 2017/3/2
 * @desc 请求后台接口成功后返回空的状态
 * @veison V1.0
 */

public interface RCRequestSuccessListener {
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
