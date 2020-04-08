package com.rocedar.deviceplatform.dto.device;

import com.rocedar.base.RCJavaUtil;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/10/9 下午2:43
 * 版本：V2.2.00
 * 描述：体脂\体重 数据对象
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDeviceFitDataDTO {

    //行为ID（固定）
    private int conductId = -1;
    //主指标ID（固定）
    private int indicatorId = RCDeviceIndicatorID.BLOOD_BODY_FAT;
    //设备ID
    private int deviceId;
    //数据日期yyyyMMddHHmmss
    private long date;


    // "4018": "体脂(%-整数)",
    private float fat = 0.0f;
    // "4019": "骨骼量(kg-1位小数)",
    private float bm = 0.0f;
    // "4020": "肌肉率(%-1位小数)",
    private float mmp = 0.0f;
    // "4021": "水分率(%-整数)",
    private int tbw = 0;
    // "4043": "体重(kg-1位小数)",
    private float weight = -1.0f;
    // "4045": "内脏脂肪(整数)",
    private int vf = 0;
    // "4046": "基础代谢(千卡-整数)"
    private int bmr = 0;
    // "4221": "身体年龄(整数)"
    private int bodyAge = 0;


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

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getBm() {
        return bm;
    }

    public void setBm(float bm) {
        this.bm = bm;
    }

    public float getMmp() {
        return mmp;
    }

    public void setMmp(float mmp) {
        this.mmp = mmp;
    }

    public int getTbw() {
        return tbw;
    }

    public void setTbw(int tbw) {
        this.tbw = tbw;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getVf() {
        return vf;
    }

    public void setVf(int vf) {
        this.vf = vf;
    }

    public int getBmr() {
        return bmr;
    }

    public void setBmr(int bmr) {
        this.bmr = bmr;
    }

    public int getBodyAge() {
        return bodyAge;
    }

    public void setBodyAge(int bodyAge) {
        this.bodyAge = bodyAge;
    }

    public JSONObject getJSON() {
        if (weight <= 0) return null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceId);
            jsonObject.put("indicator_id", indicatorId);
            jsonObject.put("conduct_id", conductId);
            jsonObject.put("date", date);
            JSONObject value = new JSONObject();
            value.put(RCDeviceIndicatorID.BLOOD_BODY_WEIGHT + "", RCJavaUtil.formatBigDecimalUP(weight, 2));
            value.put(RCDeviceIndicatorID.BLOOD_BODY_BM + "", RCJavaUtil.formatBigDecimalUP(bm, 2));
            value.put(RCDeviceIndicatorID.BLOOD_BODY_FAT + "", RCJavaUtil.formatBigDecimalUP(fat, 2));
            value.put(RCDeviceIndicatorID.BLOOD_BODY_MMP + "", RCJavaUtil.formatBigDecimalUP(mmp, 2));
            value.put(RCDeviceIndicatorID.BLOOD_BODY_TBW + "", tbw);
            value.put(RCDeviceIndicatorID.BLOOD_BODY_VF + "", vf);
            value.put(RCDeviceIndicatorID.BLOOD_BODY_BMR + "", bmr);
            value.put(RCDeviceIndicatorID.BLOOD_BODY_AGE + "", bodyAge);
            jsonObject.put("value", value);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
