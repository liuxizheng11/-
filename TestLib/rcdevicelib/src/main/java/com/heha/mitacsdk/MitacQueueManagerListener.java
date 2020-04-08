package com.heha.mitacsdk;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jeff_leung on 16/11/15.
 */
public interface MitacQueueManagerListener {

public void onConnecting();

    public void onError(MitacEventListener.QiStatus status, MitacEventListener.MitacError err);

    public void onConnected();

    public void onDisconnect();

    public void onHistoryFeatureDiscovered();

    public void onHistoryFeatureSubscribed();

    public void onMitacServiceDiscovered();

    public void onRealtimeStepFeatureDiscovered();

    public void onRealtimeStepEventSubscribed();

    public void onGetRealtimeStep(StepHistory history);

    public void onCommandPointFeatureDiscovered();

    public void onCommandPointFeatureSubscribed();

    public void onEKGFeatureDiscovered();

    public void onEKGFeatureSubscribed();

    public void onGetFactoryUUID(String strFactoryUUID);

    public void onHandShaked();

    public void onSetDistanceUnit();

    public void onSetSerial(boolean isSuccess);

    public void onGetSerial(String serial);

    public void onSetProfile(boolean isSuccess);

    public void onSetGoal(boolean isSuccess);

    public void onGetGoal(int steps, int calories, int distance, int runtime);

    public void onSetAlarm(boolean isWakeupAlarm, boolean isSuccess);

    public void onGetAlarm(boolean isWakeupAlarm, MitacManager.AlarmSetting setting);

    public void onGetDistanceUnit(MitacManager.DISTANCE_UNIT unit);

    public void onGetProfile(MitacManager.userProfile profile);

    public void onGetMacAddress(String strMAC);

    public void onGetTime(Date date, TimeZone timezone, boolean bIs12hrMode);

    public void onGetBatteryLevel(boolean isDischarging, MitacManager.BATTERY_LEVEL batterylvl);

    public void onGetFirmwareVersion(String strVersion);

    public void onStartQueueProcess(String process_id, MitacQueueManager.WristbandFunc actionid);


    public void onFinishQueueProcess(String process_id, MitacQueueManager.WristbandFunc actionid);

    public void onGetRamSize(MitacManager.RAMSIZE ramsize);

    // call for final analysized data
    public void onRawCPCEKGReceived(MitacCPCEKG data);

    // call for final analysized data
    public void onFinalCPCEKGReceived(MitacCPCEKG data);

    // call every time EKG data is received
    public void onRawHRVEKGReceived(MitacHRVEKG data);



    // call for final analysized data
    public void onFinalHRVEKGReceived(MitacHRVEKG data);

    public void onEKGStop();

    public void onDeviceFound(BluetoothDevice device, int rssi);

    public void onDeviceFound(BluetoothDevice device, int rssi, String localname);

    public void on7daysStepDataStart();

    public void on7daysStepDataReceived(ArrayList<StepHistory> stepRecords);

    public void on7daysStepDataStop();

    public void onSleepDataStop();

    public void onFlashDataErased(boolean isSuccess);

    public void onSleepDataReceived(ArrayList<SleepHistory> sleepRecords);

    public void on7DaysStepFeatureDiscovered();

    public void onGetSleepMode(boolean isSleepMode);

    public void on7DaysStepFeatureSubscribed();

    public void onOTADetected();

    public void onOTAUpdateEnd();

    public void onOTAUpdateProgress(int progress);

    public void onSetTime(boolean isSuccess, boolean isSetTimeResetCount);

    public void onGetStepMeasureMode(Boolean isBeltMode);

    public void onSetStepMeasureMode(Boolean isBeltMode);

    public void onGetOTAMode(Boolean isOTAMode);
    // public void didEKGDataReceived(final MitacEKGData ekgData, final Error error);

}
