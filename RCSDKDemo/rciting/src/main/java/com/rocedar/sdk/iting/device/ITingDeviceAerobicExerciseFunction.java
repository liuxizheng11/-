package com.rocedar.sdk.iting.device;

import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/10/28 5:24 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingDeviceAerobicExerciseFunction {

    int[] AEROBIC_FUNCTION_TYPE = {
            ResultCallBack.TYPE_GET_REST_HEART_RATE,
            ResultCallBack.TYPE_REAL_TIME_HEART_RATE_DATA,
            ResultCallBack.TYPE_OPEN_AEROBIC_HEART_RATE_TEST,
            ResultCallBack.TYPE_DELETE_AEROBIC_SPORT_DATA,
            ResultCallBack.TYPE_GET_AEROBIC_SPORT_DATA
    };

    //获取新数据
    void getNewData();

    //获取静息心率
    void getRestHeartStart();

    void getRestHeartStop();

    //打开测量
    void openAerobic();


    //返回值处理
    void parsingData(int result, Object[] objects);

    void parsingError(int result);

}
