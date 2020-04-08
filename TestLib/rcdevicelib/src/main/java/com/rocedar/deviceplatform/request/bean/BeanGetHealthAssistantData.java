package com.rocedar.deviceplatform.request.bean;

import com.rocedar.base.network.RCBean;

/**
 * 作者：lxz
 * 日期：17/7/21 下午5:58
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanGetHealthAssistantData extends BasePlatformBean {
    public String pn;
    public String status;
    public String message_id;
    public String type_id;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }
}
