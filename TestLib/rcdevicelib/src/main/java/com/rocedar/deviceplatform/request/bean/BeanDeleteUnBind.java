package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/2/16
 * @desc 设备解绑
 * @veison
 */

public class BeanDeleteUnBind extends BasePlatformBean {
    /**
     * 设备id
     */
    public String deviceId;
    /**
     * 设备编号,已绑定设备列表中返回
     */
    public String device_no;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }
}
