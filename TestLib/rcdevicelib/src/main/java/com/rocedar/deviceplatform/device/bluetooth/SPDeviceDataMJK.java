package com.rocedar.deviceplatform.device.bluetooth;

import android.content.Context;
import android.content.SharedPreferences;

import com.rocedar.base.RCLog;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.deviceplatform.unit.DateUtil;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/17 上午11:47
 * 版本：V1.0
 * 描述：摩集客耳机数据存储和数据存储逻辑
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class SPDeviceDataMJK {

    private static String TAG = "RCDevice_SP_MJK";

    private static final String INFO = "bluetooth_device_data";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(RCUtilEncode.getMd5StrUpper16(INFO), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        return getSharedPreferences(context).edit();
    }


    //---------摩集客耳机数据存储相关-------S

    /**
     * 保存摩集客耳机设备最后保存数据的日期
     *
     * @param context
     * @param mac
     * @return
     */
    public static String getMJKLastSaveDate(Context context, String mac) {
        return getSharedPreferences(context).getString(getMd5String(LAST_SAVE_DATE, mac), "");
    }


    /**
     * 获取摩集客耳机设备最后保存日期的步数（如果最后保存为今天，就是上一次计算后的今天总数据）
     *
     * @param context
     * @param dataName 数据存储的名称（步数、走路时间、跑步、跑步时间）
     * @param mac
     * @return
     */
    public static int getMJKTodayData(Context context, String dataName, String mac) {
        return getSharedPreferences(context).getInt(getMd5String(dataName, mac), -1);
    }


    /**
     * 获取保存的摩集客耳机设备传感器的数据
     *
     * @param context
     * @param dataName 数据存储的名称（步数、走路时间、跑步、跑步时间）
     * @param mac
     * @return
     */
    private static int getMJKLastSersonData(Context context, String dataName, String mac) {
        return getSharedPreferences(context).getInt(getMd5String(dataName, mac), -1);
    }


    /* 摩集客耳机最后一次存储计算后的数据*/
    //最后一次存储计算后的步数
    public static final String TODAY_STEP = "mjk_today_step";
    //最后一次存储计算后的走路时间（秒）
    public static final String TODAY_STEP_TIME = "mjk_today_time";
    //最后一次存储计算后的跑步步数
    public static final String TODAY_RUN_STEP = "mjk_today_run_step";
    //最后一次存储计算后的跑步时间（秒）
    public static final String TODAY_RUN_TIME = "mjk_today_run_time";

    /* 摩集客耳机最后一次存储的传感器数据*/
    private static final String SENSOR_STEP = "mjk_sensor_step";
    private static final String SENSOR_STEP_TIME = "mjk_sensor_step_time";
    private static final String SENSOR_RUN_STEP = "mjk_sensor_run_step";
    private static final String SENSOR_RUN_TIME = "mjk_sensor_run_time";

    //摩集客耳机最后存储数据的日期
    private static final String LAST_SAVE_DATE = "mjk_last_save_date";


    /**
     * 摩集客耳机步数记录存储
     * ps:魔集客耳机传感器为持续自增，设备重新配对（初始化）时回归0，没有时间的概念，需要APP计算后处理数据
     * <p>
     *
     * @param step     耳机传感器的步数
     * @param stepTime 耳机传感器的走路时间（秒）
     * @param runStep  耳机传感器的跑步步数
     * @param runTime  耳机传感器的跑步时间（秒）
     * @param mac      MAC地址，用于多个设备时区分不同的数据来源
     */
    public static void saveMJKStepInfo(Context context, int step, int stepTime, int runStep, int runTime, String mac) {
        RCLog.d(TAG, "获取的摩集客数据，步数：%d，走路时间：%d，跑步步数：%d，跑步时间：%d", step, stepTime, runStep, runTime);

        RCLog.d(TAG, "获取的今天摩集客数据，步数：%d，走路时间：%d，跑步步数：%d，跑步时间：%d",
                getMJKLastSersonData(context, TODAY_STEP, mac),
                getMJKLastSersonData(context, TODAY_STEP_TIME, mac),
                getMJKLastSersonData(context, TODAY_RUN_STEP, mac),
                getMJKLastSersonData(context, TODAY_RUN_TIME, mac));
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        if (getMJKLastSaveDate(context, mac).equals(DateUtil.getFormatNow("yyyyMMdd"))) {
            RCLog.d(TAG, "保存摩集客数据,历史数据是今天\n"
                            + "耳机中传感器最后的数据步数：%d，走路时间：%d，跑步步数：%d，跑步时间：%d。",
                    getMJKLastSersonData(context, SENSOR_STEP, mac),
                    getMJKLastSersonData(context, SENSOR_STEP_TIME, mac),
                    getMJKLastSersonData(context, SENSOR_RUN_STEP, mac),
                    getMJKLastSersonData(context, SENSOR_RUN_TIME, mac));
            /*
                数据判断逻辑：
                当前MAC设备获取数据的最后时间为今天，
                1.如果保存的耳机传感器存储的步数大于获取到最新的耳机传感器中存储的步数
                  说明耳机有进行过复位，传感器中存储的数据全部是今天
                   数据=之前存储的当天数据+当前的传感器数据
                2.如果保存的耳机传感器存储的步数小于获取到最新的耳机传感器中存储的步数
                  说明耳机有一直在联系记录数据，用当前的传感器数据减去存储的传感器数据为新的数据差。
                  数据=之前存储的当天数据+当前的传感器数据-存储的传感器数据
             */
            if (getMJKLastSersonData(context, SENSOR_STEP, mac) == step &&
                    getMJKLastSersonData(context, SENSOR_RUN_STEP, mac) == runStep) {
                RCLog.d(TAG, "传感器数据没有变化");
                return;
            }
            if (getMJKLastSersonData(context, SENSOR_STEP, mac) > step
                    || getMJKLastSersonData(context, SENSOR_RUN_STEP, mac) > runStep) {
                RCLog.d(TAG, "传感器有复位");
                editor.putInt(getMd5String(TODAY_STEP, mac), getMJKTodayData(context, TODAY_STEP, mac) + step);
                editor.putInt(getMd5String(TODAY_STEP_TIME, mac), getMJKTodayData(context, TODAY_STEP_TIME, mac) + stepTime);
                editor.putInt(getMd5String(TODAY_RUN_STEP, mac), getMJKTodayData(context, TODAY_RUN_STEP, mac) + runStep);
                editor.putInt(getMd5String(TODAY_RUN_TIME, mac), getMJKTodayData(context, TODAY_RUN_TIME, mac) + runTime);
            } else {
                RCLog.d(TAG, "传感器没有复位");
                editor.putInt(getMd5String(TODAY_STEP, mac),
                        getMJKTodayData(context, TODAY_STEP, mac) + step - getMJKLastSersonData(context, SENSOR_STEP, mac));
                editor.putInt(getMd5String(TODAY_STEP_TIME, mac),
                        getMJKTodayData(context, TODAY_STEP_TIME, mac) + stepTime - getMJKLastSersonData(context, SENSOR_STEP_TIME, mac));
                editor.putInt(getMd5String(TODAY_RUN_STEP, mac),
                        getMJKTodayData(context, TODAY_RUN_STEP, mac) + runStep - getMJKLastSersonData(context, SENSOR_RUN_STEP, mac));
                editor.putInt(getMd5String(TODAY_RUN_TIME, mac),
                        getMJKTodayData(context, TODAY_RUN_TIME, mac) + runTime - getMJKLastSersonData(context, SENSOR_RUN_TIME, mac));
            }
        } else {
             /*
                数据判断逻辑：
                当前MAC设备获取数据的最后时间不为今天，数据从0开始计算。
             */
            RCLog.d(TAG, "保存摩集客数据,历史数据不为今天");
            editor.putString(getMd5String(LAST_SAVE_DATE, mac), DateUtil.getFormatNow("yyyyMMdd"));
            //保存今天的数据为0
            editor.putInt(getMd5String(TODAY_STEP, mac), 0);
            editor.putInt(getMd5String(TODAY_STEP_TIME, mac), 0);
            editor.putInt(getMd5String(TODAY_RUN_STEP, mac), 0);
            editor.putInt(getMd5String(TODAY_RUN_TIME, mac), 0);
        }
        /*计算完成后保存传感器的最后数据，用于下次计算数据*/
        editor.putInt(getMd5String(SENSOR_STEP, mac), step);
        editor.putInt(getMd5String(SENSOR_STEP_TIME, mac), stepTime);
        editor.putInt(getMd5String(SENSOR_RUN_STEP, mac), runStep);
        editor.putInt(getMd5String(SENSOR_RUN_TIME, mac), runTime);
        editor.commit();
        RCLog.d(TAG, "计算后的今天数据步数：%d，走路时间：%d，跑步步数：%d，跑步时间：%d。",
                getMJKTodayData(context, TODAY_STEP, mac),
                getMJKTodayData(context, TODAY_STEP_TIME, mac),
                getMJKTodayData(context, TODAY_RUN_STEP, mac),
                getMJKTodayData(context, TODAY_RUN_TIME, mac)
        );
    }

    //---------摩集客耳机数据存储相关-------E


    public static String getMd5String(String temp, String mac) {
        return RCUtilEncode.getMd5StrUpper16(mac.toUpperCase() + temp);
    }

}
