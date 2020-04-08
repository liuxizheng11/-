package com.rocedar.deviceplatform.device.bluetooth.ble;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.device.bluetooth.BluetoothUtil;
import com.rocedar.deviceplatform.device.bluetooth.RCDeviceConfigUtil;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothConnectListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/12 下午2:52
 * 版本：V1.0
 * 描述：BLE蓝牙工具类
 * 在蓝牙设备（）数据
 * <p>
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BluetoothBleUtils {

    private static String TAG = "RCDevice_BLEUtils";

    private Context mContext;


    //单例对象
    private static BluetoothBleUtils ourInstance;


    //获得工具类的单例对象
    public static BluetoothBleUtils getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new BluetoothBleUtils(context);
        }
        return ourInstance;
    }

    private BluetoothBleUtils(Context context) {
        mContext = context.getApplicationContext();
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        initBLEService();
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }


    //-------------下面的是设备连接相关的

    private Intent gattServiceIntent;

    //蓝牙连接服务
    private BluetoothLeService mBluetoothLeService;

    /**
     * 初始化服务
     *
     * @return
     */
    private boolean initBLEService() {
        if (gattServiceIntent == null)
            gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
        return mContext.bindService(gattServiceIntent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    /**
     * 服务绑定监听
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {

            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
                    .getService();
            if (!mBluetoothLeService.initialize()) {
                RCLog.e(TAG, "BLE设备连接服务初始化失败");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    //设备连接监听
    private Map<String, RCBluetoothConnectListener> connectListenerList = new HashMap<>();


    /**
     * 设置设备连接监听
     *
     * @param connectListener 监听对象
     * @param mac             设备MAC地址
     */
    public void setConnectListener(RCBluetoothConnectListener connectListener, String mac) {
        RCLog.i(TAG, "设置%s连接监听", mac);
        if (!connectListenerList.containsKey(mac))
            connectListenerList.put(mac, connectListener);
    }

    /**
     * 移除设备连接监听
     *
     * @param mac
     */
    public void removeConnectListener(String mac) {
        if (connectListenerList.containsKey(mac))
            connectListenerList.remove(mac);
    }


    //设备连接状态，是否连接
    private Map<String, Boolean> mConnectedList = new HashMap<>();


    /**
     * 获取设备的连接状态
     *
     * @param mac 设备MAC地址
     * @return 是否已经连接
     */
    public boolean isConnected(String mac) {
        if (mConnectedList == null || !mConnectedList.containsKey(mac))
            return false;
        return mConnectedList.get(mac);
    }


    //设备mac地址和设备ID对应关系
    private Map<String, Integer> macDeviceIdMap = new HashMap<>();

    /**
     * 使用mac地址连接设备，先扫描设备。
     *
     * @param mac 设备MAC地址(用于服务尝试连接)
     */
    public void onConnect(final String mac, final int deviceId) {
        macDeviceIdMap.put(mac, deviceId);
        RCLog.i(TAG, "开始连接设备，需要连接的设备mac地址为：%s", mac);
        //使用mac地址连接需要先扫描设备以确保设备的有效性
        BluetoothScanUtils.getInstance(mContext).setScanListener(new RCBluetoothScanListener() {
            @Override
            public void scanOver() {
                //扫描完成-设备没有扫描到
                if (mac != null && !mac.equals("")) {
                    RCLog.d(TAG, "蓝牙：连接设备（" + mac + "）扫描不到需要连接的设备-找不到设备");
                    if (connectListenerList == null || !connectListenerList.containsKey(mac))
                        return;
                    connectListenerList.get(mac).connectError(RCBluetoothError.ERROR_CANNOT_FIND,
                            "找不到设备，请确认设备在手机附近");
                }
            }

            @Override
            public void scanStart() {
            }

            @Override
            public void scanInfo(BluetoothDevice device, int rssi) {
                RCLog.d(TAG, "蓝牙：连接设备（" + mac + "）扫描到设备-" + device.getAddress());
                if (device.getAddress().toUpperCase().equals(mac.toUpperCase())) {
                    //移除扫描监听
                    BluetoothScanUtils.getInstance(mContext).removeScanListener(mac);
                    RCLog.d(TAG, "蓝牙：连接设备（" + mac + "）扫描到需要连接的设备-开始连接");
                    onConnect(device, deviceId);
                }
            }

            @Override
            public void scanError(int status, String msg) {
                //移除扫描监听
                BluetoothScanUtils.getInstance(mContext).removeScanListener(mac);
            }
        }, mac);

    }


    /**
     * 扫描到设备BluetoothDevice对象或使用BluetoothDevice对象连接设备，不用再扫描设备
     *
     * @param bluetoothDevice
     */
    public void onConnect(BluetoothDevice bluetoothDevice, final int deviceId) {
        macDeviceIdMap.put(bluetoothDevice.getAddress(), deviceId);
        try {
            RCLog.i(TAG, "开始连接设备，直接连接的设备mac地址为：%s", bluetoothDevice.getAddress());
            String mac = bluetoothDevice.getAddress();
            //注册广播
            if (mBluetoothLeService != null) {
                connects(mac);
            } else {
                RCLog.d(TAG, "蓝牙：连接设备（" + mac + "）服务为NULL,重启服务");
                ConnectRunnable connectRunnable = new ConnectRunnable(mac);
                mHandler.postDelayed(connectRunnable, 3000);
            }
        } catch (Exception e) {
            onConnect(bluetoothDevice.getAddress(), deviceId);
        }

    }


    /**
     * 该线程用于连接设备时，服务初始化失败后重新初始化服务
     */
    private class ConnectRunnable implements Runnable {

        private int tryConnectNumber = 0;

        private String mac;

        public ConnectRunnable(String mac) {
            this.mac = mac;
        }

        @Override
        public void run() {
            if (mBluetoothLeService != null) {
                connects(mac);
            } else {
                if (tryConnectNumber > 5) {
                    RCLog.d(TAG, "蓝牙：重试5次，服务为NULL");
                    if (connectListenerList != null && connectListenerList.containsKey(mac)) {
                        connectListenerList.get(mac).connectError(RCBluetoothError.ERROR_CONNECT, "设备连接失败");
                    }
                    tryConnectNumber = 0;
                    return;
                }
                if (tryConnectNumber == 2) {
                    RCLog.d(TAG, "蓝牙：重试3次，服务为NULL，init");
                    initBLEService();
                }
                mHandler.postDelayed(this, 3000);
                tryConnectNumber++;
            }
        }
    }


    /**
     * 获取
     *
     * @param mac
     */
    private void connects(String mac) {
        if (!BluetoothUtil.checkBluetoothIsOpen()) {
            connectListenerList.get(mac).connectError(RCBluetoothError.ERROR_CONNECT, "蓝牙没有打开");
            return;
        }
        boolean result = mBluetoothLeService.connect(mac);
        RCLog.d(TAG, "蓝牙：开始连接，服务不为null，调用连接状态为：" + result);
        if (connectListenerList != null && connectListenerList.containsKey(mac)) {
            if (result) {
                //调用开始连接成功，通知监听者已经开始连接设备。调用后等广播通知连接状态
                connectListenerList.get(mac).connectStart();
            } else {
                //调用开始连接失败，通知监听者连接失败
                connectListenerList.get(mac).connectError(RCBluetoothError.ERROR_CONNECT, "设备连接失败");
            }
        }
    }


    //设备连接后不同设备有不同的处理，该接口用于不同设备定制不同的方法实现
    private Map<String, IDoBluetoothBleUtil> mIDoBluetoothBleUtil = new HashMap<>();

    public IDoBluetoothBleUtil getIDoBluetoothBleUtil(String mac) {
        if (mIDoBluetoothBleUtil != null && mIDoBluetoothBleUtil.containsKey(mac))
            return mIDoBluetoothBleUtil.get(mac);
        return null;
    }


    //设备连接广播接收器，
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            RCLog.d(TAG, "蓝牙：广播action----" + action);
            if (!intent.hasExtra(BluetoothLeService.EXTRA_DEVICE_MAC)) {
                return;
            }
            final String mac = intent.getStringExtra(BluetoothLeService.EXTRA_DEVICE_MAC);
            if (BluetoothLeService.getActionGattConnected().equals(action)) {
                RCLog.d(TAG, "蓝牙：广播收连接成功" + mac);
                //设置设备的连接状态为'已连接'
                mConnectedList.put(mac, true);
            } else if (BluetoothLeService.getActionGattDisconnected().equals(action)) {
                RCLog.d(TAG, "蓝牙：广播收到断开连接" + mac);
                //执行断开连接
                onDestroy(mac);

            } else if (BluetoothLeService.getActionGattServicesDiscovered()
                    .equals(action)) {
                if (mBluetoothLeService == null) {
                    //mBluetoothLeService，判定为设备连接失败
                    onDestroy(mac);
                    return;
                }
                RCLog.d(TAG, "蓝牙：广播收到发现服务" + mac);
                //发现服务成功,根据不同的实现类去实现不同的方法
                IDoBluetoothBleUtil temp = macDeviceIdMap.containsKey(mac) ?
                        RCDeviceConfigUtil.getBleUtilFromDeviceID(mBluetoothLeService, macDeviceIdMap.get(mac), mac)
                        : RCDeviceConfigUtil.getBleUtilFromMac(mBluetoothLeService, mac);
                if (temp != null) {
                    mIDoBluetoothBleUtil.put(mac, temp);
                }
                //连接成功后，不同的设备执行不同的方法
                if (mIDoBluetoothBleUtil != null && mIDoBluetoothBleUtil.containsKey(mac)) {
                    mIDoBluetoothBleUtil.get(mac).servicesDiscovered(new IDoBluetoothBleUtil.ServicesDiscoveredListener() {
                        @Override
                        public void initOver(boolean isOK) {
                            if (connectListenerList != null && connectListenerList.containsKey(mac)) {
                                if (isOK) {
                                    connectListenerList.get(mac).connectOK();
                                } else {
                                    connectListenerList.get(mac).connectError(RCBluetoothError.ERROR_CONNECT, "连接失败");
                                }
                            }
                        }
                    });
                } else {
                    //服务发现成功后,没有需要执行的操作，判定为设备连接成功
                    if (connectListenerList != null && connectListenerList.containsKey(mac)) {
                        connectListenerList.get(mac).connectOK();
                    }
                }
            } else if (BluetoothLeService.getActionDataAvailable().equals(action)) {
                RCLog.d(TAG, "蓝牙：广播收到数据" + mac);
                if (mIDoBluetoothBleUtil != null) {
                    if (!mIDoBluetoothBleUtil.get(mac).receiveData(intent)) {
                        return;
                    }
                }
                //收到通知变动时，发送消息
                Message message = new Message();
                message.what = GET_DATA_OK;
                message.obj = new String[]{mac, intent.getStringExtra(BluetoothLeService.EXTRA_DATA)};
                mHandler.sendMessage(message);
            } else if (BluetoothLeService.getActionWrite().equals(action)) {
                if (mIDoBluetoothBleUtil != null)
                    mIDoBluetoothBleUtil.get(mac).writeOK();
            }
        }
    };


    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.getActionGattConnected());
        intentFilter.addAction(BluetoothLeService.getActionGattDisconnected());
        intentFilter.addAction(BluetoothLeService.getActionGattServicesDiscovered());
        intentFilter.addAction(BluetoothLeService.getActionDataAvailable());
        return intentFilter;
    }


    /**
     * 断开连接
     */
    public void onDestroy(String mac) {
        //关闭通知接受
        if (mIDoBluetoothBleUtil != null && mIDoBluetoothBleUtil.containsKey(mac)) {
            mIDoBluetoothBleUtil.get(mac).onDestroy();
            mIDoBluetoothBleUtil.remove(mac);
        }
        //断开设备
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect(mac);
        }
        //设置设备的连接状态为'未连接'
        mConnectedList.put(mac, false);
        mConnectedList.remove(mac);
        //如果有监听者，通知监听者设备已经断开连接
        if (connectListenerList != null && connectListenerList.containsKey(mac)) {
            connectListenerList.get(mac).disconnect();
            connectListenerList.remove(mac);
        }
    }


    public void onDestroyAll() {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnectAll();
            mBluetoothLeService.stopSelf();
        }
    }

    /**
     * 执行指令
     *
     * @param bluetoothGetDataListener 设备实现类中获取数据监听
     * @param doType
     * @param mac
     */
    public void doTypeInstruction(
            RCBluetoothGetDataListener bluetoothGetDataListener, int doType, String mac) {
        if (mIDoBluetoothBleUtil != null && mIDoBluetoothBleUtil.containsKey(mac)) {
            if (bluetoothDataListenerList.containsKey(mac)) {
                mIDoBluetoothBleUtil.get(mac).doInstruction(bluetoothGetDataListener, doType);
            }
        }
    }

    //--------------  数据处理

    //消息标识-获取数据成功
    private final static int GET_DATA_OK = 31;

    //蓝牙数据获取监听
    private Map<String, BluetoothDataListener> bluetoothDataListenerList = new HashMap<>();

    /**
     * 设置一个数据监听者
     *
     * @param bluetoothDataListener
     * @param mac
     */
    public void setBluetoothDataListener(BluetoothDataListener bluetoothDataListener, String mac) {
        if (bluetoothDataListenerList.containsKey(mac)) return;
        bluetoothDataListenerList.put(mac, bluetoothDataListener);
    }

    /**
     * 移除一个数据监听者
     *
     * @param mac
     */
    public void removeBluetoothDataListener(String mac) {
        if (bluetoothDataListenerList.containsKey(mac))
            bluetoothDataListenerList.remove(mac);
    }

    //有些设备会返回多次数据，判断1秒间隔类上次数据和本次是否一致，相同不在向前发送。
    private String lastData = "";
    private long lastDataTime = -1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA_OK:
                    String[] strings = (String[]) msg.obj;
                    if (strings.length == 2) {
                        if (lastData.equals("") || new Date().getTime() - lastDataTime > 1000
                                || !lastData.equals(strings[1])) {
                            lastData = strings[1];
                            lastDataTime = new Date().getTime();
                            displayData(strings[1], strings[0]);
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 数据获取成功，向监听者发送数据
     *
     * @param data 数据类容
     * @param mac  设备的mac地址
     */
    private void displayData(String data, String mac) {
        RCLog.d(TAG, "蓝牙：接受到%s设备数据" + data, mac);
        if (data != null && bluetoothDataListenerList != null && bluetoothDataListenerList.containsKey(mac)) {
            bluetoothDataListenerList.get(mac).getData(data, mac);
        }
    }


    public interface BluetoothDataListener {
        void getData(String data, String mac);
    }

    /**
     * 获取数据超时处理
     *
     * @param mac
     */
    public void getDataOutTime(String mac) {
        if (mIDoBluetoothBleUtil != null && mIDoBluetoothBleUtil.containsKey(mac)) {
            mIDoBluetoothBleUtil.get(mac).timeOut();
        }
    }


}
