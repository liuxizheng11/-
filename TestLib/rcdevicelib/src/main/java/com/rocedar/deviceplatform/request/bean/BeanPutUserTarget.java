package com.rocedar.deviceplatform.request.bean;

/**
 * 作者：lxz
 * 日期：17/7/7 下午6:45
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanPutUserTarget extends BasePlatformBean {
    /**
     * 目标值 Map<目标ID, 目标值>
     */
    public String values;

    public String target_type_id;

    /**
     * 目标ID
     */
    public String target_id;
    /**
     * (状态值)	0： 关闭；1：开启
     */
    public String status;

    public String getTarget_type_id() {
        return target_type_id;
    }

    public void setTarget_type_id(String target_type_id) {
        this.target_type_id = target_type_id;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
