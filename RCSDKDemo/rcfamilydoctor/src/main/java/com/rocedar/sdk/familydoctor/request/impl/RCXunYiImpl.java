package com.rocedar.sdk.familydoctor.request.impl;

import android.content.Context;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.familydoctor.dto.xunyi.RCXunYiConsultDetailsDTO;
import com.rocedar.sdk.familydoctor.dto.xunyi.RCXunYiInquiryDTO;
import com.rocedar.sdk.familydoctor.request.IRCXunYiRequest;
import com.rocedar.sdk.familydoctor.request.bean.BeanGetXunYiData;
import com.rocedar.sdk.familydoctor.request.bean.BeanPostXunYiMessage;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiDetailsListListListener;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiInquiryListListListener;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiPostOrderListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/11/6 2:43 PM
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiImpl implements IRCXunYiRequest {
    private static RCXunYiImpl rcXunYi;

    public static RCXunYiImpl getInstance(Context context) {
        if (rcXunYi == null)
            rcXunYi = new RCXunYiImpl(context);
        return rcXunYi;
    }

    private Context mContext;

    public RCXunYiImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 15.1 图文问诊列表
     *
     * @param pn
     * @param listener
     */
    @Override
    public void getXunYiInquiryList(String pn, final RCXunYiInquiryListListListener listener) {
        BeanGetXunYiData mBean = new BeanGetXunYiData();
        mBean.setPn(pn);
        mBean.setActionName("/p/server/xunyi/advice/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONArray mja = data.optJSONArray("result");
                        List<RCXunYiInquiryDTO> mList = new ArrayList<>();
                        for (int i = 0; i < mja.length(); i++) {
                            JSONObject mjo = mja.optJSONObject(i);
                            RCXunYiInquiryDTO mDTO = new RCXunYiInquiryDTO();

                            mDTO.setAdvice_id(mjo.optInt("advice_id"));
                            mDTO.setDate_str(mjo.optLong("date_str"));
                            mDTO.setFee(mjo.optInt("fee"));
                            mDTO.setQuestion(mjo.optString("question"));
                            mDTO.setStatus(mjo.optInt("status"));
                            mList.add(mDTO);
                        }
                        listener.getDataSuccess(mList);
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });

    }

    /**
     * 15.2 图文问诊详情
     *
     * @param listener
     */
    @Override
    public void getXunYiConsultDetailsList(String adviceId, final RCXunYiDetailsListListListener listener) {
        RCBean mBean = new RCBean();
        mBean.setActionName("/p/server/xunyi/advice/" + adviceId + "/");
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Get,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        JSONObject mjo = data.optJSONObject("result");
                        RCXunYiConsultDetailsDTO mDTO = new RCXunYiConsultDetailsDTO();
                        boolean hasData = mjo.optJSONObject("doctor").has("doctor_icon");
                        mDTO.setStatus(mjo.optInt("status"));
                        mDTO.setAdvice_status(mjo.optInt("advice_status"));
                        mDTO.setRid(mjo.optString("rid"));
                        mDTO.setRuid(mjo.optString("ruid"));
                        mDTO.setQid(mjo.optString("qid"));
                        mDTO.setPatient_id(mjo.optLong("patient_id"));

                        mDTO.setDoctor_icon(hasData ? mjo.optJSONObject("doctor").optString("doctor_icon") : "");
                        mDTO.setDoctor_name(mjo.optJSONObject("doctor").optString("doctor_name"));
                        mDTO.setTitle_name(mjo.optJSONObject("doctor").optString("title_name"));
                        mDTO.setDepartment_name(mjo.optJSONObject("doctor").optString("department_name"));
                        mDTO.setHospital_name(mjo.optJSONObject("doctor").optString("hospital_name"));
                        List<RCXunYiConsultDetailsDTO.questionsDTO> mList = new ArrayList<>();
                        JSONArray mja = mjo.optJSONArray("questions");
                        for (int i = 0; i < mja.length(); i++) {
                            RCXunYiConsultDetailsDTO.questionsDTO mData = new RCXunYiConsultDetailsDTO.questionsDTO();
                            JSONObject data_mjo = mja.optJSONObject(i);
                            mData.setAdvice_id(data_mjo.optInt("advice_id"));
                            mData.setIcon(data_mjo.optString("icon"));
                            mData.setQuestion(data_mjo.optString("question"));
                            mData.setAdvice_status(mjo.optInt("advice_status"));
                            mData.setImg(data_mjo.optString("img"));
                            mData.setCreate_time(data_mjo.optLong("create_time"));
                            mData.setWho(data_mjo.optInt("who"));
                            mList.add(mData);
                        }
                        mDTO.setmList(mList);
                        listener.getDataSuccess(mDTO);

                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {

                    }
                });
    }

    /**
     * 15.3 发送消息
     *
     * @param advice_id  图文问诊id（没有传-1）
     * @param qid        寻医问药咨询id
     * @param patient_id 病人id
     * @param rid        医生消息id
     * @param ruid       发送人id
     * @param question   咨询文字描述
     * @param img        图片上传（多张图片用“;”分隔）
     * @param listener
     */
    @Override
    public void postXunYiMessage(String advice_id, String qid, String rid, String ruid, String patient_id, String question, String img, final RCXunYiPostOrderListener listener) {
        BeanPostXunYiMessage mBean = new BeanPostXunYiMessage();
        mBean.setActionName("/p/server/xunyi/advice/");
        mBean.setAdvice_id(advice_id);
        mBean.setQid(qid);
        mBean.setRid(rid);
        mBean.setRuid(ruid);
        mBean.setPatient_id(patient_id);
        mBean.setQuestion(question);
        mBean.setImg(img);
        RCRequestNetwork.NetWorkGetData(mContext, mBean, RCRequestNetwork.Method.Post,
                new IResponseData() {
                    @Override
                    public void getDataSucceedListener(JSONObject data) {
                        listener.getDataSuccess(data.optJSONObject("result").optInt("advice_id") + "");
                    }

                    @Override
                    public void getDataErrorListener(String msg, int status) {
                        listener.getDataError(status, msg);
                    }
                });
    }
}
