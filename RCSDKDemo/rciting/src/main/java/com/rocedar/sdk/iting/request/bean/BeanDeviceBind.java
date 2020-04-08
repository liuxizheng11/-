package com.rocedar.sdk.iting.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2018/5/30 下午4:35
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanDeviceBind extends RCBean {

    public String mac;

    public String device_no;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }
}
