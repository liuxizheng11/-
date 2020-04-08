package com.rocedar.deviceplatform.dto.behaviorlibrary;

/**
 * 作者：lxz
 * 日期：17/7/28 下午2:59
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorLibraryDietDTO {
    /**
     * 用户id
     */
    private long user_id;
    /**
     * 行为id
     */
    private int conduct_id;
    /**
     * 指标id
     */
    private int indicator_id;
    /**
     * 设备id
     */
    private int device_id;
    /**
     * 数据时间
     */
    private String data_time;
    /**
     * 饮食消息
     */
    private String diet_message;
    /**
     * 饮食图片，多张以;隔开
     */
    private String diet_images;
    /**
     * 数据更新时间
     */
    private long update_time;
    /**
     * 今日可摄入的能量
     */
    private String need;
    /**
     * 已经消耗的能量
     */
    private String yet;

    /**
     * 来源于
     *
     */
    private String device_name;

    /**
     * 记录条数
     *
     */
    private int counts;

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getConduct_id() {
        return conduct_id;
    }

    public void setConduct_id(int conduct_id) {
        this.conduct_id = conduct_id;
    }

    public int getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(int indicator_id) {
        this.indicator_id = indicator_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }

    public String getDiet_message() {
        return diet_message;
    }

    public void setDiet_message(String diet_message) {
        this.diet_message = diet_message;
    }

    public String getDiet_images() {
        return diet_images;
    }

    public void setDiet_images(String diet_images) {
        this.diet_images = diet_images;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getYet() {
        return yet;
    }

    public void setYet(String yet) {
        this.yet = yet;
    }
}
