package com.rocedar.sdk.shop.dto;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午3:48
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCOrderFromParticularsProgressDTO {

    // Long	时间
    private long order_time;
    //String	描述
    private String order_desc;

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    public String getOrder_desc() {
        return order_desc;
    }

    public void setOrder_desc(String order_desc) {
        this.order_desc = order_desc;
    }
}
