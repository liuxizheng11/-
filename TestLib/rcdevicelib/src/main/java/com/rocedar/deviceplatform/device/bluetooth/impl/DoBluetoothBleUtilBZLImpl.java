package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.device.bluetooth.ble.BLEDataUtils;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothLeService;
import com.rocedar.deviceplatform.device.bluetooth.ble.IDoBluetoothBleUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.bzl.BZLSendCodeUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.bzl.BZLUUIDConfig;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;

import java.util.List;

import static com.rocedar.deviceplatform.unit.AESUtils.TAG;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/10/30 下午5:23
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DoBluetoothBleUtilBZLImpl implements IDoBluetoothBleUtil, RCBluetoothDataType, RCBluetoothDoType {

    private Handler mHandler = new Handler();

    private BluetoothLeService mBluetoothLeService;


    //单例对象
    private static DoBluetoothBleUtilBZLImpl ourInstance;

    //最后实力的设备mac地址
    private static String lastMac = null;

    private boolean CharacteristicIsInit = false;


    //获得工具类的单例对象
    public static DoBluetoothBleUtilBZLImpl getInstance(BluetoothLeService mBluetoothLeService, String mac) {
        if (lastMac == null || !mac.equals(lastMac) || ourInstance == null) {
            ourInstance = new DoBluetoothBleUtilBZLImpl(mBluetoothLeService, mac);
        }
        return ourInstance;
    }

    public DoBluetoothBleUtilBZLImpl(BluetoothLeService mBluetoothLeService, String mac) {
        this.mBluetoothLeService = mBluetoothLeService;
        this.lastMac = mac;
    }


    private ServicesDiscoveredListener discoveredListener;

    @Override
    public void servicesDiscovered(ServicesDiscoveredListener listener) {
        //发现服务后，博之轮手环需要先开启监听通知的通道
        this.discoveredListener = listener;
        setBLENotification(lastMac);
    }

    @Override
    public void doInstruction(RCBluetoothGetDataListener bluetoothGetDataListener, int doType) {
        this.bluetoothGetDataListener = bluetoothGetDataListener;
        if (bluetoothGetDataListener != null)
            bluetoothGetDataListener.getDataStart();
        writeData(BZLSendCodeUtil.getCodeStringFromDoType(doType, lastMac), lastMac);
    }


    @Override
    public void onDestroy() {
        if (mBluetoothLeService != null && notificationChar != null) {
            mBluetoothLeService.setCharacteristicNotification(notificationChar, false, lastMac);
        }
    }

    @Override
    public void timeOut() {
        resetBLENotification();
    }

    @Override
    public void writeOK() {

    }

    @Override
    public boolean receiveData(Intent intent) {
        return true;
    }


    //博之轮通知通道
    private BluetoothGattCharacteristic notificationChar;
    //博之轮写数据通道
    private BluetoothGattCharacteristic writeChar;


    //-----------------数据通道处理-----------------S

    /**
     * 设置蓝牙数据通道，会出现设置失败的情况，使用线程对通道设置进行多次尝试
     */
    public void setBLENotification(String mac) {
        NotificationSetting notificationSetting = new NotificationSetting(mac);
        mHandler.postDelayed(notificationSetting, 5000);
    }

    /**
     * 获取蓝牙设备的读写通道
     *
     * @param mac
     * @return
     */
    private boolean getNotificationCharacteristics(String mac) {
        boolean temp = false;
        try {
            if (BZLUUIDConfig.BZLUUID_NOTIFICATION.equals("")) return false;
            if (mBluetoothLeService == null) return false;
            temp = false;
            for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices(mac)) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                        .getCharacteristics();
                if (gattService.getUuid().toString().equals(BZLUUIDConfig.BZLUUID_SERVICE)) {
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        //通知通道获取
                        if (gattCharacteristic.getUuid().toString().equals(BZLUUIDConfig.BZLUUID_NOTIFICATION)) {
                            RCLog.d(TAG, "蓝牙：博之轮通知的蓝牙通道获取到" + mac);
                            notificationChar = gattCharacteristic;
                            //设备通知监听
                            temp = mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true, mac);
                        }
                        if (gattCharacteristic.getUuid().toString().equals(BZLUUIDConfig.BZLUUID_WRITE)) {
                            Log.d(TAG, "蓝牙：博之轮写数据的蓝牙通道获取到" + mac);
                            writeChar = gattCharacteristic;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (discoveredListener != null) {
            discoveredListener.initOver(temp);
            discoveredListener = null;
        }
        return temp;
    }


    /**
     * 数据获取超时时，重新设置通道
     */
    public boolean resetBLENotification() {
        CharacteristicIsInit = false;
        if (notificationChar != null)
            mBluetoothLeService.setCharacteristicNotification(notificationChar, false, lastMac);
        //如果重新初始化通道还是失败，断开设备连接
        if (!getNotificationCharacteristics(lastMac)) {
            mBluetoothLeService.disconnect(lastMac);
            return false;
        }
        return true;
    }

    private class NotificationSetting implements Runnable {

        private String mac;

        public NotificationSetting(String mac) {
            this.mac = mac;
        }

        @Override
        public void run() {
            RCLog.d(TAG, "蓝牙：等待5s完成，开始设置蓝牙数据通道");
            CharacteristicIsInit = getNotificationCharacteristics(mac);
        }
    }

    //-----------------数据通道处理-----------------E

    //----------------数据指令解析,发送数据-----------------S

    private RCBluetoothGetDataListener bluetoothGetDataListener;

    public void writeData(String writeInfo, String mac) {
        if (!CharacteristicIsInit) {
            //如果通道没有初始化成功，重新初始化通道
            if (!resetBLENotification()) {
                if (bluetoothGetDataListener != null) {
                    bluetoothGetDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "设备连接失败");
                }
                return;
            }
        }
        if (writeChar != null) {
            writeChar.setValue(BLEDataUtils.hexStringToByte(writeInfo));
            if (mBluetoothLeService != null)
                mBluetoothLeService.wirteCharacteristic(writeChar, mac);
        }
    }


}
