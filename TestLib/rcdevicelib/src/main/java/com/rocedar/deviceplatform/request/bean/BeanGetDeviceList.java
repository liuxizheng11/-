package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备列表
 * @veison
 */

public class BeanGetDeviceList extends BasePlatformBean {
    public String type_id;
    public String indicator_id;
    public String user_id;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(String indicator_id) {
        this.indicator_id = indicator_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
