package com.rocedar.deviceplatform.dto.behaviorlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/28 上午11:43
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorLibraryDTO {

    /**
     * 总有效时长
     */
    private String total_valid_time;
    /**
     * 总距离
     */
    private String total_distance;
    /**
     * 总时长
     */
    private String total_time;
    /**
     * 总卡路里
     */
    private String total_calorie;
    /**
     * 总步数
     */
    private int step;
    /**
     * 平均每日睡眠时长
     */
    private String avg_time;

    /**
     * 今日可摄入的能量
     */
    private String need;
    /**
     * 已经消耗的能量
     */
    private String yet;

    /**
     * 骑行、步行、跑步
     */
    private List<RCBehaviorLibraryRunDTO> runList = new ArrayList<>();

    /**
     * 睡眠
     */
    private List<RCBehaviorLibrarySleepDTO> sleepList = new ArrayList<>();

    /**
     * 饮食
     */
    private List<RCBehaviorLibraryDietDTO> dietList = new ArrayList<>();

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

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getTotal_valid_time() {
        return total_valid_time;
    }

    public void setTotal_valid_time(String total_valid_time) {
        this.total_valid_time = total_valid_time;
    }

    public String getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(String total_distance) {
        this.total_distance = total_distance;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getTotal_calorie() {
        return total_calorie;
    }

    public void setTotal_calorie(String total_calorie) {
        this.total_calorie = total_calorie;
    }

    public List<RCBehaviorLibraryRunDTO> getRunList() {
        return runList;
    }

    public void setRunList(List<RCBehaviorLibraryRunDTO> runList) {
        this.runList = runList;
    }

    public List<RCBehaviorLibrarySleepDTO> getSleepList() {
        return sleepList;
    }

    public void setSleepList(List<RCBehaviorLibrarySleepDTO> sleepList) {
        this.sleepList = sleepList;
    }

    public String getAvg_time() {
        return avg_time;
    }

    public void setAvg_time(String avg_time) {
        this.avg_time = avg_time;
    }

    public List<RCBehaviorLibraryDietDTO> getDietList() {
        return dietList;
    }

    public void setDietList(List<RCBehaviorLibraryDietDTO> dietList) {
        this.dietList = dietList;
    }
}
