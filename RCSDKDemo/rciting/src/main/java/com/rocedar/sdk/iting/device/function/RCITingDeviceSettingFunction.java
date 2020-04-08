package com.rocedar.sdk.iting.device.function;

import android.content.Context;

import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.iting.RCITingDeviceUtil;
import com.rocedar.sdk.iting.device.ITingDeviceEnum;
import com.rocedar.sdk.iting.device.ITingDeviceSettingFunction;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEvent;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEventType;
import com.rocedar.sdk.iting.device.envet.RCITingSwitchEvent;
import com.rocedar.sdk.iting.device.listener.RCITingPhotoListener;
import com.rocedar.sdk.iting.request.dto.RCITingWatchInfoDTO;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
import cn.appscomm.bluetoothsdk.model.SwitchType;

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
public class RCITingDeviceSettingFunction implements ITingDeviceSettingFunction {


    private String TAG = "ITING-SETTING";

    public Context context;

    private ResultCallBack callBack;

    public RCITingDeviceSettingFunction(Context context, ResultCallBack callBack) {
        this.context = context;
        this.callBack = callBack;

    }

    private long lastFunctionTime = -1;


    private RCITingPhotoListener photoListener;
    private String filePath = null;
    private int filePathId = -999;

    @Override
    public void sendPhoto(String filePath, int id, RCITingPhotoListener listener) {
        this.photoListener = listener;
        this.filePath = filePath;
        this.filePathId = id;
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "获取表盘列表";
        BluetoothSDK.getCustomizeWatchFaceCRCAndIDEx(callBack);
    }

