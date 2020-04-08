package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;

import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.SPDeviceDataMJK;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothScanUtils;
import com.rocedar.deviceplatform.device.bluetooth.impl.mjk.BluetoothMJKReceiver;
import com.rocedar.deviceplatform.device.bluetooth.impl.mjk.BluetoothMJKService;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceRunDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/8 上午11:06
 * 版本：V1.0
 * 描述：摩集客耳机获取数据的实现类，特别说明以下几点
 * 1.摩集客耳机的数据没有区分日期，以设备传感器计数累积数值（复位时会归0）
 * 2.摩集客耳机的数据有走路、跑步、走路时间（秒）、跑步时间（秒）
 * 数据包长度 16 字节
 * 数据包格式: 0x25 0xA4 AA BB CC DD EE FF GG HH II JJ KK LL CS 1C S0 数据包以 0x25 0xA4 开头
 * AA BB CC DD 为走步步数，AA 为高字节，DD 为低字节
 * EE FF GG HH 为跑步步数，EE 为高字节，HH 为低字节
 * II JJ 为走步时间，II 为高字节，JJ 为低字节，单位为秒
 * KK LL 为跑步时间，KK 为高字节，LL 为低字节，单位为秒
 * CS1 CS0 为校验和，CS1 为高字节，CS0 为低字节
 * CS1 CS0 = AA+BB+CC+DD+EE+FF+GG+HH+II+JJ+KK+LL
 * 3.摩集客耳机
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBluetoothMJKImpl implements RCBlueTooth, BluetoothMJKReceiver.IBroadcastData {

    private String TAG = "RCDevice_MJK";

    private static RCBluetoothMJKImpl ourInstance;

    public static RCBluetoothMJKImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCBluetoothMJKImpl(context);
        return ourInstance;
    }


    private Context mContext;

    private Handler handler;

    private BluetoothScanUtils scanUtils;

    private RCBluetoothMJKImpl(Context context) {
        this.mContext = context.getApplicationContext();
        handler = new Handler();
        scanUtils = BluetoothScanUtils.getInstance(mContext);

    }

    private BluetoothMJKService btService;
    private BluetoothMJKReceiver bluetoothMJKReceiver;

    @Override
    public void scanListener(RCBluetoothScanListener scanListener) {
        scanUtils.setScanListener(scanListener);

    }

    @Override
    public void doScan(boolean enable) {
        if (enable)
            scanUtils.setScanPeriodTime(10 * 1000);
        else
            scanUtils.setScanPeriodTime(0);
        scanUtils.scanLeDevice(enable);
    }


    private RCBluetoothGetDataListener mGetDataListener;

    private int instructType;

    private String connectMac;

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, String mac, int instructType) {
        this.mGetDataListener = getDataListener;
        this.instructType = instructType;
        this.connectMac = mac;
        switch (instructType) {
            case RCBluetoothDataType.DATATYPE_STEP_AND_RUN_NOW:
            case RCBluetoothDoType.DO_GET_REALTIME_STEP_START:
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_START:
            case RCBluetoothDoType.DO_GET_REALTIME_STRP_AND_RUN_START:
                connect(mac);
                break;
            case RCBluetoothDoType.DO_GET_REALTIME_STEP_STOP:
            case RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP:
                mGetDataListener = null;
                break;
            default:
                getDataListener.getDataError(RCBluetoothError.ERROR_PHONE_NOT_SUPPORT, "设备不支持该功能");
                break;
        }
    }

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType) {
        sendInstruct(getDataListener, mac.getAddress(), instructType);
    }

    @Override
    public void doDisconnect() {
        try {
            if (bluetoothMJKReceiver != null && mContext != null) {
                mContext.unregisterReceiver(bluetoothMJKReceiver);

            }
            if (btService != null) {
                btService.stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGetDataListener = null;
    }

    private boolean isConnect = false;

    public boolean isConnect() {
        return isConnect;
    }

    private void connect(String mac) {
        mGetDataListener.getDataStart();
        if (writeIn) {
            return;
        }
        writeIn = true;
        handler.postDelayed(runnable, 30 * 1000);
        if (isConnect) {
            return;
        }
        if (btService == null) {
            btService = new BluetoothMJKService(mContext);
        }
        if (bluetoothMJKReceiver == null) {
            bluetoothMJKReceiver = new BluetoothMJKReceiver(this);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(btService.EXTRA_DATA);
        mContext.registerReceiver(bluetoothMJKReceiver, intentFilter);
        btService.connect(mac);
    }


    //----------------从设备获取到数据--------------------------


    //最后获取数据的时间，如果时间大于5S，认定设备为断开连接，发送连接断开
    private long lastDataTime = -1;

    @Override
    public void getData(String data) {
        if (!TextUtils.isEmpty(data)) {
            isConnect = true;
            lastDataTime = new Date().getTime();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isConnect) {
                        if (lastDataTime > -1 && new Date().getTime() - lastDataTime > 4 * 1000) {
                            isConnect = false;
                            RCToast.Center(mContext, "摩集客耳机已断开连接");
                            if (mGetDataListener != null) {
                                mGetDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "摩集客耳机断开连接");
                            }
                        }
                    }
                }
            }, 5000);
            JSONArray array = parsingData(data);
            if (array != null && mGetDataListener != null) {
                writeIn = false;
                mGetDataListener.dataInfo(array);
                switch (instructType) {
                    case RCBluetoothDataType.DATATYPE_STEP_AND_RUN_NOW:
                        mGetDataListener = null;
                        break;
                    case RCBluetoothDoType.DO_GET_REALTIME_STEP_STOP:
                        mGetDataListener = null;
                        break;
                    case RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP:
                        mGetDataListener = null;
                        break;
                }
            }
        }
    }

    private List<String> dataList;

    private JSONArray parsingData(String data) {
        RCLog.i(TAG, "从摩集客耳机获取到数据：%s", data);
        if (data.startsWith("25")) {
            dataList = new ArrayList<>();
        }
        if (dataList == null) return null;
        String temp[] = data.split(" ");
        for (int i = 0; i < temp.length; i++) {
            dataList.add(temp[i]);
        }
        if (dataList.size() > 16) {
            RCLog.i(TAG, "从摩集客耳机获取到数据存储集合大于16，重置");
            dataList = null;
            return null;
        }
        if (dataList.size() == 16) {
            RCLog.i(TAG, "从摩集客耳机获取到数据存储集合长度校验成功");
            RCLog.i(TAG, "从摩集客耳机获取到数据存储集合第一位数据为%s,第二位为%s",
                    dataList.get(0), dataList.get(1));
            if (dataList.get(0).equals("25") &&
                    dataList.get(1).equals("A4")) {
                RCLog.i(TAG, "从摩集客耳机获取到数据存储集合数据头校验成功");
                int tempsign = 0;
                for (int i = 2; i < 14; i++) {
                    tempsign += Integer.parseInt(dataList.get(i), 16);
                }
                RCLog.i(TAG, "从摩集客耳机获取到数据存储集获取的校验码为：" + Integer.parseInt(
                        dataList.get(14) + dataList.get(15), 16));
                RCLog.i(TAG, "从摩集客耳机获取到数据存储集计算的校验码为：" + tempsign);
                if (tempsign == Integer.parseInt(dataList.get(14) + dataList.get(15), 16)) {
                    RCLog.i(TAG, "从摩集客耳机获取到数据存储集合数据校验成功");
                    int step = Integer.parseInt(
                            dataList.get(2) + dataList.get(3) + dataList.get(4) + dataList.get(5), 16);
                    int time = Integer.parseInt(dataList.get(10) + dataList.get(11), 16);
                    int run_step = Integer.parseInt(dataList.get(6) + dataList.get(7)
                            + dataList.get(8) + dataList.get(9), 16);
                    int run_time = Integer.parseInt(dataList.get(12) + dataList.get(13), 16);
                    SPDeviceDataMJK.saveMJKStepInfo(mContext, step, time, run_step, run_time, connectMac);
                    JSONArray array = new JSONArray();
                    RCDeviceRunDataDTO runDTO = new RCDeviceRunDataDTO();
                    runDTO.setDeviceId(RCDeviceDeviceID.MJK_ANDROID);
                    runDTO.setDate(Long.parseLong(DateUtil.getFormatNow("yyyyMMdd") + "000000"));
                    runDTO.setStep(SPDeviceDataMJK.getMJKTodayData(mContext,
                            SPDeviceDataMJK.TODAY_RUN_STEP, connectMac));
                    runDTO.setTime(SPDeviceDataMJK.getMJKTodayData(mContext,
                            SPDeviceDataMJK.TODAY_RUN_TIME, connectMac) / 60 + 1);
                    RCDeviceStepDataDTO stepDTO = new RCDeviceStepDataDTO();
                    stepDTO.setDeviceId(RCDeviceDeviceID.MJK_ANDROID);
                    stepDTO.setDate(Long.parseLong(DateUtil.getFormatNow("yyyyMMdd") + "000000"));
                    stepDTO.setStep(SPDeviceDataMJK.getMJKTodayData(mContext,
                            SPDeviceDataMJK.TODAY_STEP, connectMac));
                    stepDTO.setTime(SPDeviceDataMJK.getMJKTodayData(mContext,
                            SPDeviceDataMJK.TODAY_STEP_TIME, connectMac) / 60 + 1);
                    switch (instructType) {
                        case RCBluetoothDataType.DATATYPE_STEP_AND_RUN_NOW:
                        case RCBluetoothDoType.DO_GET_REALTIME_STRP_AND_RUN_START:
                            array.put(runDTO.getJSON());
                            array.put(stepDTO.getJSON());
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_STEP_START:
                            array.put(stepDTO.getJSON());
                            break;
                        case RCBluetoothDoType.DO_GET_REALTIME_RUN_START:
                            array.put(runDTO.getJSON());
                            break;
                    }
                    return array;
                }
            }
        }
        return null;
    }


    @Override
    public void error(int status, String msg) {
        isConnect = false;
        RCLog.i(TAG, "摩集客耳机连接错误,错误码：%d，错误描述%s", status, msg);
        if (mGetDataListener != null)
            mGetDataListener.getDataError(status, msg);
    }

    private boolean writeIn = false;


    /**
     * 判断超时线程
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (writeIn) {
                writeIn = false;
                if (mGetDataListener != null)
                    mGetDataListener.getDataError(
                            RCBluetoothError.ERROR_GET_DATA_TIME_OUT, "超时"
                    );
            }
        }
    };
}
