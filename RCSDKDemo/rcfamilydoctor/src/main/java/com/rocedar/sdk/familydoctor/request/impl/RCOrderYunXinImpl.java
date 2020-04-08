package com.rocedar.sdk.familydoctor.request.impl;//package com.rcoedar.lib.sdk.yunxin.request.impl;

import android.content.Context;

import com.rocedar.sdk.familydoctor.dto.mingyi.RCOrderVideoAccidDTO;
import com.rocedar.sdk.familydoctor.request.IRCYunXinRequest;


import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.familydoctor.request.bean.RCOrderYunXinBean;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCOrderVideoAccidListener;

import org.json.JSONObject;
//

/**
 * 作者：lxz
 * 日期：2018/8/3 上午10:36
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCOrderYunXinImpl implements IRCYunXinRequest {
    private static RCOrderYunXinImpl rcOrderYunXin;

    public static RCOrderYunXinImpl getInstance(Context context) {
        if (rcOrderYunXin == null)
            rcOrderYunXin = new RCOrderYunXinImpl(context);
        return rcOrderYunXin;
    }

    private Context mContext;

    public RCOrderYunXinImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 获取云信账号信息
     *
     * @param order_id
     * @param listener
     */
    @Override
    public void getOrderVideoAccid(int order_id, final RCOrderVideoAccidListener listener) {
        RCOrderYunXinBean mBean = new RCOrderYunXinBean();
        mBean.setActionName("/p/server/1311001/order/accid/");
        mBean.setOrder_id(order_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get
                , new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject jsonObject) {
                        JSONObject mjo = jsonObject.optJSONObject("result");
                        RCOrderVideoAccidDTO mDTO = new RCOrderVideoAccidDTO();
                        mDTO.setDoctor_accid(mjo.optString("doctor_accid"));
                        mDTO.setDoctor_name(mjo.optString("doctor_name"));
                        mDTO.setDoctor_portrait(mjo.optString("doctor_portrait"));
                        mDTO.setDoctor_accid_token(mjo.optString("doctor_accid_token"));
                        mDTO.setUser_accid(mjo.optString("user_accid"));
                        mDTO.setUser_accid_token(mjo.optString("user_accid_token"));
                        listener.getDataSuccess(mDTO);
                    }

                    @Override
                    public void getDataErrorListener(String s, int i) {
                        listener.getDataError(i, s);
                    }
                });

    }

    /**
     * 结束视频通话
     *
     * @param order_id
     * @param listener
     */
    @Override
    public void putOverOrderVider(int order_id, final IRCPostListener listener) {
        RCOrderYunXinBean mBean = new RCOrderYunXinBean();
        mBean.setActionName("/p/server/1311001/order/video/");
        mBean.setOrder_id(order_id + "");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Put,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject jsonObject) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String s, int i) {
                        listener.getDataError(i, s);
                    }
                });
    }
}
