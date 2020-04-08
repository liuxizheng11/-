package com.rocedar.deviceplatform.dto.record;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 近三月行为报告DTO
 * @veison V3.3.30(动吖)
 */

public class RCHealthActionRecordDTO {

    /**
     * dataSources : 已绑定的智能穿戴设备
     * start_time : 20170301000000
     * end_time : 20170307000000
     * report : 近三个月日均步数10008步，日均步行距离3.6km。
     * values : [{"indicator_name":"","indicator_value":"10082","subsidiary_value":null,"indicator_unit":"步","indicator_time":20170529000000,"exception_level":0,"subsidiary_exception_level":0}]
     */

    /**
     * 数据来源描述
     */
    private String dataSources;
    /**
     * 开始时间
     */
    private long start_time;
    /**
     * 结束时间
     */
    private long end_time;
    /**
     * 报告内容
     */
    private String report;
    /**
     * 行为指标数据
     */
    private List<ValuesDTO> values;

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

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

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public List<ValuesDTO> getValues() {
        return values;
    }

    public void setValues(List<ValuesDTO> values) {
        this.values = values;
    }

    public static class ValuesDTO {
        /**
         * indicator_name :
         * indicator_value : 10082
         * subsidiary_value : null
         * indicator_unit : 步
         * indicator_time : 20170529000000
         * exception_level : 0
         * subsidiary_exception_level : 0
         */

        /**
         * 指标名
         */
        private String indicator_name;
        /**
         * 指标值
         */
        private String indicator_value;
        /**
         * 低压值
         */
        private String subsidiary_value;
        /**
         * 指标单位
         */
        private String indicator_unit;
        /**
         * 指标时间
         */
        private long indicator_time;
        /**
         * 异常等级
         * 0为正常
         */
        private int exception_level;
        /**
         * 低压异常等级
         * 0为正常
         */
        private int subsidiary_exception_level;
        /**
         * 血糖文案
         */
        private String indicator_interval;

        public String getIndicator_name() {
            return indicator_name;
        }

        public void setIndicator_name(String indicator_name) {
            this.indicator_name = indicator_name;
        }

        public String getIndicator_value() {
            return indicator_value;
        }

        public void setIndicator_value(String indicator_value) {
            this.indicator_value = indicator_value;
        }

        public String getSubsidiary_value() {
            return subsidiary_value;
        }

        public void setSubsidiary_value(String subsidiary_value) {
            this.subsidiary_value = subsidiary_value;
        }

        public String getIndicator_unit() {
            return indicator_unit;
        }

        public void setIndicator_unit(String indicator_unit) {
            this.indicator_unit = indicator_unit;
        }

        public long getIndicator_time() {
            return indicator_time;
        }

        public void setIndicator_time(long indicator_time) {
            this.indicator_time = indicator_time;
        }

        public int getException_level() {
            return exception_level;
        }

        public void setException_level(int exception_level) {
            this.exception_level = exception_level;
        }

        public int getSubsidiary_exception_level() {
            return subsidiary_exception_level;
        }

        public void setSubsidiary_exception_level(int subsidiary_exception_level) {
            this.subsidiary_exception_level = subsidiary_exception_level;
        }

        public String getIndicator_interval() {
            return indicator_interval;
        }

        public void setIndicator_interval(String indicator_interval) {
            this.indicator_interval = indicator_interval;
        }
    }
}
