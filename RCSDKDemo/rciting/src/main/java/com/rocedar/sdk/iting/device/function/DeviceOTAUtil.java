package com.rocedar.sdk.iting.device.function;

import android.content.Context;
import android.util.Log;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/8/4 3:10 PM
 * 版本：V1.1.00
 * 描述：瑰柏ITING-手表OTA升级
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DeviceOTAUtil {

    private String TAG = "ITING-OTA";

    private Context mContext;

    protected DeviceOTAUtil(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * @param apolloPath
     * @param touchPanelPath
     * @param pictureArray
     * @param LanguagePath
     */
    public void doUpDate(final UpdateListener updateListener, final String otaName, final String apolloPath, final String touchPanelPath, final String[] pictureArray, final String LanguagePath) {
        if (updateListener != null) {
            updateListener.start();
        }
//        BluetoothSDK.disConnect(null);
//        BluetoothSDK.startScan(new BluetoothScanCallBack() {
//            @Override
//            public void onLeScan(BluetoothDevice bluetoothDevice, int rssi) {
//                RCLog.i(TAG, "deviceName : " + bluetoothDevice.getName() + " MAC : " + bluetoothDevice.getAddress()
//                        + " rssi :" + rssi);
//                if (!TextUtils.isEmpty(bluetoothDevice.getName()) &&
//                        !TextUtils.isEmpty(bluetoothDevice.getAddress()) &&
//                        bluetoothDevice.getName().equals(otaName)) {
//                    RCLog.i(TAG, "找到空中升级设备");
//                    BluetoothSDK.stopScan();
        BluetoothSDK.startUpdate(new ResultCallBack() {
            @Override
            public void onSuccess(int resultType, Object[] objects) {
                switch (resultType) {
                    case ResultCallBack.TYPE_OTA_UPDATE_PROGRESS:
                        Log.i(TAG, "进度:" + objects[0]);
                        if (updateListener != null) {
                            updateListener.progress((Integer) objects[0]);
                        }
                        break;
                    case ResultCallBack.TYPE_OTA_UPDATE_MAX:
                        if (updateListener != null) {
                            updateListener.max((Integer) objects[0]);
                        }
                        Log.i(TAG, "最大值:" + objects[0]);
                        break;
                    case ResultCallBack.TYPE_OTA_UPDATE_RESULT:
                        if (updateListener != null) {
                            updateListener.end();
                        }
                        Log.i(TAG, "升级结果：" + objects[0]);
                        break;
                }
            }

            @Override
            public void onFail(int resultType) {
                if (updateListener != null) {
                    updateListener.error();
                }
                Log.i(TAG, "升级失败");
            }
        }, touchPanelPath, pictureArray, LanguagePath, apolloPath);
//                }
//            }
//
//            @Override
//            public void onStopScan(boolean b) {
//
//            }
//        }, null);

    }


    public interface UpdateListener {

        void start();

        void error();

        void end();

        void progress(int progress);

        void max(int max);
    }
}
