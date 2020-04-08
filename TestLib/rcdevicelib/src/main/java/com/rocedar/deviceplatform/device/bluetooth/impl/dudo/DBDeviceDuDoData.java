package com.rocedar.deviceplatform.device.bluetooth.impl.dudo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.dto.device.RCDeviceHeartRateDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceSleepDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/26 下午11:53
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DBDeviceDuDoData {

    private String TAG = "RCDevice_db_step";

    private com.yc.peddemo.sdk.i helper;
    private SQLiteDatabase db;

    private int countTemp = 0;


    public DBDeviceDuDoData(Context context) {
        helper = com.yc.peddemo.sdk.i.a(context);
        db = helper.getWritableDatabase();
        countTemp = 0;
    }

    public boolean isclose() {
        return !db.isOpen();
    }


    /**
     * 查询历史睡眠数据
     */
    public JSONArray getHistorySleep() {
        JSONArray jsonArray = new JSONArray();
        if (!db.isOpen()) {
            return jsonArray;
        }
        String Sql = "select name from sqlite_master where type='table' and name LIKE 'sleep_table_%' "
                + " order by name;";
        Cursor cursor = db.rawQuery(Sql, null);
        String dataSql = "";
        while (cursor.moveToNext()) {
            try {
                String tempName = cursor.getString(cursor.getColumnIndex("name"));
                if (!dataSql.equals("")) {
                    dataSql = dataSql + " union ";
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                String nextDay = format.format(new Date(format.parse(tempName.substring(12)).getTime() + 24L * 3600L * 1000L));
                dataSql = dataSql + "select CASE WHEN time > 720 THEN '" + nextDay + "' ELSE '" +
                        "" + tempName.substring(12) + "' END day,time, color from " + tempName;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        Map<String, List<SleepDate>> dateMap = new HashMap<>();
        cursor = db.rawQuery(dataSql, null);
        while (cursor.moveToNext()) {
            SleepDate sleepDate = new SleepDate();
            sleepDate.day = cursor.getString(cursor.getColumnIndex("day"));
            sleepDate.time = cursor.getInt(cursor.getColumnIndex("time"));
            sleepDate.color = cursor.getInt(cursor.getColumnIndex("color"));
            List<SleepDate> sleepDateList;
            if (dateMap.containsKey(sleepDate.day)) {
                sleepDateList = dateMap.get(sleepDate.day);
            } else {
                sleepDateList = new ArrayList<>();
            }
            sleepDateList.add(sleepDate);
            dateMap.put(sleepDate.day, sleepDateList);
        }
        if (dataSql.equals("")) return jsonArray;
        Map<String, Integer> allTimes = new HashMap<>();
        String sqlAll = "select date,time from sleep_total_table";
        cursor = db.rawQuery(sqlAll, null);
        while (cursor.moveToNext()) {
            allTimes.put(
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getInt(cursor.getColumnIndex("time"))
            );
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        for (String day : dateMap.keySet()) {
            if (allTimes.containsKey(day)) {
                try {
                    RCDeviceSleepDataDTO sleepDataDto = new RCDeviceSleepDataDTO();
                    List<SleepDate> dataList = dateMap.get(day);
                    int deepNumber = 0, lowNumber = 0, upNumber = 0;
                    int max_time = 0;
                    //去掉开始为清醒的数据
                    while (dataList != null && dataList.size() > 0 && dataList.get(0).color == 2) {
                        dataList.remove(0);
                    }
                    //去掉结束为清醒的数据
                    while (dataList != null && dataList.size() > 0 && dataList.get(dataList.size() - 1).color == 2) {
                        dataList.remove(dataList.size() - 1);
                    }
                    for (int i = 0; i < dataList.size(); i++) {
                        //统计睡眠状态
                        if (dataList.get(i).color == 1) {
                            lowNumber++;
                        } else if (dataList.get(i).color == 2) {
                            upNumber++;
                        } else if (dataList.get(i).color == 0) {
                            deepNumber++;
                        }
                        //计算醒来时间
                        if (dataList.get(i).time < 720 && max_time < dataList.get(i).time) {
                            max_time = dataList.get(i).time;
                        }
                    }
                    sleepDataDto.setAll(allTimes.get(day));
                    sleepDataDto.setDeep(deepNumber * 15);
                    sleepDataDto.setShallow(lowNumber * 15);
                    sleepDataDto.setUpNumber(upNumber);
                    sleepDataDto.setStopTime(Long.parseLong(getDate(max_time, day)));
                    sleepDataDto.setStartTime(Long.parseLong(
                            dateFormat.format(new Date(
                                    dateFormat.parse(sleepDataDto.getStopTime() + "").getTime()
                                            - sleepDataDto.getAll() * 60000L))));
                    sleepDataDto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
                    jsonArray.put(sleepDataDto.getJSON());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }

    private class SleepDate {
        String day;
        int time;
        int color;
    }


    /**
     * 查询当前时间到指定时间的历史睡眠数据
     *
     * @param startDate 查询开始时间 yyyyMMdd
     */
    public JSONArray getHistorySleep(int startDate) {
        JSONArray jsonArray = new JSONArray();
        if (!db.isOpen()) {
            return jsonArray;
        }
        String Sql = "select name from sqlite_master where type='table' and name LIKE 'sleep_table_%' " +
                "and name >= 'sleep_table_" + startDate + "' order by name;";
        Cursor cursor = db.rawQuery(Sql, null);
        String dataSql = "";
        while (cursor.moveToNext()) {
            try {
                String tempName = cursor.getString(cursor.getColumnIndex("name"));
                if (!dataSql.equals("")) {
                    dataSql = dataSql + " union ";
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                String nextDay = format.format(new Date(format.parse(tempName.substring(12)).getTime() + 24L * 3600L * 1000L));
                dataSql = dataSql + "select CASE WHEN time > 720 THEN '" + nextDay + "' ELSE '" +
                        "" + tempName.substring(12) + "' END day,time, color from " + tempName;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (dataSql.equals("")) return jsonArray;
        Map<String, List<SleepDate>> dateMap = new HashMap<>();
        cursor = db.rawQuery(dataSql, null);
        while (cursor.moveToNext()) {
            SleepDate sleepDate = new SleepDate();
            sleepDate.day = cursor.getString(cursor.getColumnIndex("day"));
            sleepDate.time = cursor.getInt(cursor.getColumnIndex("time"));
            sleepDate.color = cursor.getInt(cursor.getColumnIndex("color"));
            List<SleepDate> sleepDateList;
            if (dateMap.containsKey(sleepDate.day)) {
                sleepDateList = dateMap.get(sleepDate.day);
            } else {
                sleepDateList = new ArrayList<>();
            }
            sleepDateList.add(sleepDate);
            dateMap.put(sleepDate.day, sleepDateList);
        }
        Map<String, Integer> allTimes = new HashMap<>();
        String sqlAll = "select date,time from sleep_total_table";
        cursor = db.rawQuery(sqlAll, null);
        while (cursor.moveToNext()) {
            allTimes.put(
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getInt(cursor.getColumnIndex("time"))
            );
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        for (String day : dateMap.keySet()) {
            if (allTimes.containsKey(day)) {
                try {
                    RCDeviceSleepDataDTO sleepDataDto = new RCDeviceSleepDataDTO();
                    List<SleepDate> dataList = dateMap.get(day);
                    int deepNumber = 0, lowNumber = 0, upNumber = 0;
                    int max_time = 0;
                    //去掉开始为清醒的数据
                    while (dataList != null && dataList.size() > 0 && dataList.get(0).color == 2) {
                        dataList.remove(0);
                    }
                    //去掉结束为清醒的数据
                    while (dataList != null && dataList.size() > 0 && dataList.get(dataList.size() - 1).color == 2) {
                        dataList.remove(dataList.size() - 1);
                    }
                    for (int i = 0; i < dataList.size(); i++) {
                        //统计睡眠状态
                        if (dataList.get(i).color == 1) {
                            lowNumber++;
                        } else if (dataList.get(i).color == 2) {
                            upNumber++;
                        } else if (dataList.get(i).color == 0) {
                            deepNumber++;
                        }
                        //计算醒来时间
                        if (dataList.get(i).time < 720 && max_time < dataList.get(i).time) {
                            max_time = dataList.get(i).time;
                        }
                    }
                    sleepDataDto.setAll(allTimes.get(day));
                    sleepDataDto.setDeep(deepNumber * 15);
                    sleepDataDto.setShallow(lowNumber * 15);
                    sleepDataDto.setUpNumber(upNumber);
                    sleepDataDto.setStopTime(Long.parseLong(getDate(max_time, day)));
                    sleepDataDto.setStartTime(Long.parseLong(
                            dateFormat.format(new Date(
                                    dateFormat.parse(sleepDataDto.getStopTime() + "").getTime()
                                            - sleepDataDto.getAll() * 60000L))));
                    sleepDataDto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
                    jsonArray.put(sleepDataDto.getJSON());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }


    /**
     * 查询历史心率数据
     */
    public JSONArray getHistoryRate() {
        JSONArray jsonArray = new JSONArray();
        String Sql = "select name from sqlite_master where type='table' and name LIKE 'rate_table_%' " +
                "order by name;";
        Cursor cursor = db.rawQuery(Sql, null);
        String dataSql = "";
        while (cursor.moveToNext()) {
            String tempName = cursor.getString(cursor.getColumnIndex("name"));
            if (!dataSql.equals("")) {
                dataSql = dataSql + " union ";
            }
            dataSql = dataSql + "select '" + tempName.substring(11) + "' day, time, rate from " + tempName;
        }
        if (dataSql.equals("")) return jsonArray;
        cursor = db.rawQuery(dataSql, null);
        while (cursor.moveToNext()) {
            RCDeviceHeartRateDataDTO dto = new RCDeviceHeartRateDataDTO();
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setDate(getDate(
                    cursor.getInt(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("day"))
            ));
            dto.setNumber(
                    cursor.getInt(cursor.getColumnIndex("rate"))
            );
            jsonArray.put(dto.getJSON());
        }
        cursor.close();
        return jsonArray;
    }


    /**
     * 查询历史心率数据
     *
     * @param startDate 查询开始时间 yyyyMMdd
     */
    public JSONArray getHistoryRate(int startDate) {
        JSONArray jsonArray = new JSONArray();
        String Sql = "select name from sqlite_master where type='table' and name LIKE 'rate_table_%' " +
                "and name >= 'rate_table_" + startDate + "' order by name;";
        Cursor cursor = db.rawQuery(Sql, null);
        String dataSql = "";
        while (cursor.moveToNext()) {
            String tempName = cursor.getString(cursor.getColumnIndex("name"));
            if (!dataSql.equals("")) {
                dataSql = dataSql + " union ";
            }
            dataSql = dataSql + "select '" + tempName.substring(11) + "' day, time, rate from " + tempName;
        }
        if (dataSql.equals("")) return jsonArray;
        cursor = db.rawQuery(dataSql, null);
        while (cursor.moveToNext()) {
            RCDeviceHeartRateDataDTO dto = new RCDeviceHeartRateDataDTO();
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setDate(getDate(
                    cursor.getInt(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("day"))
            ));
            dto.setNumber(
                    cursor.getInt(cursor.getColumnIndex("rate"))
            );
            jsonArray.put(dto.getJSON());
        }
        cursor.close();

        return jsonArray;
    }

    /**
     * 查询今日心率
     *
     * @return
     */
    public JSONArray getTodayRate() {
        JSONArray jsonArray = new JSONArray();
        String tempName = "rate_table_" + DateUtil.getFormatNow("yyyyMMdd");
        String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tempName + "' ";
        Cursor cursor = db.rawQuery(sql, null);
        boolean result = false;
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                result = true;
            }
        }
        if (!result) return jsonArray;
        String Sql = "select '" + tempName.substring(11) + "' day, time, rate from " + tempName;
        cursor = db.rawQuery(Sql, null);
        while (cursor.moveToNext()) {
            RCDeviceHeartRateDataDTO dto = new RCDeviceHeartRateDataDTO();
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setDate(getDate(
                    cursor.getInt(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("day"))
            ));
            dto.setNumber(
                    cursor.getInt(cursor.getColumnIndex("rate"))
            );
            jsonArray.put(dto.getJSON());
        }
        cursor.close();
        return jsonArray;
    }


    /**
     * 查询历史步数
     *
     * @return
     */
    public JSONArray getHistoryStep() {
        JSONArray jsonArray = new JSONArray();
        if (!db.isOpen()) {
            return jsonArray;
        }
        String Sql = "select * from step_total_table";
        Cursor cursor = db.rawQuery(Sql, null);
        while (cursor.moveToNext()) {
            RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
            dto.setDate(cursor.getString(cursor.getColumnIndex("date")) + "000000");
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setStep(cursor.getInt(cursor.getColumnIndex("step")));
            jsonArray.put(dto.getJSON());
        }
        return jsonArray;
    }


    /**
     * 查询当前时间到指定时间的历史步数
     *
     * @param startDate
     * @return
     */
    public JSONArray getHistoryStep(int startDate) {
        JSONArray jsonArray = new JSONArray();
        if (!db.isOpen()) {
            return jsonArray;
        }
        String Sql = "select * from step_total_table where date >= " + startDate;
        Cursor cursor = db.rawQuery(Sql, null);
        while (cursor.moveToNext()) {
            RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
            dto.setDate(cursor.getString(cursor.getColumnIndex("date")) + "000000");
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setStep(cursor.getInt(cursor.getColumnIndex("step")));
            jsonArray.put(dto.getJSON());
        }
        return jsonArray;
    }


    private String getTime(int time) {
        return (time / 60 >= 10 ? time / 60 + "" : "0" + time / 60)
                + (time % 60 >= 10 ? time % 60 + "" : "0" + time % 60) + "00";
    }

    private String getDate(int time, String day) {
        return day + getTime(time);
    }


    /**
     * 查询历史睡眠数据
     */
    public JSONArray getHistorySleepNew() {
        return getHistorySleepNew(-1);
    }


    /**
     * 查询历史睡眠数据
     */
    public JSONArray getHistorySleepNew(int startDate) {
        JSONArray jsonArray = new JSONArray();
        if (!db.isOpen()) {
            return jsonArray;
        }
        String Sql = "";
        if (startDate > 0) {
            Sql = "select name from sqlite_master where type='table' and name LIKE 'sleep_table_%' " +
                    "and name >= 'sleep_table_" + startDate + "' order by name;";
        } else {
            Sql = "select name from sqlite_master where type='table' and name LIKE 'sleep_table_%' "
                    + " order by name;";
        }
        Cursor cursor = db.rawQuery(Sql, null);
        String dataSql = "";
        while (cursor.moveToNext()) {
            String tempName = cursor.getString(cursor.getColumnIndex("name"));
            if (!dataSql.equals("")) {
                dataSql = dataSql + " union ";
            }
            dataSql = dataSql + "select '" + tempName.substring(12) + "' day,time, color from " + tempName;
        }
        if (dataSql.equals("")) return jsonArray;
        List<SleepDataNew> dateMap = new ArrayList<>();
        cursor = db.rawQuery(dataSql, null);
        while (cursor.moveToNext()) {
            SleepDataNew sleepDate = new SleepDataNew();
            sleepDate.setTime(cursor.getInt(cursor.getColumnIndex("time")));
            sleepDate.setDate(cursor.getString(cursor.getColumnIndex("day")));
            sleepDate.setStatus(cursor.getInt(cursor.getColumnIndex("color")));
            dateMap.add(sleepDate);
        }
        Collections.sort(dateMap);
        //转换数据
        List<SleepDataInfo> sleepInfoList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        JSONArray a = new JSONArray();
        for (int i = 0; i < dateMap.size(); i++) {
            try {
                SleepDataInfo dto = new SleepDataInfo();
                dto.eDate = getDate(dateMap.get(i).getTime(), dateMap.get(i).getDate() + "");
                dto.sDate = format.format(new Date(format.parse(dto.eDate).getTime() - 15L * 60L * 1000L));
                dto.status = dateMap.get(i).getStatus();
                sleepInfoList.add(dto);
                try {
                    a.put(new JSONObject().put("Stime", dto.sDate + "(" + dateMap.get(i).getTime() + ")")
                            .put("Etime", dto.eDate).put("status", dto.status));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        RCLog.e("TestLib", a.toString());
        //计算数据
        List<List<SleepDataInfo>> sleepdatalist = new ArrayList<>();
        List<SleepDataInfo> listtemp = null;
        for (int i = 0; i < sleepInfoList.size(); i++) {
            if (listtemp == null) {
                listtemp = new ArrayList<>();
            }
            if (listtemp.size() == 0) {
                //状态为不为清醒，数据有效，为第一条数据
                if (sleepInfoList.get(i).status != 2) {
                    listtemp.add(sleepInfoList.get(i));
                    continue;
                } else {
                    continue;
                }
            } else {
                //数组中有数据是，判断数据有效性
                if (sleepInfoList.get(i).status != 2) {
                    //第二条数据和第一条数据间隔大于60，为新数据
                    if (isBetween60(listtemp.get(listtemp.size() - 1).eDate, sleepInfoList.get(i).sDate)) {
                        sleepdatalist.add(listtemp);
                        listtemp = new ArrayList<>();
                        listtemp.add(sleepInfoList.get(i));
                        continue;
                    } else {
                        listtemp.add(sleepInfoList.get(i));
                    }
                    if (sleepInfoList.size() - 1 == i && listtemp != null && listtemp.size() > 0) {
                        sleepdatalist.add(listtemp);
                    }
                } else {
                    //第二条数据为清醒，取下一条数据和上一条数据比较，如果下一条数据还是清醒，取下一条
                    boolean temp = false;
                    a:
                    for (int j = i + 1; j < sleepInfoList.size(); j++) {
                        if (sleepInfoList.get(j).status != 2) {
                            temp = true;
                            if (isBetween60(listtemp.get(listtemp.size() - 1).eDate, sleepInfoList.get(j).sDate)) {
                                sleepdatalist.add(listtemp);
                                listtemp = new ArrayList<>();
                                listtemp.add(sleepInfoList.get(j));
                            } else {
                                for (int k = i; k < j; k++) {
                                    listtemp.add(sleepInfoList.get(k));
                                }
                                listtemp.add(sleepInfoList.get(j));
                            }
                            i = j;
                            break a;
                        }
                    }
                    if (!temp) {
                        //最后的数据为清醒
                        sleepdatalist.add(listtemp);
                        listtemp = new ArrayList<>();
                        i = sleepInfoList.size();
                    }
                }

            }
        }
        Map<String, RCDeviceSleepDataDTO> sleepDataMap = new HashMap<>();
        for (int i = 0; i < sleepdatalist.size(); i++) {
            if (sleepdatalist.get(i).size() == 0) continue;
            while (sleepdatalist.get(i).size() > 0 && sleepdatalist.get(i).get(0).status == 2) {
                sleepdatalist.remove(0);
            }
            while (sleepdatalist.get(i).size() > 0 &&
                    sleepdatalist.get(i).get(sleepdatalist.get(i).size() - 1).status == 2) {
                sleepdatalist.remove(sleepdatalist.get(i).size() - 1);
            }
            if (sleepdatalist.get(i).size() == 0) continue;
            try {
                RCDeviceSleepDataDTO dto = new RCDeviceSleepDataDTO();
                dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
                dto.setStartTime(Long.parseLong(sleepdatalist.get(i).get(0).sDate));
                dto.setStopTime(Long.parseLong(sleepdatalist.get(i).get(sleepdatalist.get(i).size() - 1).eDate));
                dto.setAll(
                        (int) betweenNumber(dto.getStartTime() + "", dto.getStopTime() + "") / 60000
                );
                long low = 0, deep = 0, up = 0, upnumber = 0;
                if (sleepdatalist.get(i).size() == 1) {
                    if (sleepdatalist.get(i).get(0).status == 2) {
                        continue;
                    }
                    long tempTime = betweenNumber(sleepdatalist.get(i).get(0).sDate
                            , sleepdatalist.get(i).get(0).eDate);
                    if (tempTime <= 0) {
                        continue;
                    }
                    if (sleepdatalist.get(i).get(0).status == 1) {
                        low = tempTime;
                    } else if (sleepdatalist.get(i).get(0).status == 0) {
                        deep = tempTime;
                    }
                } else {
                    for (int j = 0; j < sleepdatalist.get(i).size(); j++) {
                        if (j == sleepdatalist.get(i).size() - 1) {
                            long tempTime = betweenNumber(sleepdatalist.get(i).get(j).sDate
                                    , sleepdatalist.get(i).get(j).eDate);
                            if (tempTime <= 0) {
                                continue;
                            }
                            if (sleepdatalist.get(i).get(j).status == 1) {
                                low += tempTime;
                            } else if (sleepdatalist.get(i).get(j).status == 0) {
                                deep += tempTime;
                            }
                        } else if (sleepdatalist.get(i).size() > j + 1) {
                            long tempTime = betweenNumber(sleepdatalist.get(i).get(j).sDate
                                    , sleepdatalist.get(i).get(j + 1).sDate);
                            if (tempTime <= 0) {
                                continue;
                            }
                            if (sleepdatalist.get(i).get(j).status == 1) {
                                low += tempTime;
                            } else if (sleepdatalist.get(i).get(j).status == 0) {
                                deep += tempTime;
                            } else if (sleepdatalist.get(i).get(j).status == 2) {
                                up += tempTime;
                                upnumber += 1;
                            }
                        }
                    }
                }
                dto.setShallow((int) low / 60000);
                dto.setDeep((int) deep / 60000);
                dto.setUpNumber((int) upnumber);
                dto.setUpTime((int) up / 60000);
                if (sleepDataMap.containsKey(dto.getDate() + "")) {
                    if (sleepDataMap.get(dto.getDate() + "").getAll() < dto.getAll()) {
                        sleepDataMap.put(dto.getDate() + "", dto);
                    }
                } else {
                    sleepDataMap.put(dto.getDate() + "", dto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String key : sleepDataMap.keySet()) {
            jsonArray.put(sleepDataMap.get(key).getJSON());
        }
        return jsonArray;
    }

    private boolean isBetween60(String D1E, String D2S) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return format.parse(D2S).getTime()
                    - format.parse(D1E).getTime() > 60L * 60L * 1000L;
        } catch (ParseException e) {
            return true;
        }
    }

    private long betweenNumber(String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return format.parse(end).getTime() - format.parse(start).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }


    private class SleepDataNew implements Comparable<SleepDataNew> {
        int date;
        int time;
        int status;

        public int getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = Integer.parseInt(date);
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public int compareTo(@NonNull SleepDataNew o) {
            int num = Integer.valueOf(this.date).compareTo(o.date);//先比较日期
            if (num == 0) {
                return Integer.valueOf(this.time).compareTo(o.time);//比较时间
            }
            return num;
        }
    }


    private class SleepDataInfo {

        String sDate;
        String eDate;
        int status;

    }
}
