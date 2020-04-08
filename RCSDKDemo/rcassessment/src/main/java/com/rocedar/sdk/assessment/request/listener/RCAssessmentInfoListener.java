package com.rocedar.sdk.assessment.request.listener;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.assessment.dto.RCAssessmentInfoDTO;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/26 下午12:15
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCAssessmentInfoListener extends IRCBaseListener {

    void getDataSuccess(RCAssessmentInfoDTO dto);

}
