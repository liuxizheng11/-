package com.rocedar.deviceplatform.dto.record;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 我的健康档案DTO
 * @veison V3.3.30(动吖)
 */

public class RCHealthRecordDataDTO {

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
     * 病史
     */
    private List<LabelsDTO> labels;
    /**
     * 行为数据
     */
    private List<ConductsDTO> conducts;
    /**
     * 指标数据
     */
    private List<SignsDataDTO> mSignsList;

    public List<SignsDataDTO> getmSignsList() {
        return mSignsList;
    }

    public void setmSignsList(List<SignsDataDTO> mSignsList) {
        this.mSignsList = mSignsList;
    }

    public List<LabelsDTO> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelsDTO> labels) {
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

    public static class LabelsDTO {
        private int label_id;
        private String label_name;
        private int label_type_id;

        public int getLabel_id() {
            return label_id;
        }

        public void setLabel_id(int label_id) {
            this.label_id = label_id;
        }

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }

        public int getLabel_type_id() {
            return label_type_id;
        }

        public void setLabel_type_id(int label_type_id) {
            this.label_type_id = label_type_id;
        }
    }

    public static class ConductsDTO {
        /**
         * 行为ID
         */
        private int id;
        /**
         * 行为时间
         */
        private long time;
        /**
         * 行为名字
         */
        private String name;

        /**
         * 颜色值
         */
        private String color;
        /**
         * 历史url
         */
        private String url;

        //主指标ID
        private int indicatorId;
        /**
         * 指标数据
         */
        private List<IndicatorsDTO> indicators;

        public int getIndicatorId() {
            return indicatorId;
        }

        public void setIndicatorId(int indicatorId) {
            this.indicatorId = indicatorId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<IndicatorsDTO> getIndicators() {
            return indicators;
        }

        public void setIndicators(List<IndicatorsDTO> indicators) {
            this.indicators = indicators;
        }

        public static class IndicatorsDTO {
            /**
             "name": "步数",
             "indicator_id": 4000,
             "value": "1343",
             "sub_value": "",
             "unit": "步",
             "time": -1,
             "exception_level": 0,
             "sub_exception_level": 0
             */
            /**
             * 指标单位
             */
            private String name;

            private int indicator_id;
            /**
             * 指标值
             */
            private String value;
            /**
             * 附属 指标值
             */
            private String sub_value;
            /**
             * 单位
             */
            private String unit;
            /**
             * 时间
             */
            private long time;
            private int exception_level;
            private int sub_exception_level;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIndicator_id() {
                return indicator_id;
            }

            public void setIndicator_id(int indicator_id) {
                this.indicator_id = indicator_id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getSub_value() {
                return sub_value;
            }

            public void setSub_value(String sub_value) {
                this.sub_value = sub_value;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getException_level() {
                return exception_level;
            }

            public void setException_level(int exception_level) {
                this.exception_level = exception_level;
            }

            public int getSub_exception_level() {
                return sub_exception_level;
            }

            public void setSub_exception_level(int sub_exception_level) {
                this.sub_exception_level = sub_exception_level;
            }
        }


    }

    public static class SignsDataDTO {
        private int indicator_id;
        private String name;
        private String unit;
        private String value;
        private String sub_value;
        private List<SignsValueDTO> mList;
        private List<SignsValueDTO> subList;

        public String getSub_value() {
            return sub_value;
        }

        public void setSub_value(String sub_value) {
            this.sub_value = sub_value;
        }

        public int getIndicator_id() {
            return indicator_id;
        }

        public void setIndicator_id(int indicator_id) {
            this.indicator_id = indicator_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<SignsValueDTO> getmList() {
            return mList;
        }

        public void setmList(List<SignsValueDTO> mList) {
            this.mList = mList;
        }

        public List<SignsValueDTO> getSubList() {
            return subList;
        }

        public void setSubList(List<SignsValueDTO> subList) {
            this.subList = subList;
        }

    }

    public static class SignsValueDTO {
        private float min_value;
        private String unit;
        private String exception_name;
        private float max_value;

        public float getMin_value() {
            if ((int) min_value == min_value) {
                return (int) min_value;
            } else {
                return min_value;
            }
        }

        public void setMin_value(float min_value) {
            this.min_value = min_value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getException_name() {
            return exception_name;
        }

        public void setException_name(String exception_name) {
            this.exception_name = exception_name;
        }

        public float getMax_value() {
            if ((int) max_value == max_value) {
                return (int) max_value;
            } else {
                return max_value;
            }
        }

        public void setMax_value(float max_value) {

            this.max_value = max_value;
        }
    }
}
