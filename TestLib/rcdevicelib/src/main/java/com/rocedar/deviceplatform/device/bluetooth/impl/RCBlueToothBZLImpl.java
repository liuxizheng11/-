package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothBleUtils;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothScanUtils;
import com.rocedar.deviceplatform.device.bluetooth.impl.bzl.BZLResponseUtil;
import com.rocedar.deviceplatform.device.bluetooth.impl.bzl.BZLSendCodeUtil;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothConnectListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;

import org.json.JSONObject;

import java.util.Date;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/10 下午4:18
 * 版本：V1.0
 * 描述：博之轮B15P 设备管理实现类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBlueToothBZLImpl implements RCBlueTooth, RCBluetoothDataType, RCBluetoothDoType,
        BluetoothBleUtils.BluetoothDataListener, RCBluetoothConnectListener {

    private static String TAG = "RCDevice_BZL";


    private Context mContext;
    private Handler handler;


    private BluetoothBleUtils blueBleUtil;
    private BluetoothScanUtils scanUtils;


    private static RCBlueToothBZLImpl instance;

    public static RCBlueToothBZLImpl getInstance(Context context) {
        if (instance == null) {
            instance = new RCBlueToothBZLImpl(context.getApplicationContext());
        }
        return instance;
    }

    private RCBlueToothBZLImpl(Context context) {
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
    public void doScan(boolean en) {
        if (en)
            scanUtils.setScanPeriodTime(30 * 1000);
        else
            scanUtils.setScanPeriodTime(0);
        scanUtils.scanLeDevice(en);
    }


    @Override
    public void doDisconnect() {
        writeIn = false;
        RCToast.TestCenter(mContext, "调用方法，断开BZL设备连接" + lastConnectMac);
        if (!lastConnectMac.equals("")) {
            blueBleUtil.onDestroy(lastConnectMac);
            scanUtils.removeScanListener(lastConnectMac);
            blueBleUtil.removeBluetoothDataListener(lastConnectMac);
        }
    }

    @Override
    public boolean isConnect() {
        if (!lastConnectMac.equals("") && blueBleUtil.isConnected(lastConnectMac))
            return true;
        return false;
    }


    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType) {
        doSenInstruct(getDataListener, instructType, null, mac);
    }


    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, final String mac, final int instructType) {
        doSenInstruct(getDataListener, instructType, mac, null);
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
        //停止血压不用检查是否正在
        if (instructType == RCBluetoothDoType.DO_TEST_BLODD_PRESSURE_STOP) {
            writeIn = false;
        }
        if (writeIn) {
            getDataListener.getDataError(RCBluetoothError.ERROR_DEVICE_BUSY, "正在同步数据，请稍后");
            return;
        }
        /*--判断当前连接的设备的有效性(判断当前连接的设备和需要发送数据的设备是否是同一个设备)--*/

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
        RCLog.i(TAG, "博之轮设备，准备发送指令，当前设备连接状态为：" + blueBleUtil.isConnected(mac));
        if (!blueBleUtil.isConnected(mac)) {
            //判断没有连接，先开始连接设备
            RCLog.i(TAG, "博之轮设备，设备没有连接，开始连接设备，当前连接状态为：%d\n(%s)"
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
                blueBleUtil.onConnect(bluetoothDevice, RCDeviceDeviceID.BZL);
            } else {
                blueBleUtil.onConnect(mac, RCDeviceDeviceID.BZL);
            }
        } else {
            //设备已经连接，发送数据
            writeDate(instructType, mac);
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
                    RCLog.i(TAG, "博之轮设备连接成功,等待2s完成，开始同步设备时间，截获数据监听");
                    writeDate(RCBluetoothDoType.DO_SETTING_TIME, lastConnectMac);
                }
            }, 2000);
        }

    }

    @Override
    public void disconnect() {
        //设备断开连接
        connectStatus = 0;
        RCToast.TestCenter(mContext, "博之轮手环断开连接");
        writeIn = false;
        writeInfo = "";
        bluetoothGetDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "连接失败");
    }

    @Override
    public void connectError(int status, String msg) {
        //连接设备异常
        connectStatus = 0;
        bluetoothGetDataListener.getDataError(status, msg);
    }



    /*-----------执行连接设备的操作---------------E--**/


    //是否正在写入数据
    private boolean writeIn = false;
    //最后写入的的数据
    private String writeInfo = "";


    /**
     * 写入数据
     */
    private void writeDate(int doType, String mac) {
        int waitTime = BZLSendCodeUtil.getOutTimeFromDoType(doType);
        writeInfo = BZLSendCodeUtil.getCodeStringFromDoType(doType, mac);
        RCLog.i(TAG, "writeData: " + writeInfo.toString() + "<----->" + mac);
        handler.removeCallbacks(runnable);
        if (waitTime > 0) {
            writeIn = true;
            handler.postDelayed(runnable, waitTime);
        }
        if (blueBleUtil != null && bluetoothGetDataListener != null)
            blueBleUtil.doTypeInstruction(bluetoothGetDataListener, doType, mac);
    }


    /**
     * 判断超时线程
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final String stemp = writeInfo;
            RCLog.e(TAG, "数据获取超时" + writeIn + "<->" + stemp);
            if (writeIn) {
                writeIn = false;
                if (writeInfo.startsWith("D1")) {
                    RCLog.e(TAG, "数据获取超时，是获取心率");
                    if (BZLResponseUtil.getInstance().heartRateArray != null
                            && BZLResponseUtil.getInstance().heartRateArray.length() > 0) {
                        RCLog.e(TAG, "心率有数据没有上传（数据条数）：" +
                                BZLResponseUtil.getInstance().heartRateArray.length());
                        if (bluetoothGetDataListener != null)
                            bluetoothGetDataListener.dataInfo(BZLResponseUtil.getInstance().heartRateArray);
                        BZLResponseUtil.getInstance().initHeartRateValue();
                    } else {
                        if (bluetoothGetDataListener != null) {
                            bluetoothGetDataListener.getDataError(
                                    RCBluetoothError.ERROR_GET_DATA_TIME_OUT, "超时"
                            );
                            if (blueBleUtil != null)
                                blueBleUtil.getDataOutTime(lastConnectMac);
                        }
                    }
                } else {
                    if (stemp.equals(writeInfo))
                        if (bluetoothGetDataListener != null) {
                            bluetoothGetDataListener.getDataError(
                                    RCBluetoothError.ERROR_GET_DATA_TIME_OUT, "超时"
                            );
                            if (blueBleUtil != null)
                                blueBleUtil.getDataOutTime(lastConnectMac);
                        }
                }
                RCLog.e(TAG, "数据获取超时，设置初始化" + "<->" + stemp + "<->" + writeInfo);
                writeInfo = "";
            }
        }
    };


    //--------------------------发送指令后通过蓝牙通知监听的到数据处理----------------------


    /**
     * 从蓝牙通知获取到数据
     *
     * @param data
     */
    @Override
    public void getData(final String data, String mac) {
        if (!lastConnectMac.equals(mac)) return;
        if (!writeInfo.equals("") && writeInfo.length() > 2
                && writeInfo.substring(0, 2).equals(BZLSendCodeUtil.codeSettingTime().substring(0, 2))) {
            //设置时间成功，发送设置指令
            writeDate(RCBluetoothDoType.DO_SETTING_OPEN_HEART_TEST, lastConnectMac);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    writeDate(connectInstructTemp, lastConnectMac);
                }
            }, 1000);
        } else if (!writeInfo.equals("") && writeInfo.length() > 2
                && writeInfo.substring(0, 2).equals(BZLSendCodeUtil.codeSettingOpenHeart().substring(0, 2))) {
//            if (connectInstructTemp > 0) {
//
//            } else {
//                bluetoothGetDataListener.dataInfo(new JSONArray());
//            }
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RCLog.d(TAG, "博之轮手环数据: " + data);
                    if (data.startsWith("90 1E")) {
                        return;
                    }
                    JSONObject object = BZLResponseUtil.getInstance().parsingData(data, lastConnectMac);
                    if (object != null && bluetoothGetDataListener != null) {
                        if (data.length() > 2) {
                            RCLog.i(TAG, "数据获成功，设置初始化" + writeInfo);
                            writeIn = false;
                        }
                        if (object.has("status") && object.optInt("status") != 0) {
                            bluetoothGetDataListener.getDataError(object.optInt("status"), object.optString("msg"));
                        } else
                            bluetoothGetDataListener.dataInfo(object.optJSONArray("response"));
                        writeInfo = "";
                    }
                }
            });
        }
    }


}
