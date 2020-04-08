package com.rocedar.deviceplatform.dto.behaviorlibrary;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/8/21 下午6:49
 * 版本：V1.0
 * 描述：行为图表DTO
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorRecordDTO {

    //跑步、骑行 数据
    private List<RCRunDTO> mRunDayList;

    //睡眠 数据
    private List<RCSleepDayDTO> mSleepDayList;

    public List<RCSleepDayDTO> getmSleepDayList() {
        return mSleepDayList;
    }

    public void setmSleepDayList(List<RCSleepDayDTO> mSleepDayList) {
        this.mSleepDayList = mSleepDayList;
    }


    public List<RCRunDTO> getmRunDayList() {
        return mRunDayList;
    }

    public void setmRunDayList(List<RCRunDTO> mRunDayList) {
        this.mRunDayList = mRunDayList;
    }


    public static class RCRunDTO {
        /**
         * conduct_id
         */

        private int conduct_id;
        /**
         * 1：室内 2：室外
         */
        private int environment;
        /**
         * 设备名称
         */
        private String device_name;
        /**
         * 设备id
         */
        private int device_id;
        /**
         * 跑步距离
         */
        private double distance;

        /**
         * 数据时间
         */
        private long data_time;
        /**
         * 配速
         */
        private int pace;
        /**
         * 能量
         */
        private double calorie;
        /**
         * 0：无效  1有效
         */
        private int is_active;
        /**
         * 跑步时长
         */
        private int time;

        public int getEnvironment() {
            return environment;
        }

        public int getConduct_id() {
            return conduct_id;
        }

        public void setConduct_id(int conduct_id) {
            this.conduct_id = conduct_id;
        }

        public void setEnvironment(int environment) {
            this.environment = environment;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public int getDevice_id() {
            return device_id;
        }

        public void setDevice_id(int device_id) {
            this.device_id = device_id;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public long getData_time() {
            return data_time;
        }

        public void setData_time(long data_time) {
            this.data_time = data_time;
        }

        public int getPace() {
            return pace;
        }

        public void setPace(int pace) {
            this.pace = pace;
        }

        public double getCalorie() {
            return calorie;
        }

        public void setCalorie(double calorie) {
            this.calorie = calorie;
        }

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }

    public static class RCSleepDayDTO {
        /**
         * 入睡时间
         */
        private long fall_time;
        /**
         * 醒来时间
         */
        private long wake_time;
        /**
         * 设备名称
         */
        private String device_name;
        /**
         * 设备id
         */
        private int device_id;
        /**
         * 数据时间
         */
        private long data_time;
        /**
         * 0：无效  1有效
         */
        private int is_active;
        /**
         * 睡眠时长
         */
        private int sleep_time;

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

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
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

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public int getSleep_time() {
            return sleep_time;
        }

        public void setSleep_time(int sleep_time) {
            this.sleep_time = sleep_time;
        }
    }
}
