package com.rocedar.deviceplatform.request.bean;

/**
 * 作者：lxz
 * 日期：17/7/27 下午4:00
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanGetHealthRecordData extends BasePlatformBean {
    public String pn;
    /**
     * 体检报告标题
     */
    public String title;
    /**
     * 上传图片地址
     */
    public String img_url;
    /**
     * 备注
     */
    public String remarks;
    /**
     * 体检报告时间
     */
    public String report_date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }
}
