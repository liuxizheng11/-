package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothBleUtils;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothScanUtils;
import com.rocedar.deviceplatform.device.bluetooth.impl.nianjia.NJSendCodeUtil;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothConnectListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/10/30 下午2:10
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBluetoothNianJiaImpl implements RCBlueTooth, BluetoothBleUtils.BluetoothDataListener, RCBluetoothConnectListener {


    private static String TAG = "RCDevice_NJ";


    private Context mContext;
    private Handler handler;


    private BluetoothBleUtils blueBleUtil;
    private BluetoothScanUtils scanUtils;

    private static RCBluetoothNianJiaImpl instance;

    public static RCBluetoothNianJiaImpl getInstance(Context context) {
        if (instance == null) {
            instance = new RCBluetoothNianJiaImpl(context.getApplicationContext());
        }
        return instance;
    }

    private RCBluetoothNianJiaImpl(Context context) {
        this.mContext = context;
        blueBleUtil = BluetoothBleUtils.getInstance(mContext);
        scanUtils = BluetoothScanUtils.getInstance(mContext);
        handler = new Handler();
    }


    @Override
    public void scanListener(RCBluetoothScanListener scanListener) {
        scanUtils.setScanListener(scanListener);
    }

    @Override
    public void doScan(boolean enable) {
        if (enable)
            scanUtils.setScanPeriodTime(30 * 1000);
        else
            scanUtils.setScanPeriodTime(0);
        scanUtils.scanLeDevice(enable);
    }


    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, String mac, int instructType) {
        doSenInstruct(getDataListener, instructType, mac, null);
    }

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType) {
        doSenInstruct(getDataListener, instructType, null, mac);
    }


    @Override
    public void doDisconnect() {
        RCToast.TestCenter(mContext, "调用方法，断开念家设备连接" + lastConnectMac);
        if (!lastConnectMac.equals("")) {
            blueBleUtil.onDestroy(lastConnectMac);
            scanUtils.removeScanListener(lastConnectMac);
            blueBleUtil.removeBluetoothDataListener(lastConnectMac);
        }
    }

    @Override
    public boolean isConnect() {
        return false;
    }



    /*-----------执行连接设备的操作---------------S--**/

    //上一次连接的设备MAC地址
    private String lastConnectMac = "";

    /*是否是第一次收到连接成功通知，蓝牙连接实现通过广播监听，一次连接成功后有可能收到多次连接成功的广播，
      该参数是用于标记一次连接时只执行一次后续操作(0为初始状态 1为正在连接 2为连接成功)*/
    private int connectStatus = 0;
    private long lastConnectTime = -1;

    //数据获取监听
    private RCBluetoothGetDataListener bluetoothGetDataListener;

    //连接时发送的指令（用于连接时临时存储）
    private int connectInstructTemp = -1;


    private void doSenInstruct(final RCBluetoothGetDataListener getDataListener, final int instructType
            , String macInfo, BluetoothDevice bluetoothDevice) {
        //需要连接的设备mac地址
        String mac;
        if (bluetoothDevice != null) {
            mac = bluetoothDevice.getAddress();
        } else {
            mac = macInfo;
        }
        if (mac == null || mac.equals("")) {
            getDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "设备信息异常");
            return;
        }
        //如果最后连的设备不为空，并且当前连接的设备和需要发送指令的设备不是同一个设备，并且最后连接的设备没有断开连接
        if (!lastConnectMac.equals("") && !lastConnectMac.equals(mac)) {
            if (blueBleUtil.isConnected(lastConnectMac)) {
                //移除数据监听
                blueBleUtil.removeBluetoothDataListener(lastConnectMac);
                //移除连接监听
                blueBleUtil.removeConnectListener(lastConnectMac);
                //断开设备连接
                blueBleUtil.onDestroy(lastConnectMac);
            } else {
                connectStatus = 0;
            }
        }

        /*-- 发送指令到设备 --*/
        this.bluetoothGetDataListener = getDataListener;
        //判断设备是否已经连接，如果已经连接，直接发送指令，如果没有连接，开始连接设备
        RCLog.i(TAG, "念家设备，准备发送指令，当前设备连接状态为：" + blueBleUtil.isConnected(mac));
        if (!blueBleUtil.isConnected(mac)) {
            //判断没有连接，先开始连接设备
            RCLog.i(TAG, "念家设备，设备没有连接，开始连接设备，当前连接状态为：%d\n(%s)"
                    , connectStatus, "0为空闲，1为连接中，2为连接成功");
            if (connectStatus == 2) {
                blueBleUtil.onDestroy(lastConnectMac);
            }
            if (connectStatus == 1) {
                //当前连接不为空闲时，如果距离上次连接小于30S不再进行连接，连接时间超过30s再次连接直接进行重试
                if (new Date().getTime() - lastConnectTime < 30 * 1000) {
                    return;
                } else {
                    getDataListener.getDataError(RCBluetoothError.ERROR_DEVICE_BUSY,
                            mContext.getString(R.string.rcdevice_error_connect_busy));
                }
            }
            //记录本次开始连接时间
            lastConnectTime = new Date().getTime();
            //标记正在连接设备
            connectStatus = 1;

            /*----开始连接设备----*/

            //设置最后连接的设备MAC地址（用于下次连接判断）
            lastConnectMac = mac;
            blueBleUtil.setBluetoothDataListener(this, mac);
            //设置连接监听
            connectInstructTemp = instructType;
            blueBleUtil.setConnectListener(this, mac);
            if (bluetoothDevice != null) {
                blueBleUtil.onConnect(bluetoothDevice, RCDeviceDeviceID.NJ_SH);
            } else {
                blueBleUtil.onConnect(mac, RCDeviceDeviceID.NJ_SH);
            }
        } else {
            //设备已经连接，发送数据
            if (blueBleUtil != null && bluetoothGetDataListener != null)
                blueBleUtil.doTypeInstruction(bluetoothGetDataListener, instructType, lastConnectMac);
        }
    }

    @Override
    public void connectStart() {

    }

    @Override
    public void connectOK() {
        //连接设备成功
        //blueBleUtil.setBluetoothDataListener(RCBlueToothBZLImpl.this, lastConnectMac);
        //收到连接成功后，标记连接状态为2，避免多次执行发送指令
        if (connectStatus != 2) {
            connectStatus = 2;
            //设备连接成功后，蓝牙通知监听到数据有延时，设置2s后通知连接成功
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RCLog.i(TAG, "念家设备连接成功,等待2s完成，开始同步设备时间，截获数据监听");
                    if (blueBleUtil != null && bluetoothGetDataListener != null)
                        blueBleUtil.doTypeInstruction(bluetoothGetDataListener,
                                RCBluetoothDoType.DO_SETTING_TIME, lastConnectMac);
                }
            }, 2000);
        }
    }


    @Override
    public void disconnect() {
        //设备断开连接
        connectStatus = 0;
        RCToast.TestCenter(mContext, "念家手环断开连接");
        bluetoothGetDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "连接失败");
    }

    @Override
    public void connectError(int status, String msg) {
        connectStatus = 0;
        bluetoothGetDataListener.getDataError(status, msg);
    }


    private JSONArray historyData;
    private int number = 1;

    @Override
    public void getData(String data, String mac) {
        if (data != null && !data.equals("")) {
            RCLog.i(TAG, "念家收到数据:" + data);
            //11 0A 0F 0B 02 2A
            if (data.replace(" ", "").startsWith(NJSendCodeUtil.codeNowTime())) {
                bluetoothGetDataListener.dataInfo(new JSONArray());
            } else {
                String[] datas = data.split(" ");
                if (datas.length == 14) {//今天数据
                    JSONArray array = new JSONArray();
                    RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
                    dto.setDeviceId(RCDeviceDeviceID.NJ_SH);
                    dto.setDate(Long.parseLong(DateUtil.getFormatToday()));
                    String step = datas[3] + datas[2] + datas[1] + datas[0];
                    String distance = datas[7] + datas[6] + datas[5] + datas[4];
                    String cal = datas[11] + datas[10] + datas[9] + datas[8];
                    dto.setStep(parseInt(step, 16));
                    dto.setKm(RCJavaUtil.formatBigDecimalUP(parseInt(distance, 16) / 1000.0f, 2));
                    dto.setCal(RCJavaUtil.formatBigDecimalUP(parseInt(cal, 16) / 1000.0f, 2));
                    array.put(dto.getJSON());
                    bluetoothGetDataListener.dataInfo(array);
                } else if (datas.length == 17) {
                    if (historyData == null) historyData = new JSONArray();
                    RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
                    dto.setDeviceId(RCDeviceDeviceID.NJ_SH);
                    dto.setDate("20" + parseInt(datas[0], 16) + parseInt(datas[1], 16) + parseInt(datas[2], 16) + "000000");
                    String step = datas[6] + datas[5] + datas[4] + datas[3];
                    String distance = datas[10] + datas[9] + datas[8] + datas[7];
                    String cal = datas[14] + datas[13] + datas[12] + datas[11];
                    dto.setStep(parseInt(step, 16));
                    dto.setKm(RCJavaUtil.formatBigDecimalUP(parseInt(distance, 16) / 1000.0f, 2));
                    dto.setCal(RCJavaUtil.formatBigDecimalUP(parseInt(cal, 16) / 1000.0f, 2));
                    historyData.put(dto.getJSON());
                    if (number <= 7) {
                        number++;
                        if (historyOutTimeRunnable != null)
                            handler.removeCallbacks(historyOutTimeRunnable);
                        handler.postDelayed(historyOutTimeRunnable = new HistoryOutTimeRunnable(number), 5000);
                    } else {
                        if (historyOutTimeRunnable != null)
                            handler.removeCallbacks(historyOutTimeRunnable);
                        bluetoothGetDataListener.dataInfo(historyData);
                        number = 1;
                        historyData = null;
                    }
                }
            }
        }
    }

    private HistoryOutTimeRunnable historyOutTimeRunnable;


    private class HistoryOutTimeRunnable implements Runnable {

        int tempNumber = -1;

        public HistoryOutTimeRunnable(int tempNumber) {
            this.tempNumber = tempNumber;
        }

        @Override
        public void run() {
            if (number != 0 && tempNumber == number) {
                if (bluetoothGetDataListener != null) {
                    if (historyData != null)
                        bluetoothGetDataListener.dataInfo(historyData);
                    else
                        bluetoothGetDataListener.dataInfo(new JSONArray());

                }
                number = 1;
                historyData = null;
            }
        }
    }


}
