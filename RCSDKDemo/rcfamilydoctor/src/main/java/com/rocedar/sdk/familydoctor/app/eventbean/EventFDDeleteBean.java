package com.rocedar.sdk.familydoctor.app.eventbean;

/**
 * 项目名称：平台库-行为数据
 * <p>
 * 作者：phj
 * 日期：2018/2/26 下午2:59
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class EventFDDeleteBean extends EventFDBaseBean {
    public String doctor_id;

    public EventFDDeleteBean(int type) {
        super(type);
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
}
