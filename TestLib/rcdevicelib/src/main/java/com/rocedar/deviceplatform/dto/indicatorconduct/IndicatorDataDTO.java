package com.rocedar.deviceplatform.dto.indicatorconduct;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午3:24
 * 版本：V1.0
 * 描述：指标数据
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class IndicatorDataDTO {
    /**
     * 数据来源设备ID
     */
    private int device_id;
    /**
     * 数据来源设备名称
     */
    private String device_name;
    /**
     * 指标测量时间
     */
    private long time;
    /**
     * 指标值
     */
    private float value;
    /**
     * 指标单位
     */
    private String unit;
    /**
     * 血糖时间区间范围（餐前餐后）
     */
    private String indicator_interval;
    /**
     * 正常目标值
     */
    private String indicator_target;
    /**
     * 指标告警信息
     */
    private String exception;
    /**
     * 指标值告警级别
     * 0为正常，大于0为异常
     */
    private int exception_level;
    /**
     * 指标附属值（血压：低压）
     */
    private float sub_value;
    /**
     * 指标附属值告警级别
     * 0为正常，大于0为异常
     */
    private int sub_exception_level;
    /**
     * 体重
     */
    private String weight;
    /**
     * 体重单位
     */
    private String weight_unit;
    /**
     * 骨骼量（体脂：扩展）
     */
    private String bone;
    /**
     * 骨骼量单位（体脂：扩展）
     */
    private String bone_unit;
    /**
     * 肌肉量（体脂：扩展）
     */
    private String muscle;
    /**
     * 肌肉量单位（体脂：扩展）
     */
    private String muscle_unit;
    /**
     * 水份率（体脂：扩展）
     */
    private String moisture;
    /**
     * 水份率单位（体脂：扩展）
     */
    private String moisture_unit;

    /**
     * 测量URL
     */
    private String device_measure_url;

    public String getDevice_measure_url() {
        return device_measure_url;
    }

    public void setDevice_measure_url(String device_measure_url) {
        this.device_measure_url = device_measure_url;
    }

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIndicator_interval() {
        return indicator_interval;
    }

    public void setIndicator_interval(String indicator_interval) {
        this.indicator_interval = indicator_interval;
    }

    public String getIndicator_target() {
        return indicator_target;
    }

    public void setIndicator_target(String indicator_target) {
        this.indicator_target = indicator_target;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public int getException_level() {
        return exception_level;
    }

    public void setException_level(int exception_level) {
        this.exception_level = exception_level;
    }

    public float getSub_value() {
        return sub_value;
    }

    public void setSub_value(float sub_value) {
        this.sub_value = sub_value;
    }

    public int getSub_exception_level() {
        return sub_exception_level;
    }

    public void setSub_exception_level(int sub_exception_level) {
        this.sub_exception_level = sub_exception_level;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getBone() {
        return bone;
    }

    public void setBone(String bone) {
        this.bone = bone;
    }

    public String getBone_unit() {
        return bone_unit;
    }

    public void setBone_unit(String bone_unit) {
        this.bone_unit = bone_unit;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getMuscle_unit() {
        return muscle_unit;
    }

    public void setMuscle_unit(String muscle_unit) {
        this.muscle_unit = muscle_unit;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getMoisture_unit() {
        return moisture_unit;
    }

    public void setMoisture_unit(String moisture_unit) {
        this.moisture_unit = moisture_unit;
    }
}
