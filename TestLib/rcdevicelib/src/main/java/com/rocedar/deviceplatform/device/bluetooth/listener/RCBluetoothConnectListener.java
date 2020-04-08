package com.rocedar.deviceplatform.device.bluetooth.listener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/10 下午4:12
 * 版本：V1.0
 * 描述：蓝牙设备连接监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBluetoothConnectListener {

    void connectStart();

    void connectOK();

    void disconnect();

    void connectError(int status,String msg);

}
