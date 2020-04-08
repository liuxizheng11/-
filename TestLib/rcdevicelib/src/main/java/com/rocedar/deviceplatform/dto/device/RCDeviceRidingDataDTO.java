package com.rocedar.deviceplatform.dto.device;

import com.rocedar.deviceplatform.app.scene.SceneType;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;

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

public class RCDeviceRidingDataDTO {

    //行为ID（固定）
    private int conductId = RCDeviceConductID.RIDING;
    //主指标ID（固定）
    private int indicatorId = -1;
    //设备ID
    private int deviceId;
    //数据日期yyyyMMddHHmmss
    private long date;
    //4002:骑行距离(千米)
    private double km = 0.0;
    //4034:骑行时长(分钟)
    private int time = 0;
    //4122:骑行开始时间
    private long startTime;
    //4123:骑行结束时间
    private long endTime;
    //室内／室外
    private int type = 2;
    // 4035:骑行卡路里(千卡)
    private double cal;
    // 4054:骑行速度(千米/小时)
    private double speed;
    // 4055:骑行平均心率(次/分)
    private int averageHeartRate;
    // 4096:骑行配速(秒)
    private int pace;
    //4098:骑行有效时长(分钟)
    private int validTime = 0;
    //    4115:骑行速度明细列表
    //		[{
    //        "time": "时间到秒",
    //                "speed": "速度(千米/小时)"
    //    }
    //          ...]
    private String speedList = "";

    //                  4097:骑行配速明细列表
    //		[{
    //        "time": "时间到秒",
    //                "pace": "配速(秒)"
    //    }
    //          ...]
    private String paceList = "";
    // 4100:骑行运动轨迹列表-分段
    //		[
    //                [
    //    {
    //        "time": "时间到秒",
    //            "gps": "gps值"
    //    }
    //            ...
    //                    ],
    //                    [
    //    {
    //        "time": "时间到秒",
    //            "gps": "gps值"
    //    }
    //            ...
    //                    ]
    //                    ]
    private String gpsList = "";
    // 4099:骑行心率明细列表
    //		[{
    //        "time": "时间到秒",
    //                "heartrate": "心率"
    //    }
    //          ...]
    private String heartList = "";

    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public double getSpeed() {
        return speed;

    }

