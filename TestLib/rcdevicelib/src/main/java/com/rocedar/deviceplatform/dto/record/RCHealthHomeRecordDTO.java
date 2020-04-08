package com.rocedar.deviceplatform.dto.record;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 我的健康档案DTO
 * @veison V3.3.30(动吖)
 */

public class RCHealthHomeRecordDTO {

    /**
     * conducts : [{"indicators":[{"indicator_unit":"","indicator_value":"","indicator_name":""}],
     * "conduct_id":-1,"conduct_time":-1,"conduct_name":""}]
     * record_doctor : {"record":""}
     * plan : {"report":""}
     * bmi : {"bmi":14.7,"exception_name":"","weight":60,"height":170}
     * user : {"birthday":19910524,"user_name":"","portrait":""}
     */
    /**
     * 问诊记录
     */
    private RecordDoctorDTO record_doctor;
    /**
     * 专属方案
     */
    private PlanDTO plan;

    /**
     * BMI信息
     */
    private BmiDTO bmi;
    /**
     * 用户信息
     */
    private UserDTO user;
    /**
     * 行为数据
     */
    private List<ConductsDTO> conducts;

    private String labels;

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }


    public RecordDoctorDTO getRecord_doctor() {
        return record_doctor;
    }

    public void setRecord_doctor(RecordDoctorDTO record_doctor) {
        this.record_doctor = record_doctor;
    }

    public PlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDTO plan) {
        this.plan = plan;
    }

    public BmiDTO getBmi() {
        return bmi;
    }

    public void setBmi(BmiDTO bmi) {
        this.bmi = bmi;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<ConductsDTO> getConducts() {
        return conducts;
    }

    public void setConducts(List<ConductsDTO> conducts) {
        this.conducts = conducts;
    }

    public static class RecordDoctorDTO {
        /**
         * record :
         */
        /**
         * 记录内容
         */
        private String record;

        public String getRecord() {
            return record;
        }

        public void setRecord(String record) {
            this.record = record;
        }
    }

    public static class PlanDTO {
        /**
         * report :
         */
        /**
         * 报告内容
         */
        private String report;

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }
    }

    public static class BmiDTO {
        /**
         * bmi : 14.7
         * exception_name :
         * weight : 60
         * height : 170
         */
        /**
         * BMI
         */
        private double bmi;
        /**
         * 异常名
         */
        private String exception_name;
        /**
         * 体重（KG）
         */
        private double weight;
        /**
         * 身高（CM）
         */
        private int height;

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public String getException_name() {
            return exception_name;
        }

        public void setException_name(String exception_name) {
            this.exception_name = exception_name;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class UserDTO {
        /**
         * birthday : 19910524
         * user_name :
         * portrait :
         */

        /**
         * 用户生日
         */
        private long birthday;
        /**
         * 用户名
         */
        private String user_name;
        /**
         * 头像地址
         */
        private String portrait;

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }
    }

    public static class ConductsDTO {
        /**
         * indicators : [{"indicator_unit":"","indicator_value":"","indicator_name":""}]
         * conduct_id : -1
         * conduct_time : -1
         * conduct_name :
         */
        /**
         * 行为ID  -1: 体征指标
         */
        private int conduct_id;
        /**
         * 行为时间
         */
        private long conduct_time;
        /**
         * 行为名字
         */
        private String conduct_name;

        /**
         * 颜色值
         */
        private String conduct_color;
        /**
         * 历史url
         */
        private String history_url;

        //主指标ID
        private int indicatorId;

        public int getIndicatorId() {
            return indicatorId;
        }

        public void setIndicatorId(int indicatorId) {
            this.indicatorId = indicatorId;
        }

        /**
         * 指标数据
         */
        private List<IndicatorsDTO> indicators;

        public int getConduct_id() {
            return conduct_id;
        }

        public void setConduct_id(int conduct_id) {
            this.conduct_id = conduct_id;
        }

        public long getConduct_time() {
            return conduct_time;
        }

        public void setConduct_time(long conduct_time) {
            this.conduct_time = conduct_time;
        }

        public String getConduct_name() {
            return conduct_name;
        }

        public void setConduct_name(String conduct_name) {
            this.conduct_name = conduct_name;
        }

        public String getConduct_color() {
            return conduct_color;
        }

        public void setConduct_color(String conduct_color) {
            this.conduct_color = conduct_color;
        }

        public String getHistory_url() {
            return history_url;
        }

        public void setHistory_url(String history_url) {
            this.history_url = history_url;
        }

        public List<IndicatorsDTO> getIndicators() {
            return indicators;
        }

        public void setIndicators(List<IndicatorsDTO> indicators) {
            this.indicators = indicators;
        }

        public static class IndicatorsDTO {
            /**
             * indicator_unit :
             * indicator_value :
             * indicator_name :
             */
            /**
             * 指标单位
             */
            private String indicator_unit;
            /**
             * 指标值
             */
            private String indicator_value;
            /**
             * 指标名
             */
            private String indicator_name;

            public String getIndicator_unit() {
                return indicator_unit;
            }

            public void setIndicator_unit(String indicator_unit) {
                this.indicator_unit = indicator_unit;
            }

            public String getIndicator_value() {
                return indicator_value;
            }

            public void setIndicator_value(String indicator_value) {
                this.indicator_value = indicator_value;
            }

            public String getIndicator_name() {
                return indicator_name;
            }

            public void setIndicator_name(String indicator_name) {
                this.indicator_name = indicator_name;
            }
        }
    }
}
