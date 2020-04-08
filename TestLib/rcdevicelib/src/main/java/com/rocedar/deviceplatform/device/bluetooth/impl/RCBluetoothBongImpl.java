package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.rocedar.base.RCDeveloperConfig;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothScanUtils;
import com.rocedar.deviceplatform.device.bluetooth.impl.bong.BongBraceletUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.bong.BongFitUtil;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;

import java.util.HashMap;
import java.util.Map;

import cn.ginshell.sdk.BongSdk;
import cn.ginshell.sdk.model.Gender;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/6/21 下午3:40
 * 版本：V1.0.05
 * 描述：bong设备,支持多个设备
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBluetoothBongImpl implements RCBlueTooth {


    private static String TAG = "RCDevice_Bong";


    //设备的单例
    private static Map<Integer, RCBluetoothBongImpl> bongDeviceInstances;


    /**
     * ps:bong设备有多个设备同时连接的需求（手环设备和体脂秤设备）
     * 使用时根据不同的设备创建不同的单例来同时连接多个设备
     */
    public static RCBluetoothBongImpl getInstance(Context context, int deviceId) {
        if (bongDeviceInstances == null) {
            RCLog.i(TAG, "初始化BONG SDK");
            // debug模式
            BongSdk.enableDebug(RCDeveloperConfig.isDebug);
            // 设置用户身体信息
            BongSdk.setUser(RCSPBaseInfo.getLastUserBaseInfoStature(),
                    RCSPBaseInfo.getLastUserBaseInfoWeight(), Gender.MALE);
            //初始化sdk
            BongSdk.initSdk((Application) context.getApplicationContext());
            bongDeviceInstances = new HashMap<>();
        }
        //如果设备的单例已经创建，返回单例对象
        if (bongDeviceInstances.containsKey(deviceId)) {
            return bongDeviceInstances.get(deviceId);
        }
        //没有设备的单例创建单例对象
        bongDeviceInstances.put(deviceId, new RCBluetoothBongImpl(context.getApplicationContext(), deviceId));
        return bongDeviceInstances.get(deviceId);
    }


    private Context mContext;
    private RCHandler handler;
    //扫描工具类
    private BluetoothScanUtils scanUtils;

    //设备ID
    private int mDeviceId;

    private RCBluetoothBongImpl(Context context, int deviceId) {
        this.mContext = context.getApplicationContext();
        this.mDeviceId = deviceId;
        scanUtils = BluetoothScanUtils.getInstance(mContext);
        handler = new RCHandler(context.getApplicationContext());
    }

    @Override
    public void scanListener(RCBluetoothScanListener scanListener) {
        scanUtils.setScanListener(scanListener);
    }

    @Override
    public void doScan(boolean en) {
        if (en)
            scanUtils.setScanPeriodTime(30 * 1000);
        else
            scanUtils.setScanPeriodTime(0);
        scanUtils.scanLeDevice(en);
    }


    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, String mac, int instructType) {
        doInstructTypeFirst(mac, instructType, getDataListener);
    }

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType) {
        doInstructTypeFirst(mac.getAddress(), instructType, getDataListener);
    }


    //手环工具类
    private BongBraceletUtil bongBraceletUtil;
    //体脂工具类
    private BongFitUtil bongFitUtil;

    public BongBraceletUtil getBongBraceletUtil() {
        return bongBraceletUtil;
    }

    public BongFitUtil getBongFitUtil() {
        return bongFitUtil;
    }

    private void doInstructTypeFirst(String mac, int instructType, RCBluetoothGetDataListener getDataListener) {
        switch (mDeviceId) {
            case RCDeviceDeviceID.BONG_2PH:
            case RCDeviceDeviceID.BONG_4:
                if (bongBraceletUtil == null)
                    bongBraceletUtil = new BongBraceletUtil(this, mContext, mDeviceId);
                bongBraceletUtil.doInstructTypeFirst(mac, instructType, getDataListener);
                break;
            case RCDeviceDeviceID.BONG_FIT:
                if (bongFitUtil == null)
                    bongFitUtil = new BongFitUtil(mContext);
                bongFitUtil.doInstructTypeFirst(mac, instructType, getDataListener);
                break;
        }


    }

    @Override
    public void doDisconnect() {
        if (bongBraceletUtil != null) {
            bongBraceletUtil.unConnect("doDisconnect");
        }
        if (bongFitUtil != null) {
            bongFitUtil.unConnect("doDisconnect");
        }
    }


    @Override
    public boolean isConnect() {
        if (bongBraceletUtil != null) {
            return bongBraceletUtil.isConnect();
        }
        if (bongFitUtil != null) {
            return bongFitUtil.isConnect();
        }
        return false;
    }


}
