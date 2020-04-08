package com.heha.mitacsdk;
/**
 * Created by jeff_leung on 8/9/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.mitac.ble.MitacApi;
import com.mitac.ble.MitacEKGData;
import com.mitac.callback.MitacEKGCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;


public class MitacManager {
    private static String SDKVersion = "1.0.44";
    private MitacEventListener.QiStatus status = MitacEventListener.QiStatus.QI_STATUS_NOT_AVAILABLE;
    private boolean mScanning;
    private static Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    public String deviceInSearch = "";
    public final static String QI_SERVICE = "0a0aee03-0a00-1000-8000-00805f9b34fb";
    public final static String CONTROL_POINT_CHAR = "0a0ae00a-0a00-1000-8000-00805f9b34fb";
    // Remark : sleep and step share same callback, but out of scope at this moment
    //  public final static String SLEEP_CHAR = "0a0ae00b-0a00-1000-8000-00805f9b34fb"; //
    public final static String EKG_CHAR = "0a0ae00c-0a00-1000-8000-00805f9b34fb";
    public final static String REALTIME_STEP_CHAR = "0a0ae00d-0a00-1000-8000-00805f9b34fb";
    public final static String HISTORY_CHAR = "0a0ae00b-0a00-1000-8000-00805f9b34fb";
    public final static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    private final static String EOF = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
    public static Context _context;
    private final static String TAG = MitacManager.class.getSimpleName();
    private static BluetoothManager mBluetoothManager;
    private static BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private MitacEventListener mitacListener;
    private int mConnectionState = STATE_DISCONNECTED;
    private static MitacManager instance;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_SCANNING = 3;
    private static int[] vv = new int[9];
    private BluetoothGattCharacteristic mCommandCharacteristic;
    private BluetoothGattCharacteristic mRealtimeStepCharacteristic;
    private BluetoothGattCharacteristic mHistoryCharacteristics;
    private BluetoothGattCharacteristic mEKGCharacteristics;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    public final static UUID UUID_CONTROL_CHAR =
            UUID.fromString(CONTROL_POINT_CHAR);

    private byte[] stephistoryBuffer = new byte[120];
    //private Map<Integer, byte[]> stepHistoryBuffer = null;
    private Map<Integer, byte[]> sleepHistoryBuffer = null;
    private ArrayList<SleepHistory> sleepRecords = null;
    private MitacApi oldAPI;
    private OTAStatus mOTAStatus;
    private boolean isOTAMode = false;
    private int EKGCounter = 0;
    private Handler timerHandler = new Handler();
    private Date EKGStartTime;
    private long EKGTimeout = 20000; // millisecond
    private byte[] mFWData;
    private int mCurrentPacketNumber = 0;
    private int mPacketNumber = 0;
    private int mLastDataByteNumber = 0;
    private boolean isSetTimeResetCount = false;

    public enum OTAStatus {
        EOTA_NONE,
        EOTA_START,
        EOTA_INIT,
        EOTA_REBOOT
    }

    public enum RAMSIZE {
        RAMSIZE_16K,
        RAMSIZE_32K
    }


    public enum DISTANCE_UNIT {
        DISTANCE_UNIT_KM,
        DISTANCE_UNIT_MI
    }

    public enum BATTERY_LEVEL {
        QI_BATTERY_UNKNOWN,
        QI_BATTERY_LV_0_25,
        QI_BATTERY_LV_25_50,
        QI_BATTERY_LV_50_75,
        QI_BATTERY_LV_75_100
    }


    public enum WRISTBAND_ERROR {
        WRISTBAND_ERROR_FEATURE_NOT_READY, // connected, but command not ready
        WRISTBAND_ERROR_NOT_CONNECTED,
        WRISTBAND_ERROR_DEVICE_NOT_SUPPORT,
        WRISTBAND_ERROR_DEVICE_DISABLED,
        WRISTBAND_ERROR_DEVICE_NOT_PERMITTED,
        WRISTBAND_ERROR_HANDSHAKE_FAIL,
        WRISTBAND_ERROR_UPDATE_DATA_FAIL, // characteristic update value error
        WRISTBAND_ERROR_SCAN_FAIL // fail during scan

    }


    private final BroadcastReceiver adapterReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // It means the user has changed his bluetooth state.
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)  == BluetoothAdapter.STATE_TURNING_OFF) {
                    // The user bluetooth is turning off yet, but it is not disabled yet.
                    Log.e("ble", "turning off");
                    //
                    return;
                }

                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)  == BluetoothAdapter.STATE_OFF) {
                    Log.e("ble", "disabled");
                 //   stopScan();
                    mitacListener.onDisconnect();
                    mBluetoothAdapter = null;
                    mBluetoothGatt = null;
                 //   _context.unregisterReceiver(adapterReceiver);
                    return;
                }


                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)  == BluetoothAdapter.STATE_TURNING_ON) {
                    // The user bluetooth is turning off yet, but it is not disabled yet.
                    Log.e("ble", "turning on");
                    //
                    return;
                }



                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)  == BluetoothAdapter.STATE_ON) {
                    // The user bluetooth is already disabled.
                    Log.e("ble", "on");
                    mBluetoothAdapter = ((BluetoothManager)_context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
                //    mitacListener.onBluetoothAdapterUpdated(((BluetoothManager)_context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter());
              //      _context.registerReceiver(adapterReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
                    startScan(0);
                    return;
                }

            }
        }
    };

    public static final String getSDKVersion() {
        return SDKVersion;
    }


    static final public class AlarmSetting {
        // hour in 24 hour mode

        public boolean weekdayNapAlarmEnabled;
        public int weekdayNapAlarmHour;
        public int weekdayNapAlarmMinute;
        public boolean weekdayAlarmEnabled;
        public int weekdayAlarmHour;
        public int weekdayAlarmMinute;
        public boolean weekendNapAlarmEnabled;
        public int weekendNapAlarmHour;
        public int weekendNapAlarmMinute;
        public boolean weekendAlarmEnabled;
        public int weekendAlarmHour;
        public int weekendAlarmMinute;

    }

    static final public class userProfile {
        public int age;
        public boolean isMale;
        public float height;
        public float weight;
    }

    private MitacManager() {

    }


    public void setStatus(MitacEventListener.QiStatus newStatus) {
        this.status = newStatus;
    }


    public void emptyFlashData() {

        byte[] cmd = {0x18};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_EMPTY_FLASH_DATA);
    }

    public void getDate() {
        byte[] value = {0x0f, 0x19};
        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_DATE);
    }


    public boolean setDate(Date date, TimeZone timezone, boolean bIs12hrMode, boolean bIsReset) {
        isSetTimeResetCount = bIsReset;
        int sinceSec = (int) (date.getTime() / 1000L - MitacAttributes.TimeIntervalSince1970);
        int offsetSec = timezone.getOffset(date.getTime()) / 1000;
        byte byte12hrMode = (byte) (bIs12hrMode ? 0 : 1);
        byte byteReset = (byte) (bIsReset ? 1 : 0);

        byte[] sinceBytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(sinceSec).array();
        byte[] offsetBytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(offsetSec).array();
        byte[] value = {0x07, sinceBytes[0], sinceBytes[1], sinceBytes[2], sinceBytes[3],
                offsetBytes[0], offsetBytes[1], offsetBytes[2], offsetBytes[3],
                byte12hrMode, byteReset};

        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_SET_DATE);
        return true;
    }


    public boolean setProfile(userProfile profile) {

        setStatus(MitacEventListener.QiStatus.QI_STATUS_SET_PROFILE);
        int iAge = profile.age;
        float fHeight = profile.height;
        float fWeight = profile.weight;
        boolean isMale = profile.isMale;

        if (iAge > 99 || fHeight < 0 || fHeight > 300 || fWeight < 0 || fWeight > 999) {
            return false;
        }

        byte[] startValue = {0x10, 0x0f};
        writeCharacteristic(mCommandCharacteristic, startValue);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// referenced from R11 SDK
//        byte[] height = ByteBuffer.allocate(4).putFloat(fHeight).array();
//        byte[] weight = ByteBuffer.allocate(4).putFloat(fWeight).array();
//        byte[] value = { 16, 4,
//                (byte)iAge, bIsMale ? 1 : 0, height[0], height[1], height[2], height[3], weight[0], weight[1], weight[2], weight[3] };
//        writeCharacteristic(value);
//

        byte[] height = ByteBuffer.allocate(4).putFloat(fHeight).array();
        byte[] weight = ByteBuffer.allocate(4).putFloat(fWeight).array();

        //  Log.d(TAG, "height input:" + bytesToHex(height));

        // TODO: see whether must require float value

        byte[] value = {0x10, 0x04,
                (byte) iAge, (byte) (isMale ? 1 : 0), height[0], height[1], height[2], height[3], weight[0], weight[1], weight[2], weight[3]};
        writeCharacteristic(mCommandCharacteristic, value);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] endValue = {0x10, 0x10};
        writeCharacteristic(mCommandCharacteristic, endValue);
        return true;
    }

    public void getProfile() {
        byte[] value = {0x0f, 0x12};
        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_PROFILE);
    }

    public void getFactoryUUID() {
        byte[] cmd = {0x0f, 0x02};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_FACTORY_UUID);
    }


    public void getMacAddress() {
        byte[] cmd = {0x0f, 0x03};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_MAC_ADDRESS);
    }

    public boolean setGoal(int iActivityTime, int iSteps, int iDistance, int iCalories) {
        setStatus(MitacEventListener.QiStatus.QI_STATUS_SET_GOAL);

        if (iActivityTime < 0 || iSteps < 0 || iDistance < 0 || iCalories < 0) {
            return false;
        }

        byte[] startValue = {0x10, 0x0f};
        writeCharacteristic(mCommandCharacteristic, startValue);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] activityTime = ByteBuffer.allocate(4).putInt(iActivityTime).array();
        byte[] step = ByteBuffer.allocate(4).putInt(iSteps).array();
        byte[] distance = ByteBuffer.allocate(4).putInt(iDistance).array();
        byte[] calories = ByteBuffer.allocate(4).putInt(iCalories).array();
        byte[] value = {0x10, 0x05,
                step[0], step[1], step[2], step[3],
                calories[0], calories[1], calories[2], calories[3],
                distance[0], distance[1], distance[2], distance[3],
                activityTime[0], activityTime[1], activityTime[2], activityTime[3]};
        writeCharacteristic(mCommandCharacteristic, value);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] endValue = {0x10, 0x10};
        writeCharacteristic(mCommandCharacteristic, endValue);

        return true;
    }

    public void getFirmwareVersion() {
        Log.d("firmware", "get firmware version");
        byte[] value = {0x0f, 0x01};
        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_FIRMWARE_VERSION);
    }

    public void getGoal() {
        byte[] value = {0x0f, 19};
        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_GOAL);

    }


    public void getAlarm(boolean isWakeupAlarm) {

        byte t;
        if (isWakeupAlarm) {
            t = 0x1e;
        } else {
            t = 0x1f;
        }
        byte[] value = {0x0f, t};
        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_ALARM);

    }

    public boolean setAlarm(boolean isWakeupAlarm, AlarmSetting setting)

    {
        setStatus(MitacEventListener.QiStatus.QI_STATUS_SET_ALARM);

        if (setting == null) {

            return false;
        }

        if (setting.weekdayNapAlarmHour < 0 ||
                setting.weekdayNapAlarmHour >= 60 ||
                setting.weekdayNapAlarmMinute < 0 ||
                setting.weekdayNapAlarmMinute >= 60 ||
                setting.weekdayAlarmHour < 0 ||
                setting.weekdayAlarmHour >= 60 ||
                setting.weekdayAlarmMinute < 0 ||
                setting.weekdayAlarmMinute >= 60 ||
                setting.weekendNapAlarmHour < 0 ||
                setting.weekendNapAlarmHour >= 60 ||
                setting.weekendNapAlarmMinute < 0 ||
                setting.weekendNapAlarmMinute >= 60 ||
                setting.weekendAlarmHour < 0 ||
                setting.weekendAlarmHour >= 60 ||
                setting.weekendAlarmMinute < 0 ||
                setting.weekendAlarmMinute >= 60
                ) {
            return false;
        }

        byte[] startValue = {0x10, 0x0f};
        writeCharacteristic(mCommandCharacteristic, startValue);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] value = {0x10, (byte) (isWakeupAlarm ? 0x0A : 0x0B),
                (byte) (setting.weekdayNapAlarmEnabled ? 1 : 0), (byte) setting.weekdayNapAlarmHour, (byte) setting.weekdayNapAlarmMinute,
                (byte) (setting.weekdayAlarmEnabled ? 1 : 0), (byte) setting.weekdayAlarmHour, (byte) setting.weekdayAlarmMinute,
                (byte) (setting.weekendNapAlarmEnabled ? 1 : 0), (byte) setting.weekendNapAlarmHour, (byte) setting.weekendNapAlarmMinute,
                (byte) (setting.weekendAlarmEnabled ? 1 : 0), (byte) setting.weekendAlarmHour, (byte) setting.weekendAlarmMinute,
                0x00, 0x00, 0x00};

        writeCharacteristic(mCommandCharacteristic, value);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] endValue = {0x10, 0x10};
        writeCharacteristic(mCommandCharacteristic, endValue);

        return true;
    }


    public void startSleepMonitor() {
        byte[] cmd = {0x16};


        writeCharacteristic(mCommandCharacteristic, cmd);

        setStatus(MitacEventListener.QiStatus.QI_STATUS_START_SLEEP_MONITOR);
    }

    public void stopSleepMonitor(boolean bStart) {
        byte[] cmd = {0x17};


        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_STOP_SLEEP_MONITOR);

    }

    public void getBatteryLevel() {
        byte[] value = {0x0f, 0x17};
        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_BATTERY_LVL);
    }

    public void getSleepStatus() {
        byte[] cmd = {0x19};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_SLEEP_STATUS);
    }

    public void getRAMSize() {
        byte[] cmd = {0x1A};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_RAMSIZE);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // timer logic here
            Date currentTime = new Date();
            if ((currentTime.getTime() - EKGStartTime.getTime()) > EKGTimeout) {
                stopEKG();
                //  stopEKGMeasure();
                //  stopEKGTimer();
            } else {
                timerHandler.postDelayed(this, 500);
            }
        }

    };


    private void startEKGTimer() {
        EKGStartTime = new Date();
        stopEKGTimer();
        timerHandler.postDelayed(runnable, 500);
    }


    private void stopEKGTimer() {

        timerHandler.removeCallbacks(runnable);
    }

    public static abstract interface EKGDataCallback {
        public abstract void onEKGDataReceived(int[] paramArrayOfInt, Error paramError);
    }

    /**
     * parse service data
     */
    public void ScanForServices() {
        processGattServices(getSupportedGattServices());
    }

    /**
     * Get the instance of this singleton
     *
     * @param context Context to contain this manager
     * @return
     */
    public static MitacManager getInstance(Context context) {

        _context = context;

        if (instance == null) {
            instance = new MitacManager();

            mHandler = new Handler(context.getMainLooper());

            if (!instance.checkBluetoothAvailability()) {
                return null;
            }

            final BluetoothManager bluetoothManager =  (BluetoothManager) _context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter != null){

          //     _context.registerReceiver(adapterReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            }

        }
        return instance;
    }

    private void startAdapterReceiver(){
            _context.registerReceiver(adapterReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }


    private boolean checkBluetoothAvailability() {
        if (!_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            // BLE not supported
            Log.e(TAG, "Ble not supported");
       //     mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_SUPPORT);
            return false;
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) _context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
       //     mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_SUPPORT);

            return false;
        }

        startAdapterReceiver();
        return true;
    }

    private void resetSleepHistoryData() {
        sleepHistoryBuffer = new HashMap<Integer, byte[]>();
        sleepRecords = new ArrayList<SleepHistory>();
    }

    /**
     * Clear 7 days step data history
     */
    private void resetStepHistoryData() {
        //stepHistoryBuffer = new HashMap<Integer, byte[]>();

        stephistoryBuffer = new byte[0];
    }

    /**
     * Parse  service provided by Qi ,
     *
     * @param gattServices
     */
    private void processGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        mRealtimeStepCharacteristic = null;
        mCommandCharacteristic = null;

        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();


            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();


                // subscribe characteristic notification as callback
                if (
                    // uuid.equals(SampleGattAttributes.CONTROL_POINT_CHAR)||
                        uuid.equals(REALTIME_STEP_CHAR)
                        ) {
                    if (gattCharacteristic != null)
                        mRealtimeStepCharacteristic = gattCharacteristic;

                }

                if (uuid.equals(EKG_CHAR)) {
                    if (gattCharacteristic != null)
                        mEKGCharacteristics = gattCharacteristic;

                }


                if (uuid.equals(CONTROL_POINT_CHAR)) {
                    if (gattCharacteristic != null)
                        mCommandCharacteristic = gattCharacteristic;

                    mCommandCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);

                }

                if (uuid.equals(HISTORY_CHAR)) {
                    if (gattCharacteristic != null)
                        mHistoryCharacteristics = gattCharacteristic;
                }


            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);

            if (mRealtimeStepCharacteristic != null) {
                mitacListener.onRealtimeStepFeatureDiscovered();

            }

            if (mCommandCharacteristic != null) {
                mitacListener.onCommandPointFeatureDiscovered();
                subscribeCommandPoint();
            }

            if (mHistoryCharacteristics != null) {
                mitacListener.onHistoryFeatureDiscovered();
            }

            if (mEKGCharacteristics != null) {
                mitacListener.onEKGFeatureDiscovered();
            }


        }


    }


    public void startSleepData() {
        resetSleepHistoryData();

        //   byte[] cmd = {0x03,0x00};
        byte[] cmd = {0x03, 0x42, 0x45, 0x54, 0x41, 0x53, 0x4c, 0x45, 0x45, 0x50, 0x00};
        writeCharacteristic(mCommandCharacteristic, cmd);
        this.status = MitacEventListener.QiStatus.QI_STATUS_REQUEST_SLEEP_HISTORY;

    }


    public void continueSleepData() {
        byte[] cmd = {0x04, 0x00};
        //  byte[] cmd = {0x04, 0x42, 0x45, 0x54, 0x41, 0x53, 0x4c, 0x45, 0x45, 0x50};
        writeCharacteristic(mCommandCharacteristic, cmd);


    }


    public void stopSleepData() {

        // this is for T26 firmware
        byte[] cmd = {0x06, 0x00};
        //byte[] cmd = {0x06, 0x42, 0x45, 0x54, 0x41, 0x53, 0x4c, 0x45, 0x45, 0x50};
        writeCharacteristic(mCommandCharacteristic, cmd);

// this is for R26 firmware
        byte[] cmd0 = {0x05, 0x00, 0x00, 0x00, 0x00, 0x00};
        //byte[] cmd = {0x06, 0x42, 0x45, 0x54, 0x41, 0x53, 0x4c, 0x45, 0x45, 0x50};
        writeCharacteristic(mCommandCharacteristic, cmd0);
        mitacListener.onSleepDataStop();
        setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);

    }

    /**
     * Start Getting 7 Days step data
     */
    public void startGet7daysStep() {
        resetStepHistoryData();
        byte[] cmd_start_7days_step = {0x13, 0x00};
        //  byte[] cmd_start_7days_step = {0x13, 0x42, 0x45, 0x54, 0x41, 0x53, 0x4c, 0x45, 0x45, 0x50};
        writeCharacteristic(mCommandCharacteristic, cmd_start_7days_step);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_REQUEST_STEP_HISTORY);

    }

    public void continueGet7daysStep() {
        byte[] cmd_continue_7days_step = {0x14, 0x00};
        //    byte[] cmd_continue_7days_step = {0x14, 0x42, 0x45, 0x54, 0x41, 0x53, 0x4c, 0x45, 0x45, 0x50};
        writeCharacteristic(mCommandCharacteristic, cmd_continue_7days_step);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_REQUEST_STEP_HISTORY);
    }

    /**
     * Manually Stop the Data transfer of 7 days step data
     */
    public void stopGet7daysStep() {
        resetStepHistoryData();

        byte[] cmd_stop_7days_step = {0x15, 0x00, 0x00, 0x00, 0x00};
        //   byte[] cmd_stop_7days_step = {0x15, 0x42, 0x45, 0x54, 0x41, 0x53, 0x4c, 0x45, 0x45, 0x50};
        writeCharacteristic(mCommandCharacteristic, cmd_stop_7days_step);
        mitacListener.on7daysStepDataStop();
        setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
    }


    public void startEKGMeasure(int type) {
        EKGCounter = 0;
        // just use oldAPI for parsing ekg data
        oldAPI = MitacApi.getSharedInstance(_context);
        oldAPI.restartEKGAlgorithm();
        byte[] value = new byte[2];
        value[0] = 8;
        if (type == 0) {
            value[1] = 0;
        } else if (type == 2) {
            value[1] = 1;
        }
        writeCharacteristic(mCommandCharacteristic, value);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_START_EKG_MEASURE);

    }


    public void stopEKGMeasure() {

        mitacListener.onEKGStop();
        stopEKGTimer();
    }

    //for only MITAC API
    //private int EKG_RUNMETHOD_HR = 0; // Measure
    //private int EKG_RUNMETHOD_UNKNOWN = 1;
    //private int EKG_RUNMETHOD_CPC = 2;


    // timeout must be < 200
    public void startHRVEkg(int age, long timeout) {

        if (age < 0 || timeout > 150000) {

            mitacListener.onError(MitacEventListener.QiStatus.QI_STATUS_START_HRV_EKG, MitacEventListener.MitacError.MITACERROR_EKG_PARAM);
            //  setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            return;
        }


        //   if (processCallBack == null || finalCallBack == null) {
        //      return;
        // }
        setStatus(MitacEventListener.QiStatus.QI_STATUS_START_HRV_EKG);
        EKGTimeout = timeout;
        oldAPI = MitacApi.getSharedInstance(_context);
        startEKGMeasure(com.mitac.ble.MitacAttributes.EEKG_TYPE.EKG_RUNMETHOD_HR);
        oldAPI.startEKG(age, 1, com.mitac.ble.MitacAttributes.EEKG_TYPE.EKG_RUNMETHOD_HR, com.mitac.ble.MitacAttributes.EEKG_TRAINING_LEVEL.EKG_TRAINING_LEVEL_1, new MitacEKGCallback() {
            @Override
            public void didEKGDataFinalAnalysis(final com.mitac.ble.MitacEKGData ekgData, final Error error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stopEKGMeasure();
                        if (error != null) {
                            //  finalCallBack.setErrorMsg(error.getMessage());
                            //  finalCallBack.callback(WristabndException.ERROR_CODE_BACKCALL_ERROR, null);
                            Log.e(TAG, error.getMessage());
                            //  log.error(error.getMessage(), error);
                            mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_EKG_ERROR_RESULT);
                            // setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                            // return;
                        }
                        MitacHRVEKG mitacHRVEKG = null;
                        if (ekgData != null) {
                            mitacHRVEKG = new MitacHRVEKG();

                            mitacHRVEKG.setAnsAge(ekgData.mANSAge);
                            mitacHRVEKG.setBalance(ekgData.mBalance);
                            mitacHRVEKG.setEnergy(ekgData.mEnergy);
                            mitacHRVEKG.setHeartRate(ekgData.mHeartRate);
                            mitacHRVEKG.setLeadoff(ekgData.mIsLeadoff);
                            mitacHRVEKG.setStress(ekgData.mStress);
                            mitacHRVEKG.setSuccess(ekgData.mIsSuccess);
                            mitacHRVEKG.setRrInterval(ekgData.mrrInterval);
                            mitacHRVEKG.setFinalRRInterval(ekgData.mFinalRRInterval);
                            mitacHRVEKG.setQi(ekgData.mQI);
                        }

                        //  finalCallBack.callback(0, mitacHRVEKG);
                        Log.i(TAG, "final mrr:" + ekgData.mFinalRRInterval.length);

                        String rrStr = "";

                        for (int i = 0; i < ekgData.mFinalRRInterval.length; i++) {
                            rrStr += String.valueOf(ekgData.mFinalRRInterval[i]);
                        }
                        Log.d(TAG, rrStr);
                        mitacListener.onFinalHRVEKGReceived(mitacHRVEKG);
                        setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                    }
                });
            }

            @Override
            public void didEKGDataReceived(final com.mitac.ble.MitacEKGData ekgData, final Error error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (error != null) {
                            //     processCallBack.setErrorMsg(error.getMessage());
                            //      processCallBack.callback(WristabndException.ERROR_CODE_BACKCALL_ERROR, null);
                            //   log.error(error.getMessage(), error);
                            Log.e(TAG, error.getMessage());
                            stopEKGMeasure();
                            mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_EKG_ERROR_RESULT);
                            //  setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                            //  return;
                        }
                        MitacHRVEKG mitacHRVEKG = null;
                        if (ekgData != null) {
                            Log.i(TAG, String.format("EKG Data=> Leadoff:%s, Interval: %d, Heart Rate: %d",
                                    ekgData.mIsLeadoff, ekgData.mrrInterval, ekgData.mHeartRate));

                            mitacHRVEKG = new MitacHRVEKG();
                            mitacHRVEKG.setAnsAge(ekgData.mANSAge);
                            mitacHRVEKG.setBalance(ekgData.mBalance);
                            mitacHRVEKG.setEnergy(ekgData.mEnergy);
                            mitacHRVEKG.setHeartRate(ekgData.mHeartRate);
                            mitacHRVEKG.setLeadoff(ekgData.mIsLeadoff);
                            mitacHRVEKG.setStress(ekgData.mStress);
                            mitacHRVEKG.setSuccess(ekgData.mIsSuccess);
                            mitacHRVEKG.setRrInterval(ekgData.mrrInterval);
                            mitacHRVEKG.setFinalRRInterval(ekgData.mFinalRRInterval);
                            mitacHRVEKG.setQi(ekgData.mQI);
                        }
                        if (EKGCounter == 0) {
                            // start time only when first data arrive
                            startEKGTimer();
                        }
                        EKGCounter++;
                        mitacListener.onRawHRVEKGReceived(mitacHRVEKG);

                    }
                });
            }

        });
    }

    /// timeout must < 200
    public void startCPCEkg(int age, MitacCPCEKG.TrainingType type, long timeout) {


        if (age < 0 || timeout > 210000) {

            mitacListener.onError(MitacEventListener.QiStatus.QI_STATUS_START_CPC_EKG, MitacEventListener.MitacError.MITACERROR_EKG_PARAM);
            setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            return;
        }


        EKGTimeout = timeout;
        startEKGMeasure(com.mitac.ble.MitacAttributes.EEKG_TYPE.EKG_RUNMETHOD_CPC);
        oldAPI = MitacApi.getSharedInstance(_context);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_START_CPC_EKG);

        int level = com.mitac.ble.MitacAttributes.EEKG_TRAINING_LEVEL.EKG_TRAINING_LEVEL_3;
        if (type != null && type == MitacCPCEKG.TrainingType.TRAINING_LEVEL_6) {
            level = com.mitac.ble.MitacAttributes.EEKG_TRAINING_LEVEL.EKG_TRAINING_LEVEL_3;
        } else if (type != null && type == MitacCPCEKG.TrainingType.TRAINING_LEVEL_7) {
            level = com.mitac.ble.MitacAttributes.EEKG_TRAINING_LEVEL.EKG_TRAINING_LEVEL_2;
        } else if (type != null && type == MitacCPCEKG.TrainingType.TRAINING_LEVEL_8) {
            level = com.mitac.ble.MitacAttributes.EEKG_TRAINING_LEVEL.EKG_TRAINING_LEVEL_1;
        }

        oldAPI.startEKG(age, 1, com.mitac.ble.MitacAttributes.EEKG_TYPE.EKG_RUNMETHOD_CPC, level, new MitacEKGCallback() {
            @Override
            public void didEKGDataFinalAnalysis(final com.mitac.ble.MitacEKGData ekgData, final Error error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stopEKGMeasure();
                        if (error != null) {
                            //  finalCallBack.setErrorMsg(error.getMessage());
                            //  finalCallBack.callback(WristabndException.ERROR_CODE_BACKCALL_ERROR, null);
                            //  log.error(error.getMessage(), error);
                            mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_EKG_ERROR_RESULT);
                            //    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                            //  return;
                        }

                        MitacCPCEKG mitacCPCEKG = null;
                        if (ekgData != null) {
                            mitacCPCEKG = new MitacCPCEKG();

                            mitacCPCEKG.setAnsAge(ekgData.mANSAge);
                            mitacCPCEKG.setBalance(ekgData.mBalance);
                            mitacCPCEKG.setEnergy(ekgData.mEnergy);
                            mitacCPCEKG.setHeartRate(ekgData.mHeartRate);
                            mitacCPCEKG.setLeadoff(ekgData.mIsLeadoff);
                            mitacCPCEKG.setStress(ekgData.mStress);
                            mitacCPCEKG.setSuccess(ekgData.mIsSuccess);
                            mitacCPCEKG.setInterval(ekgData.mrrInterval);
                            mitacCPCEKG.setMatching(ekgData.mMatching);
                            mitacCPCEKG.setPerfectCount(ekgData.mPerfectCount);
                            mitacCPCEKG.setPoorCount(ekgData.mPoorCount);
                            mitacCPCEKG.setScore(ekgData.mScore);
                            mitacCPCEKG.setGoodCount(ekgData.mGoodCount);
                            mitacCPCEKG.setCatchUp(ekgData.mCatchUp);
                            mitacCPCEKG.setFinalRRInterval(ekgData.mFinalRRInterval);
                            mitacCPCEKG.setQi(ekgData.mQI);
                        }

//                        finalCallBack.callback(0, mitacCPCEKG);
                        Log.i(TAG, "Mrr final Interval:" + ekgData.mFinalRRInterval.length);
                        mitacListener.onFinalCPCEKGReceived(mitacCPCEKG);
                        setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);

                    }
                });
            }

            @Override
            public void didEKGDataReceived(final MitacEKGData ekgData, final Error error) {
                if (ekgData != null) {
                    Log.i(TAG, String.format("EKG Data=> Age: %d, Heart Rate: %d", ekgData.mANSAge, ekgData.mHeartRate));
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (error != null) {
                            //  processCallBack.setErrorMsg(error.getMessage());
                            //  processCallBack.callback(WristabndException.ERROR_CODE_BACKCALL_ERROR, null);
                            //  log.error(error.getMessage(), error);
                            mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_EKG_ERROR_RESULT);
                            //  setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                            //  stopEKGMeasure();
                            // return;
                        }

                        MitacCPCEKG mitacCPCEKG = null;
                        if (ekgData != null) {
                            mitacCPCEKG = new MitacCPCEKG();

                            mitacCPCEKG.setAnsAge(ekgData.mANSAge);
                            mitacCPCEKG.setBalance(ekgData.mBalance);
                            mitacCPCEKG.setEnergy(ekgData.mEnergy);
                            mitacCPCEKG.setHeartRate(ekgData.mHeartRate);
                            mitacCPCEKG.setLeadoff(ekgData.mIsLeadoff);
                            mitacCPCEKG.setStress(ekgData.mStress);
                            mitacCPCEKG.setSuccess(ekgData.mIsSuccess);
                            mitacCPCEKG.setInterval(ekgData.mrrInterval);
                            mitacCPCEKG.setMatching(ekgData.mMatching);
                            mitacCPCEKG.setPerfectCount(ekgData.mPerfectCount);
                            mitacCPCEKG.setPoorCount(ekgData.mPoorCount);
                            mitacCPCEKG.setScore(ekgData.mScore);
                            mitacCPCEKG.setGoodCount(ekgData.mGoodCount);
                            mitacCPCEKG.setCatchUp(ekgData.mCatchUp);
                            mitacCPCEKG.setFinalRRInterval(ekgData.mFinalRRInterval);
                            mitacCPCEKG.setQi(ekgData.mQI);
                        }
                        if (EKGCounter == 0) {
                            // start time only when first data arrive
                            startEKGTimer();
                        }
                        EKGCounter++;
                        mitacListener.onRawCPCEKGReceived(mitacCPCEKG);
                    }
                });
            }

        });
    }

    public synchronized MitacEventListener.QiStatus getStatus() {
        return this.status;
    }

    public void stopEKG() {

        oldAPI = MitacApi.getSharedInstance(_context);
        oldAPI.stopEKG();
        byte[] cmd = {0x09};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);

    }

    public boolean restartEKGAlgorithm() {
        oldAPI = MitacApi.getSharedInstance(_context);
        return oldAPI.restartEKGAlgorithm();
    }

    public void getOTAMode() {

        byte[] cmd = {0x25};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_FIRMWARE_MODE);

    }

    public void setStepMeasureMode(Boolean isBeltMode) {
        byte[] cmd = {0x3f, 0x00, (byte) ((isBeltMode) ? 1 : 0)};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_SET_STEP_MEASURE_MODE);

    }

    public void getStepMeasureMode() {
        byte[] cmd = {0x3f, 0x01};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_STEP_MEASURE_MODE);
    }


    public void switchOTAMode() {
        Log.d("firmware", "switchOTAMode: " + isOTAMode);
        if (!isOTAMode) {
            Log.i(TAG, "booting to bootloader");
            mOTAStatus = OTAStatus.EOTA_INIT;
            byte[] value = {0x0A};
            writeCharacteristic(mCommandCharacteristic, value);
            mCurrentPacketNumber = 0;
            mOTAStatus = OTAStatus.EOTA_INIT;
            // setDelay(2000);
            // disconnect();
        }
        setStatus(MitacEventListener.QiStatus.QI_STATUS_SWITCH_OTA_MODE);
    }

    public boolean initFirmwareUpdate() {

        Log.d("firmware", "init firmware update");

        if (mFWData == null) {
            mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_FIRMWARE_EMPTY_DATA);
            return false;
        }


        // TODO : is that look for OTAmode rather than INIT status ?
        if (mOTAStatus == OTAStatus.EOTA_INIT) {
            Log.i(TAG, "ota init");
            startFirmwareUpdate();
            setStatus(MitacEventListener.QiStatus.QI_STATUS_INIT_OTA);
        } else {
            Log.i(TAG, "booting to bootloader");
            mOTAStatus = OTAStatus.EOTA_INIT;
            byte[] value = {0x0A};
            writeCharacteristic(mCommandCharacteristic, value);
            mCurrentPacketNumber = 0;
            mOTAStatus = OTAStatus.EOTA_INIT;

            Log.i(TAG, "isOTAMode:" + isOTAMode);

            setStatus(MitacEventListener.QiStatus.QI_STATUS_OTA_PROGRESS);
            mitacListener.onOTAUpdateProgress(0);
            if (!isOTAMode) {


            }


        }
        return true;
    }

    public boolean initFirmwareUpdate(String strFile) {

        Log.d("firmware", "init firmware update:" + strFile);

        Log.i(TAG, "loading firmware file");

        mitacListener.onOTAUpdateProgress(0);
        if (!initFirmwareData(strFile)) {
            mitacListener.onError(status, MitacEventListener.MitacError.MITACERROR_FIRMWARE_FILE);
            Log.e("ble", "Firmware File read error");
            return false;
            //    this.mCallback.onFirmwareTransferring(-1.0F, new Error(com.mitac.ble.MitacError.ERROR_READ_FILE_FAILED.toString()));

        }
        setStatus(MitacEventListener.QiStatus.QI_STATUS_INIT_OTA);
        if (isOTAMode) {
            Log.d("ble","Data ready,sending OTA handshake");

//
//            Handler handler = new Handler(Looper.getMainLooper());
//            final Runnable r = new Runnable() {
//                public void run() {
//                    startScan(0);
                    startFirmwareUpdate();
//                }
//            };
//            handler.postDelayed(r, 1000);


        } else {
            switchOTAMode();
        }

        return true;

    }


