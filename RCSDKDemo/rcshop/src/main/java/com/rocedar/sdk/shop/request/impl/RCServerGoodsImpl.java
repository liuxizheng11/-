package com.rocedar.sdk.shop.request.impl;

import android.content.Context;

import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.shop.dto.RCRenewOrderDTO;
import com.rocedar.sdk.shop.dto.RCServerGoodsInfoDTO;
import com.rocedar.sdk.shop.dto.RCServerGoodsListDTO;
import com.rocedar.sdk.shop.request.IRCServerGoodsRequest;
import com.rocedar.sdk.shop.request.bean.RCGetGoodsRenewBean;
import com.rocedar.sdk.shop.request.bean.RCPostServerOrderBean;
import com.rocedar.sdk.shop.request.listener.RCGetRenewOrderListener;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsInfoListener;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsListListener;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsSkuListener;
import com.rocedar.sdk.shop.request.listener.RCPostOrderListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/20 下午5:43
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品接口实现
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCServerGoodsImpl implements IRCServerGoodsRequest {


    private Context mContext;

    public RCServerGoodsImpl(Context context) {
        this.mContext = context;
    }


    @Override
    public void getGoodsList(final RCGetServerGoodsListListener listener) {
        RCBean bean = new RCBean();
        bean.setActionName("/p/server/goods/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONArray array = data.optJSONArray("result");
                List<RCServerGoodsListDTO> listDTOS = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    RCServerGoodsListDTO dto = new RCServerGoodsListDTO();
                    dto.setGoods_id(object.optInt("goods_id"));
                    dto.setGoods_desc(object.optString("goods_desc"));
                    dto.setGoods_Name(object.optString("goods_Name"));
                    listDTOS.add(dto);
                }
                listener.getDataSuccess(listDTOS);
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
    }

    @Override
    public void getGoodsInfo(final RCGetServerGoodsInfoListener listener, String goodsId) {
        RCBean bean = new RCBean();
        bean.setActionName("/p/server/goods/" + goodsId + "/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                List<RCServerGoodsInfoDTO> dtos = new ArrayList<>();
                JSONArray array = data.optJSONObject("result").optJSONArray("goods_skus");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    RCServerGoodsInfoDTO dto = new RCServerGoodsInfoDTO();
                    if (i == 0) {
                        dto.setSelect(true);
                    }
                    dto.setSkuId(object.optInt("sku_id"));
                    dto.setGoodsId(object.optInt("goods_id"));
                    dto.setOuterId(object.optInt("outer_id"));
                    dto.setSkuName(object.optString("sku_name"));
                    dto.setSkuBanner(object.optString("sku_banner"));
                    dto.setSkuTitle(object.optString("sku_title"));
                    dto.setSkuSubTitle(object.optString("sku_subtitle"));
                    dto.setPurchaseNotes(object.optString("purchase_notes"));
                    dto.setSku_icon(object.optString("sku_icon"));
                    dto.setValidityPeriod(object.optInt("validity_period"));
                    dto.setValidityPeriodName(object.optString("validity_period_name"));
                    dto.setFee((float) object.optDouble("fee"));
                    dto.setFeeName(object.optString("fee_name"));
                    dto.setSkuDesc(object.optString("sku_desc"));
                    dto.setStatus(object.optInt("status"));
                    dto.setSelfUse(object.optInt("self_use") == 0);
                    dto.setTypeId(object.optInt("goods_type_id"));
                    dtos.add(dto);
                }
                listener.getDataSuccess(dtos, data.optJSONObject("result").optString("goods_name"));
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
    }

    @Override
    public void getGoodsSku(final RCGetServerGoodsSkuListener listener, int skuId) {
        RCBean bean = new RCBean();
        bean.setActionName("/p/server/goods/sku/" + skuId + "/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject object = data.optJSONObject("result");
                RCServerGoodsInfoDTO dto = new RCServerGoodsInfoDTO();
                dto.setSkuId(object.optInt("sku_id"));
                dto.setGoodsId(object.optInt("goods_id"));
                dto.setOuterId(object.optInt("outer_id"));
                dto.setSkuName(object.optString("sku_name"));
                dto.setSkuBanner(object.optString("sku_banner"));
                dto.setSkuTitle(object.optString("sku_title"));
                dto.setSkuSubTitle(object.optString("sku_subtitle"));
                dto.setValidityPeriod(object.optInt("validity_period"));
                dto.setValidityPeriodName(object.optString("validity_period_name"));
                dto.setFee((float) object.optDouble("fee"));
                dto.setFeeName(object.optString("fee_name"));
                dto.setSkuDesc(object.optString("sku_desc"));
                dto.setStatus(object.optInt("status"));
                dto.setSelfUse(object.optInt("self_use") == 0);
                dto.setPurchaseNotes(object.optString("purchase_notes"));
                dto.setTypeId(object.optInt("goods_type_id"));
                listener.getDataSuccess(dto);
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
    }

    @Override
    public void postServerOrder(final RCPostOrderListener listener, RCPostServerOrderBean bean) {
        bean.setActionName("/p/server/order/" + bean.getOrderTypeId() + "/");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess(data.optJSONObject("result").optInt("order_id"));
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    @Override
    public void getRenewOrder(final RCGetRenewOrderListener listener, int orderId) {
        RCGetGoodsRenewBean bean = new RCGetGoodsRenewBean();
        bean.setActionName("/p/server/order/renew/");
        bean.setOrder_id(orderId + "");
        RCRequestNetwork.NetWorkGetData(mContext, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                JSONObject object = data.optJSONObject("result");
                RCRenewOrderDTO dto = new RCRenewOrderDTO();
                dto.setSkuName(object.optString("sku_name"));
                dto.setServer_time(object.optString("server_time"));
                dto.setPhoneNo(object.optLong("phone"));
                dto.setTrueName(object.optString("true_name"));
                dto.setFeeName(object.optString("fee_name"));
                dto.setGoodsId(object.optInt("goods_id"));
                dto.setValidityPeriodName(object.optString("validity_period_name"));
                dto.setSkuId(object.optInt("sku_id"));
                dto.setOuterId(object.optInt("outer_id"));
                dto.setRelation_user_id(object.optLong("relation_user_id"));
                dto.setPurchaseNotes(object.optString("purchase_notes"));
                dto.setTypeId(object.optInt("goods_type_id"));
                dto.setIdCardNo(object.optString("card_id"));
                listener.getDataSuccess(dto);
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
    }


}