    public void setSpeed(double speed) {
        if (pace < Integer.MAX_VALUE)
            this.speed = speed;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        if (pace < Integer.MAX_VALUE)
            this.pace = pace;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(SceneType sceneType) {
        this.type = (sceneType == SceneType.CYCLING ? 1 : 2);
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


    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getValidTime() {
        return validTime;
    }

    public void setValidTime(int validTime) {
        this.validTime = validTime;
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

    public String getHeartList() {
        return heartList;
    }

    public void setHeartList(String heartList) {
        this.heartList = heartList;
    }

    /**
     * //设备ID
     * private int deviceId;
     * //数据日期yyyyMMddHHmmss
     * private long date;
     * //4002:骑行距离(千米)
     * private int km = 0;
     * //4034:骑行时长(分钟)
     * private int time = 0;
     * //4122:骑行开始时间
     * private long startTime;
     * //4123:骑行结束时间
     * private long endTime;
     * //骑行轨迹
     * private String gps = "";
     * //心率
     * private String heart = "";
     * //室内／室外
     * private int type = 2;
     * // 4098:骑行有效时长(分钟)
     * private int effectiveTime;
     * // 4035:骑行卡路里(千卡)
     * private int cal;
     * // 4054:骑行速度(千米/小时)
     * private float speed;
     * // 4055:骑行平均心率(次/分)
     * private int averageHeartRate;
     * // 4096:骑行配速(秒)
     * private int pace;
     * //4098:骑行有效时长(分钟)
     * private int validTime = 0;
     * //4055:骑行平均心率(次/分)
     * private int heart = 0;
     *
     * @return
     */

    public JSONObject getJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceId);
            jsonObject.put("indicator_id", indicatorId);
            jsonObject.put("conduct_id", conductId);
            jsonObject.put("date", date);
            JSONObject value = new JSONObject();
            value.put(RCDeviceIndicatorID.RIDING_TIME + "", time);
            if (km > 0)
                value.put(RCDeviceIndicatorID.RIDING_DISTANCE + "", km);
            if (startTime > 0)
                value.put(RCDeviceIndicatorID.RIDING_START_TIME + "", startTime);
            if (endTime > 0)
                value.put(RCDeviceIndicatorID.RIDING_STOP_TIME + "", endTime);
            if (!gpsList.equals(""))
                value.put(RCDeviceIndicatorID.RIDING_GPS_LIST + "", gpsList);
            if (!heartList.equals(""))
                value.put(RCDeviceIndicatorID.RIDING_HEART_LIST + "", heartList);
            if (cal > 0)
                value.put(RCDeviceIndicatorID.RIDING_CAL + "", cal);
            if (speed > 0)
                value.put(RCDeviceIndicatorID.RIDING_SPEED + "", speed);
            if (averageHeartRate > 0)
                value.put(RCDeviceIndicatorID.RIDING_AVERAGE_HEARTRATE + "", averageHeartRate);
            if (pace > 0)
                value.put(RCDeviceIndicatorID.RIDING_PACE + "", pace);
            if (!speedList.equals(""))
                value.put(RCDeviceIndicatorID.RIDING_SPEED_LIST + "", speedList);
            if (!paceList.equals(""))
                value.put(RCDeviceIndicatorID.RIDING_PACE_LIST + "", paceList);
            if (validTime > 0)
                value.put(RCDeviceIndicatorID.RIDING_EFFECTIVE_TIME + "", validTime);
            value.put(RCDeviceIndicatorID.SCENE_TYPE + "", type);
            jsonObject.put("value", value);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static RCDeviceRidingDataDTO format(String object) {
        try {
            RCDeviceRidingDataDTO dto = new RCDeviceRidingDataDTO();
            JSONObject jsonObject = new JSONObject(object);
            dto.setDeviceId(jsonObject.optInt("device_id"));
            dto.setDate(jsonObject.optLong("date"));
            JSONObject value = jsonObject.optJSONObject("value");
            if (value.has(RCDeviceIndicatorID.SCENE_TYPE + ""))
                dto.setType(value.optInt(RCDeviceIndicatorID.SCENE_TYPE + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_TIME + ""))
                dto.setTime(value.optInt(RCDeviceIndicatorID.RIDING_TIME + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_DISTANCE + ""))
                dto.setKm(value.optDouble(RCDeviceIndicatorID.RIDING_DISTANCE + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_START_TIME + ""))
                dto.setStartTime(value.optLong(RCDeviceIndicatorID.RIDING_START_TIME + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_STOP_TIME + ""))
                dto.setEndTime(value.optLong(RCDeviceIndicatorID.RIDING_STOP_TIME + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_GPS_LIST + ""))
                dto.setGpsList(value.optString(RCDeviceIndicatorID.RIDING_GPS_LIST + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_HEART_LIST + ""))
                dto.setHeartList(value.optString(RCDeviceIndicatorID.RIDING_HEART_LIST + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_CAL + ""))
                dto.setCal(value.optDouble(RCDeviceIndicatorID.RIDING_CAL + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_SPEED + ""))
                dto.setSpeed(value.optDouble(RCDeviceIndicatorID.RIDING_SPEED + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_AVERAGE_HEARTRATE + ""))
                dto.setAverageHeartRate(value.optInt(RCDeviceIndicatorID.RIDING_AVERAGE_HEARTRATE + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_PACE + ""))
                dto.setPace(value.optInt(RCDeviceIndicatorID.RIDING_PACE + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_SPEED_LIST + ""))
                dto.setSpeedList(value.optString(RCDeviceIndicatorID.RIDING_SPEED_LIST + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_PACE_LIST + ""))
                dto.setPaceList(value.optString(RCDeviceIndicatorID.RIDING_PACE_LIST + ""));
            if (value.has(RCDeviceIndicatorID.RIDING_EFFECTIVE_TIME + ""))
                dto.setValidTime(value.optInt(RCDeviceIndicatorID.RIDING_EFFECTIVE_TIME + ""));
            return dto;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
