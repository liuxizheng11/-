package com.rocedar.deviceplatform.dto.indicatorconduct;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午4:01
 * 版本：V1.0
 * 描述：指标历史信息
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class ConductIndicatorHistoryInfoDTO {

    //指标名称
    private String conductIndicatorName;

    private List<ConductIndicatorHistoryDataDTO> indicatorHistoryDataDTOs;

    public String getConductIndicatorName() {
        return conductIndicatorName;
    }

    public void setConductIndicatorName(String conductIndicatorName) {
        this.conductIndicatorName = conductIndicatorName;
    }

    public List<ConductIndicatorHistoryDataDTO> getIndicatorHistoryDataDTOs() {
        return indicatorHistoryDataDTOs;
    }

    public void setIndicatorHistoryDataDTOs(List<ConductIndicatorHistoryDataDTO> indicatorHistoryDataDTOs) {
        this.indicatorHistoryDataDTOs = indicatorHistoryDataDTOs;
    }
}
