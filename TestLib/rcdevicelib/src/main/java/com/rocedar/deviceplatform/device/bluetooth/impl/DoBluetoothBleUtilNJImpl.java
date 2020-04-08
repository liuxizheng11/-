package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Handler;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothLeService;
import com.rocedar.deviceplatform.device.bluetooth.ble.IDoBluetoothBleUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.nianjia.NJSendCodeUtil;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothConnectListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;

import java.util.Calendar;
import java.util.List;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/10/31 下午3:05
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DoBluetoothBleUtilNJImpl implements IDoBluetoothBleUtil {

    private Handler mHandler = new Handler();

    private BluetoothLeService mBluetoothLeService;

    private RCBluetoothConnectListener listener;

    //单例对象
    private static DoBluetoothBleUtilNJImpl ourInstance;
    private static String lastMac = null;

    //获得工具类的单例对象
    public static DoBluetoothBleUtilNJImpl getInstance(BluetoothLeService mBluetoothLeService, String mac) {
        if (lastMac == null || !mac.equals(lastMac) || ourInstance == null) {
            ourInstance = new DoBluetoothBleUtilNJImpl(mBluetoothLeService, mac);
        }
        return ourInstance;
    }

    public DoBluetoothBleUtilNJImpl(BluetoothLeService mBluetoothLeService, String mac) {
        this.mBluetoothLeService = mBluetoothLeService;
        this.lastMac = mac;
    }


    @Override
    public void servicesDiscovered(ServicesDiscoveredListener listener) {
        listener.initOver(getNotificationCharacteristics(lastMac));
    }


    @Override
    public void doInstruction(RCBluetoothGetDataListener bluetoothGetDataListener, int doType) {
        RCLog.e("DoBluetoothBleUtilNJImpl", "执行指令：" + doType);
        if (doType == RCBluetoothDoType.DO_SETTING_TIME) {
            settingChar.setValue(
                    NJSendCodeUtil.codeSettingTime()
            );
            mBluetoothLeService.wirteCharacteristic(settingChar, lastMac);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothLeService.readCharacteristic(settingChar, lastMac);
                }
            }, 1000);
        } else if (doType == RCBluetoothDataType.DATATYPE_STEP_TODAY) {
            if (dataChar != null)
                mBluetoothLeService.readCharacteristic(dataChar, lastMac);
        } else if (doType == RCBluetoothDataType.DATATYPE_STEP_HISTORY) {
            mHandler.postDelayed(run, 1000);
        }

    }

    int index = 0;

    Runnable run = new Runnable() {
        @Override
        public void run() {
            if (historyChar1 != null) {
                index++;
                byte send[] = getBytes1(index);
                Send(send, 17);
                if (index <= 7) {
                    mHandler.postDelayed(run, 1000);
                } else {
                    index = 0;
                }
            }
        }
    };

    public int Send(byte[] buffer, int byteCount) {
        int len = Math.min(20, byteCount);
        if (len == buffer.length) {
            historyChar1.setValue(buffer);
            mBluetoothLeService.wirteCharacteristic(historyChar1, lastMac);
        } else {
            byte[] buffer2 = new byte[len];
            System.arraycopy(buffer, 0, buffer2, 0, len);
            historyChar1.setValue(buffer2);
            mBluetoothLeService.wirteCharacteristic(historyChar1, lastMac);
        }

        return 0;
    }

    private byte[] getBytes1(int n) {
        byte[] bb = new byte[17];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -n);
        int aaa = calendar.get(Calendar.YEAR);
        int aaax = aaa % 100;
        int bbb = calendar.get(Calendar.MONTH) + 1;
        int ccc = calendar.get(Calendar.DAY_OF_MONTH);
        bb[0] = (byte) aaax;
        bb[1] = (byte) bbb;
        bb[2] = (byte) ccc;
        bb[3] = (byte) 0;
        bb[4] = (byte) 0;
        bb[5] = (byte) 0;
        bb[6] = (byte) 0;
        bb[7] = (byte) 0;
        bb[8] = (byte) 0;
        bb[9] = (byte) 0;
        bb[10] = (byte) 0;
        bb[11] = (byte) 0;
        bb[12] = (byte) 0;
        bb[13] = (byte) 0;
        bb[14] = (byte) 0;
        bb[15] = (byte) 0;
        bb[16] = (byte) 0;
        return bb;
    }

    @Override
    public void onDestroy() {
        isConnectIn = 0;
    }

    @Override
    public void timeOut() {

    }

    @Override
    public void writeOK() {
        RCLog.e("DoBluetoothBleUtilNJImpl", "writeOK：");
    }

    /**
     * 念家手环连接过程（仅连接时一次）
     * 1.设置通知，通知设置成功后会收到数据
     * 2.收到数据后，获取一条数据，激活通知（念家手环测试中如果不按照这个步骤历史数据没法获取，以后发现问题根本后再优化）
     * 3.收到通知监听，写入密码
     * <p>
     * 0:等待连接
     * 1:已经开始连接，通知监听已经开启
     * 2:
     */
    private int isConnectIn = 0;

    @Override
    public boolean receiveData(Intent intent) {
        RCLog.e("DoBluetoothBleUtilNJImpl", "获取到数据：" +
                intent.getStringExtra(BluetoothLeService.EXTRA_DATA)
                + ":isConnectIn=" + isConnectIn
                + ":type=" + intent.getIntExtra(BluetoothLeService.EXTRA_DEVICE_TYPE, -1)
        );
        if (isConnectIn == 0 && intent.getIntExtra(BluetoothLeService.EXTRA_DEVICE_TYPE, -1)
                == BluetoothLeService.EXTRA_DEVICE_TYPE_VAULE_R) {
            isConnectIn = 1;
            byte send[] = getBytes1(1);
            Send(send, 17);
        }
        if (isConnectIn == 1 && intent.getIntExtra(BluetoothLeService.EXTRA_DEVICE_TYPE, -1)
                == BluetoothLeService.EXTRA_DEVICE_TYPE_VAULE_N) {
            isConnectIn = 2;
            if (passwordChar != null) {
                passwordChar.setValue(password.getBytes());
                mBluetoothLeService.wirteCharacteristic(passwordChar, lastMac);
            }
            if (settingChar != null) {
                settingChar.setValue(NJSendCodeUtil.codeSettingTime());
                mBluetoothLeService.wirteCharacteristic(settingChar, lastMac);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothLeService.readCharacteristic(settingChar, lastMac);
                }
            }, 1000);
        }
        return isConnectIn == 2;
    }


    private String password = "!@YiXingPassWord*&^0";


    private String UUID_PASSWORD = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private String UUID_SETTING = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private String UUID_DATA = "0000ffa0-0000-1000-8000-00805f9b34fb";
    private String UUID_DATA_HISTORY1 = "0000ffa5-0000-1000-8000-00805f9b34fb";
    private String UUID_DATA_HISTORY2 = "0000ffa6-0000-1000-8000-00805f9b34fb";
    private String UUID_SERVICE = "0000b00b-0000-1000-8000-00805f9b34fb";


    private BluetoothGattCharacteristic passwordChar;
    private BluetoothGattCharacteristic settingChar;
    private BluetoothGattCharacteristic dataChar;
    private BluetoothGattCharacteristic historyChar1;
