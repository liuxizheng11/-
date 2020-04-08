package com.rocedar.deviceplatform.device.bluetooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/10/30 下午4:19
 * 版本：V2.2.00
 * 描述：蓝牙扫描工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BluetoothScanUtils {


    private static String TAG = "RCDevice_BLEUtils_Scan";


    /*--扫描监听相关--*/
    private final static int LISTENER_ADD = 0;//添加扫描监听者
    private final static int LISTENER_DELETE = 1;//删除扫描监听者
    private final static int LISTENER_DATA = 2;//扫描操作通知
    private final static int LISTENER_DATA_START = 20;//开始扫描设备
    private final static int LISTENER_DATA_STOP = 21;//停止扫描设备
    private final static int LISTENER_DATA_GET_OK = 22;//扫描到设备数据
    private final static int LISTENER_DATA_GET_ERROE = 23;//获取数据出错


    // 搜索时间.0为一直搜索
    private long SCAN_PERIOD = 0;

    //是否正在扫描
    private boolean isScanIn = false;

    private Context mContext;

    private BluetoothAdapter mBluetoothAdapter;

    //存储扫描监听者
    private Map<String, RCBluetoothScanListener> scanListenerList = new HashMap<>();

    private static BluetoothScanUtils ourInstance;

    public static BluetoothScanUtils getInstance(Context mContext) {
        if (ourInstance == null) {
            ourInstance = new BluetoothScanUtils(mContext);
        }
        return ourInstance;
    }

    private BluetoothScanUtils(Context context) {
        mContext = context.getApplicationContext();
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) mContext.
                getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }


    /**
     * 开启蓝牙
     */
    private void openBluetooth() {
        // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LISTENER_ADD:
                    ScanListenerDTO listenerDTO = (ScanListenerDTO) msg.obj;
                    if (!listenerDTO.tag.equals(""))
                        scanListenerList.put(listenerDTO.tag, listenerDTO.scanListener);
                    scanLeDevice(true);
                    break;
                case LISTENER_DATA:
                    for (String tag : scanListenerList.keySet()) {
                        switch (msg.arg1) {
                            case LISTENER_DATA_START:
                                scanListenerList.get(tag).scanStart();
                                break;
                            case LISTENER_DATA_STOP:
                                scanListenerList.get(tag).scanOver();
                                break;
                            case LISTENER_DATA_GET_OK:
//                                RCLog.i(TAG,
//                                        "扫描到设备：\nMAC地址：" + ((BluetoothDevice) msg.obj).getAddress()
//                                                + "\n设备名称" + ((BluetoothDevice) msg.obj).getName()
//                                                + "\n发送给TAG：" + tag);
                                scanListenerList.get(tag).scanInfo(
                                        (BluetoothDevice) msg.obj, msg.arg2
                                );
                                break;
                            case LISTENER_DATA_GET_ERROE:
                                scanListenerList.get(tag).scanError(msg.arg2, (String) msg.obj);
                                break;
                        }
                    }
                    break;
                case LISTENER_DELETE:
                    if (scanListenerList.containsKey(msg.obj)) {
                        scanListenerList.remove(msg.obj);
                    }
                    if (scanListenerList.size() == 0) {
                        scanLeDevice(false);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };





    /**
     * 设置扫描监听
     *
     * @param scanListener
     * @param tag
     */
    public void setScanListener(RCBluetoothScanListener scanListener, String tag) {
        Message message = new Message();
        message.what = LISTENER_ADD;
        message.obj = new ScanListenerDTO(scanListener, tag);
        mHandler.sendMessage(message);

    }

    /**
     * 设置扫描监听（默认标签）
     *
     * @param scanListener
     */
    public void setScanListener(RCBluetoothScanListener scanListener) {
        setScanListener(scanListener, "scan");
    }

    /**
     * 移除扫描监听（默认标签，用于绑定时搜索）
     */
    public void removeScanListener() {
        removeScanListener("scan");
    }

    /**
     * 移除扫描监听（不同的设备标签，用于后台线程搜索，需要区分不同的设备）
     *
     * @param tag
     */
    public void removeScanListener(String tag) {
        Message message = new Message();
        message.what = LISTENER_DELETE;
        message.obj = tag;
        mHandler.sendMessage(message);
    }

    /**
     * 设置超时时间
     *
     * @param time 超时时间
     */
    public void setScanPeriodTime(int time) {
        SCAN_PERIOD = time;
    }


    /**
     * 扫描到设备数据对象
     */
    private class ScanListenerDTO {
        RCBluetoothScanListener scanListener;
        String tag;

        public ScanListenerDTO(RCBluetoothScanListener scanListener, String tag) {
            this.scanListener = scanListener;
            this.tag = tag;
        }
    }


    /**
     * 开始或结束扫描设备
     *
     * @param enable true开始扫描，false结束扫描
     */
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            if (isScanIn) return;
            isScanIn = true;
            if (SCAN_PERIOD > 0) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = LISTENER_DATA;
                        message.arg1 = LISTENER_DATA_STOP;
                        mHandler.sendMessage(message);
                        isScanIn = false;
                        if (mBluetoothAdapter != null)
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }, SCAN_PERIOD);
            }
            if (mBluetoothAdapter != null) {
                Message message = new Message();
                message.what = LISTENER_DATA;
                message.arg1 = LISTENER_DATA_START;
                mHandler.sendMessage(message);
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                Message message = new Message();
                message.what = LISTENER_DATA;
                message.arg1 = LISTENER_DATA_STOP;
                mHandler.sendMessage(message);
                isScanIn = false;
            }

        } else {
            isScanIn = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


    /**
     * 设备扫描监听（扫描到设备）
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback
            = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            //扫描到设备，发送消息通知监听者获取设备信息
            if (device != null && device.getAddress() != null && device.getName() != null) {
                Message message = new Message();
                message.what = LISTENER_DATA;
                message.arg1 = LISTENER_DATA_GET_OK;
                message.arg2 = rssi;
                message.obj = device;
                mHandler.sendMessage(message);
            }
        }
    };


}
