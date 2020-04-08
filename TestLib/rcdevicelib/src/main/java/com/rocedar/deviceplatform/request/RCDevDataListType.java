package com.rocedar.deviceplatform.request;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备列表类型的接口
 * @veison V1.0
 */

public interface RCDevDataListType {
    /**
     * 设备类型列表
     */
    int DEVICE_TYPE_LIST = 000001;
    /**
     * 指标设备列表
     */
    int DEVICE_INDICATOR_LIST = 000002;
    /**
     * 用户绑定设备列表
     */
    int DEVICE_USER_LIST = 000003;
}
