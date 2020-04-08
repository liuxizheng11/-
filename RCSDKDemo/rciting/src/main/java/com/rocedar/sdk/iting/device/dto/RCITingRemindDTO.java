package com.rocedar.sdk.iting.device.dto;

import org.json.JSONException;
import org.json.JSONObject;

import cn.appscomm.bluetoothsdk.app.SettingType;
import cn.appscomm.bluetoothsdk.model.ReminderData;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/4/22 7:22 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingRemindDTO {


    private int id;
    public int year = 0;
    public int month = 0;
    public int day = 0;
    public int hour;
    public int min;
    public int cycle;
    public boolean status;
    public int repeatType = 0;
    public String content;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }


    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReminderData getReminderData() {
        ReminderData data = new ReminderData();
        data.year = year;
        data.month = month;
        data.day = day;
        data.hour = hour;
        data.min = min;
        data.content = content;
        data.cycle = cycle;
        data.type = SettingType.REMINDER_CUSTOM;
        data.status = status;
        data.repeatType = repeatType;
        data.repeatValue = 1;
        data.id = id;
        data.shock = 0;
        return data;
    }

    public static RCITingRemindDTO getRCITingRemind(ReminderData dto) {
        RCITingRemindDTO data = new RCITingRemindDTO();
        data.year = dto.year;
        data.month = dto.month;
        data.day = dto.day;
        data.hour = dto.hour;
        data.min = dto.min;
        data.content = dto.content;
        data.cycle = dto.cycle;
        data.status = dto.status;
        data.repeatType = dto.repeatType;
        data.id = dto.id;
        return data;
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("year", year);
            object.put("month", month);
            object.put("hour", hour);
            object.put("min", min);
            object.put("content", content);
            object.put("cycle", cycle);
            object.put("status", status);
            object.put("repeatType", repeatType);
            object.put("day", day);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    public static RCITingRemindDTO toDTO(JSONObject object) {
        RCITingRemindDTO dto = new RCITingRemindDTO();
        dto.setId(object.optInt("id"));
        dto.setYear(object.optInt("year"));
        dto.setMonth(object.optInt("month"));
        dto.setDay(object.optInt("day"));
        dto.setHour(object.optInt("hour"));
        dto.setMin(object.optInt("min"));
        dto.setContent(object.optString("content"));
        dto.setStatus(object.optBoolean("status"));
        dto.setCycle(object.optInt("cycle"));
        dto.setRepeatType(object.optInt("repeatType"));
        return dto;
    }

    public String getDate() {
        StringBuilder builder = new StringBuilder();
        builder.append(year + "");
        if (month < 10)
            builder.append("0");
        builder.append(month + "");
        if (day < 10)
            builder.append("0");
        builder.append(day + "");
        return builder.toString();
    }

    public String getTime() {
        StringBuilder builder = new StringBuilder();
        if (hour < 10)
            builder.append("0");
        builder.append(hour + "");
        if (min < 10)
            builder.append("0");
        builder.append(min + "");
        return builder.toString();
    }


    /**
     * @param time HHmm
     */
    public void setTime(String time) {
        if (time.length() == 4) {
            hour = Integer.parseInt(time.substring(0, 2));
            min = Integer.parseInt(time.substring(2, 4));
        }
    }


    /**
     * @param date yyyyMMdd
     */
    public void setDate(String date) {
        if (date.length() == 8) {
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(4, 6));
            day = Integer.parseInt(date.substring(6, 8));
        }
    }

    public char[] getCycleChar() {
        char[] temp = new char[]{'0', '0', '0', '0', '0', '0', '0'};
        char[] tempCycle = Integer.toBinaryString(cycle).toCharArray();
        for (int i = 0; i < tempCycle.length; i++) {
            temp[temp.length - i - 1] = tempCycle[i];
        }
        return temp;
    }

    public void setCycleChat(char[] cycle) {
        setCycleChat(cycle.toString());
    }

    public void setCycleChat(String cycle) {
        this.cycle = Integer.parseInt(cycle, 2);
    }

    public void setOnly(boolean isOnlyOnce) {
        this.repeatType = isOnlyOnce ? 0 : 2;
    }

}
