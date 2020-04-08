package com.rocedar.deviceplatform.request.bean;

/**
 * Created by lxz on 16/10/25.
 */

public class BeanGetIndicatorData extends BasePlatformBean {
    /**
     * 页数
     */
    public String pn;
    /**
     * 家人用户ID，查看自己时不传或传-1
     */
    public String user_id;
    /**
     * 设备id（没有传-1或不传）
     */
    public String device_id;

    public String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
