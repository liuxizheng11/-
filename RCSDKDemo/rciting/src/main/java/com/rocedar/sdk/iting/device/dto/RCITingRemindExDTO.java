package com.rocedar.sdk.iting.device.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.appscomm.bluetoothsdk.app.SettingType;
import cn.appscomm.bluetoothsdk.model.ReminderExData;

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
public class RCITingRemindExDTO implements Serializable {

    public List<String> timeList;
    public String date;
    public int id;
    public int cycle;
    public boolean status;
    public boolean isOnece;
    public String content;

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<String> timeList) {
        this.timeList = timeList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isOnece() {
        return isOnece;
    }

    public void setOnece(boolean onece) {
        isOnece = onece;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReminderExData.Time getTime(String time) {
        if (time.length() == 4) {
            return new ReminderExData.Time(
                    Integer.parseInt(time.substring(0, 2)),
                    Integer.parseInt(time.substring(2, 4))
            );
        }
        return null;
    }

    public ReminderExData.Date getDate() {
        if (date.length() == 8) {
            return new ReminderExData.Date(
                    Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(4, 6)),
                    Integer.parseInt(date.substring(6, 8))
            );
        }
        return null;
    }

    public ReminderExData getReminderExData() {
        ReminderExData.Repeat repeat;
        List<ReminderExData.Time> times = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            ReminderExData.Time temp = getTime(timeList.get(i));
            if (temp != null)
                times.add(temp);
        }
        ReminderExData reminderExData;
        if (isOnece) {
            repeat = new ReminderExData.Repeat(SettingType.REPEAT_TYPE_NO_REPEAT, 0);
            reminderExData = new ReminderExData.Builder().customType(content)
                    .shockRingType(SettingType.SHOCK_MODE_INTERVAL_TWO_LONG_SHOCK)
                    .timeList(times).date(getDate()).enable(true).repeat(repeat).build();
        } else {
            repeat = new ReminderExData.Repeat(SettingType.REPEAT_TYPE_WEEK, 1);
            reminderExData = new ReminderExData.Builder().customType(content).cycle(cycle)
                    .shockRingType(SettingType.SHOCK_MODE_INTERVAL_TWO_LONG_SHOCK)
                    .timeList(times).enable(true).repeat(repeat).build();
        }
        return reminderExData;
    }

    public ReminderExData getReminderExData(int id) {
        ReminderExData.Repeat repeat;
        List<ReminderExData.Time> times = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            ReminderExData.Time temp = getTime(timeList.get(i));
            if (temp != null)
                times.add(temp);
        }
        ReminderExData reminderExData;
        if (isOnece) {
            repeat = new ReminderExData.Repeat(SettingType.REPEAT_TYPE_NO_REPEAT, 0);
            reminderExData = new ReminderExData.Builder().customType(content)
                    .id(id).shockRingType(SettingType.SHOCK_MODE_INTERVAL_TWO_LONG_SHOCK)
                    .timeList(times).date(getDate()).enable(true).repeat(repeat).build();
        } else {
            repeat = new ReminderExData.Repeat(SettingType.REPEAT_TYPE_WEEK, 1);
            reminderExData = new ReminderExData.Builder().customType(content).cycle(cycle)
                    .id(id).shockRingType(SettingType.SHOCK_MODE_INTERVAL_TWO_LONG_SHOCK)
                    .timeList(times).enable(true).repeat(repeat).build();
        }
        return reminderExData;
    }


    public static RCITingRemindExDTO getRCITingRemind(ReminderExData data) {
        RCITingRemindExDTO exDTO = new RCITingRemindExDTO();
        exDTO.content = data.customType;
        exDTO.cycle = data.cycle;
        exDTO.id = data.id;
        if (data.date != null) {
            exDTO.isOnece = true;
            if (data.date != null && !data.equals("")) {
                StringBuffer b = new StringBuffer();
                b.append(data.date.year);
                if (data.date.month < 10) {
                    b.append("0");
                }
                b.append(data.date.month);
                if (data.date.day < 10) {
                    b.append("0");
                }
                b.append(data.date.day);
                exDTO.date = b.toString();

            }
        } else {
            exDTO.isOnece = false;
        }
        List<String> times = new ArrayList<>();
        for (int i = 0; data.timeList != null && i < data.timeList.size(); i++) {
            StringBuffer b = new StringBuffer();
            if (data.timeList.get(i).hour < 10) {
                b.append("0");
            }
            b.append(data.timeList.get(i).hour);
            if (data.timeList.get(i).min < 10) {
                b.append("0");
            }
            b.append(data.timeList.get(i).min);
            times.add(b.toString());
        }
        exDTO.timeList = times;
        exDTO.status = data.enable;
        return exDTO;
    }

    public char[] getCycleChar() {
        char[] temp = new char[7];
        char[] tempCycle = Integer.toBinaryString(cycle).toCharArray();
        int index = 0;
        for (int i = tempCycle.length - 1; i >= 0; i--) {
            if (index < 7)
                temp[index] = tempCycle[i];
            index++;
        }
        return temp;
    }

}