//    private boolean setSerial(String serial) {
//        setStatus(MitacEventListener.QiStatus.QI_STATUS_SET_SERIAL);
//        if (serial.length() > 10) {
//           mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_CHAR_DATA_FORMAT);
//            return false;
//        }
//
//        byte[] b_serial = null;
//        try {
//            b_serial = serial.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            //  e.printStackTrace();
//            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_CHAR_DATA_FORMAT);
//            return false;
//        }
//
//        byte[] cmd = {0x23};
//        byte[] combined = new byte[cmd.length + b_serial.length];
//
//        System.arraycopy(cmd, 0, combined, 0, cmd.length);
//        System.arraycopy(b_serial, 0, combined, cmd.length, b_serial.length);
//
//        writeCharacteristic(mCommandCharacteristic, combined);
//
//        return true;
//    }


    public void getSerial() {
        byte[] cmd = {0x24};
        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_SERIAL);
    }

    public void getUUID() {

    }

    public void getDistanceUnit() {
        byte[] cmd = {0x1e};

        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_GET_DISTANCE_UNIT);
    }

    public boolean SetDistanceUnit(DISTANCE_UNIT unit) {


        byte[] cmd = {0x1D, (byte) unit.ordinal()};

        writeCharacteristic(mCommandCharacteristic, cmd);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_SET_DISTANCE_UNIT);
        return true;
    }


    public void startFirmwareUpdate() {

        Log.d("firmware", "start firmware update");

        mitacListener.onOTAUpdateProgress(0);
        mOTAStatus = OTAStatus.EOTA_START;
        new Thread(new Runnable() {
            public void run() {
                setDelay(1000);
                mCurrentPacketNumber = 0;
                byte[] bytes = ByteBuffer.allocate(4).putInt(mPacketNumber).array();
                byte[] value = {0x0c, bytes[0], bytes[1], bytes[2], bytes[3]};
                writeCharacteristic(mCommandCharacteristic, value);
                setStatus(MitacEventListener.QiStatus.QI_STATUS_OTA_PROGRESS);
            }
        }).start();
    }

    private void resetOTAStatus() {
        Log.d("firmware", "reset OTA status");
        // isOTAMode = false;
        mOTAStatus = OTAStatus.EOTA_NONE;
        //   mFWData = null;
        mCurrentPacketNumber = 0;
        mPacketNumber = 0;
        mLastDataByteNumber = 0;

    }

    private boolean initFirmwareData(String strFile) {
        Log.d("firmware", "init firmware data:" + strFile);
        File file = new File(strFile);
        if (!file.exists()) {
            Log.e("ble", "firmware file not exist");
            return false;
        }

        setStatus(MitacEventListener.QiStatus.QI_STATUS_OTA_PROGRESS);
        try {
            FileInputStream fis = new FileInputStream(file);
            int length = (int) file.length();
            Log.e("ble","firmware:"+file+" size:"+length+" loaded");
            mFWData = new byte[length];
            try {
                fis.read(mFWData);
                fis.close();

                mCurrentPacketNumber = 0;
                mPacketNumber = length / 16;
                mLastDataByteNumber = length % 16;
                if (mLastDataByteNumber != 0) {
                    mPacketNumber += 1;
                } else {
                    mLastDataByteNumber = 16;
                }
                //   SharedPreferences settings = this.mContext.getSharedPreferences("MitacBand", 0);
                //   settings.edit().putString("LastOTAPath", strFile).commit();
                mitacListener.onOTAUpdateProgress((int) ((float) mCurrentPacketNumber / (float) mPacketNumber * 100.0));


                return true;
            } catch (IOException e) {
                e.printStackTrace();
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                return false;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
        }
        return false;
    }

    private void setDelay(int iInterval) {
        try {
            Thread.sleep(iInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private synchronized void updateFirmwareTransfer() {
        Log.e("firmware", "update firmware transfer");
        if (mCurrentPacketNumber == mPacketNumber && mPacketNumber != 0) {
            int checksum = 0;
            for (int i = 0; i < mFWData.length; i++) {
                checksum += (mFWData[i] & 0xFF);
            }
            byte[] bytes = ByteBuffer.allocate(4).putInt(checksum).array();
            byte[] value = {14, bytes[0], bytes[1], bytes[2], bytes[3]};
            writeCharacteristic(mCommandCharacteristic, value);
            mitacListener.onOTAUpdateProgress(100);
            setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
        } else if (mCurrentPacketNumber < mPacketNumber - 1) {
            int count = 0;
            while ((mCurrentPacketNumber != mPacketNumber - 1) && (count < 64)) {
                if (mOTAStatus != OTAStatus.EOTA_START) {
                    return;
                }
                byte[] bytes = ByteBuffer.allocate(4).putInt(mCurrentPacketNumber).array();
                byte[] value = {13, bytes[2], bytes[3],
                        mFWData[(mCurrentPacketNumber * 16 + 15)], mFWData[(mCurrentPacketNumber * 16 + 14)],
                        mFWData[(mCurrentPacketNumber * 16 + 13)], mFWData[(mCurrentPacketNumber * 16 + 12)],
                        mFWData[(mCurrentPacketNumber * 16 + 11)], mFWData[(mCurrentPacketNumber * 16 + 10)],
                        mFWData[(mCurrentPacketNumber * 16 + 9)], mFWData[(mCurrentPacketNumber * 16 + 8)],
                        mFWData[(mCurrentPacketNumber * 16 + 7)], mFWData[(mCurrentPacketNumber * 16 + 6)],
                        mFWData[(mCurrentPacketNumber * 16 + 5)], mFWData[(mCurrentPacketNumber * 16 + 4)],
                        mFWData[(mCurrentPacketNumber * 16 + 3)], mFWData[(mCurrentPacketNumber * 16 + 2)],
                        mFWData[(mCurrentPacketNumber * 16 + 1)], mFWData[(mCurrentPacketNumber * 16)]};

                writeCharacteristic(mCommandCharacteristic, value);
                mitacListener.onOTAUpdateProgress((int) ((float) mCurrentPacketNumber / (float) mPacketNumber * 100.0));
                setStatus(MitacEventListener.QiStatus.QI_STATUS_OTA_PROGRESS);
                mCurrentPacketNumber += 1;
                SystemClock.sleep(8L);
                count++;
            }
            if (count < 64) {
                new Thread(new Runnable() {
                    public void run() {
                        updateFirmwareTransfer();
                    }
                }).start();
            }
        } else if (mCurrentPacketNumber == mPacketNumber - 1) {
            byte[] lastbytes = ByteBuffer.allocate(16).putInt(0).array();
            for (int i = 0; i < mLastDataByteNumber; i++) {
                lastbytes[i] = mFWData[(mCurrentPacketNumber * 16 + i)];
            }
            byte[] bytes = ByteBuffer.allocate(4).putInt(mCurrentPacketNumber).array();
            byte[] value = {13, bytes[2], bytes[3],
                    lastbytes[15], lastbytes[14], lastbytes[13], lastbytes[12],
                    lastbytes[11], lastbytes[10], lastbytes[9], lastbytes[8],
                    lastbytes[7], lastbytes[6], lastbytes[5], lastbytes[4],
                    lastbytes[3], lastbytes[2], lastbytes[1], lastbytes[0]};
            writeCharacteristic(mCommandCharacteristic, value);
            mitacListener.onOTAUpdateProgress((int) ((float) mCurrentPacketNumber / (float) mPacketNumber * 100.0));
            mCurrentPacketNumber += 1;
        }
    }


    private void subscribeEKG() {
        setCharacteristicNotification(mEKGCharacteristics, true);
    }

    private void subscribeCommandPoint() {
        setCharacteristicNotification(mCommandCharacteristic, true);
    }

    private void subscribeRealtimeStepCount() {
        setCharacteristicNotification(mRealtimeStepCharacteristic, true);
    }


    private void subscribe7daysStepCount() {
        setCharacteristicNotification(mHistoryCharacteristics, true);
    }

    /**
     * Start the Realtime Step count measure
     */
    public void startRealtimeStepCount() {

        byte[] cmd_start_realtime_step = {0x11, 0x00};
        writeCharacteristic(mCommandCharacteristic, cmd_start_realtime_step);
        setStatus(MitacEventListener.QiStatus.QI_STATUS_START_REALTIME_STEP);

    }

    /**
     * Stop the Realtime Step count measure
     */
    public void stopRealtimeStepCount() {

        byte[] cmd_stop_realtime_step = {0x12, 0x00};
        writeCharacteristic(mCommandCharacteristic, cmd_stop_realtime_step);
        // Qi has no callback on this
        setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
//mitacListener.onRealtimeStepStop();
    }


    public static byte[] hexToBytes(String str) {
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                    + Character.digit(str.charAt(i + 1), 16));
        }
        return data;
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Start the Scanning of Qi device
     *
     * @param timeout Seconds to wait until App disconnect from Qi, 0 = forever
     */
    public void startScan(final long timeout) {


        if (mBluetoothAdapter == null){
            return;
        }

        mScanning = true;

        mBluetoothAdapter.startLeScan(mLeScanCallback);

        if (timeout <= 0) {
            return;
        }

        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);


            }
        }, timeout);

    }

    /**
     * Stop scanning for qi
     */
    public void stopScan() {

        if (mBluetoothAdapter == null){
            return;
        }

        mScanning = false;
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }


    /**
     * Callback when Ble Devices are found
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {


                    // search for service uuid in broadcast signal
                    // byte[] serviceuuid = copyOfRange(scanRecord, 17, 24);

                    String broadcast_signal = bytesToHex(scanRecord);


                    // Qi  signal
                    String localname = "";
                    int retval = broadcast_signal.indexOf("020106030303EE");
                    byte[] bytes = Arrays.copyOfRange(scanRecord, 2, 12);
                    //  Log.e("ble", bytesToHex(bytes));
                    try {
                        localname = new String(bytes, "UTF-8");
                    } catch (Exception e) {

                    }

                    if (retval != -1) {
                        // retval == 22 if OTA , but not perminant
                        // normal Qi
                        //   Log.e("ble",device.getName()+"\n"+localname+"\n"+broadcast_signal);
                        //    Log.d(TAG, "Device:" + device.getName());
                        mScanning = false;
                        mitacListener.onDeviceFound(device, rssi);
                        mitacListener.onDeviceFound(device, rssi, localname);

                    }


                }
            };


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {


        @Override
        /**
         * Called when connection status is changed
         */
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                stopScan();
                mConnectionState = STATE_CONNECTED;
                mitacListener.onConnected();
               // _context.registerReceiver(adapterReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
               // stopScan();
                mConnectionState = STATE_DISCONNECTED;
                mitacListener.onDisconnect();

          //      _context.unregisterReceiver(adapterReceiver);
                setStatus(MitacEventListener.QiStatus.QI_STATUS_DISCONNECTED);
            }
        }

        @Override
        /**
         * Called when Qi Service is discovered
         */
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mitacListener.onMitacServiceDiscovered();
            } else {


            }
        }


        @Override
        /**
         * called when characteristic handler is written
         */
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

            String charUUID = descriptor.getCharacteristic().getUuid().toString();


            if (charUUID.equals(CONTROL_POINT_CHAR)) {
                mitacListener.onCommandPointFeatureSubscribed();
                subscribeRealtimeStepCount();

            }

            if (charUUID.equals(REALTIME_STEP_CHAR)) {
                mitacListener.onRealtimeStepEventSubscribed();
                subscribe7daysStepCount();
            }

            if (charUUID.equals(HISTORY_CHAR)) {
                mitacListener.onHistoryFeatureSubscribed();
                subscribeEKG();
            }
            if (charUUID.equals(EKG_CHAR)) {
                mitacListener.onEKGFeatureSubscribed();

                // all init work done
                // determine OTA mode ,and choose different path
                getOTAMode();

            }

        }

        @Override
        /**
         * called when chacteristic handler is read
         */
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);

        }

        @Override
        /**
         * called when characteristic is written
         */
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            byte[] value = characteristic.getValue();
            //    Log.d(TAG, "onwrite:" + bytesToHex(value));

            // on enter OTA , exit OTA , delay and reconnect , workaround for samsung device
            if (value[0] == 0x0A) {
                setDelay(500);
                //    reconnect(250);
                disconnect();

                ///  startScan(0);
            }
            if (value[0] == 0x0E) {

                // on OTA Finished / Timeout
                resetOTAStatus();
                setDelay(500);
                mitacListener.onOTAUpdateEnd();
                disconnect();
                //    disconnect();
            }


        }

        @Override
        /**
         * called when characteristic is read
         */
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                processCharacteristic(characteristic);
            }

        }

        @Override
        /**
         *  called when data stored in characteristic UUID changed
         */
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            processCharacteristic(characteristic);

        }
    };

    /**
     * Parse data received from Qi
     *
     * @param characteristic
     */
    private void processCharacteristic(final BluetoothGattCharacteristic characteristic) {


        if (characteristic.getUuid().toString().equals(REALTIME_STEP_CHAR)) {
            byte[] data = characteristic.getValue();
            //int stepcount = (data[3] & 0xFF) + 256 * (data[2] & 0xFF);

            StepHistory history = new StepHistory();

            history.step = (data[3] & 0xFF) + (data[2] << 8) + (data[1] << 16) + (data[0] << 24);
            history.distance = (float) (((data[7] & 0xFF) + (data[6] << 8)) / 10.0);
            history.runtime = (data[9] & 0xFF) + (data[8] << 8);
            history.calories = (data[11] & 0xFF) + (data[10] << 8);
            history.timestamp = new Date();

            mitacListener.onGetRealtimeStep(history);
            setStatus(MitacEventListener.QiStatus.QI_STATUS_START_REALTIME_STEP);
        }

        if (characteristic.getUuid().toString().equals(EKG_CHAR)) {
            byte[] data = characteristic.getValue();
            if ((data != null) && (data.length > 0)) {
                for (int i = 2; i < data.length; i += 2) {
                    vv[(i / 2 - 1)] = (data[i] << 8 | data[(i + 1)] & 0xFF);
                    //      Log.d("[EKG]", String.valueOf(vv[(i / 2 - 1)]));
                }
                oldAPI.onEKGDataReceived(vv, null);

            }
        }

        if (characteristic.getUuid().toString().equals(HISTORY_CHAR)) {


            final byte[] data = characteristic.getValue();
            if ((data == null) || (data.length == 0)) {
                return;
            }

            if (this.status == MitacEventListener.QiStatus.QI_STATUS_REQUEST_SLEEP_HISTORY) {

                int idx = (data[1] & 0xFF);


                if (bytesToHex(data).equals(EOF)) { // end of sleep data


                    mitacListener.onSleepDataReceived(sleepRecords);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);

                } else if (data[0] != (byte) 0xFF && data[1] == (byte) 0xFF) { // end of single packet

                    // append to last record
                    sleepHistoryBuffer.put(sleepHistoryBuffer.size(), Arrays.copyOfRange(data, 2, data.length));

                    ByteArrayOutputStream sleepStreamBuffer = new ByteArrayOutputStream();

                    for (int i = 0; i < sleepHistoryBuffer.size(); i++) {
                        try {

                            sleepStreamBuffer.write(sleepHistoryBuffer.get(i));
                            //  Log.d(TAG, "payload:" + bytesToHex(sleepHistoryBuffer.get(i)));
                        } catch (IOException e) {
                            e.printStackTrace();
                            setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                        }
                    }

                    if (sleepStreamBuffer.size() >= 40) {
                        byte[] buf = sleepStreamBuffer.toByteArray();

                        int startIdx = 0;
                        int recordLen = ((int) Byte2IntLBS(buf, startIdx, 4) * 4);
                        if (recordLen > buf.length) {

                            return;
                        }
                        startIdx = 4;
                        long sinceSec = Byte2IntLBS(buf, startIdx, 4) * 1000L;


                        startIdx = 8;
                        MitacAttributes.ETimeZone timezone = MitacAttributes.ETimeZone.TimeZone_UNKNOWN;


                        if ((buf[(startIdx + 1)] & buf[(startIdx + 2)] & buf[(startIdx + 3)]) == -86) {
                            //    if ((data[(startIdx + 1)] + data[(startIdx + 2)] + data[(startIdx + 3)])== 0){


                            timezone = MitacAttributes.ETimeZone.getTimeZoneByIndex(buf[startIdx] & 0x3F);
                        }

                        startIdx = recordLen - 28;
                        int awaknCount = (buf[(startIdx + 3)] << 24) + (buf[(startIdx + 2)] << 16) + (buf[(startIdx + 1)] << 8) + (buf[(startIdx + 0)] & 0xFF);

                        startIdx = recordLen - 24;
                        long eSec = Byte2IntLBS(buf, startIdx, 4) * 1000L;

                        // TODO: use parsed timezone or device timezone
                        int offsetSec = TimeZone.getDefault().getOffset(new Date().getTime());
                        sinceSec -= offsetSec;
                        eSec -= offsetSec;

                        startIdx = recordLen - 20;
                        int bedTime = (int) Byte2IntLBS(buf, startIdx, 4);

                        startIdx = recordLen - 16;
                        int latency = (int) Byte2IntLBS(buf, startIdx, 4);

                        startIdx = recordLen - 12;
                        int effiviency = (int) Byte2IntLBS(buf, startIdx, 4);

                        startIdx = recordLen - 8;
                        int period = (int) Byte2IntLBS(buf, startIdx, 4);

                        SleepHistory sleeprecord = new SleepHistory();
                        sleeprecord.StartTime = new Date(sinceSec);
                        sleeprecord.EndTime = new Date(eSec);
                        sleeprecord.TotalBedTime = bedTime;
                        sleeprecord.Latency = latency;
                        sleeprecord.Efficiency = effiviency;
                        sleeprecord.PeriodTime = period;
                        sleeprecord.AwakenCount = awaknCount;
                        sleeprecord.AwakenDuration = (awaknCount * 0.5F);
                        sleeprecord.SleepDuration = (bedTime * effiviency / 100.0F);
                        sleeprecord.eTimeZone = timezone;
                        sleepRecords.add(sleeprecord);

                        //Log.d(TAG, "start:" + new Date(sinceSec) + " end:" + new Date(eSec));
                        // MitacSleepData sleepData = new MitacSleepData(new Date(sinceSec), new Date(eSec), bedTime, latency, awaknCount, effiviency, period);
                        // mSleepDataList.add(sleepData);
                        sleepStreamBuffer.reset();


                    } else {
                        // TODO: data handling
                        // data error
                    }


                    sleepHistoryBuffer.clear();
                    continueSleepData();

                } else {
                    // put to Map Object with packet index as id  , so duplicated or lost packet can be handled if necessary
                    sleepHistoryBuffer.put(idx, Arrays.copyOfRange(data, 2, data.length));

                }


            }

            if (this.status == MitacEventListener.QiStatus.QI_STATUS_REQUEST_STEP_HISTORY) {


                int idx = ((data[0] & 0xFF) << 8) + (data[1] & 0xFF);


                // start to parse here
                if (bytesToHex(data).contains(EOF)) {

                    // 113x20

//                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//                    // process step data now
//
////Log.e("ble","historybuffer:"+stepHistoryBuffer.size());
//                    for (int i = 0; i < stepHistoryBuffer.size() - 1; i++) {
//
//
//                        try {
//                            buffer.write(stepHistoryBuffer.get(i));
//
//
//                        } catch (IOException e) {
//
//                            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_STEP_HISTORY_READ);
//                            setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
//                        }
//
//                    }
//
//
//                    byte[] appendData = buffer.toByteArray();
//


//Log.e("ble","step buffer:"+bytesToHex(stephistoryBuffer));


                    // last record is rubbish
                    int datasetCount = (stephistoryBuffer.length / 12) - 1;


                    long last_accum_step = 0;

                    ArrayList<StepHistory> records = new ArrayList<>();
                    records = new ArrayList<StepHistory>();

                    for (int j = 0; j < datasetCount; j++) {
                        byte[] rawdata = Arrays.copyOfRange(stephistoryBuffer, 4 + (j * 12), 4 + ((j * 12) + 12));
                        StepHistory history = new StepHistory();

                        int iYear = 0;
                        int iMonth = 0;
                        int iDay = 0;
                        int iHour = 0;
                        MitacAttributes.ETimeZone timezone = MitacAttributes.ETimeZone.TimeZone_UNKNOWN;

                        SimpleDateFormat formater;
                        long iTime = Byte2IntLBS(rawdata, 0, 4);
                        if (((iTime & 0x800000) != 0L)) {
                            iYear = (int) (iTime >> 24 & 0xFF) + 1900;
                            iMonth = (int) (iTime >> 18 & 0x1F);
                            iDay = (int) (iTime >> 12 & 0x3F);
                            iHour = (int) (iTime >> 6 & 0x3F);
                            timezone = MitacAttributes.ETimeZone.getTimeZoneByIndex((int) (iTime & 0x3F));
                            formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

                        } else {
                            iYear = 1900 + (rawdata[3] & 0xFF);
                            iMonth = (rawdata[2] & 0xFF);
                            iDay = (rawdata[1] & 0xFF);
                            iHour = (rawdata[0] & 0xFF);
                            //TODO: use device timezone
                            //  timezone = MitacAttributes.ETimeZone.UTC;
                            formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        }


                        String datestr = (iYear + "-" + (iMonth + 1) + "-" + iDay + " " + ((iHour < 10) ? "0" + iHour : iHour) + ":00:00 " + timezone.toString());


                        try {
                            history.timestamp = formater.parse(datestr);
                        } catch (ParseException e) {
                            Log.e("ble", datestr);
                            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_STEP_HISTORY_READ);
                            // e.printStackTrace();

                            continue;
                        }

                        long this_accum_step = ((rawdata[7] & 0xFF) << 12) + ((rawdata[6] & 0xFF) << 4) + ((rawdata[5] & 0xF0) >> 4);
                        long prev_step = 0;

                        if (records.size() > 0) {
                            if (iHour > 0) {
                                prev_step = last_accum_step;
                            }
                        }

                        if (this_accum_step - prev_step < 0) {
                            history.step = this_accum_step;
                        } else {
                            history.step = this_accum_step - prev_step;
                        }

                        //  Log.e("ble", "raw2:" + bytesToHex(rawdata));
                        history.accum_step = this_accum_step;
                        history.distance = (float) (((rawdata[5] & 0x0F) << 8) + (rawdata[4] & 0xFF) / 10.0D);
                        history.calories = ((rawdata[9] & 0xFF) << 8) + (rawdata[8] & 0xFF);
                        history.runtime = ((rawdata[11] & 0xFF) << 8) + (rawdata[10] & 0xFF);
                        history.eTimeZone = timezone;
                        records.add(history);
                        last_accum_step = this_accum_step;

                    }
                    mitacListener.on7daysStepDataReceived(records);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                } else {
                    // put to Map Object with packet index as id  , so duplicated or lost packet can be handled if necessary
                    //  stepHistoryBuffer.put(idx, Arrays.copyOfRange(data, 2, data.length));


                    byte[] newbuf = Arrays.copyOfRange(data, 2, data.length);
                    byte[] buf = new byte[stephistoryBuffer.length + newbuf.length];
                    System.arraycopy(stephistoryBuffer, 0, buf, 0, stephistoryBuffer.length);
                    System.arraycopy(newbuf, 0, buf, stephistoryBuffer.length, newbuf.length);
                    stephistoryBuffer = buf;


                    //  Log.e("ble","inject length:"+data.length);
//                    Log.e("ble","idx:"+idx);
                    //  Log.e("ble","appendddata:"+bytesToHex(stephistoryBuffer));
                }
            }

        }

        // handle command callback

        if (characteristic.getUuid().toString().equals(CONTROL_POINT_CHAR)) {

            byte[] data = characteristic.getValue();

            if (data[0] == (byte) 0x01) { // firmware callback
                if (data[1] == (byte) 0x0C) {

                    if (data[2] == (byte) 0x01) {

                        updateFirmwareTransfer();
                        Log.i(TAG, "success set firmware size");
                    } else {
                        resetOTAStatus();
                        disconnect();

                        Log.i(TAG, "set firmwaresize error");
                    }

                }
            }
            if (data[0] == (byte) 0x25) { // get firmware mode
                if (data[1] == (byte) 0x00) {
// normal mode
                    //  stopScan();
                    isOTAMode = false;
                    mitacListener.onHandShaked();

                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }
                if (data[1] == (byte) 0x01) {
// OTA mode
                    stopScan();
                    isOTAMode = true;
                    mitacListener.onOTADetected();


                }
                mitacListener.onGetOTAMode(isOTAMode);

            }

            if (data[0] == (byte) 0x0A) {
                setDelay(1000);
                disconnect();
            }

            if (data[0] == (byte) 0x0B) { // init firmware transfer callback
                // OTA mode


                if (mOTAStatus == OTAStatus.EOTA_INIT) {
                    startFirmwareUpdate();
                } else {
                    mOTAStatus = OTAStatus.EOTA_INIT;
                    disconnect();

                }


            }

            if (data[0] == (byte) 0x0C) {
                // total no of packets
                Log.i(TAG, "firmware size received:" + bytesToHex(data));
// TODO: not sure whether need init data transfer here
//updateFirmwareTransfer();
            }

            if (data[0] == (byte) 0x0D) {
                if (mFWData != null) {
                    int packetNumber = Byte2Int(data, 1, 2);
                    if (packetNumber == 0) {
                        mCurrentPacketNumber = packetNumber;
                    } else if (packetNumber == 65535) {
                        mCurrentPacketNumber = mPacketNumber;
                    } else {
                        mCurrentPacketNumber = packetNumber + 1;
                    }
                    new Thread(new Runnable() {
                        public void run() {
                            updateFirmwareTransfer();
                        }
                    }).start();

                    Log.i(TAG, "firmware transferring : " + mCurrentPacketNumber + "/" + mPacketNumber);
                    //  if (this.mCallback != null) {
                    //       this.mCallback.onFirmwareTransferring(mCurrentPacketNumber / mPacketNumber * 100.0F, null);
                    ///  }
                }


            }

            if (data[0] == 0x12) { // stop realtime step callback
                mitacListener.onRealtimeStepStop();
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            }


            if (data[0] == 0x0E) {
                resetOTAStatus();
                if (data[1] == 0) {
                    //    this.mCallback.onFirmwareTransferring(100.0F, null);
                } else if (data[1] == 1) {
                    //  this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_OTA_FAILED.toString()));
                } else if ((data[1] == 2) || (data[1] == 3)) {
                    //  this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_TIMEOUT.toString()));
                } else if (data[1] == 4) {
                    //  this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_WRONG_SIZE.toString()));
                }
                //  reconnect(1000);
                setDelay(3000);

                disconnect();
            }

            if (data[0] == 0x09) {
                // TODO: check whether this is reliable or not
                mitacListener.onEKGStop();
            }

            if (data[0] == 0x3f) {

                if (data[1] == 0x01) {

                    if (data[2] == 0) {
                        mitacListener.onGetStepMeasureMode(false);
                    } else if (data[2] == 1) {
                        mitacListener.onGetStepMeasureMode(true);
                    } else {
                        mitacListener.onGetStepMeasureMode(false);
                        //  mitacListener.onError(MitacEventListener.QiStatus.QI_STATUS_GET_STEP_MEASURE_MODE, MitacEventListener.MitacError.MITACERROR_STEP_MEASURE_UNKNOWN_MODE);
                    }
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);

                } else if (data[1] == 0x00) {
                    if (data[2] == 0) {
                        mitacListener.onSetStepMeasureMode(false);
                    } else if (data[2] == 1) {
                        mitacListener.onSetStepMeasureMode(true);
                    }
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                } else {
                    mitacListener.onError(MitacEventListener.QiStatus.QI_STATUS_SET_STEP_MEASURE_MODE, MitacEventListener.MitacError.MITACERROR_STEP_MEASURE_UNKNOWN_MODE);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }
            }

            // get device info
            if (data[0] == 0x0f) {

                if (data[1] == 0x01) {
                    byte[] firmwareversion = {data[5], data[4], data[3], data[2]};
                    String strVersion = new String(firmwareversion[3] + "." + firmwareversion[2] + "." + firmwareversion[1] + "." + firmwareversion[0]);
                    mitacListener.onGetFirmwareVersion(strVersion);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[1] == 0x02) {
                    byte[] FactoryUUID = {data[9], data[8], data[7], data[6], data[5], data[4], data[3], data[2], data[1], data[0]};
                    String strFactoryUUID = bytesToHex(FactoryUUID);
                    mitacListener.onGetFactoryUUID(strFactoryUUID);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }


                if (data[1] == 0x03) {
                    byte[] macAddress = {data[7], data[6], data[5], data[4], data[3], data[2]};
                    String strMAC = bytesToHex(macAddress);
                    mitacListener.onGetMacAddress(strMAC);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[1] == 0x12) {


                    userProfile profile = new userProfile();
                    profile.age = data[2];
                    profile.isMale = (data[3] == 1);

                    profile.height = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.BIG_ENDIAN).getFloat();//Byte2Int(data, 4, 4);
                    profile.weight = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.BIG_ENDIAN).getFloat();//(float)Byte2Int(data, 8, 4);

                    mitacListener.onGetProfile(profile);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[1] == 0x13) {
                    mitacListener.onGetGoal(Byte2Int(data, 2, 4), Byte2Int(data, 6, 4), Byte2Int(data, 10, 4), Byte2Int(data, 14, 4));
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[1] == 0x17) {

                    if (data[3] >= BATTERY_LEVEL.values().length) {
                        // data 3 error
                        return;
                    }

                    mitacListener.onGetBatteryLevel(data[2] == 0x00, BATTERY_LEVEL.values()[data[3]]);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[1] == 0x19) {
                    GregorianCalendar dateInfo = new GregorianCalendar((data[2] & 0xFF) * 100 + (data[3] & 0xFF),
                            (data[4] & 0xFF) - 1,
                            data[5] & 0xFF,
                            data[6] & 0xFF,
                            data[7] & 0xFF,
                            data[8] & 0xFF);

                    int iTimeZone = Byte2Int(data, 9, 4);

                    TimeZone timezone = TimeZone.getDefault();
                    timezone.setRawOffset(iTimeZone);

                    boolean bIs12hrMode = data[13] == 1;

                    mitacListener.onGetTime(dateInfo.getTime(), timezone, bIs12hrMode);
                    //   this.mCallback.onReceiveDateTime(dateInfo.getTime(), timezone, bIs12hrMode);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[1] == 0x1E || data[1] == 0x1f) {


                    AlarmSetting setting = new AlarmSetting();

                    setting.weekdayNapAlarmEnabled = (data[2] == 0x01);
                    setting.weekdayNapAlarmHour = data[3] & 0xff;
                    setting.weekdayNapAlarmMinute = data[4] & 0xff;
                    setting.weekdayAlarmEnabled = (data[5] == 0x01);
                    setting.weekdayAlarmHour = data[6] & 0xff;
                    setting.weekdayAlarmMinute = data[7] & 0xff;
                    setting.weekendNapAlarmEnabled = (data[8] == 0x01);
                    setting.weekendNapAlarmHour = data[9] & 0xff;
                    setting.weekendNapAlarmMinute = data[10] & 0xff;
                    setting.weekendAlarmEnabled = (data[11] == 0x01);
                    setting.weekendAlarmHour = data[12] & 0xff;
                    setting.weekendAlarmMinute = data[13] & 0xff;

                    mitacListener.onGetAlarm(data[1] == 0x1e, setting);
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }


            }

            if (data[0] == 0x13 || data[0] == 0x14) {

                if (data[1] == 0x00) { // OK
                    if (data[0] == 0x13) {
                        mitacListener.on7daysStepDataStart();
                    }
                    //       continueGet7daysStep();
                } else if (data[1] == 0x01) { // flash write error
                    Log.e("ble", "step history error");
                    stopGet7daysStep();
                } else if (data[1] == 0x02) { // device busy
                    Log.e("ble", "step history error");
                    stopGet7daysStep();
                }


            }

            if (data[0] == 0x18) {
                if (data[1] == 0x00) {
                    mitacListener.onFlashDataErased(true);
                } else {
                    mitacListener.onFlashDataErased(false);
                }
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            }

            if (data[0] == 0x19) {
                if (data[1] == 0x01) {
                    mitacListener.onGetSleepMode(true);
                } else {
                    mitacListener.onGetSleepMode(false);
                }
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            }

            if (data[0] == 0x07) {

                if (data[1] == 0) {
                    //success
                    mitacListener.onSetTime(true, isSetTimeResetCount);
                } else {
                    mitacListener.onSetTime(false, isSetTimeResetCount);
                }
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            }

            if (data[0] == 0x08) { // ekg error
                if (data[1] == 2) {
                    // TODO: link up callback here
                    //    this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_BATTERY_LOW.toString()));
                } else if (data[1] == 4) {
                    //    this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_SLEEP_MODE.toString()));
                } else if (data[1] == 8) {
                    //   this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_WRITING_DATA.toString()));
                } else if (data[1] == 16) {
                    //    this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_HEARTRATE_ON_DEVICE.toString()));
                }
                mitacListener.onError(getStatus(), MitacEventListener.MitacError.MITACERROR_EKG_DEVICE_FAIL);
                // status = MitacEventListener.QiStatus.QI_STATUS_READY;
            }
