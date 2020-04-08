package com.rocedar.deviceplatform.dto.behaviorlibrary;

/**
 * 作者：lxz
 * 日期：17/7/28 下午12:56
 * 版本：V1.0
 * 描述：运动
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorLibraryRunDTO {

    /**
     * 距离
     */
    private float distance;
    /**
     * 跑步速度
     */
    private float speed;
    /**
     * 跑步时长
     */
    private int time;
    /**
     * 跑步配速
     */
    private int pace;
    /**
     * 跑步配速
     */
    private String pace_CA;
    /**
     * 跑步轨迹
     */
    private String locus;
    /**
     * 步数
     */
    private int step;
    /**
     * 路里
     */
    private float calorie;
    /**
     * 1室内，2室外
     */
    private int environment;
    /**
     * 用户id
     */
    private long user_id;
    /**
     * 行为id
     */
    private int conduct_id;
    /**
     * 设备id
     */
    private int device_id;
    /**
     * 数据时间
     */
    private long data_time;
    /**
     * 跑步速度轨迹
     */
    private String speed_locus;
    /**
     * 配速轨迹
     */
    private String pace_locus;
    /**
     * 有效时长
     */
    private int valid_time;
    /**
     * 心率
     */
    private int heart_rate;
    /**
     * 心率轨迹
     */
    private String heart_rate_locus;
    /**
     * 数据修改时间
     */
    private long update_time;
    /**
     * 设备来源
     */
    private String device_name;

    /**
     * String	跑步时长
     */
    private  String   time_CA;
    /**
     * String	有效时长
     */
    private  String  valid_time_CA;

    /**
     *
     * long 开始时间
     * */
    private  long start_time;
    /**
     *
     * long 结束时间
     * */
    private  long end_time;

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getPace_CA() {
        return pace_CA;
    }

    public void setPace_CA(String pace_CA) {
        this.pace_CA = pace_CA;
    }

    public String getTime_CA() {
        return time_CA;
    }

    public void setTime_CA(String time_CA) {
        this.time_CA = time_CA;
    }

    public String getValid_time_CA() {
        return valid_time_CA;
    }

    public void setValid_time_CA(String valid_time_CA) {
        this.valid_time_CA = valid_time_CA;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public int getEnvironment() {
        return environment;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
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

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public long getData_time() {
        return data_time;
    }

    public void setData_time(long data_time) {
        this.data_time = data_time;
    }

    public String getSpeed_locus() {
        return speed_locus;
    }

    public void setSpeed_locus(String speed_locus) {
        this.speed_locus = speed_locus;
    }

    public String getPace_locus() {
        return pace_locus;
    }

    public void setPace_locus(String pace_locus) {
        this.pace_locus = pace_locus;
    }

    public int getValid_time() {
        return valid_time;
    }

    public void setValid_time(int valid_time) {
        this.valid_time = valid_time;
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getHeart_rate_locus() {
        return heart_rate_locus;
    }

    public void setHeart_rate_locus(String heart_rate_locus) {
        this.heart_rate_locus = heart_rate_locus;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
