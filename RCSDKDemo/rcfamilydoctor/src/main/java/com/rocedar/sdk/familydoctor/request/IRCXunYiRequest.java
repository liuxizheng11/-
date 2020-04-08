package com.rocedar.sdk.familydoctor.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiDetailsListListListener;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiInquiryListListListener;
import com.rocedar.sdk.familydoctor.request.listener.xunyi.RCXunYiPostOrderListener;

/**
 * 作者：lxz
 * 日期：2018/11/5 6:23 PM
 * 版本：V1.0
 * 描述：寻医问药
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCXunYiRequest {
    /**
     * 15.1 图文问诊列表
     */
    void getXunYiInquiryList(String pn, RCXunYiInquiryListListListener listener);

    /**
     * 15.2 图文问诊详情
     *
     * @param listener
     */
    void getXunYiConsultDetailsList(String adviceId, RCXunYiDetailsListListListener listener);

    /**
     * 15.3 发送消息
     *
     * @param advice_id  图文问诊id（没有传-1）
     * @param qid        寻医问药咨询id
     * @param patient_id 病人id
     * @param question   咨询文字描述
     * @param img        图片上传（多张图片用“;”分隔）
     * @param listener
     */
    void postXunYiMessage(String advice_id, String qid,  String rid, String ruid,String patient_id, String question, String img, RCXunYiPostOrderListener listener);
}
