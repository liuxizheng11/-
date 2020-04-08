package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorInfoDTO;
import com.rocedar.deviceplatform.dto.record.RCHealthReportPlanDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/27 下午3:50
 * 版本：V1.0
 * 描述：健康报告监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface  RCHealthReportPlanListener {
    void getDataSuccess(List<RCHealthReportPlanDTO> dtoList);

    void getDataError(int status, String msg);
}
