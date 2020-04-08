package com.rocader.sdk.data.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/4/16 7:34 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCDeviceStepDetailDTO {

    //数据值-步数
    private int step = 0;
    //数据值-km
    private float km = 0F;
    //数据值-卡路里
    private double cal = 0F;
    //数据日期yyyyMMddHHmmss
    private long time;

    public long getDate() {
        return time;
    }

    public void setDate(long date) {
        this.time = date;
    }

    public void setDate(String date) {
        try {
            this.time = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.time = 0L;
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

    public JSONObject getJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("t", time + "");
            jsonObject.put("c", cal + "");
            jsonObject.put("d", km + "");
            jsonObject.put("s", step + "");
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
