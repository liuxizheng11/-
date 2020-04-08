package com.rocader.sdk.data.dto;


import com.rocader.sdk.data.RCConductID;
import com.rocader.sdk.data.RCIndicatorID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

public class RCDeviceStepDataDTO {

    //行为ID（固定）
    private int conductId = RCConductID.WALK;
    //主指标ID（固定）
    private int indicatorId = -1;
    //设备ID
    private int deviceId;
    //数据日期yyyyMMddHHmmss
    private long date;
    //数据值-步数
    private int step;
    //数据值-km
    private float km = -1F;
    //数据值-卡路里
    private double cal = -1F;
    //数据值-时间（分钟）
    private int time = -1;
    //分段数据
    private List<RCDeviceStepDetailDTO> detailDTOS;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

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

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public List<RCDeviceStepDetailDTO> getDetailDTOS() {
        return detailDTOS;
    }

    public void setDetailDTOS(List<RCDeviceStepDetailDTO> detailDTOS) {
        this.detailDTOS = detailDTOS;
    }

    public JSONObject getJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceId);
            jsonObject.put("indicator_id", indicatorId);
            jsonObject.put("conduct_id", conductId);
            jsonObject.put("date", date);
            JSONObject value = new JSONObject();
            value.put(RCIndicatorID.STEP + "", step);
            if (km > 0)
                value.put(RCIndicatorID.STEP_DISTANCE + "", km);
            if (cal > 0)
                value.put(RCIndicatorID.STEP_KCAL + "", cal);
            if (time > 0)
                value.put(RCIndicatorID.STEP_TIME + "", time);
            if (detailDTOS != null) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < detailDTOS.size(); i++) {
                    array.put(detailDTOS.get(i).getJSON());
                }
                value.put(RCIndicatorID.STEP_DETAIL + "", array + "");
            }
            jsonObject.put("value", value);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
