package com.rocedar.sdk.iting.device.db.bong;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/6/28 下午2:48
 * 版本：V1.0.01
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCITingDBHeartDTO {

    //数据开始时间
    private long DateTime;
    //数据日期(yyyyMMdd)
    private int Date;
    //设备ID
    private int deviceId;
    //设备MAC地址
    private String deviceMac;
    //设备数据
    private String heartValue;

    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }

    public long getDateTime() {
        return DateTime;
    }

    public void setDateTime(long dateTime) {
        DateTime = dateTime;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getHeartValue() {
        return heartValue;
    }

    public void setHeartValue(String heartValue) {
        this.heartValue = heartValue;
    }
}
