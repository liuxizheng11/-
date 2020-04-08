package com.rocedar.deviceplatform.dto.record;

/**
 * 作者：lxz
 * 日期：17/7/27 下午3:52
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthReportPlanDTO {
    /**
     * 体检报告名称
     */
    private String title;
    /**
     * 体检报告详情地址
     */
    private String html;
    /**
     * 体检报告id
     */
    private int report_id;
    /**
     * 用户id
     */
    private long user_id;
    /**
     * 体检报告类型
     */
    private int type_id;
    /**
     * 体检报告时间
     */
    private long report_time;
    /**
     * 设备id
     */
    private int device_id;
    /**
     * 状态
     */
    private int  status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public long getReport_time() {
        return report_time;
    }

    public void setReport_time(long report_time) {
        this.report_time = report_time;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
