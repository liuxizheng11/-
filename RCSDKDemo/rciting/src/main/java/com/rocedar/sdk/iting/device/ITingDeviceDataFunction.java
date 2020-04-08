package com.rocedar.sdk.iting.device;

import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/3/29 2:46 PM
 * 版本：V1.1.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingDeviceDataFunction {



    void setGetSleepDataFirst(boolean getSleepDataFirst);

    int[] DATA_FUNCTION_TYPE = {
            ResultCallBack.TYPE_GET_SPORT_DATA,
            ResultCallBack.TYPE_GET_SLEEP_DATA,
            ResultCallBack.TYPE_GET_HEART_RATE_DATA,
            ResultCallBack.TYPE_GET_REAL_TIME_SPORT_DATA,
            ResultCallBack.TYPE_DELETE_SPORT_DATA,
            ResultCallBack.TYPE_DELETE_HEART_RATE_DATA,
            ResultCallBack.TYPE_DELETE_SLEEP_DATA,
            ResultCallBack.TYPE_DELETE_REAL_TIME_SPORT_DATA
    };

    boolean isDoingGetData();

    void getDeviceSportInfo();

    void getDeviceSleepInfo();

    void getDeviceHeartInfo();

    void getDeviceRealTimeSportInfo();

    void parsingData(int result, Object[] objects);

    void parsingError(int result);

    void doGetDeviceData();
}
