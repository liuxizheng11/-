package com.rocedar.deviceplatform.request.listener.message;

import com.rocedar.deviceplatform.dto.message.RCHealthAssistantDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/21 下午5:51
 * 版本：V1.0
 * 描述：  我的消息--健康小助手
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCGetHealthAssistantDataListener {
    void getDataSuccess(List<RCHealthAssistantDTO> dtoList);

    void getDataError(int status, String msg);
}