    private void sendPhoto(int ids[]) {
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                if (filePathId == ids[i]) {
                    lastFunctionName = "设置表盘" + filePathId;
                    if (filePath == null)
                        BluetoothSDK.setDefaultWatchFace(callBack, filePathId);
                    else
                        BluetoothSDK.setCustomizeWatchFaceEx(callBack, filePathId,
                                filePath, null);
                    return;
                }
            }
        }
        doSendPhoto();
    }

    @Override
    public void getSettingPhotoId() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "获取当前设置的表盘ID";
        BluetoothSDK.getTimeSurface(callBack);
    }

    private void doSendPhoto() {
        if (filePath == null || filePathId == -999) {
            if (photoListener != null) {
                photoListener.error("文件读取出错");
            }
            return;
        }
        RCITingWatchInfoDTO dto = RCITingDeviceUtil.getInstance(context).getConnectFunction().getLastConnectDeviceDTO();
        if (dto != null) {
            int imageWight = ITingDeviceEnum.checkFromDeviceId(dto.getDeviceId()).imageWight;
            String filename = PhotoImageUtil.getImage(filePath, imageWight);
//            if (RCITingDeviceUtil.getInstance(context).lastConnectIsCreateOne()) {
            RCLog.i("上传图片，P03设备" + filename);
            BluetoothSDK.setCustomizeWatchFaceEx(callBack, filePathId, filename, null);
//            } else {
//                String otaName = dto.getDeviceModel() + dto.getDeviceNo().substring(dto.getDeviceNo().length() - 5);
//                RCLog.i("上传图片，非P03设备" + filename + "《---" + otaName);
//                int imageSize = SettingType.CUSTOMIZE_WATCH_FACE_SIZE_180;
//                BluetoothSDK.setCustomizeWatchFace(callBack, otaName, filePathId, filename, imageSize,
//                        null, null, null, null, null, 1, null, false, null, null, null);
//            }
            if (photoListener != null) {
                photoListener.start();
            }
        } else {
            if (photoListener != null) {
                photoListener.error("未连接设备");
            }
        }
    }

    @Override
    public void setHeartAuto(boolean status, int time) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "设置心率时间";
        if (status)
            BluetoothSDK.setAutoHeartRateFrequency(callBack, time);
        else
            BluetoothSDK.setAutoHeartRateFrequency(callBack, 0);

    }

    @Override
    public void setHeartAlarmThreshold(boolean status, int min, int max) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "设置心率报警";
        BluetoothSDK.setHeartRateAlarmThreshold(callBack, status ? 1 : 0, min, max);
    }

    @Override
    public void getHeartAuto() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "获取心率设置";
        BluetoothSDK.getAutoHeartRateFrequency(callBack);
    }


    @Override
    public void getSetting() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "获取开关设置";
        BluetoothSDK.getSwitchSetting(callBack);

    }

    @Override
    public void setSetting(int type, boolean status) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "设置开关（" + type + "）";
        BluetoothSDK.setSwitchSetting(callBack, type, status);
    }


    @Override
    public void setSleepAutoTime(String startTime, String endTime) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "设置睡眠时间";
        if (startTime.length() == 4 && endTime.length() == 4) {
            BluetoothSDK.setAutoSleep(callBack,
                    Integer.parseInt(startTime.substring(0, 2)),
                    Integer.parseInt(startTime.substring(2, 4)),
                    Integer.parseInt(endTime.substring(0, 2)),
                    Integer.parseInt(endTime.substring(2, 4)),
                    127);
        } else {
            RCITingDeviceEvent event = new RCITingDeviceEvent(RCITingDeviceEventType.SETTING_SET_SLEEP_AUTO_TIME, false);
            event.setObject("时间格式不对");
            EventBus.getDefault().post(event);
        }

    }

    @Override
    public void getSleepAutoTime() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "睡眠设置状态";
        BluetoothSDK.getAutoSleep(callBack);
    }

    private String lastFunctionName = "";

    public void parsingData(int result, Object[] objects) {
        RCLog.i(TAG, "本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        RCITingDeviceEvent event;
        switch (result) {
            //心率设置
            case ResultCallBack.TYPE_SET_AUTO_HEART_RATE_FREQUENCY:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_SET_HEART_AUTO, true
                ));
                break;
            case ResultCallBack.TYPE_GET_AUTO_HEART_RATE_FREQUENCY:
                event = new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_GET_HEART_AUTO, true);
                event.setObject(objects[0]);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_SET_HEART_RATE_ALARM_THRESHOLD:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_SET_HEART_ALARM_THRESHOLD, true
                ));
                break;
            case ResultCallBack.TYPE_GET_HEART_RATE_ALARM_THRESHOLD:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_GET_HEART_ALARM_THRESHOLD, true
                ));
                break;
            //睡眠设置
            case ResultCallBack.TYPE_GET_AUTO_SLEEP:
                RCLog.i(TAG, "auto sleep : " + Arrays.toString(objects));
                event = new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_GET_SLEEP_AUTO_TIME, true);
                Object[] temp = objects;
                if (temp.length > 4) {
                    JSONObject object = new JSONObject();
                    int sH = (int) temp[0];
                    int sM = (int) temp[1];
                    String startTime = (sH >= 10 ? sH + "" : "0" + sH) + (sM >= 10 ? sM + "" : "0" + sM);
                    int eH = (int) temp[2];
                    int eM = (int) temp[3];
                    String endTime = (eH >= 10 ? eH + "" : "0" + eH) + (eM >= 10 ? eM + "" : "0" + eM);
                    try {
                        object.put("startTime", startTime);
                        object.put("endTime", endTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    event.setObject(object);
                }
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_SET_AUTO_SLEEP:
                RCLog.i(TAG, "set auto sleep success");
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_SET_SLEEP_AUTO_TIME, true
                ));
                break;
            //开关设置
            case ResultCallBack.TYPE_SET_SWITCH_SLEEP_STATE:
                RCLog.i(TAG, "set sleep status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_SLEEP_STATE, true)
                );
                break;
            case ResultCallBack.TYPE_SET_SWITCH_INCOME_CALL:
                RCLog.i(TAG, "set income call status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_INCOME_CALL, true)
                );
                break;
            case ResultCallBack.TYPE_SET_SWITCH_MISS_CALL:
                RCLog.i(TAG, "set miss call status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_MISS_CALL, true)
                );
                break;
            case ResultCallBack.TYPE_SET_SWITCH_MAIL:
                RCLog.i(TAG, "set mail status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_MAIL, true)
                );
                break;
            case ResultCallBack.TYPE_SET_SWITCH_CALENDAR:
                RCLog.i(TAG, "set calendar status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_CALENDAR, true)
                );
                break;
            case ResultCallBack.TYPE_SET_SWITCH_SOCIAL:
                RCLog.i(TAG, "set social status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_SOCIAL, true)
                );
                break;
            case ResultCallBack.TYPE_SET_SWITCH_SMS:
                RCLog.i(TAG, "set sms status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_SMS, true)
                );
                break;
            case ResultCallBack.TYPE_SET_SWITCH_AUTO_SYNC:
                RCLog.i(TAG, "set auto sync status success");
                EventBus.getDefault().post(
                        new RCITingSwitchEvent(RCITingSwitchEvent.SWITCH_AUTO_SYNC, true)
                );
                break;
            case ResultCallBack.TYPE_GET_SWITCH_SETTING:
                RCLog.i(TAG, "get status success");
                SwitchType switchType = (SwitchType) objects[0];
                RCITingSwitchEvent deviceEvent =
                        new RCITingSwitchEvent(RCITingSwitchEvent.SETTING_GET_SWITCH, true);
                deviceEvent.setObject(RCITingSwitchUtil.getInfo(switchType));
                EventBus.getDefault().post(deviceEvent);
                break;
            //表盘
            case ResultCallBack.TYPE_UPLOAD_IMAGE_PROGRESS:
                RCLog.i("上传图片，进度：" + (int) objects[0]);
                if (photoListener != null) {
                    photoListener.progress((int) objects[0]);
                }
                break;
            case ResultCallBack.TYPE_UPLOAD_IMAGE_RESULT:
                RCLog.i("上传图片，完成");
                if (photoListener != null) {
                    photoListener.over();
                }
                break;
            case ResultCallBack.TYPE_UPLOAD_IMAGE_MAX:
                RCLog.i("上传图片，获取到最大值" + (int) objects[0]);
                if (photoListener != null) {
                    photoListener.maxValue((int) objects[0]);
                }
                break;
            case ResultCallBack.TYPE_SET_CUSTOMIZE_WATCH_FACE:
                RCLog.i("上传存在，设置成功");
                if (photoListener != null) {
                    photoListener.over();
                }
                break;
            case ResultCallBack.TYPE_GET_CUSTOMIZE_WATCH_FACE_CRC_AND_ID_EX:
                int ids[] = (int[]) objects[0];
                RCLog.i("表盘列表获取完成，个数总共有：" + ids.length);
                sendPhoto(ids);
                break;
            case ResultCallBack.TYPE_GET_TIME_SURFACE:
                if (objects.length >= 6) {
                    event = new RCITingDeviceEvent(
                            RCITingDeviceEventType.SETTING_GET_PHOTO_ID, true);
                    event.setObject(objects[6]);
                    EventBus.getDefault().post(event);
                }
                break;
        }
    }

    @Override
    public void parsingError(int result) {
        switch (result) {

            case ResultCallBack.TYPE_SET_AUTO_HEART_RATE_FREQUENCY:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_SET_HEART_AUTO, false
                ));
                break;
            case ResultCallBack.TYPE_GET_AUTO_HEART_RATE_FREQUENCY:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_GET_HEART_AUTO, false
                ));
                break;
            case ResultCallBack.TYPE_SET_HEART_RATE_ALARM_THRESHOLD:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_SET_HEART_ALARM_THRESHOLD, false
                ));
                break;
            case ResultCallBack.TYPE_GET_HEART_RATE_ALARM_THRESHOLD:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_GET_HEART_ALARM_THRESHOLD, false
                ));
                break;
            case ResultCallBack.TYPE_GET_AUTO_SLEEP:
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_GET_SLEEP_AUTO_TIME, false));
                break;
            case ResultCallBack.TYPE_SET_AUTO_SLEEP:
                RCLog.i(TAG, "set auto sleep fail");
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingDeviceEventType.SETTING_SET_SLEEP_AUTO_TIME, false
                ));
                break;
            case ResultCallBack.TYPE_GET_SWITCH_SETTING:
                RCLog.i(TAG, "get sleep status fail");
                EventBus.getDefault().post(new RCITingDeviceEvent(
                        RCITingSwitchEvent.SETTING_GET_SWITCH, false
                ));
                break;
            case ResultCallBack.TYPE_SET_SWITCH_SLEEP_STATE:

                break;
            case ResultCallBack.TYPE_SET_SWITCH_INCOME_CALL:

                break;
            case ResultCallBack.TYPE_SET_SWITCH_MISS_CALL:

                break;
            case ResultCallBack.TYPE_SET_SWITCH_MAIL:

                break;
            case ResultCallBack.TYPE_SET_SWITCH_CALENDAR:

                break;
            case ResultCallBack.TYPE_SET_SWITCH_SOCIAL:

                break;
            case ResultCallBack.TYPE_SET_SWITCH_SMS:

                break;
            case ResultCallBack.TYPE_SET_SWITCH_AUTO_SYNC:

                break;
            case ResultCallBack.TYPE_SET_CUSTOMIZE_WATCH_FACE:

                break;
            case ResultCallBack.TYPE_GET_CUSTOMIZE_WATCH_FACE_CRC_AND_ID_EX:

                break;
            case ResultCallBack.TYPE_GET_TIME_SURFACE:

                break;
            case ResultCallBack.TYPE_UPLOAD_IMAGE_PROGRESS:
                if (photoListener != null) {
                    photoListener.error("上传失败");
                }
                break;
            case ResultCallBack.TYPE_UPLOAD_IMAGE_RESULT:
                if (photoListener != null) {
                    photoListener.error("上传失败");
                }
                break;
            case ResultCallBack.TYPE_UPLOAD_IMAGE_MAX:
                if (photoListener != null) {
                    photoListener.error("上传失败");
                }
                break;
        }
    }


}
