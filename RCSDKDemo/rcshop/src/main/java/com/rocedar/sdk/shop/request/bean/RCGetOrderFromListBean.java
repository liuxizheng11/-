package com.rocedar.sdk.shop.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午4:51
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCGetOrderFromListBean extends RCBean {

    //    status	订单状态（多个状态英文逗号隔开）
//    pn	页码（从0开始）
    public String status;
    public String pn;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }
}
