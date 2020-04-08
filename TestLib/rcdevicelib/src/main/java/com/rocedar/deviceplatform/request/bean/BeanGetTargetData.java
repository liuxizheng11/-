package com.rocedar.deviceplatform.request.bean;

import com.rocedar.base.network.RCBean;

/**
 * 作者：lxz
 * 日期：17/7/7 下午4:20
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanGetTargetData extends BasePlatformBean {
    /**
     * 周一的日期（yyyyMMdd）
     */
    public String monday;
    /**
     * 目标类型ID
     */
    public String target_type_id;
    /**
     * 日期
     */
    public String record_date;


    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTarget_type_id() {
        return target_type_id;
    }

    public void setTarget_type_id(String target_type_id) {
        this.target_type_id = target_type_id;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }
}
