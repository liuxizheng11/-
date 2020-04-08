package com.rocedar.sdk.iting.device.function;

import android.content.Context;

import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.iting.device.ITingDeviceAerobicExerciseFunction;
import com.rocedar.sdk.iting.device.dto.RCITingAerobicDTO;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEvent;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEventType;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.app.SettingType;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
import cn.appscomm.bluetoothsdk.model.AerobicSportData;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019-10-29 16:27
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingDeviceAerobicExerciseFunction implements ITingDeviceAerobicExerciseFunction {

    private String TAG = "ITING-REMIND";

    public Context context;

    private ResultCallBack callBack;


    public RCITingDeviceAerobicExerciseFunction(Context context, ResultCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public void getNewData() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "获取有氧数据";
        BluetoothSDK.getAerobicSportData(callBack,
                SettingType.AEROBIC_SPORT_TYPE_HEART_RATE | SettingType.AEROBIC_SPORT_TYPE_DISTANCE);
    }

    @Override
    public void getRestHeartStart() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "获取静息心率";
        BluetoothSDK.getRestHeartRate(callBack);
    }

    @Override
    public void getRestHeartStop() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "停止获取静息心率";
        getRealHeartRate(false);
    }

    private void getRealHeartRate(boolean isOpen) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = (isOpen ? "" : "停止") + "获取实时心率";
        BluetoothSDK.setRealHeartRateCallBack(callBack, isOpen);
    }

    @Override
    public void openAerobic() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "开始测量有氧";
        BluetoothSDK.openAerobicHeartRateTest(callBack);
    }

    private void deleteAerobic() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "删除有氧数据";
        BluetoothSDK.delAerobicSportData(callBack);
    }

    private String lastFunctionName = "";
    private long lastFunctionTime = -1;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public void parsingData(int result, Object[] objects) {
        RCLog.i(TAG, "本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        RCITingDeviceEvent event;
        switch (result) {
            case ResultCallBack.TYPE_GET_REST_HEART_RATE:
                getRealHeartRate(true);
                break;
            case ResultCallBack.TYPE_REAL_TIME_HEART_RATE_DATA:
                RCLog.i(TAG, "real time heart rate : " + objects[0]);
                event = new RCITingDeviceEvent(
                        RCITingDeviceEventType.AEROBIC_REAL_TIME_HEART_RATE, true);
                event.setObject(objects[0]);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_OPEN_AEROBIC_HEART_RATE_TEST:
                event = new RCITingDeviceEvent(RCITingDeviceEventType.AEROBIC_DO_TEST, true);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_DELETE_AEROBIC_SPORT_DATA:
                event = new RCITingDeviceEvent(RCITingDeviceEventType.AEROBIC_DELETE_DATA, true);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_GET_AEROBIC_SPORT_DATA:
                List<AerobicSportData> data = (List<AerobicSportData>) objects[0];
                List<RCITingAerobicDTO> dtoList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    RCITingAerobicDTO dto = new RCITingAerobicDTO();
                    dto.setDateTime(dateFormat.format(new Date(data.get(i).timestamp * 1000)));
                    dto.setDistance(data.get(i).distance);
                    dto.setHeartRate(data.get(i).heartRate);
                    dtoList.add(dto);
                }
                event = new RCITingDeviceEvent(RCITingDeviceEventType.AEROBIC_GET_DATA, true);
                event.setObject(dtoList);
                EventBus.getDefault().post(event);
                deleteAerobic();
                break;
        }
    }


    @Override
    public void parsingError(int result) {
        RCLog.i(TAG, "(错误)本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        RCITingDeviceEvent event;
        switch (result) {
            case ResultCallBack.TYPE_GET_REST_HEART_RATE:
            case ResultCallBack.TYPE_REAL_TIME_HEART_RATE_DATA:
                getRealHeartRate(false);
                event = new RCITingDeviceEvent(
                        RCITingDeviceEventType.AEROBIC_REAL_TIME_HEART_RATE, false);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_OPEN_AEROBIC_HEART_RATE_TEST:
                event = new RCITingDeviceEvent(RCITingDeviceEventType.AEROBIC_DO_TEST, false);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_DELETE_AEROBIC_SPORT_DATA:
                event = new RCITingDeviceEvent(RCITingDeviceEventType.AEROBIC_DELETE_DATA, false);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_GET_AEROBIC_SPORT_DATA:
                event = new RCITingDeviceEvent(RCITingDeviceEventType.AEROBIC_GET_DATA, false);
                EventBus.getDefault().post(event);
                break;
        }
    }
}
