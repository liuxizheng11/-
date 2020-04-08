package com.rocedar.deviceplatform.dto.indicatorconduct;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/5/3 下午3:45
 * 版本：V1.0.01
 * 描述：指标数据查看更多（健康立方）
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class IndicatorMoreDataDTO {


    /**
     * "unit": "%",
     * "bone_unit": "Kg",
     * "weight_unit": "Kg",
     * "data_time": 20170504160822,
     * "muscle": "56",
     * "weight": "62.9",
     * "exception_level": 2,
     * "bone": "2.2",
     * "moisture_unit": "%",
     * "moisture": "36",
     * "value": 39.66,
     * "muscle_unit": "%"
     */

    //指标值（指标为血压时，value为高值）
    private double value;
    //血压低值（只有血压有low字段）
    private double sub_value;
    //指标值对应的时间
    private String data_time;
    //高压值是否正常（0，正常，1不正常）
    private int exception_level;
    // 低压值是否正常（0，正常，1不正常）
    private int sub_exception_level;

    private String unit;
    private String bone_unit;
    private String weight_unit;
    private String muscle;
    private String weight;
    private String bone;
    private String moisture_unit;
    private String moisture;
    private String muscle_unit;

    private String exception;
    private long time;
    private String device_name;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBone_unit() {
        return bone_unit;
    }

    public void setBone_unit(String bone_unit) {
        this.bone_unit = bone_unit;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBone() {
        return bone;
    }

    public void setBone(String bone) {
        this.bone = bone;
    }

    public String getMoisture_unit() {
        return moisture_unit;
    }

    public void setMoisture_unit(String moisture_unit) {
        this.moisture_unit = moisture_unit;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getMuscle_unit() {
        return muscle_unit;
    }

    public void setMuscle_unit(String muscle_unit) {
        this.muscle_unit = muscle_unit;
    }

    public int getException_level() {
        return exception_level;
    }

    public void setException_level(int exception_level) {
        this.exception_level = exception_level;
    }

    public int getSub_exception_level() {
        return sub_exception_level;
    }

    public void setSub_exception_level(int sub_exception_level) {
        this.sub_exception_level = sub_exception_level;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getSub_value() {
        return sub_value;
    }

    public void setSub_value(double sub_value) {
        this.sub_value = sub_value;
    }

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }
}
