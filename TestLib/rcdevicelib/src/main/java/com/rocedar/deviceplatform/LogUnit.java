package com.rocedar.deviceplatform;

import android.os.Environment;
import android.util.Log;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCDeveloperConfig;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.unit.DateUtil;

import java.io.File;
import java.io.FileWriter;

import static com.rocedar.deviceplatform.device.bluetooth.impl.yd.Utils.getNowDate;
import static com.rocedar.deviceplatform.device.bluetooth.impl.yd.Utils.mSdRootPath;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/20 下午7:29
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class LogUnit {

    public static String s1 = "开始获取【%s】设备【%s】数据";
    public static String s2 = "结束获取【%s】设备【%s】数据";
    public static String s3 = "发送【%s】设备【%s】指令";
    public static String s4 = "获取【%s】设备【%s】数据成功，数据为：\n 【%s】";
    public static String s5 = "获取【%s】设备【%s】数据失败，失败原因：%s";
    public static String s15 = "获取【%s】设备数据完成";
    public static String s6 = "获取【%s】设备【%s】数据超时";
    public static String s7 = "服务5分钟获取数据开始";
    public static String s8 = "服务5分钟获取失败，失败原因：%s";
    public static String s9 = "服务5分钟获取数据完成";
    public static String s10 = "开始上传数据，上传的数据为：\n %s";
    public static String s11 = "数据上传成功";
    public static String s12 = "数据上传失败，失败原因：%s";
    public static String s13 = "服务2分钟上传数据开始";
    public static String s14 = "服务2分钟上传数据异常，【%s】";


    public static String logString(String info, String from) {
        String temp = "==========================================";
        StringBuffer out = new StringBuffer();
        out.append(temp);
        out.append("\n");
        out.append("时间：" + DateUtil.getFormatNow("yyyy-MM-dd HH:mm:ss"));
        out.append("\n");
        out.append("来源：" + from);
        out.append("\n");
        out.append(info);
        out.append("\n");
        out.append(temp);
        return out.toString();
    }

    /**
     * 发送指令日志
     *
     * @param deviceId
     * @param doType
     * @return
     */
    public static String logStringSendZL(int deviceId, int doType) {
        return String.format(s3, getDeviceName(deviceId), getDoTypeName(doType));
    }

    /**
     * 获取数据成功
     *
     * @param deviceId
     * @param doType
     * @param data
     * @return
     */
    public static String logStringGetDataOk(int deviceId, int doType, String data) {
        return String.format(s4, getDeviceName(deviceId), getDoTypeName(doType), data);
    }

    /**
     * 获取数据完成
     *
     * @param deviceId
     * @return
     */
    public static String logStringGetDataOver(int deviceId) {
        return String.format(s15, getDeviceName(deviceId));
    }


    /**
     * 获取数据失败
     *
     * @param deviceId
     * @param doType
     * @param msg
     * @return
     */
    public static String logStringGetDataError(int deviceId, int doType, int status, String msg) {
        if (status == RCBluetoothError.ERROR_GET_DATA_TIME_OUT) {
            return String.format(s6, getDeviceName(deviceId), getDoTypeName(doType));
        }
        return String.format(s5, getDeviceName(deviceId), getDoTypeName(doType), msg);
    }


    public static String getDeviceName(int deviceId) {
        switch (deviceId) {
            case RCDeviceDeviceID.Phone:
                return "手机";
            case RCDeviceDeviceID.BZL:
                return "博之轮手环";
            case RCDeviceDeviceID.MJK_ANDROID:
                return "摩集客耳机";
            case RCDeviceDeviceID.YD:
                return "缘渡手串";
            case RCDeviceDeviceID.HEHAQI:
                return "HeHaQi手环";
            case RCDeviceDeviceID.HF_DUDO:
                return "DUDO手环";
        }
        return "?";
    }

    private static String getDoTypeName(int doType) {
        switch (doType) {
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_START:
                return "开始获取实时跑步数据";
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP:
                return "停止获取实时跑步数据";
            case RCBluetoothDoType.DO_GET_REALTIME_STEP_STOP:
                return "停止获取实时步数";
            case RCBluetoothDoType.DO_GET_REALTIME_STEP_START:
                return "开始获取实时步数";
            case RCBluetoothDoType.DO_SETTING_TIME:
                return "设置时间";
            case RCBluetoothDoType.DO_TEST_BLODD_PRESSURE_START:
                return "开始血压测量";
            case RCBluetoothDataType.DATATYPE_STEP_TODAY:
                return "获取今日步数";
            case RCBluetoothDataType.DATATYPE_STEP_HISTORY:
                return "获取历史步数";
            case RCBluetoothDataType.DATATYPE_SLEPP_TODAY:
                return "获取昨晚睡眠";
            case RCBluetoothDataType.DATATYPE_SLEPP_HISTORY:
                return "获取历史睡眠";
            case RCBluetoothDataType.DATATYPE_HEARTR_ATE_TODAY:
                return "获取今日最新心率";
            case RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY:
                return "获取历史心率";
            case RCBluetoothDataType.DATATYPE_STEP_AND_RUN_NOW:
                return "获取步行和跑步数据";
        }
        return "?";
    }


    private final static String FOLDER_NAME_GEN = RCBaseConfig.APPTAG.equals(RCBaseConfig.APPTAG_DONGYA) ?
            "/dongya" : "/fangzhou";

    private final static String LOG = FOLDER_NAME_GEN + "/ptlog/";

    private final static String LOG2 = FOLDER_NAME_GEN + "/steplog/";

    /**
     * 获取储存Log文件目录
     *
     * @return
     */
    public static String getLogPath() {
        String temp = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? mSdRootPath + LOG : ""
                + LOG;
        return temp;
    }

    public static String getStepInfoPath() {
        String temp = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? mSdRootPath + LOG2 : ""
                + LOG2;
        return temp;
    }

    public static void writeDeviceLogtoSD(String log, String pathstr) {
        if (!RCDeveloperConfig.isDebug || true) return;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName = pathstr;
            String fileName = "pt_" + Long.parseLong(getNowDate("yyyyMMddHHMM")) / 10 + ".txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                Log.d("TestFile", "Create the path:" + pathName);
                path.mkdir();
            }
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + fileName);
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, true);
            writer.write(log + "\n");
            writer.close();

        } catch (Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
            e.printStackTrace();
        }
    }

    public static void writeDeviceLogtoSD(String log) {
        writeDeviceLogtoSD(log, getLogPath());
    }
}
