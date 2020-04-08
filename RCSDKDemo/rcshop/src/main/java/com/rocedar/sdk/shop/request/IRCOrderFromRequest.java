package com.rocedar.sdk.shop.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.shop.enums.PaymentType;
import com.rocedar.sdk.shop.request.listener.RCGetOrderStatusListener;
import com.rocedar.sdk.shop.request.listener.RCOrderFromListListener;
import com.rocedar.sdk.shop.request.listener.RCOrderFromParticularsListener;
import com.rocedar.sdk.shop.request.listener.RCServerGoodsParticularsListener;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午3:04
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCOrderFromRequest {
    /**
     * 13.1 订单列表
     *
     * @param status       订单状态
     * @param pn           页码
     * @param listListener
     */
    void getOrderFromListData(String status, int pn, RCOrderFromListListener listListener);

    /**
     * 13.2 订单详情
     *
     * @param order_id   订单id
     * @param order_type 订单类型
     * @param listener
     */
    void getOrderFeomDetailData(int order_id, int order_type, RCOrderFromParticularsListener listener);

    /**
     *  13.21  13.22  订单详情（家庭医生、协议无忧）
     * @param order_id
     * @param order_type
     * @param listener
     */
    void getServerGoodsOrderParticulars(int order_id, int order_type,RCServerGoodsParticularsListener listener);
    /**
     * 支付状态查询
     *
     * @param listener
     */
    void getOrderPayStatus(String orderId, PaymentType orderType, final RCGetOrderStatusListener listener);

    /**
     * 13.5 取消订单
     *
     * @param order_id 订单id
     * @param listener
     */
    void postOrderFromCancel(int order_id, IRCPostListener listener);

    /**
     * 13.6 退款
     *
     * @param order_id    订单id
     * @param refund_desc 退款原因
     * @param listener
     */
    void postOrderFromRefund(int order_id, String refund_desc, IRCPostListener listener);
}
