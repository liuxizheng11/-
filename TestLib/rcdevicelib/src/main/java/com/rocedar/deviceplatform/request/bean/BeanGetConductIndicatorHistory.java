package com.rocedar.deviceplatform.request.bean;

/**
 * Created by phj on 2016/12/23.
 */

public class BeanGetConductIndicatorHistory extends BasePlatformBean {

    public String device_id;

    public String pn;

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
