package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/2/14
 * @desc SN设备绑定
 * @veison
 */

public class BeanPostSnBind extends BasePlatformBean {
    /**
     * 设备id
     */
    public String deviceId;
    /**
     * Sn号
     */
    public String sn;
    /**
     * role
     */
    public String role;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
