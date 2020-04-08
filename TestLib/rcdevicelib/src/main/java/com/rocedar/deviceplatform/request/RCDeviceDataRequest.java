package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCDeviceAlreadyBindListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataListTypeListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceFamliyRelationListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceGetDataListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备数据相关的后台接口请求定义的接口类
 * @veison
 */

public interface RCDeviceDataRequest {
    /**
     * 数据上传
     *
     * @param listener
     * @param data     上传的设备数据（需要将jsonarray转换成string）
     */
    void doUploading(RCRequestSuccessListener listener, String data);

    /**
     * 获取设备类型
     *
     * @param listener
     */
    void getDeviceDataType(RCDeviceDataListTypeListener listener);

    /**
     * 获取指定类型的设备列表
     *
     * @param listener
     * @param type_id  设备类型id
     */
    void getDeviceTypeList(RCDeviceGetDataListener listener, int type_id);

    /**
     * 获取指标设备列表
     *
     * @param listener
     * @param indicator_id 设备指标id
     */
    void getDeviceIndicatorList(RCDeviceGetDataListener listener, int indicator_id);

    /**
     * 获取指标设备列表
     *
     * @param listener
     * @param indicator_id   设备指标id
     * @param user_id 家人用户id
     */
    void getDeviceIndicatorList(RCDeviceGetDataListener listener, int indicator_id, long user_id);

    /**
     * 获取用户绑定设备列表
     *
     * @param listener
     */
    void getDeviceUserBindList(RCDeviceGetDataListener listener);

    /**
     * 查询已绑定的蓝牙设备
     *
     * @param listener
     */
    void queryDeviceAlreadyBindList(RCDeviceAlreadyBindListener listener);

    /**
     * 查询家人关系列表
     *
     * @param listener
     */
    void queryFamilyRelationList(RCDeviceFamliyRelationListener listener);

}
