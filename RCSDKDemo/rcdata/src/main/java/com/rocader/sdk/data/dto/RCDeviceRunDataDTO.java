package com.rocader.sdk.data.dto;

import com.rocader.sdk.data.RCConductID;
import com.rocader.sdk.data.RCIndicatorID;
import com.rocader.sdk.data.SceneType;
import com.rocedar.lib.base.unit.RCJavaUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/7/27 下午4:03
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDeviceRunDataDTO {


    //行为ID（固定）
    private int conductId = RCConductID.RUN;
    //主指标ID（固定）
    private int indicatorId = -1;
    //设备ID
    private int deviceId;
    //数据日期yyyyMMddHHmmss
    private long date;
    //4036:跑步步数
    private int step = 0;
    //4001:跑步距离(千米)
    private double km = 0.0;
    //4025:跑步卡路里(千卡)
    private double cal = 0;
    //4024:跑步时长(分钟)
    private int time = 0;
    //4120:跑步开始时间
    private long startTime;
    //4121:跑步结束时间
    private long endTime;
    // 4052:跑步平均心率(次/分)
    private double averageHeartRate = 0;
    // 4095:跑步心率明细列表
    //      [{
    //        "time": "时间到秒",
    //                "heartrate": "心率"
    //      }
    //      ...]
    private String heartList = "";
    //室内／室外
    private int type = 2;
    // 4101:跑步有效时长(分钟)
    private int validTime = 0;
    // 4051:跑步速度(千米/小时)
    private double speed = 0;

    // 4092:跑步配速(秒)
    private int pace = 0;

    // 4114:跑步速度明细列表
    //    [{
    //      "time": "时间到秒",
    //      "speed": "速度(千米/小时)"
    //    }
    //    ...]
    private String speedList = "";

    // 4093:跑步配速明细列表
    //       [{
    //        "time": "时间到秒",
    //        "pace": "配速(秒)"
    //       }
    //       ...]
    private String paceList = "";

    // 4102:跑步运动轨迹列表-分段
    //    [
    //      [
    //      {
    //        "time": "时间到秒",
    //        "gps": "gps值"
    //      }...],
    //      [
    //      {
    //        "time": "时间到秒",
    //        "gps": "gps值"
    //      }...]
    //    ]
    private String gpsList = "";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(SceneType sceneType) {
        this.type = (sceneType == SceneType.RUN ? 1 : 2);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime) {
        try {
            this.startTime = Long.parseLong(startTime);
        } catch (NumberFormatException e) {
            this.startTime = 0L;
        }
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime) {
        try {
            this.endTime = Long.parseLong(endTime);
        } catch (NumberFormatException e) {
            this.endTime = 0L;
        }
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

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        if (km > 0)
            this.km = RCJavaUtil.formatBigDecimalUP(km, 3);
    }

    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(double averageHeartRate) {
        if (averageHeartRate > 0)
            this.averageHeartRate = RCJavaUtil.formatBigDecimalUP(averageHeartRate, 2);
    }

    public String getHeartList() {
        return heartList;
    }

    public void setHeartList(String heartList) {
        this.heartList = heartList;
    }

    public int getValidTime() {
        return validTime;
    }

    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        if (speed > 0)
            this.speed = RCJavaUtil.formatBigDecimalUP(speed, 2);
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public String getSpeedList() {
        return speedList;
    }

    public void setSpeedList(String speedList) {
        this.speedList = speedList;
    }

    public String getPaceList() {
        return paceList;
    }

    public void setPaceList(String paceList) {
        this.paceList = paceList;
    }

    public String getGpsList() {
        return gpsList;
    }

    public void setGpsList(String gpsList) {
        this.gpsList = gpsList;
    }


    public JSONObject getJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceId);
            jsonObject.put("indicator_id", indicatorId);
            jsonObject.put("conduct_id", conductId);
            jsonObject.put("date", date);
            JSONObject value = new JSONObject();
            if (step > 0)
                value.put(RCIndicatorID.RUN_STEP + "", step);
            if (time > 0)
                value.put(RCIndicatorID.RUN_TIME + "", time);
            if (startTime > 0)
                value.put(RCIndicatorID.RUN_START_TIME + "", startTime);
            if (endTime > 0)
                value.put(RCIndicatorID.RUN_STOP_TIME + "", endTime);
            if (km > 0)
                value.put(RCIndicatorID.RUN_DISTANCE + "", km);
            if (pace > 0)
                value.put(RCIndicatorID.RUN_PACE + "", pace);
            if (cal > 0)
                value.put(RCIndicatorID.RUN_KCAL + "", cal);
            if (averageHeartRate > 0)
                value.put(RCIndicatorID.RUN_AVERAGE_HEARTRATE + "", averageHeartRate);
            if (!heartList.equals(""))
                value.put(RCIndicatorID.RUN_HEART_LIST + "", heartList);
            if (validTime > 0)
                value.put(RCIndicatorID.RUN_VALID_TIME + "", validTime);
            if (speed > 0)
                value.put(RCIndicatorID.RUN_SPEED + "", speed);
            if (!speedList.equals(""))
                value.put(RCIndicatorID.RUN_SPEED_LIST + "", speedList);
            if (!paceList.equals(""))
                value.put(RCIndicatorID.RUN_PACE_LIST + "", paceList);
            if (!gpsList.equals(""))
                value.put(RCIndicatorID.RUN_GPS_LIST + "", gpsList);
            value.put(RCIndicatorID.SCENE_TYPE + "", type);
            jsonObject.put("value", value);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static RCDeviceRunDataDTO fromat(String object) {
        try {
            RCDeviceRunDataDTO dto = new RCDeviceRunDataDTO();
            JSONObject jsonObject = new JSONObject(object);
            dto.setDeviceId(jsonObject.optInt("device_id"));
            dto.setDate(jsonObject.optLong("date"));
            JSONObject value = jsonObject.optJSONObject("value");
            if (value.has(RCIndicatorID.SCENE_TYPE + ""))
                dto.setType(value.optInt(RCIndicatorID.SCENE_TYPE + ""));
            if (value.has(RCIndicatorID.RUN_STEP + ""))
                dto.setStep(value.optInt(RCIndicatorID.RUN_STEP + ""));
            if (value.has(RCIndicatorID.RUN_TIME + ""))
                dto.setTime(value.optInt(RCIndicatorID.RUN_TIME + ""));
            if (value.has(RCIndicatorID.RUN_DISTANCE + ""))
                dto.setKm(value.optDouble(RCIndicatorID.RUN_DISTANCE + ""));
            if (value.has(RCIndicatorID.RUN_START_TIME + ""))
                dto.setStartTime(value.optLong(RCIndicatorID.RUN_START_TIME + ""));
            if (value.has(RCIndicatorID.RUN_STOP_TIME + ""))
                dto.setEndTime(value.optLong(RCIndicatorID.RUN_STOP_TIME + ""));
            if (value.has(RCIndicatorID.RUN_GPS_LIST + ""))
                dto.setGpsList(value.optString(RCIndicatorID.RUN_GPS_LIST + ""));
            if (value.has(RCIndicatorID.RUN_HEART_LIST + ""))
                dto.setHeartList(value.optString(RCIndicatorID.RUN_HEART_LIST + ""));
            if (value.has(RCIndicatorID.RUN_KCAL + ""))
                dto.setCal(value.optDouble(RCIndicatorID.RUN_KCAL + ""));
            if (value.has(RCIndicatorID.RUN_SPEED + ""))
                dto.setSpeed(value.optDouble(RCIndicatorID.RUN_SPEED + ""));
            if (value.has(RCIndicatorID.RUN_AVERAGE_HEARTRATE + ""))
                dto.setAverageHeartRate(value.optInt(RCIndicatorID.RUN_AVERAGE_HEARTRATE + ""));
            if (value.has(RCIndicatorID.RUN_PACE + ""))
                dto.setPace(value.optInt(RCIndicatorID.RUN_PACE + ""));
            if (value.has(RCIndicatorID.RUN_SPEED_LIST + ""))
                dto.setSpeedList(value.optString(RCIndicatorID.RUN_SPEED_LIST + ""));
            if (value.has(RCIndicatorID.RUN_PACE_LIST + ""))
                dto.setPaceList(value.optString(RCIndicatorID.RUN_PACE_LIST + ""));
            if (value.has(RCIndicatorID.RUN_VALID_TIME + ""))
                dto.setValidTime(value.optInt(RCIndicatorID.RUN_VALID_TIME + ""));
            return dto;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
