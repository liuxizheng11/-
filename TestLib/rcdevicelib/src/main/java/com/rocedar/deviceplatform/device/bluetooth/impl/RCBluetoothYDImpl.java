package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothBleUtils;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothScanUtils;
import com.rocedar.deviceplatform.device.bluetooth.impl.yd.Array;
import com.rocedar.deviceplatform.device.bluetooth.impl.yd.Funtion;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothConnectListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/16 下午8:36
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBluetoothYDImpl implements RCBlueTooth, BluetoothBleUtils.BluetoothDataListener
        , RCBluetoothConnectListener {

    private static String TAG = "RCDevice_YD";

    private int[] supportInstructType = {RCBluetoothDataType.DATATYPE_STEP_TODAY};


    private static RCBluetoothYDImpl ourInstance;

    private Context mContext;

    private Handler mHandler;

    private Funtion funtion;

    public static RCBluetoothYDImpl getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCBluetoothYDImpl(context);
        return ourInstance;
    }


    private BluetoothBleUtils bluetoothBleUtils;
    private BluetoothScanUtils scanUtils;

    private RCBluetoothYDImpl(Context context) {
        this.mContext = context;
        mHandler = new Handler();
        funtion = new Funtion();
        bluetoothBleUtils = BluetoothBleUtils.getInstance(mContext);
        scanUtils = BluetoothScanUtils.getInstance(mContext);

    }

    @Override
    public void scanListener(RCBluetoothScanListener scanListener) {
        scanUtils.setScanListener(scanListener);
    }

    @Override
    public void doScan(boolean enable) {
        if (enable)
            scanUtils.setScanPeriodTime(45 * 1000);
        else
            scanUtils.setScanPeriodTime(0);
        scanUtils.scanLeDevice(enable);
    }


    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType) {
        doSendIntruct(getDataListener, instructType, null, mac);
    }


    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, String mac, int instructType) {
        doSendIntruct(getDataListener, instructType, mac, null);

    }

    private RCBluetoothGetDataListener mGetDataListener;


    private boolean isGetDataIn = false;
    //最后发送的指令
    private int mInstructType;
    //最后连接设备的MAC地址
    private String lastConnectMac = "";

    private void doSendIntruct(RCBluetoothGetDataListener getDataListener,
                               int instructType, String macinfo, BluetoothDevice bluetoothDevice) {
        //判断设备是否支持该指令
        if (!binarySearch(supportInstructType, instructType)) {
            mGetDataListener.getDataError(RCBluetoothError.ERROR_PHONE_NOT_SUPPORT,
                    mContext.getString(R.string.rcdevice_error_not_support));
            RCLog.i(TAG, "缘渡不支持该协议，" + instructType);
            return;
        }
        if (isGetDataIn) {
            RCLog.i(TAG, "缘渡正在获取数据。" + instructType);
            mGetDataListener.getDataError(RCBluetoothError.ERROR_DEVICE_BUSY,
                    mContext.getString(R.string.rcdevice_error_busy));
            return;
        }
        this.mGetDataListener = getDataListener;
        String mac;
        if (bluetoothDevice != null) {
            mac = bluetoothDevice.getAddress();
        } else {
            mac = macinfo;
        }
        if (mac == null || mac.equals("")) {
            getDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "设备信息异常");
            return;
        }
        //如果最后连的设备不为空，并且当前连接的设备和需要发送指令的设备不是同一个设备，并且最后连接的设备没有断开连接
        if (!lastConnectMac.equals("") && !lastConnectMac.equals(mac) && bluetoothBleUtils.isConnected(lastConnectMac)) {
            //移除数据监听
            bluetoothBleUtils.removeBluetoothDataListener(lastConnectMac);
            //移除连接监听
            bluetoothBleUtils.removeConnectListener(lastConnectMac);
            //断开设备连接
            bluetoothBleUtils.onDestroy(lastConnectMac);
        }

        mInstructType = instructType;
        lastConnectMac = mac;
        /*----连接设备--*/
        isGetDataIn = true;

        //设备是否连接
        if (!bluetoothBleUtils.isConnected(mac)) {
            bluetoothBleUtils.setBluetoothDataListener(this, mac);

            bluetoothBleUtils.setConnectListener(this, mac);
            RCLog.d(TAG, "缘渡开始连接设备。" + mac);
            if (bluetoothDevice != null)
                bluetoothBleUtils.onConnect(bluetoothDevice, RCDeviceDeviceID.YD);
            else
                bluetoothBleUtils.onConnect(mac, RCDeviceDeviceID.YD);
        } else {
            if (bluetoothBleUtils.getIDoBluetoothBleUtil(mac) != null
                    && bluetoothBleUtils.getIDoBluetoothBleUtil(mac) instanceof DoBluetoothBleUtilYDImpl) {
                funtion.sendWriteData((DoBluetoothBleUtilYDImpl) bluetoothBleUtils.getIDoBluetoothBleUtil(mac));
            }
        }
        if (mInstructType != RCBluetoothDoType.DO_GET_REALTIME_RUN_START) {
            mHandler.postDelayed(timeOut, 60 * 1000);
        }
    }

    @Override
    public void connectStart() {
        isGetDataIn = true;
        RCLog.d(TAG, "缘渡开始连接设备。connectStart");
    }

    @Override
    public void connectOK() {
        RCLog.d(TAG, "缘渡连接成功");
    }

    @Override
    public void disconnect() {
        RCLog.d(TAG, "缘渡断开连接");
        if (isGetDataIn) {
            isGetDataIn = false;
            mGetDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "断开连接");
        }
        if (!lastConnectMac.equals("") && bluetoothBleUtils.isConnected(lastConnectMac)) {
            bluetoothBleUtils.onDestroy(lastConnectMac);
        }
    }

    @Override
    public void connectError(int status, String msg) {
        isGetDataIn = false;
        mGetDataListener.getDataError(status, msg);
    }


    private Runnable timeOut = new Runnable() {
        @Override
        public void run() {
            if (isGetDataIn) {
                isGetDataIn = false;
                if (mGetDataListener != null) {
                    mGetDataListener.getDataError(RCBluetoothError.ERROR_GET_DATA_TIME_OUT,
                            mContext.getString(R.string.rcdevice_error_timeout));
                }
            }
        }
    };


    @Override
    public void doDisconnect() {
        RCLog.i(TAG, "缘渡数据获取完成，开始解析数据");
        isGetDataIn = false;
        if (!lastConnectMac.equals("") && bluetoothBleUtils.isConnected(lastConnectMac)) {
            bluetoothBleUtils.onDestroy(lastConnectMac);
            lastConnectMac = "";
        }
//        parsingData();

    }

    @Override
    public boolean isConnect() {
        return isGetDataIn;
    }


    @Override
    public void getData(final String data, String mac) {
        RCLog.e(TAG, "缘渡获取到数据->" + data);
        if (bluetoothBleUtils.getIDoBluetoothBleUtil(mac) != null
                && bluetoothBleUtils.getIDoBluetoothBleUtil(mac) instanceof DoBluetoothBleUtilYDImpl) {
            funtion.setdata(data, (DoBluetoothBleUtilYDImpl) bluetoothBleUtils.getIDoBluetoothBleUtil(mac),
                    RCBluetoothYDImpl.this);
        }
    }

    private String lastData = "";

    public void parsingData() {
        JSONArray array = new JSONArray();
        RCDeviceStepDataDTO stepDataDTO = new RCDeviceStepDataDTO();
        switch (mInstructType) {
            case RCBluetoothDataType.DATATYPE_STEP_TODAY:
                stepDataDTO.setDeviceId(RCDeviceDeviceID.YD);
                stepDataDTO.setStep(Array.totaldata);
                stepDataDTO.setDate(DateUtil.getFormatNow("yyyyMMdd") + "000000");
                stepDataDTO.setCal(Array.totalcalorie);
                array.put(stepDataDTO.getJSON());
                break;
        }
        isGetDataIn = false;
        if (mGetDataListener != null) {
            if (lastData.equals("") || !array.toString().equals(lastData)) {
                mGetDataListener.dataInfo(array);
            }
        }
    }


    private boolean binarySearch(int[] s, int t) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] == t) {
                return true;
            }
        }
        return false;
    }
}
