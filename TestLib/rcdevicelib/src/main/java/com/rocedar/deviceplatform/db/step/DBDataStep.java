package com.rocedar.deviceplatform.db.step;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.db.DBHelperStep;
import com.rocedar.deviceplatform.unit.AESUtils;
import com.rocedar.deviceplatform.unit.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by phj on 15/12/28.
 */
public class DBDataStep implements IStepInfo {


    private String TAG = "RCDevice_db_step";

    private DBHelperStep helper;
    private SQLiteDatabase db;

    private int countTemp = 0;


    public DBDataStep(Context context) {
        helper = DBHelperStep.getInstance(context);
        db = helper.getWritableDatabase();
        countTemp = 0;
    }

    public boolean isclose() {
        return !db.isOpen();
    }


    /**
     * 添加一条数据到数据库
     *
     * @param infoNew
     */
    public void addStepInfo(final StepInfoDTO infoNew) {
        if (!db.isOpen()) {
            return;
        }
        new Thread() { // 开启线程执行防止阻塞
            @Override
            public void run() {
                db.beginTransaction(); // 开始事务
                try {
                    try {
                        ContentValues values = new ContentValues();
                        values.put(TIMES, infoNew.getTimes());
                        values.put(SENSOR_TYPE, infoNew.getSensorType());
                        values.put(DATE, infoNew.getDate());
                        String Sql = "select * from " + TB_NAME_STEP + " where "
                                + TIMES + " = " + infoNew.getTimes();
                        Cursor cursor = db.rawQuery(Sql, null);
                        RCLog.d(TAG, "查询是否存在:" + Sql);
                        int temp = -1;
                        if (cursor.moveToFirst()) {
                            temp = cursor.getInt(cursor.getColumnIndex(STEP));
                        }
                        infoNew.setOtherone(countTemp + "," + infoNew.getOtherone());
                        values.put(OTHER_ONE, infoNew.getOtherone());
                        RCLog.d(TAG, "查询是否存在结果:" + temp);
                        if (temp >= 0) {
                            RCLog.d(TAG, "有值覆盖和为：" + (infoNew.getStep() + temp));
                            values.put(STEP, infoNew.getStep() + temp);
                            values.put(AES_STEP, AESUtils.aesEncrypt(RCBaseConfig.NETWORK_SIGN_SECRET_KEY,
                                    (infoNew.getStep() + temp) + ""));
                            db.update(TB_NAME_STEP, values, TIMES + " = " + infoNew.getTimes(), null);
                        } else {
                            countTemp++;
                            RCLog.d(TAG, "无值:" + infoNew.getStep());
                            values.put(STEP, infoNew.getStep());
                            values.put(AES_STEP, AESUtils.aesEncrypt(
                                    RCBaseConfig.NETWORK_SIGN_SECRET_KEY, infoNew.getStep() + ""));
                            db.insert(TB_NAME_STEP, null, values);
                        }
                        db.setTransactionSuccessful(); // 设置事务成功完成
                    } finally {
                        db.endTransaction(); // 结束事务
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * @return
     */
    public List<StepInfoDTO> selectAllDate() {
        List<StepInfoDTO> templist = new ArrayList<>();
        if (!db.isOpen()) {
            return templist;
        }
        String Sql = "select * from " + TB_NAME_STEP +
                " order by " + TIMES + " desc";
        Cursor cursor = db.rawQuery(Sql, null);
        while (cursor.moveToNext()) {
            StepInfoDTO dto = new StepInfoDTO();
            dto.setId(cursor.getInt(cursor.getColumnIndex(STEP_INFO_ID)));
            dto.setSensorType(cursor.getInt(cursor.getColumnIndex(SENSOR_TYPE)));
            dto.setStep(cursor.getLong(cursor.getColumnIndex(STEP)));
            dto.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
            dto.setTimes(cursor.getLong(cursor.getColumnIndex(TIMES)));
            dto.setOtherone(cursor.getString(cursor.getColumnIndex(OTHER_ONE)));
            String temp = cursor.getString(cursor.getColumnIndex(AES_STEP));
            if (null != temp && !temp.equals("")) {
                try {
                    temp = AESUtils.aesDecrypt(RCBaseConfig.NETWORK_SIGN_SECRET_KEY, temp);
                    dto.setStep(Long.parseLong(temp));
                } catch (NumberFormatException e) {
                    dto.setStep(-1);
                }
            }
            templist.add(dto);
        }
        cursor.close();
        return templist;
    }

    /**
     * 获取指定一天中的所有数据
     *
     * @param date
     * @return
     */
    public long getStepNumberFromDate(String date, boolean isShow) {
        if (!db.isOpen()) {
            return 0;
        }
        long allstep = 0;
        Cursor cursor = db.rawQuery("SELECT * from " +
                DBHelperStep.TB_NAME_STEP + " WHERE "
                + DATE + " = " + date, null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(cursor.getColumnIndex(AES_STEP));
            if (null != temp && !temp.equals("") && !isShow) {
                try {
                    temp = AESUtils.aesDecrypt(RCBaseConfig.NETWORK_SIGN_SECRET_KEY, temp);
                    allstep += Long.parseLong(temp);
                } catch (NumberFormatException e) {
                }
            } else {
                allstep += cursor.getLong(cursor.getColumnIndex(STEP));
            }
        }
        cursor.close();
        return allstep;
    }

    /**
     * 获取指定一天后的所有数据
     *
     * @return
     */
    public Map<String, Long> getStepNumberFromDates(String sData) {
        if (!db.isOpen()) {
            return null;
        }
        RCLog.i(TAG, "获取指定一天后的所有数据:" + sData);
        Cursor cursor = db.rawQuery("SELECT * from " +
                DBHelperStep.TB_NAME_STEP + " WHERE "
                + DATE + " >= " + sData, null);
        Map<String, Long> datas = new HashMap<>();
        while (cursor.moveToNext()) {
            long stepTemp = 0;
            String temp = cursor.getString(cursor.getColumnIndex(AES_STEP));
            if (null != temp && !temp.equals("")) {
                try {
                    temp = AESUtils.aesDecrypt(RCBaseConfig.NETWORK_SIGN_SECRET_KEY, temp);
                    stepTemp = Long.parseLong(temp);
                } catch (NumberFormatException e) {
                }
            } else {
                stepTemp = cursor.getLong(cursor.getColumnIndex(STEP));
            }
            String data = cursor.getString(cursor.getColumnIndex(DATE));
            if (datas.containsKey(data)) {
                datas.put(data, datas.get(data) + stepTemp);
            } else {
                datas.put(data, stepTemp);
            }
        }
        cursor.close();
        return datas;
    }


    /**
     * 删除10天前的数据
     */
    public void deleteTenDaysAgo() {
        if (!db.isOpen()) {
            return;
        }
        new Thread() { // 开启线程执行防止阻塞
            @Override
            public void run() {
                db.delete(TB_NAME_STEP, DATE + " < ?"
                        , new String[]{DateUtil.getFormatIntervalDay(10, "yyyyMMdd")});
            }
        }.start();
    }


    public void close() {
        db.close();
    }


}
