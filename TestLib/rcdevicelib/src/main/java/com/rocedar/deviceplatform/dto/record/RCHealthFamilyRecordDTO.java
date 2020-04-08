package com.rocedar.deviceplatform.dto.record;

/**
 * @author liuyi
 * @date 2017/3/11
 * @desc 家人档案DTO
 * @veison V3.3.30(动吖)
 */

public class RCHealthFamilyRecordDTO {

    /**
     * id : 4012
     * name :
     * time : -1
     * value :
     * unit :
     * device_num : 0
     * device_remind : 您还未绑定爷爷的血压计
     * device_title :
     */

    /**
     * 指标ID
     */
    private int id;
    /**
     * 指标名称
     */
    private String name;
    /**
     * 数据时间
     */
    private long time;
    /**
     * 指标值
     */
    private String value;
    /**
     * 指标单位
     */
    private String unit;
    /**
     * 指标绑定设备数
     */
    private int device_num;
    /**
     * 指标绑定设备提示
     */
    private String device_remind;
    /**
     * 指标绑定设备标题
     */
    private String device_title;
    /**
     * 家人用户id
     */
    private long user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDevice_num() {
        return device_num;
    }

    public void setDevice_num(int device_num) {
        this.device_num = device_num;
    }

    public String getDevice_remind() {
        return device_remind;
    }

    public void setDevice_remind(String device_remind) {
        this.device_remind = device_remind;
    }

    public String getDevice_title() {
        return device_title;
    }

    public void setDevice_title(String device_title) {
        this.device_title = device_title;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
