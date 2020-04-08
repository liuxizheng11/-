package com.rocedar.deviceplatform.request.impl;

import android.content.Context;

import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.dto.message.RCHealthAssistantDTO;
import com.rocedar.deviceplatform.request.RCHealthMessageRequest;
import com.rocedar.deviceplatform.request.bean.BeanGetHealthAssistantData;
import com.rocedar.deviceplatform.request.listener.RCPostListener;
import com.rocedar.deviceplatform.request.listener.message.RCGetHealthAssistantDataListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/21 下午5:56
 * 版本：V1.0
 * 描述：我的消息--健康小助手消息
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthAssistantRequestImpl implements RCHealthMessageRequest {
    private static RCHealthAssistantRequestImpl ourInstance;

    public static RCHealthAssistantRequestImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCHealthAssistantRequestImpl(context);
        return ourInstance;
    }

    private Context mContext;

    public RCHealthAssistantRequestImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 2.1 消息列表查询
     *
     * @param pn       页码（从0开始）
     * @param listener
     */
    @Override
    public void getHealthAssistantData(String pn, final RCGetHealthAssistantDataListener listener) {
        BeanGetHealthAssistantData mBean = new BeanGetHealthAssistantData();
        mBean.setActionName("/p/confirm/message/");
        mBean.setPn(pn);
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCHealthAssistantDTO> dtoList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCHealthAssistantDTO mDTO = new RCHealthAssistantDTO();
                            mDTO.setMessage_id(mjo.optInt("message_id"));
                            mDTO.setUser_id(mjo.optLong("user_id"));
                            mDTO.setType_id(mjo.optInt("type_id"));
                            mDTO.setContent(mjo.optString("content"));
                            mDTO.setExpire_time(mjo.optLong("expire_time"));
                            mDTO.setCreate_time(mjo.optLong("create_time"));
                            mDTO.setStatus(mjo.optInt("status"));
                            dtoList.add(mDTO);
                        }
                        listener.getDataSuccess(dtoList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }

    /**
     * 2.2 更新消息状态
     * @param status
     * @param messageId
     * @param typeId
     * @param listener
     */
    @Override
    public void putHealthAssistantStatus(String status, String messageId, String typeId,final RCPostListener listener) {
        BeanGetHealthAssistantData mBean = new BeanGetHealthAssistantData();
        mBean.setActionName("/p/confirm/message/");
        mBean.setStatus(status);
        mBean.setType_id(typeId);
        mBean.setMessage_id(messageId);
        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Put,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess();
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }
}
