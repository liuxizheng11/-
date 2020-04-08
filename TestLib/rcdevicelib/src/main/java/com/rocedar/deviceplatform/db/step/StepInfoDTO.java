package com.rocedar.deviceplatform.db.step;

import java.io.Serializable;

/**
 * 计步数据库的字段名
 */
public class StepInfoDTO implements Serializable {


    private int id = 0;
    private long times = 19700101000001l;//时间戳
    private long step = 0;//步数
    private int sensorType = 0;//传感器类型
    private String otherone = "";
    private String date = "";

    public StepInfoDTO() {

    }

    public StepInfoDTO(long times, long step, int sensorType, String date) {
        this.times = times;
        this.step = step;
        this.sensorType = sensorType;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public String getOtherone() {
        return otherone;
    }

    public void setOtherone(String otherone) {
        this.otherone = otherone;
    }


}
