package com.rocedar.sdk.shop.request.impl;

import android.content.Context;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.shop.dto.RCOrderFromListDTO;
import com.rocedar.sdk.shop.dto.RCOrderFromParticularsDTO;
import com.rocedar.sdk.shop.dto.RCOrderFromParticularsProgressDTO;
import com.rocedar.sdk.shop.dto.RCServerGoodsParticularsDTO;
import com.rocedar.sdk.shop.enums.PaymentType;
import com.rocedar.sdk.shop.request.IRCOrderFromRequest;
import com.rocedar.sdk.shop.request.bean.RCGetOrderFromListBean;
import com.rocedar.sdk.shop.request.bean.RCGetOrderFromParticularsBean;
import com.rocedar.sdk.shop.request.listener.RCGetOrderStatusListener;
import com.rocedar.sdk.shop.request.listener.RCOrderFromListListener;
import com.rocedar.sdk.shop.request.listener.RCOrderFromParticularsListener;
import com.rocedar.sdk.shop.request.listener.RCServerGoodsParticularsListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午4:49
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCOrderFromImpl implements IRCOrderFromRequest {
    private static RCOrderFromImpl rcOrderFrom;

    public static RCOrderFromImpl getInstance(Context context) {
        if (rcOrderFrom == null)
            rcOrderFrom = new RCOrderFromImpl(context);
        return rcOrderFrom;
    }

    private Context mContext;

    public RCOrderFromImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 13.1 订单列表
     *
     * @param status       订单状态
     * @param pn           页码
     * @param listListener
     */
    @Override
    public void getOrderFromListData(final String status, int pn, final RCOrderFromListListener listListener) {
        RCGetOrderFromListBean mBean = new RCGetOrderFromListBean();
        mBean.setActionName("/p/server/order/");
        mBean.setStatus(status);
        mBean.setPn(pn + "");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject jsonObject) {
                        JSONArray mja = jsonObject.optJSONObject("result").optJSONArray("orders");
                        List<RCOrderFromListDTO> mList = new ArrayList<>();

                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCOrderFromListDTO mDTO = new RCOrderFromListDTO();
                            mDTO.setFee((float) mjo.optDouble("fee"));
                            mDTO.setFee_paid((float) mjo.optDouble("fee_paid"));
                            mDTO.setCreate_time(mjo.optLong("create_time"));
                            mDTO.setExpire_time(mjo.optLong("expire_time"));
                            mDTO.setOrder_name(mjo.optString("order_name"));
                            mDTO.setOrder_icon(mjo.optString("order_icon"));

                            mDTO.setOrder_id(mjo.optInt("order_id"));
                            mDTO.setOrder_type(mjo.optInt("order_type"));
                            mDTO.setPay_time(mjo.optLong("pay_time"));
                            mDTO.setStatus(mjo.optInt("status"));
                            if (mjo.has("snapshot")) {
                                if (!mjo.optJSONObject("snapshot").optString("service_time").equals("")) {
                                    mDTO.setService_time(Long.parseLong(mjo.optJSONObject("snapshot").optString("service_time")));
                                }
                                if (!mjo.optJSONObject("snapshot").optString("reservation_time").equals("")) {
                                    mDTO.setReservation_time(Long.parseLong(mjo.optJSONObject("snapshot").optString("reservation_time")));
                                }
                                mDTO.setPatient_id(mjo.optJSONObject("snapshot").optLong("patient_id"));
                                mDTO.setService_type_id(mjo.optJSONObject("snapshot").optInt("service_type_id"));
                                mDTO.setDoctor_name(mjo.optJSONObject("snapshot").optString("doctor_name"));
                                mDTO.setTitle_name(mjo.optJSONObject("snapshot").optString("title_name"));
                                mDTO.setHospital_name(mjo.optJSONObject("snapshot").optString("hospital_name"));
                                mDTO.setServer_time(mjo.optJSONObject("snapshot").optString("server_time"));
                                mDTO.setProfession_name(mjo.optJSONObject("snapshot").optString("profession_name"));
                                mDTO.setService_type_name(mjo.optJSONObject("snapshot").optString("service_type_name"));
                                mDTO.setServer_status(mjo.optJSONObject("snapshot").optInt("server_status"));
                                mDTO.setServer_user_type(mjo.optJSONObject("snapshot").optInt("server_user_type"));
                            }
                            mList.add(mDTO);
                        }
                        listListener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String s, int i) {
                        listListener.getDataError(i, s);
                    }
                });
    }

    /**
     * 13.2 订单详情
     *
     * @param order_id   订单id
     * @param order_type 订单类型
     * @param listener
     */
    @Override
    public void getOrderFeomDetailData(final int order_id, int order_type, final RCOrderFromParticularsListener listener) {
        RCGetOrderFromParticularsBean mBean = new RCGetOrderFromParticularsBean();
        mBean.setActionName("/p/server/order/detail/");
        mBean.setOrder_id(order_id + "");
        mBean.setOrder_type(order_type + "");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject jsonObject) {
                        JSONObject mjo = jsonObject.optJSONObject("result");
                        RCOrderFromParticularsDTO allDTO = new RCOrderFromParticularsDTO();
                        allDTO.setPortrait(mjo.optString("portrait"));
                        allDTO.setOrder_id(mjo.optInt("order_id"));
                        allDTO.setFee((float) mjo.optDouble("fee"));
                        allDTO.setFee_paid((float) mjo.optDouble("fee_paid"));
                        allDTO.setReservation_time(mjo.optLong("reservation_time"));
                        allDTO.setService_time(mjo.optLong("service_time"));
                        allDTO.setPatient_id(mjo.optLong("patient_id"));
                        allDTO.setStatus(mjo.optInt("status"));
                        allDTO.setPhone(mjo.optLong("phone"));
                        allDTO.setPatient_name(mjo.optString("patient_name"));
                        if (mjo.has("snapshot")) {
                            allDTO.setDoctor_name(mjo.optJSONObject("snapshot").optString("doctor_name"));
                            allDTO.setTitle_name(mjo.optJSONObject("snapshot").optString("title_name"));
                            allDTO.setHospital_name(mjo.optJSONObject("snapshot").optString("hospital_name"));
                            allDTO.setProfession_name(mjo.optJSONObject("snapshot").optString("profession_name"));
                            allDTO.setService_type_name(mjo.optJSONObject("snapshot").optString("service_type_name"));
                        }
                        if (mjo.has("progress")) {
                            JSONArray mja = mjo.optJSONArray("progress");
                            List<RCOrderFromParticularsProgressDTO> progressDTOList = new ArrayList<>();
                            for (int i = 0; i < mja.length(); i++) {
                                JSONObject progress_mjo = mja.optJSONObject(i);
                                RCOrderFromParticularsProgressDTO mDTO = new RCOrderFromParticularsProgressDTO();
                                mDTO.setOrder_time(progress_mjo.optLong("order_time"));
                                mDTO.setOrder_desc(progress_mjo.optString("order_desc"));
                                progressDTOList.add(mDTO);
                            }
                            allDTO.setProgressDTOList(progressDTOList);
                        }
                        listener.getDataSuccess(allDTO);
                    }

                    @Override
                    public void getDataErrorListener(String s, int i) {
                        listener.getDataError(i, s);
                    }
                });
    }

    /**
     * 13.21  13.22  订单详情（家庭医生、协议无忧）
     *
     * @param order_id
     * @param order_type
     * @param listener
     */
    @Override
    public void getServerGoodsOrderParticulars(int order_id, int order_type, final RCServerGoodsParticularsListener listener) {
        RCGetOrderFromParticularsBean mBean = new RCGetOrderFromParticularsBean();
        mBean.setActionName("/p/server/order/detail/");
        mBean.setOrder_id(order_id + "");
        mBean.setOrder_type(order_type + "");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        RCServerGoodsParticularsDTO allDTO = new RCServerGoodsParticularsDTO();

                        allDTO.setOrder_icon(mjo.optString("order_icon"));
                        allDTO.setOrder_name(mjo.optString("order_name"));
                        allDTO.setOrder_id(mjo.optInt("order_id"));
                        allDTO.setFee_paid((float) mjo.optDouble("fee_paid"));
                        allDTO.setFee((float) mjo.optDouble("fee"));
                        allDTO.setNick_name(mjo.optString("nick_name"));
                        allDTO.setPhone(mjo.optLong("phone"));

                        allDTO.setValidity_period(mjo.optString("validity_period"));
                        allDTO.setServer_user_type(mjo.optInt("server_user_type"));
                        allDTO.setServer_status(mjo.optInt("server_status"));
                        allDTO.setStatus(mjo.optInt("status"));
                        if (mjo.has("progress")) {
                            JSONArray mja = mjo.optJSONArray("progress");
                            List<RCOrderFromParticularsProgressDTO> progressDTOList = new ArrayList<>();
                            for (int i = 0; i < mja.length(); i++) {
                                JSONObject progress_mjo = mja.optJSONObject(i);
                                RCOrderFromParticularsProgressDTO mDTO = new RCOrderFromParticularsProgressDTO();
                                mDTO.setOrder_time(progress_mjo.optLong("order_time"));
                                mDTO.setOrder_desc(progress_mjo.optString("order_desc"));
                                progressDTOList.add(mDTO);
                            }
                            allDTO.setProgressDTOList(progressDTOList);
                        }
                        listener.getDataSuccess(allDTO);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 13.4 去支付
     *
     * @param listener
     */
    @Override
    public void getOrderPayStatus(String orderId, PaymentType paymentType, final RCGetOrderStatusListener listener) {
        RCGetOrderFromParticularsBean mBean = new RCGetOrderFromParticularsBean();
        mBean.setOrder_id(orderId);
        mBean.setPayment_type(paymentType.getTypeId() + "");
        mBean.setActionName("/p/server/order/payment/query/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess(
                                data.optJSONObject("result").optString("trade_status").equals("success")
                        );

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 13.5 取消订单
     *
     * @param order_id 订单id
     * @param listener
     */
    @Override
    public void postOrderFromCancel(int order_id, final IRCPostListener listener) {
        RCGetOrderFromParticularsBean mBean = new RCGetOrderFromParticularsBean();
        mBean.setOrder_id(order_id + "");
        mBean.setActionName("/p/server/order/cancel/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 13.6 退款
     *
     * @param order_id    订单id
     * @param refund_desc 退款原因
     * @param listener
     */
    @Override
    public void postOrderFromRefund(int order_id, String refund_desc, final IRCPostListener listener) {
        RCGetOrderFromParticularsBean mBean = new RCGetOrderFromParticularsBean();
        mBean.setOrder_id(order_id + "");
        mBean.setRefund_desc(refund_desc);
        mBean.setActionName("/p/server/order/payment/refund/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }
}
