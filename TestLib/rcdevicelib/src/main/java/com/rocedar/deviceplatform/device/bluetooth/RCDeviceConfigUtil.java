package com.rocedar.deviceplatform.device.bluetooth;

import android.content.Context;

import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothLeService;
import com.rocedar.deviceplatform.device.bluetooth.ble.IDoBluetoothBleUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.DoBluetoothBleUtilBZLImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.DoBluetoothBleUtilNJImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.DoBluetoothBleUtilYDImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBlueToothBZLImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothBongImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothDuDoImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothHeHaQiImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothMJKImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothNianJiaImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothYDImpl;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/17 下午4:02
 * 版本：V1.0
 * 描述：设备扫描配对实现工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCDeviceConfigUtil {


    /**
     * 根据设备ID实例对应的实现类
     *
     * @param context
     * @param deviceId
     * @return
     */
    public static RCBlueTooth getBluetoothImpl(Context context, int deviceId) {
        switch (deviceId) {
            case RCDeviceDeviceID.BZL:
                return RCBlueToothBZLImpl.getInstance(context);
            case RCDeviceDeviceID.HEHAQI:
                return RCBluetoothHeHaQiImpl.getInstance(context);
            case RCDeviceDeviceID.YD:
                return RCBluetoothYDImpl.getInstance(context);
            case RCDeviceDeviceID.MJK_ANDROID:
                return RCBluetoothMJKImpl.getInstance(context);
            case RCDeviceDeviceID.HF_DUDO:
                return RCBluetoothDuDoImpl.getInstance(context);
            case RCDeviceDeviceID.BONG_2PH:
            case RCDeviceDeviceID.BONG_4:
            case RCDeviceDeviceID.BONG_FIT:
                return RCBluetoothBongImpl.getInstance(context, deviceId);
            case RCDeviceDeviceID.NJ_SH:
                return RCBluetoothNianJiaImpl.getInstance(context);
        }
        return null;
    }


    /**
     * 根据设备ID判断绑定设备时发送的指令
     *
     * @param deviceId
     * @return
     */
    public static int getConnectDeviceInstruct(int deviceId) {
        switch (deviceId) {
            case RCDeviceDeviceID.BZL:
                return RCBluetoothDataType.DATATYPE_STEP_TODAY;
            case RCDeviceDeviceID.HEHAQI:
                return RCBluetoothDataType.DATATYPE_STEP_TODAY;
            case RCDeviceDeviceID.YD:
                return RCBluetoothDataType.DATATYPE_STEP_TODAY;
            case RCDeviceDeviceID.MJK_ANDROID:
                return RCBluetoothDataType.DATATYPE_STEP_AND_RUN_NOW;
            case RCDeviceDeviceID.HF_DUDO:
                return RCBluetoothDataType.DATATYPE_STEP_TODAY;
            case RCDeviceDeviceID.BONG_2PH:
            case RCDeviceDeviceID.BONG_4:
            case RCDeviceDeviceID.BONG_FIT:
                return RCBluetoothDoType.DO_BINDING;
            case RCDeviceDeviceID.NJ_SH:
                return RCBluetoothDoType.DO_SETTING_TIME;
        }
        return -1;
    }


    /**
     * 根据mac地址，从本地存储的绑定设备列表获取到设备ID，找到对应BLE工具类实现类
     *
     * @param mac
     * @return
     */
    public static IDoBluetoothBleUtil getBleUtilFromMac(BluetoothLeService leService, String mac) {
        int deviceId = RCSPDeviceInfo.getBluetoothDeviceIdFormMac(mac);
        return getBleUtilFromDeviceID(leService, deviceId, mac);
    }


    public static IDoBluetoothBleUtil getBleUtilFromDeviceID(BluetoothLeService leService, int deviceId, String mac) {
        switch (deviceId) {
            case RCDeviceDeviceID.BZL:
                return DoBluetoothBleUtilBZLImpl.getInstance(leService, mac);
            case RCDeviceDeviceID.YD:
                return DoBluetoothBleUtilYDImpl.getInstance(leService, mac);
            case RCDeviceDeviceID.NJ_SH:
                return DoBluetoothBleUtilNJImpl.getInstance(leService, mac);
        }
        return null;
    }


}
