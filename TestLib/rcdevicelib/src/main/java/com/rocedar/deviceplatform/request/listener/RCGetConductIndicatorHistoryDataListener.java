package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.indicatorconduct.ConductIndicatorHistoryInfoDTO;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午3:52
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCGetConductIndicatorHistoryDataListener {

    void getDataSuccess(ConductIndicatorHistoryInfoDTO dtoList);

    void getDataError(int status, String msg);

}
