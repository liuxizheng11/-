package com.rocedar.sdk.shop.request;

import com.rocedar.sdk.shop.request.bean.RCPostServerOrderBean;
import com.rocedar.sdk.shop.request.listener.RCGetRenewOrderListener;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsInfoListener;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsListListener;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsSkuListener;
import com.rocedar.sdk.shop.request.listener.RCPostOrderListener;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/20 下午5:38
 * 版本：V1.1.00
 * 描述：瑰柏SDK-商城接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCServerGoodsRequest {

    /**
     * 服务商品列表
     *
     * @param listener
     */
    void getGoodsList(RCGetServerGoodsListListener listener);


    /**
     * 服务商品详情（商品列表）
     *
     * @param listener
     */
    void getGoodsInfo(RCGetServerGoodsInfoListener listener, String goodsId);


    /**
     * 服务商品详情（单个商品）
     *
     * @param listener
     * @param skuId
     */
    void getGoodsSku(RCGetServerGoodsSkuListener listener, int skuId);


    /**
     * 提交订单
     *
     * @param listener
     * @param bean
     */
    void postServerOrder(RCPostOrderListener listener, RCPostServerOrderBean bean);


    /**
     * 保单续费
     */
    void getRenewOrder(RCGetRenewOrderListener listener, int orderId);
}
