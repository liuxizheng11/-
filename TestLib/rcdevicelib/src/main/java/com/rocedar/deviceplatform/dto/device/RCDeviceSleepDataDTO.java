package com.rocedar.deviceplatform.dto.device;

import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/11 下午3:04
 * 版本：V1.0
 * 描述：睡眠数据对象
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDeviceSleepDataDTO {


    //行为ID（固定）
    private int conductId = RCDeviceConductID.SLEEP;
    //主指标ID（固定）
    private int indicatorId = -1;
    //设备ID
    private int deviceId;
    //数据日期yyyyMMddHHmmss
    private long date;

    //深睡（分钟）
    private int deep = -1;
    //浅睡（分钟）
    private int shallow = -1;
    //总睡眠时长（分钟）
    private int all;
    //清醒次数（次）
    private int upNumber = -1;
    //清醒时间（分钟）
    private int upTime = -1;
    //入睡时间（yyyyMMddHHmmss）
    private long startTime;
    //醒来时间（yyyyMMddHHmmss）
    private long stopTime = -1;

    //4107:睡前状态(1-饮酒/2-压力大/3-陌生床/4-吃宵夜)
    private int sleepBeforeStatus = -1;
    //4108:梦境状态(1-噩梦/2-美梦/3-无梦)
    private int sleepDreamStatus = -1;
    //4109:睡眠备注(备注)
    private String sleepNote = "";
    //4110:睡眠心率明细类
    //     [{
    //        "time": "时间到秒",
    //                "heartrate": "心率值"
    //    }
    //  		   …]
    private String heart = "";
    //4111:睡眠轨迹
    //    	 [{
    //        "start_time": 这段睡眠开始时间到秒,
    //                "end_time": 这段睡眠结束时间到秒,
    //                "duration": 这段睡眠持续时长(秒),
    //                "status": 这段睡眠状态(1-浅睡/2-深睡/3-清醒)
    //    }
    //		 …]
    private String sleepTrajectory = "";

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public String getSleepTrajectory() {
        return sleepTrajectory;
    }

    public void setSleepTrajectory(String sleepTrajectory) {
        this.sleepTrajectory = sleepTrajectory;
    }

    public int getSleepBeforeStatus() {
        return sleepBeforeStatus;
    }

    public void setSleepBeforeStatus(int sleepBeforeStatus) {
        this.sleepBeforeStatus = sleepBeforeStatus;
    }

    public int getSleepDreamStatus() {
        return sleepDreamStatus;
    }

    public void setSleepDreamStatus(int sleepDreamStatus) {
        this.sleepDreamStatus = sleepDreamStatus;
    }

    public String getSleepNote() {
        return sleepNote;
    }

    public void setSleepNote(String sleepNote) {
        this.sleepNote = sleepNote;
    }

    public long getDate() {
        return date;
    }

    private void setDate(long date) {
        this.date = date;
    }

    public void setDate(String date) {
        try {
            this.date = Long.parseLong(date);
        } catch (NumberFormatException e) {
            this.date = 0L;
        }
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public int getShallow() {
        return shallow;
    }

    public void setShallow(int shallow) {
        this.shallow = shallow;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public int getUpNumber() {
        return upNumber;
    }

    public void setUpNumber(int upNumber) {
        this.upNumber = upNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        try {
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            //开始时间是开始日期12点之后，入睡时间是开始日期
            if (sf2.parse(sf1.format(new Date(sf2.parse(startTime + "").getTime()))
                    + "120000").getTime() < sf2.parse(startTime + "").getTime()) {
                setDate(Long.parseLong(sf1.format(new Date(sf2.parse(startTime + "").getTime())) + "000000"));
            } else {
                //开始时间是开始日期12点之，入睡时间是开始日期之前一天
                setDate(Long.parseLong(sf1.format(new Date(sf2.parse(startTime + "").getTime()
                        - 24 * 60 * 60 * 1000L)) + "000000"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getUpTime() {
        return upTime;
    }

    public void setUpTime(int upTime) {
        this.upTime = upTime;
    }

    public JSONObject getJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceId);
            jsonObject.put("indicator_id", indicatorId);
            jsonObject.put("conduct_id", conductId);
            jsonObject.put("date", date);
            JSONObject value = new JSONObject();
            if (startTime > 0)
                value.put(RCDeviceIndicatorID.SLEEP_IN + "", startTime);
            if (stopTime > 0)
                value.put(RCDeviceIndicatorID.SLEEP_WAKE + "", stopTime);
            if (all > 0)
                value.put(RCDeviceIndicatorID.SLEEP_TIME + "", all);
            if (deep > 0)
                value.put(RCDeviceIndicatorID.SLEEP_DEEP + "", deep);
            if (shallow > 0)
                value.put(RCDeviceIndicatorID.SLEEP_SHALLOW + "", shallow);
            if (sleepBeforeStatus > 0)
                value.put(RCDeviceIndicatorID.SLEEP_BEFORE_STATUS + "", sleepBeforeStatus);
            if (sleepDreamStatus > 0)
                value.put(RCDeviceIndicatorID.SLEEP_DREAM_STATUS + "", sleepDreamStatus);
            if (!sleepNote.equals(""))
                value.put(RCDeviceIndicatorID.SLEEP_NOTE + "", sleepNote);
            if (upNumber > 0)
                value.put(RCDeviceIndicatorID.SLEEP_SOBER_ORDER + "", upNumber);
            if (upTime > 0)
                value.put(RCDeviceIndicatorID.SLEEP_SOBER_TIME + "", upTime);
            if (!heart.equals(""))
                value.put(RCDeviceIndicatorID.SLEEP_HEART + "", sleepNote);
            if (!sleepTrajectory.equals(""))
                value.put(RCDeviceIndicatorID.SLEEP_GPS + "", sleepTrajectory);
            jsonObject.put("value", value);
            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RCDeviceSleepDataDTO fromJson(JSONObject object) {
        RCDeviceSleepDataDTO dto = new RCDeviceSleepDataDTO();
        dto.setDeviceId(object.optInt("device_id"));
        dto.setDate(object.optLong("date"));
        JSONObject value = object.optJSONObject("value");
        if (value.has(RCDeviceIndicatorID.SLEEP_TIME + ""))
            dto.setAll(value.optInt(RCDeviceIndicatorID.SLEEP_TIME + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_IN + ""))
            dto.setStartTime(value.optLong(RCDeviceIndicatorID.SLEEP_IN + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_WAKE + ""))
            dto.setStopTime(value.optLong(RCDeviceIndicatorID.SLEEP_WAKE + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_DEEP + ""))
            dto.setDeep(value.optInt(RCDeviceIndicatorID.SLEEP_DEEP + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_SHALLOW + ""))
            dto.setShallow(value.optInt(RCDeviceIndicatorID.SLEEP_SHALLOW + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_SOBER_ORDER + ""))
            dto.setUpNumber(value.optInt(RCDeviceIndicatorID.SLEEP_SOBER_ORDER + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_SOBER_TIME + ""))
            dto.setUpTime(value.optInt(RCDeviceIndicatorID.SLEEP_SOBER_TIME + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_BEFORE_STATUS + ""))
            dto.setSleepBeforeStatus(value.optInt(RCDeviceIndicatorID.SLEEP_BEFORE_STATUS + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_DREAM_STATUS + ""))
            dto.setSleepDreamStatus(value.optInt(RCDeviceIndicatorID.SLEEP_DREAM_STATUS + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_NOTE + ""))
            dto.setSleepNote(value.optString(RCDeviceIndicatorID.SLEEP_NOTE + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_HEART + ""))
            dto.setHeart(value.optString(RCDeviceIndicatorID.SLEEP_HEART + ""));
        if (value.has(RCDeviceIndicatorID.SLEEP_GPS + ""))
            dto.setSleepTrajectory(value.optString(RCDeviceIndicatorID.SLEEP_GPS + ""));
        return dto;
    }


}
