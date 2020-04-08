package com.rocedar.deviceplatform.dto.device;

import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/11 下午3:04
 * 版本：V1.0
 * 描述：血压数据对象
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDeviceBloodPressureDataDTO {

    //行为ID（固定）
    private int conductId = -1;
    //主指标ID（固定）
    private int indicatorId = RCDeviceIndicatorID.Blood_Pressure;
    //设备ID
    private int deviceId;
    //数据日期yyyyMMddHHmmss
    private long date;

    //数据值
    private int higt;
    private int low;
    private int heartRate;


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

    public int getHigt() {
        return higt;
    }

    public void setHigt(int higt) {
        this.higt = higt;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public JSONObject getJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceId);
            jsonObject.put("indicator_id", indicatorId);
            jsonObject.put("conduct_id", conductId);
            jsonObject.put("date", date);
            JSONObject value = new JSONObject();
            value.put(RCDeviceIndicatorID.Blood_Pressure + "", higt + ";" + low);
            if (heartRate > 0) {
                value.put(RCDeviceIndicatorID.Heart_Rate + "", heartRate);
            }
            jsonObject.put("value", value);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
