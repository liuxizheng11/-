package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/2/14
 * @desc 蓝牙设备绑定
 * @veison
 */

public class BeanPostBlueToothBind extends BasePlatformBean{
    /**
     * 设备id
     */
    public String deviceId;
    /**
     * Mac地址
     */
    public String mac;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
