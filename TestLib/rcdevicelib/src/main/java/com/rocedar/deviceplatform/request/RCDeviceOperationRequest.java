package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCDeviceDataDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/6 下午12:31
 * 版本：V1.0
 * 描述：设备操作相关的后台接口请求定义的接口类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCDeviceOperationRequest {
    /**
     * OAuth2设备绑定
     *
     * @param listener
     * @param deviceId 设备id
     * @param code     OAuth2返回url中code参数
     */
    void doOAuth2Binding(RCRequestSuccessListener listener, int deviceId, String code);

    /**
     * SN设备绑定
     *
     * @param listener
     * @param deviceId 设备id
     * @param sn       Sn号
     * @param role     [设备角色id:家人关系id]——>(需要将jsonarray转换成string)
     *                 1、单人单角色直接——>""
     *                 2、多角色多家人——>[设备角色id1:家人关系id1,设备角色id2:家人关系id2,...]
     *                 3、单人多角色——>[设备角色id1:-1,设备角色id2:-1,...]
     */
    void doSNBinding(RCDeviceDataDetailsListener listener, int deviceId, String sn, String role);

    /**
     * 蓝牙设备绑定
     *
     * @param listener
     * @param deviceId 设备id
     * @param mac      mac地址
     */
    void doBlueToothBinding(RCRequestSuccessListener listener, int deviceId, String mac);

    /**
     * 设备解绑
     *
     * @param listener
     * @param deviceId  设备id
     * @param device_no 设备编号,已绑定设备列表中返回
     */
    void doUnBinding(RCRequestSuccessListener listener, int deviceId, String device_no);

}
