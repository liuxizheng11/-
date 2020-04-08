package com.rocedar.deviceplatform.request.bean;

/**
 * 作者：lxz
 * 日期：17/7/28 下午3:33
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanGetBehaviorLibraryData extends BasePlatformBean {
    /**
     * 设备id
     */
    public String device_id;
    /**
     * 数据时间
     */
    public String data_time;
    /**
     * 行为id
     */
    public String conductId;
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

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }

    public String getConductId() {
        return conductId;
    }

    public void setConductId(String conductId) {
        this.conductId = conductId;
    }
}
