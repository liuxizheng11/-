package com.rocedar.sdk.shop.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/12 下午4:49
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCPostServerOrderBean extends RCBean {

    public String orderTypeId;
    public String goods_id;
    public String sku_id;
    public String outer_id;
    public String extra_param;

    public String getExtra_param() {
        return extra_param;
    }

    public void setExtra_param(RCPostServerOrderFDBean fdBean) {
        this.extra_param = fdBean.getExtra_param();
    }

    public void setExtra_param(RCPostServerOrderXYBean xyBean) {
        this.extra_param = xyBean.getExtra_param();
    }
    public void setExtra_param(RCPostServerOrderXunYiBean xyBean) {
        this.extra_param = xyBean.getExtra_param();
    }

    public String getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getOuter_id() {
        return outer_id;
    }

    public void setOuter_id(String outer_id) {
        this.outer_id = outer_id;
    }

}
