package com.rocedar.deviceplatform.dto.data;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备列表DTO
 * @veison
 */

public class RCDeviceDataListDTO {
    /**
     * 设备ID
     */
    private int device_id;

    /**
     * 设备名字
     */
    private String device_name;

    /**
     * 设备logo
     */
    private String device_logo;

    /**
     * 绑定url
     */
    private String bind_url;

    /**
     * 配置wifi url
     */
    private String wifi_url;

    /**
     * 数据展示url
     */
    private String data_url;

    /**
     * 去测量url
     */
    private String measure_url;

    /**
     * 用户是否绑定该设备（已绑定1， 未绑定0， 37血压-1）
     */
    private int bind;

    /**
     * 设备sn码
     */
    private String device_no;

    /**
     * 显示关系名称
     */
    private String relation_name;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_logo() {
        return device_logo;
    }

    public void setDevice_logo(String device_logo) {
        this.device_logo = device_logo;
    }

    public String getBind_url() {
        return bind_url;
    }

    public void setBind_url(String bind_url) {
        this.bind_url = bind_url;
    }

    public String getWifi_url() {
        return wifi_url;
    }

    public void setWifi_url(String wifi_url) {
        this.wifi_url = wifi_url;
    }

    public String getData_url() {
        return data_url;
    }

    public void setData_url(String data_url) {
        this.data_url = data_url;
    }

    public String getMeasure_url() {
        return measure_url;
    }

    public void setMeasure_url(String measure_url) {
        this.measure_url = measure_url;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }
}
