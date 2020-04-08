package com.rocedar.deviceplatform.dto.record;

/**
 * @author liuyi
 * @date 2017/3/8
 * @desc 医生报告列表DTO
 * @veison V3.3.30(动吖)
 */

public class RCHealthDoctorRecordListDTO {

    /**
     * report_name : 医生报告
     * start_time : 20161201000000
     * end_time : 20170301000000
     * detail_url :
     */

    /**
     * 报告名字
     */
    private String report_name;
    /**
     * 报告开始时间
     */
    private long start_time;
    /**
     * 报告结束时间
     */
    private long end_time;
    /**
     * 详情URL
     */
    private String detail_url;

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
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

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }
}
