package com.rocedar.deviceplatform.dto.behaviorlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/28 下午2:36
 * 版本：V1.0
 * 描述：睡眠
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorLibrarySleepDTO  {
    /**
     * 用户id
     */
    private long user_id;
    /**
     * 行为id
     */
    private int conduct_id;
    /**
     * 设备名称
     */
    private int device_id;
    /**
     * 数据时间
     */
    private long data_time;
    /**
     * 入睡时间
     */
    private long fall_time;
    /**
     * 醒来时间
     */
    private long wake_time;
    /**
     * 睡眠时长
     */
    private String sleep_time;
    /**
     * 深睡时长
     */
    private int deep_sleep_time;
    /**
     * 浅睡眠时长
     */
    private int shallow_sleep_time;
    /**
     * 清醒时长，单位：分
     */
    private int awake_sleep_time;
    /**
     * 清醒次数
     */
    private int awake_num;
    /**
     * 入睡市场，单位：分
     */
    private int fall_sleep_time;
    /**
     * 做梦时长，单位：分
     */
    private int dream_time;
    /**
     * 梦境状态，1噩梦，2美梦，3无梦
     */
    private int dream_status;
    /**
     * 环境噪音 单位：分贝
     */
    private int environment_noise;
    /**
     * 翻身次数
     */
    private int turn_over_num;
    /**
     * 睡前状态，1饮酒，2压力大，3陌生床，4吃宵夜
     */
    private int before_sleep_status;
    /**
     * 睡眠轨迹
     */
    private List<RCBehaviorLibrarySleepLocusDTO> mList=new ArrayList<>();
    /**
     * 睡眠心率轨迹
     */
    private String heart_rate_locus;
    /**
     * 睡眠备注
     */
    private String sleep_remark;
    /**
     * 数据修改时间
     */
    private long update_time;
    private List<RCBehaviorLibrarySleepDetialDTO> mDetialList=new ArrayList<>();

    /**
     * 设备来源
     */
    private String device_name;

    /**
     * String	睡眠时长
     */
    private String sleep_time_CA;
    /**
     * String	深睡眠时长
     */
    private String deep_sleep_time_CA;
    /**
     * String	浅睡眠时长
     */
    private String shallow_sleep_time_CA;
    /**
     * String	清醒时长
     */
    private String awake_sleep_time_CA;
    /**
     * String	入睡时长
     */
    private String fall_sleep_time_CA;

    public String getSleep_time_CA() {
        return sleep_time_CA;
    }

    public void setSleep_time_CA(String sleep_time_CA) {
        this.sleep_time_CA = sleep_time_CA;
    }

    public String getDeep_sleep_time_CA() {
        return deep_sleep_time_CA;
    }

    public void setDeep_sleep_time_CA(String deep_sleep_time_CA) {
        this.deep_sleep_time_CA = deep_sleep_time_CA;
    }

    public String getShallow_sleep_time_CA() {
        return shallow_sleep_time_CA;
    }

    public void setShallow_sleep_time_CA(String shallow_sleep_time_CA) {
        this.shallow_sleep_time_CA = shallow_sleep_time_CA;
    }

    public String getAwake_sleep_time_CA() {
        return awake_sleep_time_CA;
    }

    public void setAwake_sleep_time_CA(String awake_sleep_time_CA) {
        this.awake_sleep_time_CA = awake_sleep_time_CA;
    }

    public String getFall_sleep_time_CA() {
        return fall_sleep_time_CA;
    }

    public void setFall_sleep_time_CA(String fall_sleep_time_CA) {
        this.fall_sleep_time_CA = fall_sleep_time_CA;
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

    public long getFall_time() {
        return fall_time;
    }

    public void setFall_time(long fall_time) {
        this.fall_time = fall_time;
    }

    public long getWake_time() {
        return wake_time;
    }

    public void setWake_time(long wake_time) {
        this.wake_time = wake_time;
    }

    public String  getSleep_time() {
        return sleep_time;
    }

    public void setSleep_time(String sleep_time) {
        this.sleep_time = sleep_time;
    }

    public int getDeep_sleep_time() {
        return deep_sleep_time;
    }

    public void setDeep_sleep_time(int deep_sleep_time) {
        this.deep_sleep_time = deep_sleep_time;
    }

    public int getShallow_sleep_time() {
        return shallow_sleep_time;
    }

    public void setShallow_sleep_time(int shallow_sleep_time) {
        this.shallow_sleep_time = shallow_sleep_time;
    }

    public int getAwake_sleep_time() {
        return awake_sleep_time;
    }

    public void setAwake_sleep_time(int awake_sleep_time) {
        this.awake_sleep_time = awake_sleep_time;
    }

    public int getAwake_num() {
        return awake_num;
    }

    public void setAwake_num(int awake_num) {
        this.awake_num = awake_num;
    }

    public int getFall_sleep_time() {
        return fall_sleep_time;
    }

    public void setFall_sleep_time(int fall_sleep_time) {
        this.fall_sleep_time = fall_sleep_time;
    }

    public int getDream_time() {
        return dream_time;
    }

    public void setDream_time(int dream_time) {
        this.dream_time = dream_time;
    }

    public int getDream_status() {
        return dream_status;
    }

    public void setDream_status(int dream_status) {
        this.dream_status = dream_status;
    }

    public int getEnvironment_noise() {
        return environment_noise;
    }

    public void setEnvironment_noise(int environment_noise) {
        this.environment_noise = environment_noise;
    }

    public int getTurn_over_num() {
        return turn_over_num;
    }

    public void setTurn_over_num(int turn_over_num) {
        this.turn_over_num = turn_over_num;
    }

    public int getBefore_sleep_status() {
        return before_sleep_status;
    }

    public void setBefore_sleep_status(int before_sleep_status) {
        this.before_sleep_status = before_sleep_status;
    }

    public List<RCBehaviorLibrarySleepLocusDTO> getmList() {
        return mList;
    }

    public void setmList(List<RCBehaviorLibrarySleepLocusDTO> mList) {
        this.mList = mList;
    }

    public String getHeart_rate_locus() {
        return heart_rate_locus;
    }

    public void setHeart_rate_locus(String heart_rate_locus) {
        this.heart_rate_locus = heart_rate_locus;
    }

    public String getSleep_remark() {
        return sleep_remark;
    }

    public void setSleep_remark(String sleep_remark) {
        this.sleep_remark = sleep_remark;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public List<RCBehaviorLibrarySleepDetialDTO> getmDetialList() {
        return mDetialList;
    }

    public void setmDetialList(List<RCBehaviorLibrarySleepDetialDTO> mDetialList) {
        this.mDetialList = mDetialList;
    }
}
