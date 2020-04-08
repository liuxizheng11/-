package com.rcoedar.sdk.healthclass.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午4:51
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCGetHealthClassListBean extends RCBean {

    public String type_id;
    public String pn;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }
}