//
//            if (data[0] == 0x09) {
//                // TODO: check whether this is reliable or not
//               // mitacListener.onEKGStop();
//            }

            if (data[0] == 0x1D) {
                mitacListener.onSetDistanceUnit();
            }


            if (data[0] == 0x1E) {
                if (data[1] == 0x00) {
                    mitacListener.onGetDistanceUnit(DISTANCE_UNIT.DISTANCE_UNIT_KM);
                }
                if (data[1] == 0x01) {
                    mitacListener.onGetDistanceUnit(DISTANCE_UNIT.DISTANCE_UNIT_MI);
                }
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            }

            if (data[0] == 0x1A) {
                if (data[1] == 0x10) {
                    mitacListener.onGetRamSize(RAMSIZE.RAMSIZE_16K);
                }
                if (data[1] == 0x20) {
                    mitacListener.onGetRamSize(RAMSIZE.RAMSIZE_32K);
                }
                if (data[1] == 0x20) {

                }
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            }

            if (data[0] == 0x23) {
                if (data[1] == 0x00) {
                    mitacListener.onSetSerial(true);
                }
                if (data[1] == 0x01) {
                    mitacListener.onSetSerial(false);
                }
                this.status = MitacEventListener.QiStatus.QI_STATUS_READY;
            }

            if (data[0] == 0x24) {

                String serial = new String(data).substring(1, 11);

                mitacListener.onGetSerial(serial);
                setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
            }


            // set device info
            // TODO: verify 0x10,0x10 really callback header for set device info
            if (data[0] == 0x10 && data[1] == 0x10) {

                if (data[2] == 0x04) { // set profile
                    if (data[3] == 0x00) {
                        mitacListener.onSetProfile(true);
                    } else {
                        mitacListener.onSetProfile(false);
                    }
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[2] == 0x05) { // set goal
                    if (data[3] == 0x00) {
                        mitacListener.onSetGoal(true);

                    } else {
                        mitacListener.onSetGoal(false);
                    }
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }

                if (data[2] == 0x0A || data[2] == 0x0B) {

                    boolean isWakeupAlarm = false;
                    if (data[2] == 0x0A) {
                        isWakeupAlarm = true;
                    }


                    if (data[3] == 0x00) {
                        mitacListener.onSetAlarm(isWakeupAlarm, true);

                    } else {
                        mitacListener.onSetAlarm(isWakeupAlarm, false);
                    }
                    setStatus(MitacEventListener.QiStatus.QI_STATUS_READY);
                }
            }

        }


    }

    /**
     * specific event listener to SDK
     *
     * @param listener
     */
    public void addEventListener(MitacEventListener listener) {
        mitacListener = listener;
    }

    /**
     * Start to discover BLE service on  BLE device
     */
    public void startServiceDiscovery() {


        if (!mBluetoothGatt.discoverServices()) {
            Log.e(TAG, "service discover error");
        }
    }


    /**
     * Connect To Qi using specified mac Address
     *
     * @param address macaddress
     * @return connect can be start or not
     */
    public boolean connect(final String address) {


        if (mBluetoothAdapter == null || address == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_SUPPORT);
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {



//            if ( Build.MANUFACTURER.equals("samsung")) {
//                (new Thread(new Runnable() {
//
//                    @Override
//
//                    public void run() {
//                        if (mBluetoothGatt.connect()) {
//                            mConnectionState = STATE_CONNECTING;
//                            mitacListener.onConnecting();
//
//                        } else {
//
//                        }
//
//                    }
//                })).start();
//                return true;
//            }else {


                if (mBluetoothGatt.connect()) {
                    mConnectionState = STATE_CONNECTING;
                    mitacListener.onConnecting();
                    return true;
                } else {
                    return false;
                }
            }
      //  }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_INIT);
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.


        if (mBluetoothGatt != null && device != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (((BluetoothManager) MitacManager._context.getSystemService(Context.BLUETOOTH_SERVICE)).getConnectionState(device, BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED) {
                    return false;
                }
            }else{
                return false;
            }
        }

        // TODO: recheck no extra thread remain after several re-connect


        String manufacturer = Build.MANUFACTURER;

        // ugly fix to handle samsung 4.3 - 4.4 device
        if (manufacturer.equals("samsung")) {
            (new Thread(new Runnable() {

                @Override

                public void run() {
                    mBluetoothGatt = device.connectGatt(_context, false, mGattCallback);

                }
            })).start();
        } else {
            mBluetoothGatt = device.connectGatt(_context, false, mGattCallback);
            mitacListener.onConnecting();
            mBluetoothDeviceAddress = address;
            mConnectionState = STATE_CONNECTING;
        }

        return true;
    }


    /**
     * Disconnect from Qi
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_INIT);
            return;
        }
        mBluetoothGatt.disconnect();

        //  mBluetoothGatt.close();

        // 4.4 bug onConnectionStateChange not always called, manual callback instead
        //mitacListener.onDisconnect();


    }

    public void reconnect(final int iDelay) {


        new Thread(new Runnable() {
            public void run() {
                setDelay(250);

                disconnect();
                //stopScan();
                setDelay(iDelay);

                //  startScan(0);
            }
        }).start();

    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_INIT);
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * @param characteristic Bluetooth characteristic
     * @param data           byte[] data send to Qi
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || characteristic == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_INIT);
            return;
        }
        characteristic.setValue(data);
        if (!mBluetoothGatt.writeCharacteristic(characteristic)) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_CHAR_WRITE_ERROR);
//            Logger.getLogger("MitacEventListener.MitacError.MITACERROR_CHAR_WRITE_ERROR");


        }
    }


    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_INIT);
            return;
        }
        if (!mBluetoothGatt.readCharacteristic(characteristic)) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_CHAR_READ_ERROR);
        }
    }


    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                               boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_NOT_INIT);
            return;
        }

        if (characteristic == null) {
            mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_DATA_SUBSCRIPTION_NOT_READY);
            return;
        }


        if (CONTROL_POINT_CHAR.equals(characteristic.getUuid().toString()) ||
                REALTIME_STEP_CHAR.equals(characteristic.getUuid().toString()) ||
                HISTORY_CHAR.equals(characteristic.getUuid().toString()) ||
                EKG_CHAR.equals(characteristic.getUuid().toString())
                ) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                        UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            } else {
                mitacListener.onError(this.status, MitacEventListener.MitacError.MITACERROR_BLUETOOTH_GATT_ERROR);
            }
        }


    }


    private List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private long Byte2IntLBS(byte[] data, int iIndex, int iLength) {
        long iValue = 0L;
        for (int i = iIndex + iLength - 1; i >= iIndex; i--) {
            iValue <<= 8;
            iValue |= data[i] & 0xFF;
        }
        return iValue;
    }

    private int Byte2Int(byte[] data, int iIndex, int iLength) {
        int iValue = 0;
        for (int i = iIndex; i < iIndex + iLength; i++) {
            iValue <<= 8;
            iValue |= data[i] & 0xFF;
        }
        return iValue;
    }


}
