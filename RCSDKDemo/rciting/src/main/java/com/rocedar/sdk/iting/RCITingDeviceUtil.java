package com.rocedar.sdk.iting;

import android.content.Context;

import com.rocader.sdk.data.data.RCUploadDeviceData;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.iting.device.ITingDeviceAerobicExerciseFunction;
import com.rocedar.sdk.iting.device.ITingDeviceBaseFunction;
import com.rocedar.sdk.iting.device.ITingDeviceConnectFunction;
import com.rocedar.sdk.iting.device.ITingDeviceDataFunction;
import com.rocedar.sdk.iting.device.ITingDeviceMusicFunction;
import com.rocedar.sdk.iting.device.ITingDeviceRemindFunction;
import com.rocedar.sdk.iting.device.ITingDeviceSettingFunction;
import com.rocedar.sdk.iting.device.function.RCITingDeviceAerobicExerciseFunction;
import com.rocedar.sdk.iting.device.function.RCITingDeviceBaseFunction;
import com.rocedar.sdk.iting.device.function.RCITingDeviceConnectFunction;
import com.rocedar.sdk.iting.device.function.RCITingDeviceDataFunction;
import com.rocedar.sdk.iting.device.function.RCITingDeviceMusicFunction;
import com.rocedar.sdk.iting.device.function.RCITingDeviceRemindFunction;
import com.rocedar.sdk.iting.device.function.RCITingDeviceSettingFunction;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/3 4:49 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingDeviceUtil {

    private static String TAG = "ITING-设备";

    private static RCITingDeviceUtil tingDeviceUtil;

    public static RCITingDeviceUtil getInstance(Context context) {
        if (tingDeviceUtil == null) {
            tingDeviceUtil = new RCITingDeviceUtil(context);
        }
        return tingDeviceUtil;
    }

    private Context mContext;


    public RCITingDeviceUtil(Context mContext) {
        this.mContext = mContext;
        BluetoothSDK.setPhoneCallBack(callBack);
        BluetoothSDK.setVolumeCallBack(callBack);
        BluetoothSDK.setMusicCallBack(callBack);
    }

    public boolean isConnect() {
        return getConnectFunction().isConnect();
    }

    public boolean isConnecting() {
        return getConnectFunction().isConnectIng();
    }

    //手表链接相关功能处理类
    private ITingDeviceConnectFunction connectFunction;

    public ITingDeviceConnectFunction getConnectFunction() {
        if (connectFunction == null) {
            connectFunction = new RCITingDeviceConnectFunction(mContext, callBack);
        }
        return connectFunction;
    }

    //手表数据获取相关功能处理类
    private ITingDeviceDataFunction dataFunction;

    public ITingDeviceDataFunction getDataFunction() {
        if (dataFunction == null) {
            dataFunction = new RCITingDeviceDataFunction(mContext, callBack);
        }
        return dataFunction;
    }

    //手表基本设置相关功能处理类
    private ITingDeviceBaseFunction baseFunction;

    public ITingDeviceBaseFunction getBaseFunction() {
        if (baseFunction == null) {
            baseFunction = new RCITingDeviceBaseFunction(mContext, callBack);
        }
        return baseFunction;
    }

    //手表提醒设置相关功能处理类
    private ITingDeviceRemindFunction remindFunction;

    public ITingDeviceRemindFunction getRemindFunction() {
        if (remindFunction == null) {
            remindFunction = new RCITingDeviceRemindFunction(mContext, callBack);
        }
        return remindFunction;
    }

    //手表基本设置相关功能处理类
    private ITingDeviceSettingFunction settingFunction;

    public ITingDeviceSettingFunction getSettingFunction() {
        if (settingFunction == null) {
            settingFunction = new RCITingDeviceSettingFunction(mContext, callBack);
        }
        return settingFunction;
    }

    //手表有氧相关功能处理类
    private ITingDeviceAerobicExerciseFunction aerobicFunction;

    public ITingDeviceAerobicExerciseFunction getAerobicFunction() {
        if (aerobicFunction == null) {
            aerobicFunction = new RCITingDeviceAerobicExerciseFunction(mContext, callBack);
        }
        return aerobicFunction;
    }

    //手表有氧相关功能处理类
    private ITingDeviceMusicFunction musicFunction;

    public ITingDeviceMusicFunction getMusicFunction() {
        if (musicFunction == null) {
            musicFunction = new RCITingDeviceMusicFunction(mContext, callBack);
        }
        return musicFunction;
    }

    public ResultCallBack callBack = new ResultCallBack() {
        @Override
        public void onSuccess(int result, Object[] objects) {
            RCLog.i(TAG, "type:" + result + "<-->" + (objects != null ? objects.toString() : "object null"));
            RCLog.i(TAG, "type:" + result + "<-->" + (objects != null ? objects[0].toString() : "object null"));

            //连接功能返回值处理
            for (int i = 0; i < ITingDeviceConnectFunction.CONNECT_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceConnectFunction.CONNECT_FUNCTION_TYPE[i]) {
                    getConnectFunction().parsingData(result, objects);
                    return;
                }
            }
            //数据类型功能返回值处理
            for (int i = 0; i < ITingDeviceDataFunction.DATA_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceDataFunction.DATA_FUNCTION_TYPE[i]) {
                    getDataFunction().parsingData(result, objects);
                    return;
                }
            }
            //基本功能返回值处理
            for (int i = 0; i < ITingDeviceBaseFunction.BASE_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceBaseFunction.BASE_FUNCTION_TYPE[i]) {
                    getBaseFunction().parsingData(result, objects);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceRemindFunction.REMIND_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceRemindFunction.REMIND_FUNCTION_TYPE[i]) {
                    getRemindFunction().parsingData(result, objects);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceSettingFunction.SETTING_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceSettingFunction.SETTING_FUNCTION_TYPE[i]) {
                    getSettingFunction().parsingData(result, objects);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceAerobicExerciseFunction.AEROBIC_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceAerobicExerciseFunction.AEROBIC_FUNCTION_TYPE[i]) {
                    getAerobicFunction().parsingData(result, objects);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceMusicFunction.MUSIC_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceMusicFunction.MUSIC_FUNCTION_TYPE[i]) {
                    getMusicFunction().parsingData(result, objects);
                    return;
                }
            }
            RCLog.e("类型不对:" + result);

        }

        @Override
        public void onFail(int result) {
            RCLog.e(TAG, "error type:" + result);
            //链接功能返回值处理
            for (int i = 0; i < ITingDeviceConnectFunction.CONNECT_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceConnectFunction.CONNECT_FUNCTION_TYPE[i]) {
                    getConnectFunction().parsingError(result);
                    return;
                }
            }
            //数据类型功能返回值处理
            for (int i = 0; i < ITingDeviceDataFunction.DATA_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceDataFunction.DATA_FUNCTION_TYPE[i]) {
                    getDataFunction().parsingError(result);
                    return;
                }
            }
            //基本功能返回值处理
            for (int i = 0; i < ITingDeviceBaseFunction.BASE_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceBaseFunction.BASE_FUNCTION_TYPE[i]) {
                    getBaseFunction().parsingError(result);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceSettingFunction.SETTING_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceSettingFunction.SETTING_FUNCTION_TYPE[i]) {
                    getSettingFunction().parsingError(result);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceRemindFunction.REMIND_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceRemindFunction.REMIND_FUNCTION_TYPE[i]) {
                    getRemindFunction().parsingError(result);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceAerobicExerciseFunction.AEROBIC_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceAerobicExerciseFunction.AEROBIC_FUNCTION_TYPE[i]) {
                    getAerobicFunction().parsingError(result);
                    return;
                }
            }
            for (int i = 0; i < ITingDeviceMusicFunction.MUSIC_FUNCTION_TYPE.length; i++) {
                if (result == ITingDeviceMusicFunction.MUSIC_FUNCTION_TYPE[i]) {
                    getMusicFunction().parsingError(result);
                    return;
                }
            }
            RCLog.e("类型不对(error):" + result);
        }
    };




    /**
     * 开始获取手表数据
     */
    public void doGetWatchData() {
        //判断手表是否连接
        if (isConnect()) {
            //开始获取数据
            getDataFunction().doGetDeviceData();
        }
    }

    /**
     * 上传数据
     */
    public void uploadData() {
        RCUploadDeviceData.postBlueDeviceData(mContext);
    }


}
