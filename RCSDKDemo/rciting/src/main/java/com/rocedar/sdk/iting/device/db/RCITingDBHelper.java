package com.rocedar.sdk.iting.device.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCUtilEncode;
import com.rocedar.sdk.iting.device.db.bong.RCITingDBHeartInfo;


/**
 * 数据库帮助类
 */
public class RCITingDBHelper extends SQLiteOpenHelper implements RCITingDBHeartInfo {

    private static String TAG = "RCDevice_db";


    //数据库名称
    private static String getUserDatabaseName() {
        return RCUtilEncode.getMd5StrUpper16("ptheart") + ".db";
    }


    //数据库版本
    private static int DB_VERSION = 1;

    private static RCITingDBHelper instance;

    public RCITingDBHelper(Context context) {
        super(context, getUserDatabaseName(), null, DB_VERSION);
        useDBName = getUserDatabaseName();
    }


    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        RCLog.e(TAG, "#############数据库创建了##############:" + DB_VERSION);

        db.execSQL(INITIALIZE_BONG_HEART_TABLE_CREATE);

    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RCLog.e(TAG, "#############数据库升级了##############:" + oldVersion + "<-->" + newVersion);
        for (int i = oldVersion; i < newVersion; i++) {

        }
    }



    private static String useDBName = "";

    public static RCITingDBHelper getInstance(Context context) {

        if (instance == null) {
            instance = new RCITingDBHelper(context.getApplicationContext());
            RCLog.i(TAG, "初始化数据库－当前使用的数据库是：" + useDBName);
        } else {
            RCLog.i(TAG, "使用数据库－当前使用的数据库是：" + useDBName);
            RCLog.i(TAG, "使用数据库－需要使用的数据库是：" + getUserDatabaseName());
            if (!useDBName.equals("") && !useDBName.equals(getUserDatabaseName())) {
                closeDB();
                RCLog.i(TAG, "数据库不符,重新初始化");
                instance = new RCITingDBHelper(context.getApplicationContext());
                RCLog.i(TAG, "重新初始化数据库－当前使用的数据库是：" + useDBName);
            }
        }
        return instance;
    }


    public static void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }


}
