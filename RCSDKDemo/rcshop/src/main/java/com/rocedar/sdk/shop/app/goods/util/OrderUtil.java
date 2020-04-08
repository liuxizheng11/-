package com.rocedar.sdk.shop.app.goods.util;

import android.content.Context;

import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.shop.request.bean.RCPostServerOrderBean;
import com.rocedar.sdk.shop.request.bean.RCPostServerOrderFDBean;
import com.rocedar.sdk.shop.request.bean.RCPostServerOrderXYBean;
import com.rocedar.sdk.shop.request.bean.RCPostServerOrderXunYiBean;
import com.rocedar.sdk.shop.request.impl.RCServerGoodsImpl;
import com.rocedar.sdk.shop.request.listener.RCPostOrderListener;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/12 下午4:47
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class OrderUtil<T extends RCPostServerOrderBean> {


    public static void saveOrder(Context context, RCHandler handler,
                                 String typeId, String goodId, String skuId, String outerId,
                                 String phoneNo, String userID, String cardId, String trueName,
                                 String nickName, RCPostOrderListener listener) {
        RCPostServerOrderBean orderBean = new RCPostServerOrderBean();
        orderBean.setOrderTypeId(typeId);
        orderBean.setGoods_id(goodId);
        orderBean.setSku_id(skuId);
        orderBean.setOuter_id(outerId);
        RCPostServerOrderXYBean bean = new RCPostServerOrderXYBean();
        bean.setPhone(phoneNo);
        bean.setRelation_user_id(userID);
        bean.setTrue_name(trueName);
        bean.setCard_id(cardId);
        orderBean.setExtra_param(bean);
        saveOrder(context, handler, orderBean, listener);
    }


    public static void saveOrder(Context context, RCHandler handler, String typeId,
                                 String goodId, String skuId, String outerId,
                                 String phoneNo, String userID, String nickName,
                                 RCPostOrderListener listener) {
        RCPostServerOrderBean orderBean = new RCPostServerOrderBean();
        orderBean.setOrderTypeId(typeId);
        orderBean.setGoods_id(goodId);
        orderBean.setSku_id(skuId);
        orderBean.setOuter_id(outerId);
        RCPostServerOrderFDBean bean = new RCPostServerOrderFDBean();
        bean.setPhone(phoneNo);
        bean.setNick_name(nickName);
        bean.setRelation_user_id(userID);
        orderBean.setExtra_param(bean);
        saveOrder(context, handler, orderBean, listener);
    }

    /**
     * 协议问诊
     *
     * @param context
     * @param handler
     * @param goodId
     * @param skuId
     * @param advice_id
     * @param listener
     */
    public static void saveOrder(Context context, RCHandler handler,
                                 String goodId, String skuId,
                                 String advice_id,String typeId,
                                 RCPostOrderListener listener) {
        RCPostServerOrderBean orderBean = new RCPostServerOrderBean();
        orderBean.setOrderTypeId(typeId);
        orderBean.setGoods_id(goodId);
        orderBean.setSku_id(skuId);
        RCPostServerOrderXunYiBean bean = new RCPostServerOrderXunYiBean();
        bean.setAdvice_id(advice_id);
        orderBean.setExtra_param(bean);
        saveOrder(context, handler, orderBean, listener);
    }

    public static void saveOrder(Context context, final RCHandler handler,
                                 RCPostServerOrderBean baseBean, final RCPostOrderListener listener) {
        handler.sendMessage(RCHandler.START);
        new RCServerGoodsImpl(context).postServerOrder(new RCPostOrderListener() {
            @Override
            public void getDataSuccess(int orderId) {
                listener.getDataSuccess(orderId);
                handler.sendMessage(RCHandler.GETDATA_OK);
            }


            @Override
            public void getDataError(int status, String msg) {
                listener.getDataError(status, msg);
                handler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, baseBean);
    }


}
