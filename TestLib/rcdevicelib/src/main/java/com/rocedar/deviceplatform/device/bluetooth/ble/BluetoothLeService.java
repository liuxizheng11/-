package com.rocedar.deviceplatform.device.bluetooth.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.rocedar.base.RCLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class BluetoothLeService extends Service {

    private static String TAG = "RCDevice_SERVICE";

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disconnectAll();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }


    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                RCLog.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            RCLog.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }


    public static String EXTRA_DATA = "EXTRA_DATA";
    public static String EXTRA_DEVICE_MAC = "EXTRA_DEVICE_MAC";
    public static String EXTRA_DEVICE_TYPE = "EXTRA_DEVICE_DATA_TYPE";
    public static int EXTRA_DEVICE_TYPE_VAULE_R = 0;
    public static int EXTRA_DEVICE_TYPE_VAULE_N = 1;

    public static String getActionGattConnected() {
        return "com.rc.bt.ACTION_GATT_CONNECTED";
    }

    public static String getActionGattDisconnected() {
        return "com.rc.bt.ACTION_GATT_DISCONNECTED";
    }

    public static String getActionGattServicesDiscovered() {
        return "com.rc.bt.ACTION_GATT_SERVICES_DISCOVERED";
    }

    public static String getActionDataAvailable() {
        return "com.rc.bt.ACTION_DATA_AVAILABLE";
    }

    public static String getActionWrite() {
        return "com.rc.bt.ACTION_GATT_SERVICES_WRITE";
    }


    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private Map<String, BluetoothGatt> mBluetoothGattList = new HashMap<>();

    private BluetoothGattCallback getBTGattCallback(final String mac) {
        BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                int newState) {
                String intentAction;
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    intentAction = getActionGattConnected();
                    mConnectionState = STATE_CONNECTED;
                    broadcastUpdate(intentAction, mac);
                    if (mBluetoothGattList.containsKey(mac)
                            && mBluetoothGattList.get(mac) != null) {
                        mBluetoothGattList.get(mac).discoverServices();
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    intentAction = getActionGattDisconnected();
                    mConnectionState = STATE_DISCONNECTED;
                    broadcastUpdate(intentAction, mac);
                    if (mBluetoothGattList.containsKey(mac)) {
                        mBluetoothGattList.remove(mac);
                    }
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    broadcastUpdate(getActionGattServicesDiscovered(), mac);
                } else {
                    RCLog.i(TAG, "onServicesDiscovered received: " + status);
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt,
                                             BluetoothGattCharacteristic characteristic, int status) {
                RCLog.e(TAG, "数据来源onCharacteristicRead");
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    broadcastUpdate(getActionDataAvailable(), mac, EXTRA_DEVICE_TYPE_VAULE_R, characteristic);
                }
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt,
                                          BluetoothGattDescriptor descriptor, int status) {
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt,
                                                BluetoothGattCharacteristic characteristic) {
                RCLog.d(TAG, "数据来源onCharacteristicChanged");
                broadcastUpdate(getActionDataAvailable(), mac, EXTRA_DEVICE_TYPE_VAULE_N, characteristic);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            }

            public void onCharacteristicWrite(BluetoothGatt gatt,
                                              BluetoothGattCharacteristic characteristic, int status) {
                RCLog.e(TAG, "onCharacteristicWrite:--------write success----- status:" + status);
                broadcastUpdate(getActionWrite(), mac);
            }
        };
        return mGattCallback;
    }


    private void broadcastUpdate(final String action, final String mac) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DEVICE_MAC, mac);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final String mac, final int type,
                                 final BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        // For all other profiles, writes the data formatted in HEX.
        byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            StringBuilder stringBuilder = new StringBuilder(
                    data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            RCLog.e(TAG, "蓝牙：广播收到数据收到数据，数据为：" + stringBuilder.toString());
            intent.putExtra(EXTRA_DATA, stringBuilder.toString());
            intent.putExtra(EXTRA_DEVICE_MAC, mac);
            intent.putExtra(EXTRA_DEVICE_TYPE, type);
            sendBroadcast(intent);
        }
    }


    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            RCLog.e(TAG, "蓝牙没有初始化");
            return false;
        }
        if (mBluetoothGattList.containsKey(address)
                && mBluetoothGattList.get(address) != null) {
            mBluetoothGattList.get(address).close();
//            RCLog.e(TAG, "蓝牙设备已经连接" + address);
//            if (mBluetoothGattList.get(address).connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//                return false;
//            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            RCLog.e(TAG, "蓝牙Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
        mBluetoothGattList.put(address,
                device.connectGatt(this, false, getBTGattCallback(address)));
        RCLog.d(TAG, "创建一个新的设备连接");
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect(String mac) {
        if (!mBluetoothGattList.containsKey(mac) || mBluetoothGattList.get(mac) == null) {
            RCLog.i(TAG, "断开连接，蓝牙适配器为null或BluetoothGatt为null");
            return;
        }
        if (mBluetoothGattList.get(mac) != null)
            mBluetoothGattList.get(mac).disconnect();
        if (mBluetoothGattList.get(mac) != null)
            mBluetoothGattList.get(mac).close();
        if (mBluetoothGattList.get(mac) != null)
            mBluetoothGattList.remove(mac);
    }

    public void disconnectAll() {
        if (mBluetoothAdapter == null || mBluetoothGattList == null) {
            RCLog.i(TAG, "断开连接，蓝牙适配器为null或BluetoothGatt为null");
            return;
        }
        for (String s : mBluetoothGattList.keySet()) {
            mBluetoothGattList.get(s).disconnect();
            mBluetoothGattList.get(s).close();
        }
    }


    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic, String mac) {
        if (mBluetoothAdapter == null || !mBluetoothGattList.containsKey(mac) || mBluetoothGattList.get(mac) == null) {
            RCLog.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGattList.get(mac).writeCharacteristic(characteristic);
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic, String mac) {
        if (mBluetoothAdapter == null || !mBluetoothGattList.containsKey(mac) || mBluetoothGattList.get(mac) == null) {
            RCLog.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGattList.get(mac).readCharacteristic(characteristic);
    }

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public boolean setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled, String mac) {
        if (characteristic == null) {
            disconnect(mac);
            return false;
        }
        RCLog.d(TAG, "蓝牙：设置通知开始");
        RCLog.d(TAG, "蓝牙：设置通知开始" + characteristic.getUuid().toString());
        List<BluetoothGattDescriptor> gattDescriptors = characteristic.getDescriptors();
        RCLog.d(TAG, "蓝牙：设置通知开始+descriptor?" + gattDescriptors.size());
        for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
            RCLog.d(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
            byte[] desData = gattDescriptor.getValue();
            if (desData != null && desData.length > 0) {
                RCLog.d(TAG, "-------->desc value:" + new String(desData));
            }
        }

        if (mBluetoothAdapter == null || !mBluetoothGattList.containsKey(mac) || mBluetoothGattList.get(mac) == null) {
            RCLog.e(TAG, "蓝牙：BluetoothAdapter没有初始化");
            return false;
        }
        mBluetoothGattList.get(mac).setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString(CLIENT_CHARACTERISTIC_CONFIG));
        RCLog.d(TAG, "蓝牙：设置通知开始+descriptor?" + CLIENT_CHARACTERISTIC_CONFIG);

        if (gattDescriptors.size() == 0) {
            return false;
        }

        RCLog.d(TAG, "蓝牙：设置通知开始+descriptor?" + (descriptor != null));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGattList.get(mac).writeDescriptor(descriptor);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices(String mac) {
        if (!mBluetoothGattList.containsKey(mac) || mBluetoothGattList.get(mac) == null)
            return null;

        return mBluetoothGattList.get(mac).getServices();
    }

    /**
     * Read the RSSI for a connected remote device.
     */
    public boolean getRssiVal(String mac) {
        if (!mBluetoothGattList.containsKey(mac) || mBluetoothGattList.get(mac) == null)
            return false;

        return mBluetoothGattList.get(mac).readRemoteRssi();
    }
}
