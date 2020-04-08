package com.rocedar.deviceplatform.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rocedar.base.RCLog;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.deviceplatform.db.bong.IBongHeartInfo;
import com.rocedar.deviceplatform.db.bong.IBongInfo;
import com.rocedar.deviceplatform.db.step.IStepInfo;



/**
 * 数据库帮助类
 */
public class DBHelperStep extends SQLiteOpenHelper implements IStepInfo,IBongInfo,IBongHeartInfo {

    private static String TAG = "RCDevice_db";


    //数据库名称
    private static String getUserDatabaseName() {
        return RCUtilEncode.getMd5StrUpper16("ptstep") + ".db";
    }


    //数据库版本
    private static int DB_VERSION = 2;

    private static DBHelperStep instance;

    public DBHelperStep(Context context) {
        super(context, getUserDatabaseName(), null, DB_VERSION);
        useDBName = getUserDatabaseName();
    }


    private static final String INITIALIZE_SPORT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TB_NAME_STEP + " ("
            + STEP_INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TIMES + " LONG, "
            + STEP + " LONG, "
            + SENSOR_TYPE + " INTEGER, "
            + DATE + " VARCHAR, "
            + OTHER_ONE + " VARCHAR, "
            + AES_STEP + " VARCHAR "
            + "); ";


    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        RCLog.e(TAG, "#############数据库创建了##############:" + DB_VERSION);

        db.execSQL(INITIALIZE_SPORT_TABLE_CREATE);

        db.execSQL(INITIALIZE_BONG_TABLE_CREATE);

        db.execSQL(INITIALIZE_BONG_HEART_TABLE_CREATE);

//        final int FIRST_DATABASE_VERSION = 1;
//        onUpgrade(db, FIRST_DATABASE_VERSION, DB_VERSION);
    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RCLog.e(TAG, "#############数据库升级了##############:" + oldVersion + "<-->" + newVersion);
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:
                    db.execSQL(INITIALIZE_BONG_TABLE_CREATE);
                    db.execSQL(INITIALIZE_BONG_HEART_TABLE_CREATE);
                    break;
            }
        }
    }



    private static String useDBName = "";

    public static DBHelperStep getInstance(Context context) {

        if (instance == null) {
            instance = new DBHelperStep(context.getApplicationContext());
            RCLog.i(TAG, "初始化数据库－当前使用的数据库是：" + useDBName);
        } else {
            RCLog.i(TAG, "使用数据库－当前使用的数据库是：" + useDBName);
            RCLog.i(TAG, "使用数据库－需要使用的数据库是：" + getUserDatabaseName());
            if (!useDBName.equals("") && !useDBName.equals(getUserDatabaseName())) {
                closeDB();
                RCLog.i(TAG, "数据库不符,重新初始化");
                instance = new DBHelperStep(context.getApplicationContext());
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
