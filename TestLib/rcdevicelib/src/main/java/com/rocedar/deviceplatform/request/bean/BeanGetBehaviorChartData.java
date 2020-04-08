package com.rocedar.deviceplatform.request.bean;

/**
 * 作者：lxz
 * 日期：17/11/6 下午6:03
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanGetBehaviorChartData extends BasePlatformBean {
    /**
     * 数据时间
     */
    public String end_date;
    /**
     * 行为id
     */
    public String conduct_id;
    /**
     * 3个类型day,week,month
     */
    public String type;

    public String getConduct_id() {
        return conduct_id;
    }

    public void setConduct_id(String conduct_id) {
        this.conduct_id = conduct_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
