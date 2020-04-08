package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.heha.mitacsdk.MitacCPCEKG;
import com.heha.mitacsdk.MitacEventListener;
import com.heha.mitacsdk.MitacHRVEKG;
import com.heha.mitacsdk.MitacManager;
import com.heha.mitacsdk.MitacQueueManager;
import com.heha.mitacsdk.MitacQueueManagerListener;
import com.heha.mitacsdk.SleepHistory;
import com.heha.mitacsdk.StepHistory;
import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothScanUtils;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceSleepDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceSaveTime;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/5 下午11:06
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBluetoothHeHaQiImpl implements RCBlueTooth {

    private static String TAG = "RCDevice_HeHaQi";

    private static RCBluetoothHeHaQiImpl ourInstance;

    public static RCBluetoothHeHaQiImpl getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new RCBluetoothHeHaQiImpl(context).init(context);
        }
        return ourInstance;
    }


    private BluetoothScanUtils scanUtils;

    private Context mContext;
    private Handler handler;

    private RCBluetoothHeHaQiImpl(Context context) {
        this.mContext = context;
        scanUtils = BluetoothScanUtils.getInstance(mContext);
        handler = new Handler();
    }


    private MitacQueueManager mgr;


    public RCBluetoothHeHaQiImpl init(Context context) {
        if (mgr != null) {
            return this;
        }
        this.mContext = context.getApplicationContext();
        mgr = MitacQueueManager.getInstance(mContext);
        setListener();
        mgr.initWithDevice(mContext);
        copyAssets(mContext);
        return this;
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


    private RCBluetoothGetDataListener getDataListener;

    private int mInstructType;

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType) {
        doSendInstruct(getDataListener, instructType, mac, null);
    }

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, String mac, int instructType) {
        doSendInstruct(getDataListener, instructType, null, mac);
    }

    /**
     * 设备连接状态，0未连接，1连接中，2已连接
     */
    private int isConnectStatus = CONNECT_STATUS_NO;

    private final static int CONNECT_STATUS_NO = 0;
    private final static int CONNECT_STATUS_CONNECTING = 1;
    private final static int CONNECT_STATUS_CONNECT = 2;

    public boolean isConnect() {
        return isConnectStatus == CONNECT_STATUS_CONNECT;
    }


    private String lastConnectMac = "";


    //连接成功后需要发送的指令（仅连接时使用）
    private int todoInstructType = -1;


    private void doSendInstruct(RCBluetoothGetDataListener getDataListener, int instructType
            , BluetoothDevice bluetoothDevice, String macInfo) {
        this.getDataListener = getDataListener;
        this.mInstructType = instructType;
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
        if (!lastConnectMac.equals("") && !lastConnectMac.equals(mac) ) {
            if (mgr != null) {
                mgr.disconnect();
            }
            isConnectStatus = CONNECT_STATUS_NO;
        }

        if (isConnectStatus == CONNECT_STATUS_CONNECT) {
            getDataListener.getDataStart();
            RCLog.e(TAG, "HehaQi为连接状态，发送的指令为->" + instructType);
            switch (instructType) {
                case RCBluetoothDataType.DATATYPE_STEP_TODAY:
                    startStepListen();
                    break;
                case RCBluetoothDataType.DATATYPE_STEP_HISTORY:
                    startStepHistory();
                    writeIn = true;
                    handler.postDelayed(runnable, 20000);
                    break;
                case RCBluetoothDataType.DATATYPE_SLEPP_HISTORY:
                    startSleepHistory();
                    writeIn = true;
                    handler.postDelayed(runnable, 20000);
                    break;
                case RCBluetoothDoType.DO_GET_REALTIME_STEP_START:
                    startStepListen();
                    break;
                case RCBluetoothDoType.DO_GET_REALTIME_STEP_STOP:
                    stopStepListen();
                    getDataListener.dataInfo(new JSONArray());
                    break;
                case RCBluetoothDoType.DO_SETTING_TIME:
                    writeIn = true;
                    getDataListener.dataInfo(new JSONArray());
                    break;
                default:
                    RCLog.e(TAG, "设备不支持%d指令", instructType);
                    getDataListener.getDataError(RCBluetoothError.ERROR_PHONE_NOT_SUPPORT,
                            mContext.getString(R.string.rcdevice_error_not_support));
                    break;
            }
        } else {
            RCLog.i(TAG, "连接设备-》" + mac);
            lastConnectMac = mac;
            todoInstructType = instructType;
            if (bluetoothDevice != null) {
                if (isConnectStatus == CONNECT_STATUS_CONNECTING) {
                    getDataListener.getDataError(RCBluetoothError.ERROR_DEVICE_BUSY,
                            mContext.getString(R.string.rcdevice_error_connect_busy));
                    return;
                }
                isConnectStatus = CONNECT_STATUS_CONNECTING;
                if (mgr != null) {
                    mgr.connect(bluetoothDevice);
                }
                return;
            } else {
                scanUtils.setScanPeriodTime(0);
                scanUtils.setScanListener(new RCBluetoothScanListener() {
                    @Override
                    public void scanOver() {
                        isConnectStatus = CONNECT_STATUS_NO;
                    }

                    @Override
                    public void scanStart() {

                    }

                    @Override
                    public void scanInfo(BluetoothDevice device, int rssi) {
                        String localname = device.getName();
                        if (rssi > -75 && localname.substring(0, 3).equals("S00")) {
                            RCLog.i(TAG, "扫描到设备" + localname);
                            if (localname.equals(lastConnectMac)) {
                                scanUtils.removeScanListener(lastConnectMac);
                                if (isConnectStatus == CONNECT_STATUS_CONNECTING) {
                                    return;
                                }
                                isConnectStatus = CONNECT_STATUS_CONNECTING;
                                if (mgr != null) {
                                    mgr.connect(device);
                                }
                                return;
                            }
                        }
                    }

                    @Override
                    public void scanError(int status, String msg) {
                        isConnectStatus = CONNECT_STATUS_NO;
                    }
                }, mac);
            }
        }
    }


    private boolean writeIn;

    /**
     * 判断超时线程
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final int stemp = mInstructType;
            RCLog.e(TAG, "数据获取超时" + writeIn + "<->" + stemp + "<-->" + mInstructType);
            if (writeIn) {
                writeIn = false;
                if (stemp == mInstructType)
                    if (getDataListener != null) {
                        if (stemp == RCBluetoothDataType.DATATYPE_SLEPP_HISTORY) {
                            RCLog.e(TAG, "数据获取超时，获取的是睡眠数据");
                            getDataListener.dataInfo(new JSONArray());
                        } else {
                            getDataListener.getDataError(
                                    RCBluetoothError.ERROR_GET_DATA_TIME_OUT, stemp + "超时"
                            );
                        }
                    }
                RCLog.e(TAG, "数据获取超时，设置初始化" + "<->" + stemp + "<->" + stemp);
            }
        }
    };


    @Override
    public void doDisconnect() {
        lastConnectMac = "";
        isConnectStatus = CONNECT_STATUS_NO;
        if (mgr != null)
            mgr.disconnect();
    }


    /**
     * 开始扫描设备
     */
    public void onStartScan() {
        if (mgr != null) {
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_SCAN, "{process_id:\"a002\",param:[1,20,0],actionid:" + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_SCAN.ordinal() + ",timeout:2}");
        }
    }

    /**
     * 终止扫描设备
     */
    public void onStopScan() {
        if (mgr != null) {
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_SCAN,
                    "{delay:2,param:[0],actionid:" + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_SCAN.ordinal() + ",timeout:100}");
        }
    }


    private void setListener() {
        mgr.addEventListener(
                new MitacQueueManagerListener() {
                    @Override
                    public void onGetOTAMode(Boolean isOTAMode) {
                        RCLog.d(TAG, "OTAMode:" + isOTAMode);
                        if (isOTAMode) {
                            RCLog.e(TAG, "OTA Mode detected, pushing firmware");
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                            String firmware = String.valueOf(preferences.getString("firmware", null));
                            if (firmware.equals("null")) {
                                firmware = "R26.bin";
                            }
                            if (!mgr.initFirmwareUpdate(Environment.getExternalStorageDirectory().getPath() + "/firmware/" + firmware)) {
                                RCLog.e(TAG, "firmware file error:" + Environment.getExternalStorageDirectory().getPath() + "/firmware/" + firmware);
                            }
                        }
                    }

                    @Override
                    public void onSleepDataStop() {

                    }

                    @Override
                    public void onConnecting() {
                        isConnectStatus = CONNECT_STATUS_CONNECTING;
                        RCLog.d(TAG, "connecting");
                    }

                    @Override
                    public void onConnected() {
                        isConnectStatus = CONNECT_STATUS_CONNECT;
                    }

                    @Override
                    public void onDisconnect() {
                        //断开连接
                        RCLog.e(TAG, "activity:ondisconnect");
                        isConnectStatus = CONNECT_STATUS_NO;
//                        mgr.startScan(0);
                        if (getDataListener != null) {
                            getDataListener.getDataError(RCBluetoothError.ERROR_CONNECT, "断开连接");
                        }
                    }

                    @Override
                    public void onFinishQueueProcess(String process_id, MitacQueueManager.WristbandFunc actionid) {
                        RCLog.e(TAG, "Process #" + process_id + "# Finished:" + actionid);
                    }

                    @Override
                    public void onGetFactoryUUID(String strFactoryUUID) {

                    }

                    @Override
                    public void onStartQueueProcess(String process_id, MitacQueueManager.WristbandFunc actionid) {
                        RCLog.e(TAG, "Process #" + process_id + "#  Start:" + actionid);
                    }

                    @Override
                    public void onHistoryFeatureDiscovered() {

                    }

                    @Override
                    public void onHistoryFeatureSubscribed() {

                    }

                    @Override
                    public void onMitacServiceDiscovered() {

                    }

                    @Override
                    public void onRealtimeStepFeatureDiscovered() {

                    }

                    @Override
                    public void onRealtimeStepEventSubscribed() {

                    }

                    @Override
                    public void onGetRealtimeStep(StepHistory step) {
                        writeIn = false;
                        RCLog.e(TAG, "获取到实时步数->" + step.toString());
                        if (step != null) {
                            RCDeviceStepDataDTO stepDataDTO = new RCDeviceStepDataDTO();
                            stepDataDTO.setDate(DateUtil.getFormatNow("yyyyMMdd") + "000000");
                            stepDataDTO.setCal((double) step.calories);
                            stepDataDTO.setStep((int) step.step);
                            stepDataDTO.setDeviceId(RCDeviceDeviceID.HEHAQI);
                            stepDataDTO.setKm(step.distance);
                            if (getDataListener != null) {
                                getDataListener.dataInfo(new JSONArray().put(stepDataDTO.getJSON()));
                            }
                            if (mInstructType == RCBluetoothDataType.DATATYPE_STEP_TODAY) {
                                stopStepListen();
                            }
                        }
                    }

                    @Override
                    public void onCommandPointFeatureDiscovered() {

                    }

                    @Override
                    public void onCommandPointFeatureSubscribed() {

                    }

                    @Override
                    public void onEKGFeatureDiscovered() {

                    }

                    @Override
                    public void onEKGFeatureSubscribed() {

                    }

                    @Override
                    public void onError(MitacEventListener.QiStatus status, final MitacEventListener.MitacError err) {
                        RCLog.e(TAG, "Error:" + err.toString());
                    }

                    /**
                     * 设备连接成功
                     */
                    @Override
                    public void onHandShaked() {
                        RCLog.e(TAG, "HehaQi设备连接成功,开始设置时间");
                        if (mgr != null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String param = "\"" + dateFormat.format(new Date()) + "\",true,false";
                            RCLog.e(TAG, "HehaQi设备连接成功,开始设置时间" + param);
                            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_SET_TIME, "{delay:2,param:[" + param + "],actionid:" +
                                    MitacQueueManager.WristbandFunc.WRISTBANDFUNC_SET_TIME.ordinal() + ",timeout:100}");
                        }
                        isConnectStatus = CONNECT_STATUS_CONNECT;
                    }

                    @Override
                    public void onSetDistanceUnit() {

                    }

                    @Override
                    public void onSetSerial(boolean isSuccess) {

                    }

                    @Override
                    public void onGetSerial(String serial) {
                        RCLog.d(TAG, "Serial:" + serial);
                    }

                    @Override
                    public void onSetStepMeasureMode(Boolean isBeltMode) {
                        RCLog.d(TAG, "set step measure mode:" + isBeltMode);
                    }

                    @Override
                    public void onGetStepMeasureMode(Boolean isBeltMode) {
                        RCLog.d(TAG, "get step measure mode:" + isBeltMode);
                    }

                    @Override
                    public void onSetProfile(boolean isSuccess) {
                        RCLog.i(TAG, "on set profile:" + isSuccess);
                    }

                    @Override
                    public void onSetGoal(boolean isSuccess) {
                        RCLog.i(TAG, "goal set");
                    }

                    @Override
                    public void onGetGoal(int steps, int calories, int distance, int runtime) {
                        RCLog.i(TAG, "goal: steps:" + steps + " calories:" + calories + " distance:" + distance + " runtime:" + runtime);
                    }

                    @Override
                    public void onSetAlarm(boolean isWakeupAlarm, boolean isSuccess) {
                        RCLog.i(TAG, "alarm set");
                    }

                    @Override
                    public void onGetAlarm(boolean isWakeupAlarm, MitacManager.AlarmSetting setting) {

                        RCLog.i(TAG, "==================");
                        RCLog.i(TAG, ((isWakeupAlarm) ? "Wakeup " : "Sleep ") + "Alarm");
                        RCLog.i(TAG, "==================");

                        RCLog.i(TAG, "Weekday Nap Alarm: [" + ((setting.weekdayNapAlarmEnabled) ? "x" : "") + "] " + ((setting.weekdayNapAlarmHour < 10) ? "0" : "") + setting.weekdayNapAlarmHour + ":" + ((setting.weekdayNapAlarmMinute < 10) ? "0" : "") + setting.weekdayNapAlarmMinute);
                        RCLog.i(TAG, "Weekday Alarm: [" + ((setting.weekdayAlarmEnabled) ? "x" : "") + "] " + ((setting.weekdayAlarmHour < 10) ? "0" : "") + setting.weekdayAlarmHour + ":" + ((setting.weekdayAlarmMinute < 10) ? "0" : "") + setting.weekdayAlarmMinute);
                        RCLog.i(TAG, "weekend Nap Alarm: [" + ((setting.weekendNapAlarmEnabled) ? "x" : "") + "] " + ((setting.weekendNapAlarmHour < 10) ? "0" : "") + setting.weekendNapAlarmHour + ":" + ((setting.weekendNapAlarmMinute < 10) ? "0" : "") + setting.weekendNapAlarmMinute);
                        RCLog.i(TAG, "Weekend Alarm: [" + ((setting.weekendAlarmEnabled) ? "x" : "") + "] " + ((setting.weekendAlarmHour < 10) ? "0" : "") + setting.weekendAlarmHour + ":" + ((setting.weekendAlarmMinute < 10) ? "0" : "") + setting.weekendAlarmMinute);
                    }

                    @Override
                    public void onGetDistanceUnit(MitacManager.DISTANCE_UNIT unit) {

                    }

                    @Override
                    public void onGetProfile(MitacManager.userProfile profile) {

                        RCLog.i(TAG, "==================");
                        RCLog.i(TAG, "Profile:");
                        RCLog.i(TAG, "==================");
                        RCLog.i(TAG, "isMale:" + profile.isMale);
                        RCLog.i(TAG, "age:" + profile.age);
                        RCLog.i(TAG, "weight:" + profile.weight);
                        RCLog.i(TAG, "height:" + profile.height);

                    }

                    @Override
                    public void onGetMacAddress(String strMAC) {
                        RCLog.i(TAG, "mac:" + strMAC);
                    }

                    @Override
                    public void onGetTime(Date date, TimeZone timezone, boolean bIs12hrMode) {
                        RCLog.i(TAG, "==================");
                        RCLog.i(TAG, "Date:");
                        RCLog.i(TAG, "==================");
                        RCLog.i(TAG, date.toString());
                        RCLog.i(TAG, "Timezone:" + timezone.toString());
                    }

                    @Override
                    public void onGetBatteryLevel(boolean isDischarging, MitacManager.BATTERY_LEVEL batterylvl) {
                        RCLog.i(TAG, "discharging:" + isDischarging + "battery level:" + batterylvl.toString());
                    }

                    @Override
                    public void onGetFirmwareVersion(String strVersion) {
                        RCLog.i(TAG, "firmware:" + strVersion);
                    }


                    @Override
                    public void onGetRamSize(MitacManager.RAMSIZE ramsize) {

                    }


                    @Override
                    public void onFinalCPCEKGReceived(MitacCPCEKG data) {
                        String finalRR = "[";
                        int[] arr_interval = data.getFinalRRInterval();

                        if (arr_interval != null) {
                            for (int i = 0; i < arr_interval.length; i++) {
                                finalRR = finalRR.concat(" " + String.valueOf(arr_interval[i]));
                            }
                            finalRR = finalRR.concat("]");

                        }
//
//                RCLog.i("mitac", "Age:" + data.getAnsAge() +
//                        " balance:" + data.getBalance() +
//                        " energy:" + data.getEnergy() +
//                        " heartRate:" + data.getHeartRate() +
//                        " stress:" + data.getStress() +
//                        " isleadoff:" + data.isLeadoff() +
//                        " isSuccess:" + data.isSuccess() +
//                        " goodcount:" + data.getGoodCount() +
//                        " perfectcount:" + data.getPerfectCount() +
//                        " poorcount:" + data.getPoorCount() +
//                        " matching:" + data.getMatching() +
//                        " interval:" + data.getInterval() +
//                        " score:" + data.getScore() +
//                        " catchup:" + data.getCatchUp() +
//                        " finalRRInterval:" + finalRR +
//                        " qi:" + data.getQi()
//
//                );
                    }

                    @Override
                    public void onRawCPCEKGReceived(MitacCPCEKG data) {

                        RCLog.i(TAG, "CPC raw:\n" + data.toString());

//                beatTimeIdx += data.getInterval();
//                int cpcIncrement;
//                ///////// respiration pn code ////////////
//                int RRthd = 10; // ms 調越大越嚴格
//
//                int prebeatTimeIdx = last_beatTimeIdx;
//
//                int precpcrr = last_cpcrr;
//
//                int diffrr = data.getInterval() - precpcrr;
//
//                if (prebeatTimeIdx != beatTimeIdx)
//
//                {
//
//                    if (diffrr > RRthd) {
//
//                        cpcIncrement = 1;
//                    } else if (diffrr < -RRthd) {
//
//                        cpcIncrement = -1;
//                    } else {
//                        cpcIncrement = 0;
//
//                    }
//
//                    last_beatTimeIdx = beatTimeIdx;
//
//                    last_cpcrr = data.getInterval();
//
//                    RCLog.d(TAG, "pn: " + cpcIncrement + " rr:" + last_cpcrr + " beatTime:" + last_beatTimeIdx);
//
//                }
                    }

                    @Override
                    public void onRawHRVEKGReceived(MitacHRVEKG data) {
                        RCLog.i(TAG, "HRV:\n" + data.toString());
                    }

                    @Override
                    public void onFinalHRVEKGReceived(MitacHRVEKG data) {
                        String finalRR = "[";
                        int[] arr_interval = data.getFinalRRInterval();

                        if (arr_interval != null) {

                            for (int i = 0; i < arr_interval.length; i++) {
                                finalRR = finalRR.concat(" " + String.valueOf(arr_interval[i]));
                            }
                            finalRR = finalRR.concat("]");

                        }

                        RCLog.i("mitac", "id:" + data.getId() +
                                " ansAge:" + data.getAnsAge() +
                                " balance:" + data.getBalance() +
                                " energy:" + data.getEnergy() +
                                " heartrate:" + data.getHeartRate() +
                                " stress:" + data.getStress() +
                                " isleadoff:" + data.isLeadoff() +
                                " issuccess:" + data.isSuccess() +
                                " rr interval:" + data.getRrInterval() +
                                " rr final interval:" + finalRR +
                                " qi :" + data.getQi()
                        );
                    }


                    @Override
                    public void onDeviceFound(BluetoothDevice device, int rssi) {


                    }

                    /**
                     * 扫描到设备处理
                     * @param device 设备信息
                     * @param rssi 设备信号
                     * @param localname 设备SN
                     */
                    @Override
                    public void onDeviceFound(BluetoothDevice device, int rssi, String localname) {
                        //如果需要连接设备，扫描到设备后进行连接
                        //如果是扫描设备，通过监听返回数据
                        if (rssi > -75 && localname.substring(0, 3).equals("S00")) {
                            RCLog.i(TAG, "扫描到设备" + localname);
                            if (localname.equals(lastConnectMac)) {
                                if (mgr != null) {
                                    mgr.connect(device);
                                }
                                mgr.stopScan();
                                return;
                            }
                        }

                    }

                    @Override
                    public void on7daysStepDataStart() {

                    }

                    @Override
                    public void on7daysStepDataReceived(ArrayList<StepHistory> stepHistories) {
                        writeIn = false;
                        RCLog.e(TAG, "获取到历史步数->" + stepHistories.size());
                        if (getDataListener != null) {
                            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                            Map<String, StepHistory> datas = new HashMap<>();
                            for (int i = 0; i < stepHistories.size(); i++) {
                                //一天会有多条数据,取总数最多的一条
                                RCLog.d(TAG, "HeHaQI手环数据获取-获取到计步历史数据" + stepHistories.get(i).toString());
                                if (datas.containsKey(date.format(stepHistories.get(i).timestamp))) {
                                    if (stepHistories.get(i).timestamp.getTime() >
                                            datas.get(date.format(stepHistories.get(i).timestamp)).timestamp.getTime()) {
                                        datas.put(
                                                date.format(stepHistories.get(i).timestamp), stepHistories.get(i));
                                    }
                                } else {
                                    datas.put(
                                            date.format(stepHistories.get(i).timestamp), stepHistories.get(i));
                                }

                            }
                            JSONArray jsonArray = new JSONArray();
                            for (Map.Entry<String, StepHistory> entry : datas.entrySet()) {
                                if (!entry.getKey().equals(DateUtil.getFormatNow("yyyyMMdd")) &&
                                        Long.parseLong(entry.getKey()) >=
                                                Long.parseLong(RCSPDeviceSaveTime.getDataTime(RCDeviceDeviceID.HEHAQI,
                                                        RCDeviceIndicatorID.STEP, lastConnectMac))) {
                                    StepHistory step = entry.getValue();
                                    RCDeviceStepDataDTO stepDataDTO = new RCDeviceStepDataDTO();
                                    stepDataDTO.setDate(entry.getKey() + "000000");
                                    stepDataDTO.setCal((double) step.calories);
                                    stepDataDTO.setStep((int) step.accum_step);
                                    stepDataDTO.setDeviceId(RCDeviceDeviceID.HEHAQI);
                                    stepDataDTO.setKm(step.distance);
                                    jsonArray.put(stepDataDTO.getJSON());
                                }
                            }

                            RCLog.e(TAG, "获取到历史步数->" + jsonArray.toString());
                            getDataListener.dataInfo(jsonArray);
                        }
                        stopStepHistory();
                    }

                    @Override
                    public void on7daysStepDataStop() {

                    }

                    @Override
                    public void onFlashDataErased(boolean isSuccess) {

                    }

                    @Override
                    public void onEKGStop() {


                    }

                    @Override
                    public void onSleepDataReceived(ArrayList<SleepHistory> sleepHistories) {
                        writeIn = false;
                        RCLog.e(TAG, "获取到历史睡眠->" + sleepHistories.size());
                        if (getDataListener != null) {
                            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                            SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
                            JSONArray array = new JSONArray();
                            for (int i = 0; i < sleepHistories.size(); i++) {
                                if (Long.parseLong(date.format(sleepHistories.get(i).EndTime)) >=
                                        Long.parseLong(RCSPDeviceSaveTime.getDataTime(RCDeviceDeviceID.HEHAQI,
                                                RCDeviceIndicatorID.SLEEP_TIME, lastConnectMac))) {
                                    RCDeviceSleepDataDTO dto = new RCDeviceSleepDataDTO();
                                    dto.setDeviceId(RCDeviceDeviceID.HEHAQI);
                                    dto.setStopTime(Long.parseLong(time.format(sleepHistories.get(i).EndTime)));
                                    dto.setStartTime(Long.parseLong(time.format(sleepHistories.get(i).StartTime)));
                                    dto.setAll(sleepHistories.get(i).TotalBedTime);
                                    dto.setDeep((int) sleepHistories.get(i).SleepDuration);
                                    dto.setShallow(sleepHistories.get(i).TotalBedTime - (int) sleepHistories.get(i).SleepDuration);
                                    dto.setUpTime((int) sleepHistories.get(i).AwakenDuration);
                                    dto.setUpNumber(sleepHistories.get(i).AwakenCount);
                                    array.put(dto.getJSON());
                                }
                            }
                            RCLog.e(TAG, "获取到历史睡眠->" + array.toString());
                            getDataListener.dataInfo(array);
                        }
                        stopSleepHistory();
                    }

                    @Override
                    public void on7DaysStepFeatureDiscovered() {

                    }


                    @Override
                    public void onGetSleepMode(boolean isSuccess) {

                    }

                    @Override
                    public void on7DaysStepFeatureSubscribed() {

                    }

                    @Override
                    public void onOTADetected() {
                        if (true) {
                            return;
                        }
                        RCLog.e(TAG, "OTA Mode detected, pushing firmware");

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                        String firmware = String.valueOf(preferences.getString("firmware", null));

                        if (firmware.equals("null")) {
                            firmware = "R26.bin";
                        }
                        //TODO: use Environment.getExternalStoragePublicDirectory
                        //     if (!mgr.initFirmwareUpdate(getBaseContext().getFilesDir()+firmware)) {
                        // if (!mgr.initFirmwareUpdate(getBaseContext().getApplicationContext().getFileStreamPath(firmware).getPath())) {
                        if (!mgr.initFirmwareUpdate(Environment.getExternalStorageDirectory().getPath() + "/firmware/" + firmware)) {
                            //  if (!mgr.initFirmwareUpdate(Environment.getExternalStorageDirectory() + "/EKG/"+firmware)) {


                            // TODO: firmware file error
                            RCLog.e(TAG, "firmware file error:" + Environment.getExternalStorageDirectory().getPath() + "/firmware/" + firmware);
                        }
                    }

                    @Override
                    public void onOTAUpdateEnd() {
                        RCLog.d(TAG, "OTA update end");
                    }

                    @Override
                    public void onOTAUpdateProgress(int progress) {
                        RCLog.i(TAG, "OTA progress:" + progress);
                    }

                    @Override
                    public void onSetTime(boolean isSuccess, boolean isSetTimeResetCount) {
                        RCLog.i(TAG, "时间设置成功：" + isSuccess + "<->" + isSetTimeResetCount
                                + "\ntodoInstructType：" + todoInstructType + "<-connectMac->" + lastConnectMac);
                        if (todoInstructType > 0) {
                            sendInstruct(getDataListener, lastConnectMac, todoInstructType);
                        }
                        todoInstructType = -1;
                    }
                }

        );
    }


    private static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            RCLog.e(TAG, e.getMessage());
        }

        //  RCLog.e(TAG,"iterating file list");
        for (String filename : files) {

            if (!filename.contains(".bin")) {
                continue;
            }

            File file = new File(Environment.getExternalStorageDirectory() + "/firmware/");
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    RCLog.e(TAG, "error creating folder " + Environment.getExternalStorageDirectory() + "/firmware/");
                }
            }

            //    System.out.println("File name => "+filename);
            //    RCLog.e(TAG,"copying file->"+Environment.getExternalStorageDirectory().getPath() +"/firmware/" + filename);

            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);   // if files resides inside the "Files" directory itself

                //    out = new FileOutputStream( _context.getFilesDir()+filename);
                //    out = new FileOutputStream( (_context.getApplicationContext().getFileStreamPath(filename).getPath()));
                out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/firmware/" + filename);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                RCLog.e(TAG, e.getMessage());
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    /**
     * 开始获取实时步数
     */
    private void startStepListen() {
        if (mgr != null)
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_REALTIME_STEP_COUNT,
                    "{process_id:\"a004\",param:[],actionid:" + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_REALTIME_STEP_COUNT.ordinal() + ",timeout:2}");
    }


    /**
     * 结束获取实时步数
     */
    private void stopStepListen() {
        if (mgr != null)
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_REALTIME_STEP_COUNT, "{process_id:\"a004\",param:[],actionid:"
                    + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_REALTIME_STEP_COUNT.ordinal() + ",timeout:2}");
    }


    /**
     * 开始获取睡眠历史数据
     */
    private void startSleepHistory() {
        if (mgr != null) {
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_REQUEST_SLEEP_HISTORY,
                    "{delay:2,param:[0],actionid:" + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_REQUEST_SLEEP_HISTORY.ordinal() + ",timeout:100}");
        }
    }


    /**
     * 结束获取睡眠历史数据
     */
    private void stopSleepHistory() {
        if (mgr != null) {
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_REQUEST_SLEEP_HISTORY,
                    "{delay:2,param:[0],actionid:" + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_REQUEST_SLEEP_HISTORY.ordinal() + ",timeout:100}");
        }
    }


    /**
     * 开始获取步数历史数据
     */
    private void startStepHistory() {
        if (mgr != null) {
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_REQUEST_STEP_HISTORY,
                    "{delay:2,param:[0],actionid:" + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_START_REQUEST_STEP_HISTORY.ordinal() + ",timeout:100}");
        }
    }

    /**
     * 结束获取步数历史数据
     */
    private void stopStepHistory() {
        if (mgr != null) {
            mgr.insertAction(MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_REQUEST_STEP_HISTORY,
                    "{delay:2,param:[0],actionid:" + MitacQueueManager.WristbandFunc.WRISTBANDFUNC_STOP_REQUEST_STEP_HISTORY.ordinal() + ",timeout:100}");
        }
    }
}
