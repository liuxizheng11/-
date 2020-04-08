package com.rocader.sdk.data.dto;

import com.rocader.sdk.data.RCIndicatorID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/11 下午3:04
 * 版本：V1.0
 * 描述：步数数据对象
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDeviceHeartRateDataDTO {

    //行为ID（固定）
    private int conductId = -1;
    //主指标ID（固定）
    private int indicatorId = RCIndicatorID.Heart_Rate;
    //设备ID
    private int deviceId;
    //数据日期yyyyMMddHHmmss
    private long date;
    //数据值
    private int number;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDate(String date) {
        try {
            this.date = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.date = 0L;
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public JSONObject getJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceId);
            jsonObject.put("indicator_id", indicatorId);
            jsonObject.put("conduct_id", conductId);
            jsonObject.put("date", date);
            JSONObject value = new JSONObject();
            value.put(RCIndicatorID.Heart_Rate + "", number);
            value.put(RCIndicatorID.Resting_Heart_Rate + "", number);
            jsonObject.put("value", value);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
