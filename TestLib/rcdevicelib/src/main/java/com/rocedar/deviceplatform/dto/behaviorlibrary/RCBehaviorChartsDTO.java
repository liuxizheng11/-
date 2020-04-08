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
public class RCBehaviorChartsDTO {

    //步行 数据
    private List<RCStepDayChartDTO> mStepDayList;
    //跑步、骑行、运动数据
    private List<RCRunDayChartDTO> mRunDayList;
    //睡眠 数据
    private List<RCSleepDayDTO> mSleepDayList;

    public List<RCSleepDayDTO> getmSleepDayList() {
        return mSleepDayList;
    }

    public void setmSleepDayList(List<RCSleepDayDTO> mSleepDayList) {
        this.mSleepDayList = mSleepDayList;
    }


    public List<RCStepDayChartDTO> getmStepDayList() {
        return mStepDayList;
    }

    public void setmStepDayList(List<RCStepDayChartDTO> mStepDayList) {
        this.mStepDayList = mStepDayList;
    }


    public List<RCRunDayChartDTO> getmRunDayList() {
        return mRunDayList;
    }

    public void setmRunDayList(List<RCRunDayChartDTO> mRunDayList) {
        this.mRunDayList = mRunDayList;
    }


    public static class RCStepDayChartDTO {
        /**
         * 步数
         */
        private int step;
        /**
         * 距离
         */
        private double distance;
        /**
         * 能量
         */
        private double calorie;
        /**
         * 设备id
         */
        private int device_id;
        /**
         * 设备名称
         */
        private String device_name;
        /**
         * 最新更新时间
         */
        private long update_time;
        /**
         * 开始时间
         */
        private int start_time;
        /**
         * 结束时间
         */
        private int end_time;

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getCalorie() {
            return calorie;
        }

        public void setCalorie(double calorie) {
            this.calorie = calorie;
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

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }
    }


    public static class RCRunDayChartDTO {
        /**
         * 跑步时长
         */
        private int time;
        /**
         * 有效时长
         */
        private int valid_time;

        /**
         * 距离
         */
        private double distance;
        /**
         * 能量
         */
        private double calorie;
        /**
         * 心率
         */
        private int heart_rate;
        /**
         * 设备id
         */
        private int device_id;
        /**
         * 设备名称
         */
        private String device_name;
        /**
         * 最新更新时间
         */
        private long update_time;
        /**
         * 开始时间
         */
        private int start_time;
        /**
         * 结束时间
         */
        private int end_time;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getValid_time() {
            return valid_time;
        }

        public void setValid_time(int valid_time) {
            this.valid_time = valid_time;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getCalorie() {
            return calorie;
        }

        public void setCalorie(double calorie) {
            this.calorie = calorie;
        }

        public int getHeart_rate() {
            return heart_rate;
        }

        public void setHeart_rate(int heart_rate) {
            this.heart_rate = heart_rate;
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

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }
    }


    public static class RCSleepDayDTO {
        /**
         * 睡眠时长
         */
        private int sleep_time;
        /**
         * 睡眠平均时长
         */
        private double sleep_time_avg;
        /**
         * 睡眠基准心率
         */
        private double standard_heart_rate;
        /**
         * 设备id
         */
        private int device_id;
        /**
         * 设备名称
         */
        private String device_name;
        /**
         * 最新更新时间
         */
        private long update_time;
        /**
         * 开始时间
         */
        private int start_time;
        /**
         * 结束时间
         */
        private int end_time;

        public int getSleep_time() {
            return sleep_time;
        }

        public void setSleep_time(int sleep_time) {
            this.sleep_time = sleep_time;
        }

        public double getSleep_time_avg() {
            return sleep_time_avg;
        }

        public void setSleep_time_avg(double sleep_time_avg) {
            this.sleep_time_avg = sleep_time_avg;
        }

        public double getStandard_heart_rate() {
            return standard_heart_rate;
        }

        public void setStandard_heart_rate(double standard_heart_rate) {
            this.standard_heart_rate = standard_heart_rate;
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

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }
    }
}
