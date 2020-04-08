package com.rocedar.deviceplatform.request.listener.target;

import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetWeekProgressDTO;

/**
 * 作者：lxz
 * 日期：17/7/10 上午11:13
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCUserWeekTargetProgressListener {

    void getDataSuccess(RCIndicatorTargetWeekProgressDTO dto);

    void getDataError(int status, String msg);

}
