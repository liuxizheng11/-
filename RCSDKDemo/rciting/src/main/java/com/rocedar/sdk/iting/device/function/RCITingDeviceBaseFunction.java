package com.rocedar.sdk.iting.device.function;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.iting.RCITingDeviceUtil;
import com.rocedar.sdk.iting.device.ITingDeviceBaseFunction;
import com.rocedar.sdk.iting.device.ResultCallBackEx;
import com.rocedar.sdk.iting.device.TingMessageType;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEvent;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEventType;
import com.rocedar.sdk.iting.device.listener.ITingGetDeviceVersionListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.app.SettingType;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
import cn.appscomm.bluetoothsdk.model.WeatherDataEx;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/15 4:24 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingDeviceBaseFunction implements ITingDeviceBaseFunction, RCITingDeviceEventType {

    private String TAG = "ITING-BASE_FUNCTION";

    public Context context;

    private ResultCallBack callBack;

    private DeviceOTAUtil otaUtil;

    private String deviceVersion = null;

    public RCITingDeviceBaseFunction(Context context, ResultCallBack callBack) {
        this.context = context;
        this.callBack = callBack;

        BluetoothSDK.setSMSReplyCallBack(smsReplyCallBack);
        BluetoothSDK.setFindPhoneCallBack(findPhoneCallBack);
    }

    private long lastFunctionTime = -1;

    private void getDeviceVersion() {
        lastFunctionName = "设备版本";
        BluetoothSDK.getDeviceVersion(callBack);
    }


    private ITingGetDeviceVersionListener versionListener;

    @Override
    public void getDeviceVersion(ITingGetDeviceVersionListener listener) {
        if (deviceVersion != null) {
            listener.versionInfo(deviceVersion);
        }
        this.versionListener = listener;
        getDeviceVersion();
    }

    @Override
    public void getDeviceVersionBuild() {
        lastFunctionName = "设备版本";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.getDeviceVersionWithBuild(callBack);
    }


    @Override
    public void getDeviceInfo() {
        lastFunctionName = "设备信息";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.getDeviceInfo(callBack);
    }

    @Override
    public void getPower() {
        lastFunctionName = "电量信息";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.getBatteryPower(callBack);
    }


    @Override
    public void pushMsg(String pkgName, String title, String info, long time) {
        lastFunctionName = "推送消息";
        lastFunctionTime = new Date().getTime();
        if (!TingMessageType.filter(pkgName, title)) return;
        BluetoothSDK.sendMessagePushEx(callBack, title, info, new Date(time), TingMessageType.getType(pkgName), 1,
                SettingType.SHOCK_MODE_NO_SHOCK, true);
    }

    @Override
    public void inCallPhone(String phone, boolean status) {
        lastFunctionName = "来电";
        lastFunctionTime = new Date().getTime();
        if (status)
            BluetoothSDK.sendMessagePushEx(callBack, phone, phone, new Date(), SettingType.MESSAGE_INCOME_CALL, 1,
                    SettingType.SHOCK_MODE_ONE_SHOCK_SOUND, true);
        else
            BluetoothSDK.sendMessagePushEx(callBack, phone, phone, new Date(), SettingType.MESSAGE_OFFHOOK, 1,
                    SettingType.SHOCK_MODE_ONE_SHOCK_SOUND, true);
    }

    @Override
    public void missCallPhone(String phone) {
        lastFunctionName = "未接听";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.sendMessagePushEx(callBack, phone, phone, new Date(), SettingType.MESSAGE_MISS_CALL, 1,
                SettingType.SHOCK_MODE_ONE_SHOCK_SOUND, true);
    }

    @Override
    public void setWeatherInfo(JSONObject weatherInfo) {
        lastFunctionName = "设置天气";
        lastFunctionTime = new Date().getTime();
        JSONObject objectBasic = weatherInfo.optJSONObject("basic");
        JSONObject objectNow = weatherInfo.optJSONObject("now");
        JSONObject objectAir = weatherInfo.optJSONObject("air_now_city");
        JSONArray objectDaily = weatherInfo.optJSONArray("daily_forecast");
        List<WeatherDataEx.DailyInfo> dailyInfos = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (objectDaily.length() > i) {
                JSONObject object = objectDaily.optJSONObject(i);
                WeatherDataEx.DailyInfo dailyInfo = new WeatherDataEx.DailyInfo();
                dailyInfo.day = i;
                dailyInfo.weatherCode = Integer.parseInt(object.optString("cond_code_d"));
                dailyInfo.max = Integer.parseInt(object.optString("tmp_max"));
                dailyInfo.min = Integer.parseInt(object.optString("tmp_min"));
                dailyInfos.add(dailyInfo);
            }
        }
        WeatherDataEx weatherDataEx = new WeatherDataEx.Builder()
                .current(Integer.parseInt(objectNow.optString("fl")))
                .place(objectBasic.optString("location"))
                .windSpeed(Integer.parseInt(objectNow.optString("wind_spd")))
                .windDirection(Integer.parseInt(objectNow.optString("wind_deg")))
                .airQuality(Integer.parseInt(objectAir.optString("aqi")))
                .dailyInfoList(dailyInfos)
                .build();
        BluetoothSDK.sendWeatherEx(callBack, weatherDataEx);
    }

    @Override
    public void restoreFactory() {
        lastFunctionName = "重置";
        lastFunctionTime = new Date().getTime();
        //第一步：重置
        BluetoothSDK.restoreFactory(callBack);
    }

    @Override
    public void startFindDevice() {
        lastFunctionName = "开始找手表";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.startFindDevice(callBack);
    }

    @Override
    public void endFindDevice() {
        lastFunctionName = "结束找手表";
        lastFunctionTime = new Date().getTime();
        BluetoothSDK.endFindDevice(callBack);
    }

    @Override
    public void otaUpdate(DeviceOTAUtil.UpdateListener updateListener, String apolloPath, String touchPanelPath, String[] pictureArray) {
        otaUpdate(updateListener, apolloPath, touchPanelPath, pictureArray, "");
    }

    @Override
    public void otaUpdate(DeviceOTAUtil.UpdateListener updateListener, String apolloPath, String touchPanelPath, String[] pictureArray, String LanguagePath) {
        lastFunctionName = "开始OTA";
        lastFunctionTime = new Date().getTime();
        if (otaUtil == null)
            otaUtil = new DeviceOTAUtil(context);
        otaUtil.doUpDate(updateListener, RCITingDeviceUtil.getInstance(context).getConnectFunction().getLastConnectDeviceDTO().getDeviceNo(),
                apolloPath, touchPanelPath, pictureArray, LanguagePath);
    }

    private String lastFunctionName = "";

    public void parsingData(int result, Object[] objects) {
        RCLog.i(TAG, "本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        switch (result) {
            case ResultCallBackEx.TYPE_SET_WEATHER_INFO://天气设置完成
                RCLog.i(TAG, "weather over");
                EventBus.getDefault().post(new RCITingDeviceEvent(SETTING_WEATHER, true));
                break;
            case ResultCallBack.TYPE_START_FIND_DEVICE:
                RCLog.i(TAG, "start find device");
                EventBus.getDefault().post(new RCITingDeviceEvent(FIND_WATCH_START, true));
                break;
            case ResultCallBack.TYPE_END_FIND_DEVICE:
                RCLog.i(TAG, "end find device");
                EventBus.getDefault().post(new RCITingDeviceEvent(FIND_WATCH_END, true));
                break;
            case ResultCallBack.TYPE_GET_DEVICE_VERSION:
                RCLog.i(TAG, "device version : " + objects[0]);
                deviceVersion = objects[0].toString();
                if (versionListener != null) {
                    versionListener.versionInfo(deviceVersion);
                }
                break;
            case ResultCallBack.TYPE_SET_UID:
                break;
            case ResultCallBack.TYPE_GET_BATTERY_POWER:
                Log.i(TAG, "battery power : " + objects[0]);
                RCITingDeviceEvent event = new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_GET_POWER, true);
                event.setObject(objects[0]);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_DEVICE_REJECT_CALL:
                RCLog.e("挂断电话");
                endPhone(context);
                break;
        }
    }

    public static void endPhone(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method = null;
        try {
            method = TelephonyManager.class.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            ITelephony telephony = (ITelephony) method.invoke(telephonyManager);
            telephony.endCall();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void parsingError(int result) {
        switch (result) {
            case ResultCallBackEx.TYPE_SET_WEATHER_INFO://天气设置完成
                RCLog.i(TAG, "weather over");
                EventBus.getDefault().post(new RCITingDeviceEvent(SETTING_WEATHER, false));
                break;
        }
    }


    private ResultCallBack smsReplyCallBack = new ResultCallBack() {
        @Override
        public void onSuccess(int resultType, Object[] objects) {
            RCLog.i(TAG, "smsReply : " + Arrays.toString(objects));
            if (resultType == ResultCallBack.TYPE_DEVICE_SMS_REPLY_DEFAULT) {
                String emoji = objects[0].toString();                                               // 表情
                int language = (int) objects[1];                                                    // 语言
                byte defaultSMSReplyIndex = (byte) objects[2];                                      // 设备的预设回复的索引
                int crc = (int) objects[3];                                                         // crc(对应setCustomizeReply里面设置的crc值)
                String nameOrNumber = objects[4].toString();                                        // 号码或姓名
                RCLog.i(TAG, "nameOrNumber : " + nameOrNumber + " language : " + language);
                if (crc == 65535) {                                                                 // emoji 或 设备默认的预设回复
                    if (TextUtils.isEmpty(emoji)) {
                        RCLog.i(TAG, "SMS Type(index) index : " + defaultSMSReplyIndex);
                    } else {                                                                        // 自定义的预设回复
                        RCLog.i(TAG, "SMS Type(emoji) emoji : " + emoji);
                    }
                } else {
                    RCLog.i(TAG, "SMS Type(custom) crc : " + crc);
                }
                BluetoothSDK.sendReplyResponse(0, true);
            }
        }

        @Override
        public void onFail(int i) {
        }
    };

    private ResultCallBack findPhoneCallBack = new ResultCallBack() {
        @Override
        public void onSuccess(int resultType, Object[] objects) {
            switch (resultType) {
                case ResultCallBack.TYPE_DEVICE_START_FIND_PHONE:
                    Log.i(TAG, "device start find phone");
                    EventBus.getDefault().post(new RCITingDeviceEvent(FIND_PHONE_START, true));
                    break;
                case ResultCallBack.TYPE_DEVICE_END_FIND_PHONE:
                    EventBus.getDefault().post(new RCITingDeviceEvent(FIND_PHONE_END, true));
                    Log.i(TAG, "device end find phone");
                    break;
            }
        }

        @Override
        public void onFail(int i) {
        }
    };
}
