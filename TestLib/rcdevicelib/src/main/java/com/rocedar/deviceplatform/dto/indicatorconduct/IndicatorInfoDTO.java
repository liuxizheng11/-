package com.rocedar.deviceplatform.dto.indicatorconduct;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午3:30
 * 版本：V1.0
 * 描述：指标相关的全部数据
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class IndicatorInfoDTO {

    private int type_i;
    private String type_name;
    private long user_id;
    private long user_phone;
    private String device_bind_url;
    private int device_bind;
    private int rights = 1;
    private IndicatorDataDTO lastData;
    private List<IndicatorDataDTO> historyData;

    public int getDevice_bind() {
        return device_bind;
    }

    public void setDevice_bind(int device_bind) {
        this.device_bind = device_bind;
    }

    public int getType_i() {
        return type_i;
    }

    public void setType_i(int type_i) {
        this.type_i = type_i;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(long user_phone) {
        this.user_phone = user_phone;
    }

    public String getDevice_bind_url() {
        return device_bind_url;
    }

    public void setDevice_bind_url(String device_bind_url) {
        this.device_bind_url = device_bind_url;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public IndicatorDataDTO getLastData() {
        return lastData;
    }

    public void setLastData(IndicatorDataDTO lastData) {
        this.lastData = lastData;
    }

    public List<IndicatorDataDTO> getHistoryData() {
        return historyData;
    }

    public void setHistoryData(List<IndicatorDataDTO> historyData) {
        this.historyData = historyData;
    }
}
