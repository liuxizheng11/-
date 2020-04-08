package com.heha.mitacsdk;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jeff_leung on 9/9/15.
 */

public interface MitacEventListener {



    enum QiStatus {
        QI_STATUS_INITED,// not start scan, disconnected
        QI_STATUS_READY, // ready for command
        QI_STATUS_NOT_AVAILABLE,
        QI_STATUS_DISCONNECTED,
        QI_STATUS_CONNECTING,
        QI_STATUS_DISCONNECTING,
        QI_STATUS_CONNECTED, // just connected, but not sucessfully handshake
        QI_STATUS_START_REALTIME_STEP,
        QI_STATUS_STOP_REALTIME_STEP,
        QI_STATUS_REALTIME_DATA, // data pushing to app
        QI_STATUS_7DAY_HISTORY_DATA, // data is receiving
        QI_STATUS_REQUEST_STEP_HISTORY,
        QI_STATUS_ABORT_STEP_HISTORY,
        QI_STATUS_REQUEST_SLEEP_HISTORY,
        QI_STATUS_ABORT_SLEEP_HISTORY,
        QI_STATUS_EMPTY_FLASH_DATA,
        QI_STATUS_GET_DATE,
        QI_STATUS_SET_DATE,
        QI_STATUS_SET_PROFILE,
        QI_STATUS_GET_PROFILE,
        QI_STATUS_GET_MAC_ADDRESS,
        QI_STATUS_SET_GOAL,
        QI_STATUS_GET_FIRMWARE_VERSION,
        QI_STATUS_GET_FACTORY_UUID,
        QI_STATUS_GET_GOAL,
        QI_STATUS_GET_ALARM,
        QI_STATUS_SET_ALARM,
        QI_STATUS_START_SLEEP_MONITOR,
        QI_STATUS_STOP_SLEEP_MONITOR,
        QI_STATUS_INIT_OTA,
        QI_STATUS_OTA_PROGRESS,
        QI_STATUS_GET_BATTERY_LVL,
        QI_STATUS_GET_SLEEP_STATUS,
        QI_STATUS_GET_RAMSIZE,
        QI_STATUS_START_EKG_MEASURE,
        QI_STATUS_START_HRV_EKG,
        QI_STATUS_START_CPC_EKG,
        QI_STATUS_GET_FIRMWARE_MODE,
        QI_STATUS_SWITCH_OTA_MODE,
        QI_STATUS_SET_SERIAL,
        QI_STATUS_GET_SERIAL,
        QI_STATUS_GET_DISTANCE_UNIT,
        QI_STATUS_SET_DISTANCE_UNIT,
        QI_STATUS_ONHANDSHAKE, // if success, change to status_ready
        QI_STATUS_SET_STEP_MEASURE_MODE,
        QI_STATUS_GET_STEP_MEASURE_MODE,
        QiStatus, QI_STATUS_SCANNING // scanning for qi
    }

    enum MitacError {
        MITACERROR_BLUETOOTH_NOTREADY,
        MITACERROR_BLUETOOTH_NOT_SUPPORT,
        MITACERROR_BLUETOOTH_NOTCONNECTED,
        MITACERROR_BLUETOOTH_GATT_ERROR,
        MITACERROR_DEVICE_NOTFOUND,
        MITACERROR_FLASH_WRITE,
        MITACERROR_DEVICE_BUSY,
        MITACERROR_STEP_HISTORY_READ,
        MITACERROR_DATA_SUBSCRIPTION_NOT_READY,
        MITACERROR_BLUETOOTH_NOT_INIT,
        MITACERROR_CHAR_WRITE_ERROR,
        MITACERROR_CHAR_READ_ERROR,
        MITACERROR_EKG_ERROR_RESULT,
        MITACERROR_FIRMWARE_EMPTY_DATA,
        MITACERROR_FIRMWARE_FILE,
        MITACERROR_CHAR_DATA_FORMAT,
        MITACERROR_EKG_PARAM,
        MITACERROR_EXCEED_QUEUE_LIMIT,
        MITACERROR_SET_STEP_MEASURE_MODE,
        MITACERROR_STEP_MEASURE_UNKNOWN_MODE,
        MITACERROR_EKG_DEVICE_FAIL


    }

    //public void onBluetoothAdapterUpdated(BluetoothAdapter adapter);

    public void onConnecting();

    public void onConnected();

    public void onDisconnect();

    public void onHistoryFeatureDiscovered();

    public void onHistoryFeatureSubscribed();

    public void onMitacServiceDiscovered();

    public void onRealtimeStepFeatureDiscovered();

    public void onRealtimeStepEventSubscribed();

    public void onGetRealtimeStep(StepHistory step);

    public void onRealtimeStepStop();

    public void onCommandPointFeatureDiscovered();

    public void onCommandPointFeatureSubscribed();

    public void onEKGFeatureDiscovered();

    public void onEKGFeatureSubscribed();

    public void onError(QiStatus status, MitacError err);

    public void onHandShaked();

    public void onSetDistanceUnit();

    public void onEKGStop();

    public void onSetSerial(boolean isSuccess);

    public void onGetSerial(String serial);

    public void onSetProfile(boolean isSuccess);

    public void onSetGoal(boolean isSuccess);

    public void onGetGoal(int steps, int calories, int distance, int runtime);

    public void onSetAlarm(boolean isWakeupAlarm, boolean isSuccess);

    public void onGetAlarm(boolean isWakeupAlarm, MitacManager.AlarmSetting setting);

    public void onGetDistanceUnit(MitacManager.DISTANCE_UNIT unit);

    public void onGetProfile(MitacManager.userProfile profile);

    public void onGetFactoryUUID(String strFactoryUUID);

    public void onGetMacAddress(String strMAC);

    public void onGetTime(Date date, TimeZone timezone, boolean bIs12hrMode);

    public void onGetBatteryLevel(boolean isDischarging, MitacManager.BATTERY_LEVEL batterylvl);

    public void onGetFirmwareVersion(String strVersion);

    public void onGetRamSize(MitacManager.RAMSIZE ramsize);

    // call for final analysized data
    public void onRawCPCEKGReceived(MitacCPCEKG data);

    public void onFinalCPCEKGReceived(MitacCPCEKG data);

    // call every time EKG data is received
    public void onRawHRVEKGReceived(MitacHRVEKG data);

    public void onFinalHRVEKGReceived(MitacHRVEKG data);


    public void onDeviceFound(BluetoothDevice device, int rssi);

    public void onDeviceFound(BluetoothDevice device, int rssi, String localname);

    public void on7daysStepDataStart();

    public void on7daysStepDataReceived(ArrayList<StepHistory> stepRecords);

    public void on7daysStepDataStop();

    public void onFlashDataErased(boolean isSuccess);

    public void onSleepDataReceived(ArrayList<SleepHistory> sleepRecords);

    public void onSleepDataStop();

    public void on7DaysStepFeatureDiscovered();

    public void onGetSleepMode(boolean isSleepmode);

    public void on7DaysStepFeatureSubscribed();

    public void onOTADetected();

    public void onOTAUpdateEnd();

    // in percent
    public void onOTAUpdateProgress(int progress);

    public void onSetTime(boolean isSuccess, boolean isResetCount);

    public void onGetStepMeasureMode(Boolean isBeltMode);

    public void onSetStepMeasureMode(Boolean isBeltMode);

    public void onGetOTAMode(Boolean isOTAMode);

    // public void didEKGDataReceived(final MitacEKGData ekgData, final Error error);


}
