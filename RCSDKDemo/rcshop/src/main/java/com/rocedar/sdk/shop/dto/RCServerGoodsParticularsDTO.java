package com.rocedar.sdk.shop.dto;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/10/15 上午9:39
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCServerGoodsParticularsDTO {
    //Int	订单Id
    private int order_id;
    // String	订单名
    private String order_name;
    // String	商品icon
    private String order_icon;
    //Float	实付金额
    private float fee_paid;
    //float	订单金额
    private float fee;
    // Int	服务期限
    private String validity_period;
    // int	0，他人；1，自用。
    private int server_user_type;
    // 	Int	0， 未开通；1，已开通
    private int server_status;
    // Int	订单状态
    private int status;
    private String nick_name;
    private long phone;

    private List<RCOrderFromParticularsProgressDTO> progressDTOList;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_icon() {
        return order_icon;
    }

    public void setOrder_icon(String order_icon) {
        this.order_icon = order_icon;
    }

    public float getFee_paid() {
        return fee_paid;
    }

    public void setFee_paid(float fee_paid) {
        this.fee_paid = fee_paid;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getValidity_period() {
        return validity_period;
    }

    public void setValidity_period(String validity_period) {
        this.validity_period = validity_period;
    }

    public int getServer_user_type() {
        return server_user_type;
    }

    public void setServer_user_type(int server_user_type) {
        this.server_user_type = server_user_type;
    }

    public int getServer_status() {
        return server_status;
    }

    public void setServer_status(int server_status) {
        this.server_status = server_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<RCOrderFromParticularsProgressDTO> getProgressDTOList() {
        return progressDTOList;
    }

    public void setProgressDTOList(List<RCOrderFromParticularsProgressDTO> progressDTOList) {
        this.progressDTOList = progressDTOList;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
