//package com.rocedar.deviceplatform.dto.behaviorlibrary;
//
//import java.util.List;
//
///**
// * 作者：lxz
// * 日期：17/8/21 下午6:49
// * 版本：V1.0
// * 描述：行为图表DTO
// * <p>
// * CopyRight©北京瑰柏科技有限公司
// */
//public class RCBehaviorChartDTO {
//
//    //步行 天数据
//    private List<RCStepDayChartDTO> mStepDayList;
//    // 步行 月数据
//    private List<RCStepMonthChartDTO> mStepMonthList;
//    //步行 年数据
//    private List<RCStepMonthChartDTO> mStepYearList;
//    //跑步、骑行 天数据
//    private List<RCRunDayChartDTO> mRunDayList;
//    //跑步、骑行 月数据
//    private List<RCRunMonthData> mRunMonthList;
//    //跑步、骑行 年数据
//    private List<RCRunMonthData> mRunYearList;
//
//    //睡眠 天数据
//    private List<RCSleepDayDTO> mSleepDayList;
//    //睡眠 月数据
//    private List<RCSleepDayDTO> mSleepMonthList;
//    //睡眠 年数据
//    private List<RCSleepDayDTO> mSleepYearList;
//
//    public List<RCSleepDayDTO> getmSleepDayList() {
//        return mSleepDayList;
//    }
//
//    public void setmSleepDayList(List<RCSleepDayDTO> mSleepDayList) {
//        this.mSleepDayList = mSleepDayList;
//    }
//
//    public List<RCSleepDayDTO> getmSleepMonthList() {
//        return mSleepMonthList;
//    }
//
//    public void setmSleepMonthList(List<RCSleepDayDTO> mSleepMonthList) {
//        this.mSleepMonthList = mSleepMonthList;
//    }
//
//    public List<RCSleepDayDTO> getmSleepYearList() {
//        return mSleepYearList;
//    }
//
//    public void setmSleepYearList(List<RCSleepDayDTO> mSleepYearList) {
//        this.mSleepYearList = mSleepYearList;
//    }
//
//    public List<RCStepDayChartDTO> getmStepDayList() {
//        return mStepDayList;
//    }
//
//    public void setmStepDayList(List<RCStepDayChartDTO> mStepDayList) {
//        this.mStepDayList = mStepDayList;
//    }
//
//    public List<RCStepMonthChartDTO> getmSteoMonthList() {
//        return mStepMonthList;
//    }
//
//    public void setmSteoMonthList(List<RCStepMonthChartDTO> mSteoMonthList) {
//        this.mStepMonthList = mSteoMonthList;
//    }
//
//    public List<RCRunDayChartDTO> getmRunDayList() {
//        return mRunDayList;
//    }
//
//    public void setmRunDayList(List<RCRunDayChartDTO> mRunDayList) {
//        this.mRunDayList = mRunDayList;
//    }
//
//    public List<RCRunMonthData> getmRunMonthList() {
//        return mRunMonthList;
//    }
//
//    public void setmRunMonthList(List<RCRunMonthData> mRunMonthList) {
//        this.mRunMonthList = mRunMonthList;
//    }
//
//    public List<RCStepMonthChartDTO> getmStepYearList() {
//        return mStepYearList;
//    }
//
//    public void setmStepYearList(List<RCStepMonthChartDTO> mStepYearList) {
//        this.mStepYearList = mStepYearList;
//    }
//
//    public List<RCRunMonthData> getmRunYearList() {
//        return mRunYearList;
//    }
//
//    public void setmRunYearList(List<RCRunMonthData> mRunYearList) {
//        this.mRunYearList = mRunYearList;
//    }
//
//    public static class RCStepDayChartDTO {
//        /**
//         * 行为日期
//         */
//        private int date;
//        /**
//         * 最新更新时间
//         */
//        private String update_time;
//        /**
//         * 设备名称
//         */
//        private String device_name;
//        /**
//         * 距离
//         */
//        private String distance;
//        /**
//         * 消耗
//         */
//        private String calorie;
//        /**
//         * 步数
//         */
//        private int step;
//
//        public int getDate() {
//            return date;
//        }
//
//        public void setDate(int date) {
//            this.date = date;
//        }
//
//        public String getUpdate_time() {
//            return update_time;
//        }
//
//        public void setUpdate_time(String update_time) {
//            this.update_time = update_time;
//        }
//
//        public String getDevice_name() {
//            return device_name;
//        }
//
//        public void setDevice_name(String device_name) {
//            this.device_name = device_name;
//        }
//
//        public String getDistance() {
//            return distance;
//        }
//
//        public void setDistance(String distance) {
//            this.distance = distance;
//        }
//
//        public String getCalorie() {
//            return calorie;
//        }
//
//        public void setCalorie(String calorie) {
//            this.calorie = calorie;
//        }
//
//        public int getStep() {
//            return step;
//        }
//
//        public void setStep(int step) {
//            this.step = step;
//        }
//    }
//
//    public static class RCStepMonthChartDTO {
//        /**
//         * 行为日期
//         */
//        private int date;
//        /**
//         * 距离
//         */
//        private String distance;
//        /**
//         * 消耗
//         */
//        private String calorie;
//        /**
//         * 步数
//         */
//        private int step;
//
//        public int getStep() {
//            return step;
//        }
//
//        public void setStep(int step) {
//            this.step = step;
//        }
//
//        public int getDate() {
//            return date;
//        }
//
//        public void setDate(int date) {
//            this.date = date;
//        }
//
//        public String getDistance() {
//            return distance;
//        }
//
//        public void setDistance(String distance) {
//            this.distance = distance;
//        }
//
//        public String getCalorie() {
//            return calorie;
//        }
//
//        public void setCalorie(String calorie) {
//            this.calorie = calorie;
//        }
//    }
//
//    public static class RCRunDayChartDTO {
//        /**
//         * 行为日期
//         */
//        private int date;
//        /**
//         * 距离
//         */
//        private String distance;
//        /**
//         * 消耗
//         */
//        private String calorie;
//        /**
//         * 有效时长
//         */
//        private int valid_time;
//        /**
//         * 时长
//         */
//        private int time;
//        private List<RCRunDayMoreData> mDayMoreList;
//
//        public int getDate() {
//            return date;
//        }
//
//        public void setDate(int date) {
//            this.date = date;
//        }
//
//        public String getDistance() {
//            return distance;
//        }
//
//        public void setDistance(String distance) {
//            this.distance = distance;
//        }
//
//        public String getCalorie() {
//            return calorie;
//        }
//
//        public void setCalorie(String calorie) {
//            this.calorie = calorie;
//        }
//
//        public int getValid_time() {
//            return valid_time;
//        }
//
//        public void setValid_time(int valid_time) {
//            this.valid_time = valid_time;
//        }
//
//        public int getTime() {
//            return time;
//        }
//
//        public void setTime(int time) {
//            this.time = time;
//        }
//
//        public List<RCRunDayMoreData> getmDayMoreList() {
//            return mDayMoreList;
//        }
//
//        public void setmDayMoreList(List<RCRunDayMoreData> mDayMoreList) {
//            this.mDayMoreList = mDayMoreList;
//        }
//    }
//
//    public static class RCRunDayMoreData {
//        /**
//         * 环境（1室内，2室外）
//         */
//        private int environment;
//        /**
//         * Conduct_id
//         */
//        private int conduct_id;
//        /**
//         * 设备名称
//         */
//        private String device_name;
//        /**
//         * 数据时间
//         */
//        private long data_time;
//        /**
//         * 距离
//         */
//        private String distance_CA;
//        /**
//         * 配速
//         */
//        private String pace_CA;
//        /**
//         * 时长
//         */
//        private String time_CA;
//
//        private int device_id;
//
//        public int getDevice_id() {
//            return device_id;
//        }
//
//        public void setDevice_id(int device_id) {
//            this.device_id = device_id;
//        }
//
//        public int getConduct_id() {
//            return conduct_id;
//        }
//
//        public void setConduct_id(int conduct_id) {
//            this.conduct_id = conduct_id;
//        }
//
//        public int getEnvironment() {
//            return environment;
//        }
//
//        public void setEnvironment(int environment) {
//            this.environment = environment;
//        }
//
//        public String getDevice_name() {
//            return device_name;
//        }
//
//        public void setDevice_name(String device_name) {
//            this.device_name = device_name;
//        }
//
//        public long getData_time() {
//            return data_time;
//        }
//
//        public void setData_time(long data_time) {
//            this.data_time = data_time;
//        }
//
//        public String getDistance_CA() {
//            return distance_CA;
//        }
//
//        public void setDistance_CA(String distance_CA) {
//            this.distance_CA = distance_CA;
//        }
//
//        public String getPace_CA() {
//            return pace_CA;
//        }
//
//        public void setPace_CA(String pace_CA) {
//            this.pace_CA = pace_CA;
//        }
//
//        public String getTime_CA() {
//            return time_CA;
//        }
//
//        public void setTime_CA(String time_CA) {
//            this.time_CA = time_CA;
//        }
//    }
//
//    public static class RCRunMonthData {
//        /**
//         * 环境（1室内，2室外）
//         */
//        private int date;
//        /**
//         * 距离
//         */
//        private String distance;
//        /**
//         * 消耗
//         */
//        private String calorie;
//        /**
//         * 有效时长
//         */
//        private int valid_time;
//        /**
//         * 时长
//         */
//        private int time;
//
//        public int getDate() {
//            return date;
//        }
//
//        public void setDate(int date) {
//            this.date = date;
//        }
//
//        public String getDistance() {
//            return distance;
//        }
//
//        public void setDistance(String distance) {
//            this.distance = distance;
//        }
//
//        public String getCalorie() {
//            return calorie;
//        }
//
//        public void setCalorie(String calorie) {
//            this.calorie = calorie;
//        }
//
//        public int getValid_time() {
//            return valid_time;
//        }
//
//        public void setValid_time(int valid_time) {
//            this.valid_time = valid_time;
//        }
//
//        public int getTime() {
//            return time;
//        }
//
//        public void setTime(int time) {
//            this.time = time;
//        }
//    }
//
//    public static class RCSleepDayDTO{
//        private int date;
//        private int sleep_time;
//        private List<RCSleepDayList> mDayList;
//
//        public int getDate() {
//            return date;
//        }
//
//        public void setDate(int date) {
//            this.date = date;
//        }
//
//        public int getSleep_time() {
//            return sleep_time;
//        }
//
//        public void setSleep_time(int sleep_time) {
//            this.sleep_time = sleep_time;
//        }
//
//        public List<RCSleepDayList> getmDayList() {
//            return mDayList;
//        }
//
//        public void setmDayList(List<RCSleepDayList> mDayList) {
//            this.mDayList = mDayList;
//        }
//    }
//    public static class RCSleepDayList{
//        private int device_id;
//        private long fall_time;
//        private long wake_time;
//        private String device_name;
//        private int sleep_time;
//
//        public int getDevice_id() {
//            return device_id;
//        }
//
//        public void setDevice_id(int device_id) {
//            this.device_id = device_id;
//        }
//
//        public long getFall_time() {
//            return fall_time;
//        }
//
//        public void setFall_time(long fall_time) {
//            this.fall_time = fall_time;
//        }
//
//        public long getWake_time() {
//            return wake_time;
//        }
//
//        public void setWake_time(long wake_time) {
//            this.wake_time = wake_time;
//        }
//
//        public String getDevice_name() {
//            return device_name;
//        }
//
//        public void setDevice_name(String device_name) {
//            this.device_name = device_name;
//        }
//
//        public int getSleep_time() {
//            return sleep_time;
//        }
//
//        public void setSleep_time(int sleep_time) {
//            this.sleep_time = sleep_time;
//        }
//    }
//}