//    private BluetoothGattCharacteristic historyChar2;

    /**
     * 获取蓝牙设备的读写通道
     *
     * @param mac
     * @return
     */
    private boolean getNotificationCharacteristics(String mac) {
        boolean temp = false;
        try {
            if (mBluetoothLeService == null) return false;
            temp = false;
            for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices(mac)) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                        .getCharacteristics();
                if (gattService.getUuid().toString().equals(UUID_SERVICE)) {
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        if (gattCharacteristic.getUuid().toString().equals(UUID_PASSWORD)) {
                            passwordChar = gattCharacteristic;
//                            passwordChar.setValue(password.getBytes());
//                            mBluetoothLeService.wirteCharacteristic(passwordChar, mac);
//                            if (settingChar != null) {
//                                settingChar.setValue(
//                                        NJSendCodeUtil.codeSettingTime()
//                                );
//                                mBluetoothLeService.wirteCharacteristic(settingChar, mac);
//                            }
                            temp = true;
                        } else if (gattCharacteristic.getUuid().toString().equals(UUID_SETTING)) {
                            settingChar = gattCharacteristic;
//                            if (passwordChar != null) {
//                                settingChar.setValue(
//                                        NJSendCodeUtil.codeSettingTime()
//                                );
//                                mBluetoothLeService.wirteCharacteristic(settingChar, mac);
//                            }
                        } else if (gattCharacteristic.getUuid().toString().equals(UUID_DATA)) {
                            dataChar = gattCharacteristic;
                        } else if (gattCharacteristic.getUuid().toString().equals(UUID_DATA_HISTORY1)) {
                            historyChar1 = gattCharacteristic;
                        } else if (gattCharacteristic.getUuid().toString().equals(UUID_DATA_HISTORY2)) {
//                            historyChar2 = gattCharacteristic;
                        }

                    }
                }
            }
            if (historyChar1 != null) {
                RCLog.e("DoBluetoothBleUtilNJImpl", "通知");
                mBluetoothLeService.setCharacteristicNotification(historyChar1, true, mac);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


}
