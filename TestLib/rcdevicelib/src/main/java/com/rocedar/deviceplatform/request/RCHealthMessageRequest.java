package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCPostListener;
import com.rocedar.deviceplatform.request.listener.message.RCGetHealthAssistantDataListener;

/**
 * 作者：lxz
 * 日期：17/7/21 下午5:23
 * 版本：V1.0
 * 描述：我的消息--健康小助手消息
 * 2.1 消息列表查询  2.2 更新消息状态
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCHealthMessageRequest {

    /**
     * 2.1 消息列表查询
     *
     * @param pn 页码（从0开始）
     */
    void getHealthAssistantData(String pn, RCGetHealthAssistantDataListener listener);

    /**
     * 2.2 更新消息状态
     * @param status
     * @param messageId
     * @param tyidId
     * @param listener
     */
    void putHealthAssistantStatus(String status, String messageId, String tyidId, RCPostListener listener);
}
