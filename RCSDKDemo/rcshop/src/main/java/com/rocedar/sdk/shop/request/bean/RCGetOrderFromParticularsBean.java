package com.rocedar.sdk.shop.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午4:52
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCGetOrderFromParticularsBean extends RCBean {

    public String order_id;
    public String order_type;
    public String refund_desc;
    public String payment_type;


    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getRefund_desc() {
        return refund_desc;
    }

    public void setRefund_desc(String refund_desc) {
        this.refund_desc = refund_desc;
    }
}
