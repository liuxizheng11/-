package com.rocedar.deviceplatform.device.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/10 下午3:52
 * 版本：V1.0
 * 描述：蓝牙设备功能接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBlueTooth {


    /**
     * 扫描设备
     *
     * @param scanListener
     */
    void scanListener(RCBluetoothScanListener scanListener);

    /**
     * 停止扫描设备
     */
    void doScan(boolean enable);


    /**
     * 向设备发送指令
     *
     * @param instructType
     */
    void sendInstruct(RCBluetoothGetDataListener getDataListener, String mac, int instructType);

    void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType);

    /**
     * 断开连接，释放资源
     */
    void doDisconnect();

    /**
     * 是否连接
     *
     * @return
     */
    boolean isConnect();


}
