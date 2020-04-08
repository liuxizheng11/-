package com.rocedar.deviceplatform.db.bong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.db.DBHelperStep;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/6/28 下午2:48
 * 版本：V1.0.01
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DBDataBong implements IBongInfo {

    private String TAG = "DBDataBong";

    private DBHelperStep helper;
    private SQLiteDatabase db;

    public DBDataBong(Context context) {
        helper = DBHelperStep.getInstance(context);
        db = helper.getWritableDatabase();
    }


    /**
     * 添加一条数据到数据库
     *
     * @param infoNew
     */
    public void addInfo(final BongInfoDTO infoNew) {
        if (!db.isOpen()) {
            return;
        }
        db.beginTransaction(); // 开始事务
        try {
            String Sql = "select count(*) from " + BONG_TB_NAME + " where "
                    + BONG_DATETIME + " = " + infoNew.getDateTime() + " and " +
                    BONG_DEVICEMAC + " = '" + infoNew.getDeviceMac() + "'";
            Cursor cursor = db.rawQuery(Sql, null);
            int temp = -1;
            if (cursor.moveToFirst()) {
                temp = cursor.getInt(0);
            }
            ContentValues values = new ContentValues();
            values.put(BONG_DATE, infoNew.getDate());
            values.put(BONG_DATETIME, infoNew.getDateTime());
            values.put(BONG_DEVICEID, infoNew.getDeviceId());
            values.put(BONG_DEVICEMAC, infoNew.getDeviceMac());
            values.put(BONG_INFO, infoNew.getInfo());
            if (temp >= 0) {
                db.update(BONG_TB_NAME, values, BONG_DATETIME + " = " + infoNew.getDateTime() + " and " +
                        BONG_DEVICEMAC + " = '" + infoNew.getDeviceMac() + "'", null);
            } else {
                db.insert(BONG_TB_NAME, null, values);
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * 删除指定日期之前的数据（不包括指定日期）
     */
    public void deleteInfoDate(final String Date, final String deviceMac) {
        if (!db.isOpen()) {
            return;
        }
        db.delete(BONG_TB_NAME, BONG_DATE + " = ? and " + BONG_DEVICEMAC + " = ? "
                , new String[]{Date, deviceMac});
    }

    /**
     * 添加多条数据到数据库
     *
     * @param infoNewList
     */
    public void addInfo(final List<BongInfoDTO> infoNewList) {
        if (!db.isOpen()) {
            return;
        }

        db.beginTransaction(); // 开始事务
        try {
            for (int i = 0; i < infoNewList.size(); i++) {
                BongInfoDTO infoNew = infoNewList.get(i);
                String Sql = "select count(*) from " + BONG_TB_NAME + " where "
                        + BONG_DATETIME + " = " + infoNew.getDateTime() + " and " +
                        BONG_DEVICEMAC + " = '" + infoNew.getDeviceMac() + "'";
                Cursor cursor = db.rawQuery(Sql, null);
                int temp = -1;
                if (cursor.moveToFirst()) {
                    temp = cursor.getInt(0);
                }
                ContentValues values = new ContentValues();
                values.put(BONG_DATE, infoNew.getDate());
                values.put(BONG_DATETIME, infoNew.getDateTime());
                values.put(BONG_DEVICEID, infoNew.getDeviceId());
                values.put(BONG_DEVICEMAC, infoNew.getDeviceMac());
                values.put(BONG_INFO, infoNew.getInfo());
                if (temp > 0) {
                    db.update(BONG_TB_NAME, values, BONG_DATETIME + " = " + infoNew.getDateTime() + " and " +
                            BONG_DEVICEMAC + " = '" + infoNew.getDeviceMac() + "'", null);
                } else {
                    db.insert(BONG_TB_NAME, null, values);
                }
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }


    /**
     * 删除指定日期之前的数据（不包括指定日期）
     */
    public void deleteInfoBeforeDate(int Date, String deviceMac) {
        deleteInfoBeforeDate(Date + "", deviceMac);
    }

    /**
     * 删除指定日期之前的数据（不包括指定日期）
     */
    public void deleteInfoBeforeDate(final String Date, final String deviceMac) {
        if (!db.isOpen()) {
            return;
        }
        new Thread() { // 开启线程执行防止阻塞
            @Override
            public void run() {
                db.delete(BONG_TB_NAME, BONG_DATE + " < ? and " +
                                BONG_DEVICEMAC + "= '?' "
                        , new String[]{Date, deviceMac});
            }
        }.start();
    }


    /**
     * 获取指定MAC地址设备的所以数据
     *
     * @param mac
     * @return
     */
    public List<BongInfoDTO> getAllInfoFromMac(String mac) {
        if (!db.isOpen()) {
            return null;
        }
        RCLog.i(TAG, "获取指定MAC的所有数据:" + mac);
        Cursor cursor = db.rawQuery("SELECT * from " + BONG_TB_NAME + " WHERE "
                + BONG_DEVICEMAC + " = '" + mac + "' order by " + BONG_DATETIME, null);
        List<BongInfoDTO> bongList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BongInfoDTO dto = new BongInfoDTO();
            dto.setDeviceId(cursor.getInt(cursor.getColumnIndex(BONG_DEVICEID)));
            dto.setDeviceMac(cursor.getString(cursor.getColumnIndex(BONG_DEVICEMAC)));
            dto.setDate(cursor.getInt(cursor.getColumnIndex(BONG_DATE)));
            dto.setDateTime(cursor.getLong(cursor.getColumnIndex(BONG_DATETIME)));
            dto.setInfo(cursor.getString(cursor.getColumnIndex(BONG_INFO)));
            bongList.add(dto);
        }
        cursor.close();
        return bongList;
    }

    /**
     * 获取指定MAC地址设备的所以数据
     *
     * @param mac
     * @return
     */
    public List<BongInfoDTO> getAllInfoFromMac(String mac, String date) {
        if (!db.isOpen()) {
            return null;
        }
        RCLog.i(TAG, "获取指定MAC的所有数据:" + mac);
        Cursor cursor = db.rawQuery("SELECT * from " + BONG_TB_NAME + " WHERE "
                + BONG_DEVICEMAC + " = '" + mac + "' and " + BONG_DATE + " = " + date + "" +
                " order by " + BONG_DATETIME, null);
        List<BongInfoDTO> bongList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BongInfoDTO dto = new BongInfoDTO();
            dto.setDeviceId(cursor.getInt(cursor.getColumnIndex(BONG_DEVICEID)));
            dto.setDeviceMac(cursor.getString(cursor.getColumnIndex(BONG_DEVICEMAC)));
            dto.setDate(cursor.getInt(cursor.getColumnIndex(BONG_DATE)));
            dto.setDateTime(cursor.getLong(cursor.getColumnIndex(BONG_DATETIME)));
            dto.setInfo(cursor.getString(cursor.getColumnIndex(BONG_INFO)));
            bongList.add(dto);
        }
        cursor.close();
        return bongList;
    }


}
