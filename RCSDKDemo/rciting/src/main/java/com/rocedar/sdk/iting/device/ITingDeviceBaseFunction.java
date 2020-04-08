package com.rocedar.sdk.iting.device;

import com.rocedar.sdk.iting.device.function.DeviceOTAUtil;
import com.rocedar.sdk.iting.device.listener.ITingGetDeviceVersionListener;

import org.json.JSONObject;

import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/15 4:23 PM
 * 版本：V1.0.00
 * 描述：设备基本方法功能定义及方法接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingDeviceBaseFunction {

    int[] BASE_FUNCTION_TYPE = {
            ResultCallBack.TYPE_GET_DEVICE_VERSION,
            ResultCallBack.TYPE_GET_DEVICE_VERSION_WITH_BUILD,
            ResultCallBack.TYPE_GET_DEVICE_INFO,
            ResultCallBack.TYPE_INCOME_CALL,
            ResultCallBack.TYPE_MISS_CALL,
            ResultCallBack.TYPE_MESSAGE_PUSH_EX,
            ResultCallBack.TYPE_START_FIND_DEVICE,
            ResultCallBack.TYPE_END_FIND_DEVICE,
            ResultCallBack.TYPE_DEVICE_START_FIND_PHONE,
            ResultCallBack.TYPE_DEVICE_END_FIND_PHONE,
            ResultCallBackEx.TYPE_SET_WEATHER_INFO,
            ResultCallBack.TYPE_GET_BATTERY_POWER,
            ResultCallBack.TYPE_DEVICE_REJECT_CALL

    };

    void getDeviceVersion(ITingGetDeviceVersionListener listener);

    void getDeviceVersionBuild();

    void getDeviceInfo();

    void getPower();

    void pushMsg(String pkgName, String title, String info, long time);

    void inCallPhone(String phone,boolean status);

    void missCallPhone(String phone);

    void setWeatherInfo(JSONObject weatherInfo);

    void restoreFactory();

    void startFindDevice();

    void endFindDevice();

    void otaUpdate(DeviceOTAUtil.UpdateListener updateListener, String apolloPath, String touchPanelPath, String[] pictureArray);

    void otaUpdate(DeviceOTAUtil.UpdateListener updateListener, String apolloPath, String touchPanelPath, String[] pictureArray, String LanguagePath);

    //返回值处理
    void parsingData(int result, Object[] objects);

    void parsingError(int result);


}
