package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothLeService;
import com.rocedar.deviceplatform.device.bluetooth.ble.IDoBluetoothBleUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.bzl.BZLUUIDConfig;
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

public class DoBluetoothBleUtilYDImpl implements IDoBluetoothBleUtil {

    private Handler mHandler = new Handler();

    private BluetoothLeService mBluetoothLeService;

    //单例对象
    private static DoBluetoothBleUtilYDImpl ourInstance;
    private static String lastMac = null;


    //获得工具类的单例对象
    public static DoBluetoothBleUtilYDImpl getInstance(BluetoothLeService mBluetoothLeService, String mac) {
        if (lastMac == null || !mac.equals(lastMac) || ourInstance == null) {
            ourInstance = new DoBluetoothBleUtilYDImpl(mBluetoothLeService, mac);
        }
        return ourInstance;
    }

    public DoBluetoothBleUtilYDImpl(BluetoothLeService mBluetoothLeService, String mac) {
        this.mBluetoothLeService = mBluetoothLeService;
        this.lastMac = mac;
    }

    @Override
    public void servicesDiscovered(ServicesDiscoveredListener listener) {
        //发现服务后，博之轮手环需要先开启监听通知的通道
        setBLENotification(lastMac);
    }

    //缘度通知通道
    private BluetoothGattCharacteristic notificationChar;
    //缘度写数据通道
    private BluetoothGattCharacteristic writeChar;


    private static String SERVER_UUID = "0000fc00-0000-1000-8000-00805f9b34fb";
    private static String READ_UUID = "0000fc20-0000-1000-8000-00805f9b34fb";
    private static String WRITE_UUID = "0000fc21-0000-1000-8000-00805f9b34fb";

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
                if (gattService.getUuid().toString().equals(SERVER_UUID)) {
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        //通知通道获取
                        if (gattCharacteristic.getUuid().toString().equals(READ_UUID)) {
                            RCLog.d(TAG, "蓝牙：博之轮通知的蓝牙通道获取到" + mac);
                            notificationChar = gattCharacteristic;
                            //设备通知监听
                            temp = mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true, mac);
                        }
                        if (gattCharacteristic.getUuid().toString().equals(WRITE_UUID)) {
                            Log.d(TAG, "蓝牙：博之轮写数据的蓝牙通道获取到" + mac);
                            writeChar = gattCharacteristic;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    /**
     * 设置蓝牙数据通道，会出现设置失败的情况，使用线程对通道设置进行多次尝试
     */
    public void setBLENotification(String mac) {
        NotificationSetting notificationSetting = new NotificationSetting(mac);
        mHandler.postDelayed(notificationSetting, 5000);
    }

    /**
     * 数据获取超时时，重新设置通道
     *
     * @param mac
     */
    public void resetBLENotification(String mac) {
        if (notificationChar != null)
            mBluetoothLeService.setCharacteristicNotification(notificationChar, false, mac);
        getNotificationCharacteristics(mac);
    }


    @Override
    public void doInstruction(RCBluetoothGetDataListener bluetoothGetDataListener, int doType) {

    }

    @Override
    public void onDestroy() {
        if (mBluetoothLeService != null && notificationChar != null) {
            mBluetoothLeService.setCharacteristicNotification(notificationChar, false, lastMac);
        }
    }

    @Override
    public void timeOut() {

    }

    @Override
    public void writeOK() {

    }

    @Override
    public boolean receiveData(Intent data) {
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
            getNotificationCharacteristics(mac);
        }
    }

    /**
     * 通过通道写入数据
     *
     * @param writeInfo
     */
    public void writeData(final byte[] writeInfo) {
        RCLog.i(TAG, "writeData: " + writeInfo.toString() + "<----->");
        if (writeChar != null) {
            writeChar.setValue(writeInfo);
            if (mBluetoothLeService != null)
                mBluetoothLeService.wirteCharacteristic(writeChar, lastMac);
        }
    }


}
