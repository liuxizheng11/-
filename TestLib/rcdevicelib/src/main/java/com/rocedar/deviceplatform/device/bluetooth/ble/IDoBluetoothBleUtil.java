package com.rocedar.deviceplatform.device.bluetooth.ble;

import android.content.Intent;

import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/10/30 下午4:17
 * 版本：V2.2.00
 * 描述：不同设备在BLE连接成功后有不同的实现
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface IDoBluetoothBleUtil {


    /**
     * 发现服务后执行的方法
     */
    void servicesDiscovered(ServicesDiscoveredListener listener);

    /**
     * 执行指令
     *
     * @param doType
     */
    void doInstruction(RCBluetoothGetDataListener bluetoothGetDataListener, int doType);


    /**
     * 断开连接
     */
    void onDestroy();


    /**
     * 数据获取超时
     */
    void timeOut();

    /**
     * 写入数据成功
     */
    void writeOK();

    /**
     * Receive the data
     * 收到数据后是否有特殊处理，没有就发送给监听对象.
     *
     * @return true 发送数据 false 不发送数据，数据拦截
     */
    boolean receiveData(Intent intent);


    interface ServicesDiscoveredListener {

        void initOver(boolean isOK);

    }

}
