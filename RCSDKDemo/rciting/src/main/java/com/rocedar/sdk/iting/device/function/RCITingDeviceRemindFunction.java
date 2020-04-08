package com.rocedar.sdk.iting.device.function;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.iting.device.ITingDeviceRemindFunction;
import com.rocedar.sdk.iting.device.ResultCallBackEx;
import com.rocedar.sdk.iting.device.dto.RCITingRemindExDTO;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEvent;
import com.rocedar.sdk.iting.device.envet.RCITingDeviceEventType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.app.SettingType;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
import cn.appscomm.bluetoothsdk.model.ReminderExData;

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
public class RCITingDeviceRemindFunction implements ITingDeviceRemindFunction, RCITingDeviceEventType {

    private String TAG = "ITING-REMIND";

    public Context context;

    private ResultCallBack callBack;

    public RCITingDeviceRemindFunction(Context context, ResultCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    private long lastFunctionTime = -1;

    @Override
    public void getRemind() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "获取提醒";
        BluetoothSDK.getReminderCountEx(callBack);
    }

    @Override
    public void addRemind(RCITingRemindExDTO dto) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "添加提醒";
        BluetoothSDK.setReminderEx(callBack, SettingType.REMINDER_EX_ACTION_NEW, dto.getReminderExData());
    }


    @Override
    public void deleteRemind(int remindId) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "删除提醒";
        BluetoothSDK.setReminderEx(callBack, SettingType.REMINDER_EX_ACTION_DELETE_ONE,
                new ReminderExData.Builder().id(remindId).build());
    }

    @Override
    public void deleteAllRemind() {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "删除全部提醒";
        BluetoothSDK.setReminderEx(callBack, SettingType.REMINDER_EX_ACTION_DELETE_ALL_REMIND,
                new ReminderExData());
    }

    @Override
    public void changeRemind(int remindId, RCITingRemindExDTO newRemind) {
        lastFunctionTime = new Date().getTime();
        lastFunctionName = "修改提醒";
        BluetoothSDK.setReminderEx(callBack, SettingType.REMINDER_EX_ACTION_CHANGE,
                newRemind.getReminderExData(remindId));
    }


    private String lastFunctionName = "";

    public void parsingData(int result, Object[] objects) {
        RCLog.i(TAG, "本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        switch (result) {
            case ResultCallBack.TYPE_GET_REMINDER:
                List<RCITingRemindExDTO> dtos = new ArrayList<>();
                if (objects == null || objects.length == 0) {
                    RCLog.i("没有提醒数据");
                } else {
                    RCLog.i("有提醒数据：" + objects[0]);
                    List<ReminderExData> reminderDataList = (List<ReminderExData>) objects[0];
                    for (int i = 0; i < reminderDataList.size(); i++) {
                        dtos.add(RCITingRemindExDTO.getRCITingRemind(reminderDataList.get(i)));
                    }
                }
                RCITingDeviceEvent event = new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_GET, true);
                event.setObject(dtos);
                EventBus.getDefault().post(event);
                break;
            case ResultCallBack.TYPE_NEW_REMINDER:
                RCLog.i(TAG, "add reminder success");
                Toast.makeText(context, "add reminder success", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_ADD, true));
                break;
            case ResultCallBack.TYPE_CHANGE_REMINDER:
                RCLog.i(TAG, "change reminder success");
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_CHANGE, true));
                Toast.makeText(context, "change reminder success", Toast.LENGTH_SHORT).show();
                break;
            case ResultCallBack.TYPE_DELETE_A_REMINDER:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_DELETE, true));
                RCLog.i(TAG, "delete a reminder success");
                Toast.makeText(context, "delete a reminder success", Toast.LENGTH_SHORT).show();
                break;
            case ResultCallBackEx.TYPE_GET_REMIND_COUNT:
                if (objects != null && objects.length > 0 && objects[0] != null) {
                    int count = (int) objects[0];
                    if (count > 0) {
                        BluetoothSDK.getReminderEx(callBack, SettingType.REMINDER_EX_CHECK_TYPE_GET_REMIND_DATA);
                        return;
                    }
                }
                RCLog.i("没有提醒数据");
                RCITingDeviceEvent remindNo = new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_GET, true);
                remindNo.setObject(new ArrayList<RCITingRemindExDTO>());
                EventBus.getDefault().post(remindNo);
                break;
            case ResultCallBackEx.TYPE_GET_REMIND:
                List<RCITingRemindExDTO> dtoList = new ArrayList<>();
                if (objects == null || objects.length == 0) {
                    RCLog.i("没有提醒数据");
                } else {
                    RCLog.i("有提醒数据");
                    List<ReminderExData> reminderDataList = (List<ReminderExData>) objects[0];
                    for (int i = 0; i < reminderDataList.size(); i++) {
                        dtoList.add(RCITingRemindExDTO.getRCITingRemind(reminderDataList.get(i)));
                    }
                }
                RCITingDeviceEvent remindHas = new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_GET, true);
                remindHas.setObject(dtoList);
                EventBus.getDefault().post(remindHas);
                break;
            case ResultCallBackEx.TYPE_SET_REMIND_ADD:
                RCITingDeviceEvent remindAdd = new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_ADD, true);
                remindAdd.setObject(objects[0]);
                EventBus.getDefault().post(remindAdd);
                break;
            case ResultCallBackEx.TYPE_SET_REMIND_CHANGE:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_CHANGE, true));
                break;
            case ResultCallBackEx.TYPE_SET_REMIND_DELETE_ONE:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_DELETE_ONE, true));
                break;
            case ResultCallBackEx.TYPE_SET_REMIND_DELETE:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_DELETE_ALL, true));
                break;
        }
    }

    @Override
    public void parsingError(int result) {
        switch (result) {
            case ResultCallBack.TYPE_GET_REMINDER:
                RCITingDeviceEvent event = new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_GET, false);
                EventBus.getDefault().post(event);
                RCLog.e(TAG, "get reminder error");
                break;
            case ResultCallBack.TYPE_NEW_REMINDER:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_ADD, false));
                RCLog.e(TAG, "new reminder error");
                break;
            case ResultCallBack.TYPE_CHANGE_REMINDER:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_CHANGE, false));
                RCLog.e(TAG, "change reminder error");
                break;
            case ResultCallBack.TYPE_DELETE_A_REMINDER:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_DELETE, false));
                RCLog.e(TAG, "delete a reminder error");
                break;
            case ResultCallBackEx.TYPE_GET_REMIND_COUNT:
                EventBus.getDefault().post(new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_GET, false));
                break;
            case ResultCallBackEx.TYPE_GET_REMIND:
                EventBus.getDefault().post(new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_GET, false));
                break;
            case ResultCallBackEx.TYPE_SET_REMIND_ADD:
                EventBus.getDefault().post(new RCITingDeviceEvent(RCITingDeviceEventType.REMINDER_ADD, false));
                break;
            case ResultCallBackEx.TYPE_SET_REMIND_CHANGE:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_CHANGE, false));
                break;
            case ResultCallBackEx.TYPE_SET_REMIND_DELETE_ONE:
                EventBus.getDefault().post(new RCITingDeviceEvent(REMINDER_DELETE_ONE, false));
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
}
