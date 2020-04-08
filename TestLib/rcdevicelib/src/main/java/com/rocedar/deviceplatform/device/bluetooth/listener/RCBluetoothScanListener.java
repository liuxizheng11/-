package com.rocedar.deviceplatform.device.bluetooth.listener;

import android.bluetooth.BluetoothDevice;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/10 下午4:13
 * 版本：V1.0
 * 描述：蓝牙扫描监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBluetoothScanListener {

    void scanOver();

    void scanStart();

    void scanInfo(BluetoothDevice device, int rssi);

    void scanError(int status, String msg);

}
