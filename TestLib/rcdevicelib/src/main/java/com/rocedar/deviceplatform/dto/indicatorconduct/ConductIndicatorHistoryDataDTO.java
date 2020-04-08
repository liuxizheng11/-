package com.rocedar.deviceplatform.dto.indicatorconduct;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午4:01
 * 版本：V1.0
 * 描述：指标历史数据
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class ConductIndicatorHistoryDataDTO {

    //日期
    private long time;
    //行为数据
    private String value;
    //附属数据
    private String sub_value;
    //设备ID
    private int device_id;
    //设备名字
    private String device_name;
    //告警等级
    private int exception_level;
    //告警信息
    private String exception;
    //附属告警等级
    private int sub_exception_level;

    public int getSub_exception_level() {
        return sub_exception_level;
    }

    public void setSub_exception_level(int sub_exception_level) {
        this.sub_exception_level = sub_exception_level;
    }

    private List<ConductIndicatorHistoryDataDetailDTO> indicatorHistoryDataDetailDTOs;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSub_value() {
        return sub_value;
    }

    public void setSub_value(String sub_value) {
        this.sub_value = sub_value;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public int getException_level() {
        return exception_level;
    }

    public void setException_level(int exception_level) {
        this.exception_level = exception_level;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public List<ConductIndicatorHistoryDataDetailDTO> getIndicatorHistoryDataDetailDTOs() {
        return indicatorHistoryDataDetailDTOs;
    }

    public void setIndicatorHistoryDataDetailDTOs(List<ConductIndicatorHistoryDataDetailDTO> indicatorHistoryDataDetailDTOs) {
        this.indicatorHistoryDataDetailDTOs = indicatorHistoryDataDetailDTOs;
    }


}
