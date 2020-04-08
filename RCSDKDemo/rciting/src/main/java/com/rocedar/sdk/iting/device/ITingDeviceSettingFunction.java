package com.rocedar.sdk.iting.device;

import com.rocedar.sdk.iting.device.listener.RCITingPhotoListener;

import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/15 4,23 PM
 * 版本：V1.0.00
 * 描述：设备基本方法功能定义及方法接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingDeviceSettingFunction {

    int[] SETTING_FUNCTION_TYPE = {
             ResultCallBack.TYPE_SET_AUTO_HEART_RATE_FREQUENCY,
             ResultCallBack.TYPE_GET_AUTO_HEART_RATE_FREQUENCY,
             ResultCallBack.TYPE_SET_HEART_RATE_ALARM_THRESHOLD,
             ResultCallBack.TYPE_GET_HEART_RATE_ALARM_THRESHOLD,
             ResultCallBack.TYPE_GET_AUTO_SLEEP,
             ResultCallBack.TYPE_SET_AUTO_SLEEP,
             ResultCallBack.TYPE_SET_SWITCH_SLEEP_STATE,
             ResultCallBack.TYPE_SET_SWITCH_INCOME_CALL,
             ResultCallBack.TYPE_SET_SWITCH_MISS_CALL,
             ResultCallBack.TYPE_SET_SWITCH_MAIL,
             ResultCallBack.TYPE_SET_SWITCH_CALENDAR,
             ResultCallBack.TYPE_SET_SWITCH_SOCIAL,
             ResultCallBack.TYPE_SET_SWITCH_SMS,
             ResultCallBack.TYPE_SET_SWITCH_AUTO_SYNC,
             ResultCallBack.TYPE_GET_SWITCH_SETTING,
             ResultCallBack.TYPE_UPLOAD_IMAGE_PROGRESS,
             ResultCallBack.TYPE_UPLOAD_IMAGE_RESULT,
             ResultCallBack.TYPE_UPLOAD_IMAGE_MAX,
             ResultCallBack.TYPE_SET_CUSTOMIZE_WATCH_FACE,
             ResultCallBack.TYPE_GET_CUSTOMIZE_WATCH_FACE_CRC_AND_ID_EX,
             ResultCallBack.TYPE_GET_TIME_SURFACE
    };



    void sendPhoto(String filePath, int id, RCITingPhotoListener listener);

    void getSettingPhotoId();

    void setHeartAuto(boolean status, int time);

    void setHeartAlarmThreshold(boolean status, int min, int max);

    void getHeartAuto();

    void getSetting();

    void setSetting(int type, boolean status);

    void setSleepAutoTime(String startTime, String endTime);

    void getSleepAutoTime();


    //返回值处理
    void parsingData(int result, Object[] objects);

    void parsingError(int result);
}
