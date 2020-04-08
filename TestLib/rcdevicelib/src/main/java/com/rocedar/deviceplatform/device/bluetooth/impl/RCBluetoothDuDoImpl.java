package com.rocedar.deviceplatform.device.bluetooth.impl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothScanUtils;
import com.rocedar.deviceplatform.device.bluetooth.impl.dudo.DBDeviceDuDoData;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothScanListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceHeartRateDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceSleepDataDTO;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceSaveTime;
import com.rocedar.deviceplatform.unit.DateUtil;
import com.yc.peddemo.sdk.BLEServiceOperate;
import com.yc.peddemo.sdk.BluetoothLeService;
import com.yc.peddemo.sdk.DataProcessing;
import com.yc.peddemo.sdk.ICallback;
import com.yc.peddemo.sdk.ICallbackStatus;
import com.yc.peddemo.sdk.RateChangeListener;
import com.yc.peddemo.sdk.ServiceStatusCallback;
import com.yc.peddemo.sdk.SleepChangeListener;
import com.yc.peddemo.sdk.StepChangeListener;
import com.yc.peddemo.sdk.UTESQLOperate;
import com.yc.peddemo.sdk.WriteCommandToBLE;
import com.yc.peddemo.utils.CalendarUtils;
import com.yc.peddemo.utils.GlobalVariable;
import com.yc.pedometer.info.RateOneDayInfo;
import com.yc.pedometer.info.SleepTimeInfo;
import com.yc.pedometer.info.StepInfo;

import org.json.JSONArray;

import java.util.List;

import static com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceSaveTime.getDataTime;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/21 下午2:58
 * 版本：V1.0
 * 描述：琥蜂DuDo手环
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBluetoothDuDoImpl implements RCBlueTooth, ICallback, ServiceStatusCallback {

    private final String TAG = "RCDevice_Dudo";


    private static RCBluetoothDuDoImpl instance;
    private UTESQLOperate mySQLOperate;
    private DBDeviceDuDoData mDBDeviceDuDoData;
    private SharedPreferences sp;

    public static RCBluetoothDuDoImpl getInstance(Context context) {
        if (instance == null) {
            instance = new RCBluetoothDuDoImpl(context);
        }
        return instance;
    }

    private Context mContext;

    private RCBluetoothDuDoImpl(Context context) {
        this.mContext = context.getApplicationContext();
        mBLEServiceOperate = BLEServiceOperate.getInstance(mContext);// 用于BluetoothLeService实例化准备,必须
        mBLEServiceOperate.isSupportBle4_0();
        scanUtils = BluetoothScanUtils.getInstance(mContext);
        initDuDoSDK();
    }

    private BLEServiceOperate mBLEServiceOperate;
    private BluetoothLeService mBluetoothLeService;

    private BluetoothScanUtils scanUtils;


    @Override
    public void scanListener(final RCBluetoothScanListener scanListener) {
        scanUtils.setScanListener(scanListener);
    }

    @Override
    public void doScan(boolean enable) {
        scanUtils.scanLeDevice(enable);
    }


    //==================数据获取==========================================


    /*设备支持的指令集*/
    private int[] supportInstructType = {
            RCBluetoothDataType.DATATYPE_STEP_TODAY, RCBluetoothDataType.DATATYPE_STEP_HISTORY,
            RCBluetoothDataType.DATATYPE_HEARTR_ATE_TODAY, RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY,
            RCBluetoothDataType.DATATYPE_SLEPP_HISTORY,
            RCBluetoothDoType.DO_SETTING_TIME,
            RCBluetoothDoType.DO_TEST_HEART_RATE_START, RCBluetoothDoType.DO_TEST_HEART_RATE_STOP};


    //最后执行的指令
    private int lastInstructType;

    private WriteCommandToBLE mWriteCommand;

    private RCBluetoothGetDataListener mGetDataListener;

    private Handler handler = new Handler();

    private DataProcessing mDataProcessing;

    private String lastConnectMac = "";

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, final String mac, int instructType) {
        doSendInstruct(getDataListener, null, mac, instructType);
    }

    @Override
    public void sendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice mac, int instructType) {
        doSendInstruct(getDataListener, mac, null, instructType);

    }

    private void doSendInstruct(RCBluetoothGetDataListener getDataListener, BluetoothDevice device,
                                final String mac, int instructType) {
        //判断设备是否支持该指令
        if (!binarySearch(supportInstructType, instructType)) {
            getDataListener.getDataError(RCBluetoothError.ERROR_PHONE_NOT_SUPPORT,
                    mContext.getString(R.string.rcdevice_error_not_support));
            RCLog.i(TAG, "DuDO不支持该协议，" + instructType);
            return;
        }

        //判断当前连接的设备是否是需要发送指令的设备

        //获取本次操作的MAC地址
        String tempMac = "";
        if (device != null) {
            tempMac = device.getAddress();
        } else if (mac != null) {
            tempMac = mac;
        }
        if (!lastConnectMac.equals("") && !tempMac.equals(lastConnectMac)) {
            RCLog.i(TAG, "切换了设备断开设备连接01");
            //当前为切换的设备，断开设备连接
            if (isConnect)
                doDisconnect();
            isConnect = false;
            lastStepData = null;
        }

        this.mGetDataListener = getDataListener;
        this.lastInstructType = instructType;
        this.lastConnectMac = tempMac;
        if (!isConnect) {
            //当设备没有连接时，连接设备
            if (device != null) {
                mBLEServiceOperate.connect(device.getAddress());
            } else {
                scanUtils.setScanListener(new RCBluetoothScanListener() {
                    @Override
                    public void scanOver() {

                    }

                    @Override
                    public void scanStart() {

                    }

                    @Override
                    public void scanInfo(BluetoothDevice device, int rssi) {
                        if (device.getAddress().toUpperCase().equals(lastConnectMac.toUpperCase())) {
                            scanUtils.removeScanListener(lastConnectMac);
                            mBLEServiceOperate.connect(device.getAddress());
                        }
                    }

                    @Override
                    public void scanError(int status, String msg) {

                    }
                }, lastConnectMac);
                scanUtils.scanLeDevice(true);
            }
        } else {
            doInstruct(instructType);
        }

    }

    /**
     * 初始化DuDoSDK
     */
    private void initDuDoSDK() {
        mBluetoothLeService = mBLEServiceOperate.getBleService();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.setICallback(this);
        }
        mWriteCommand = WriteCommandToBLE.getInstance(mContext);
        mDataProcessing = DataProcessing.getInstance(mContext);
        mySQLOperate = new UTESQLOperate(mContext);
        mDBDeviceDuDoData = new DBDeviceDuDoData(mContext);
        sp = mContext.getSharedPreferences(GlobalVariable.SettingSP, Toast.LENGTH_SHORT);
        mDataProcessing.setOnStepChangeListener(mOnStepChangeListener);
        mDataProcessing.setOnSleepChangeListener(mOnSlepChangeListener);
        mDataProcessing.setOnRateListener(mOnRateListener);
        mBLEServiceOperate.setServiceStatusCallback(this);
    }


    private void doInstruct(int instructType) {
        if (!isConnect) return;
        if (writeIn) {
            mGetDataListener.getDataError(RCBluetoothError.ERROR_DEVICE_BUSY, "正在同步数据，请稍后");
            return;
        }
        int waitTime = 0;
        writeIn = true;
        switch (instructType) {
            case RCBluetoothDoType.DO_SETTING_TIME:
                RCLog.i(TAG, "开始同步时间");
                mWriteCommand.syncBLETime();
                break;
            case RCBluetoothDataType.DATATYPE_HEARTR_ATE_TODAY:
                RCLog.i(TAG, "开始同步心率");
                waitTime = 30 * 1000;
                mWriteCommand.syncAllRateData();
                break;
            case RCBluetoothDoType.DO_GET_REALTIME_STEP_STOP:
                waitTime = 0;
                writeIn = false;
                mGetDataListener = null;
                break;
            case RCBluetoothDoType.DO_GET_REALTIME_STEP_START:
                waitTime = 0;
                break;
            case RCBluetoothDataType.DATATYPE_STEP_TODAY:
                if (lastStepData != null) {
                    waitTime = 0;
                    updateDataInfo(new JSONArray().put(lastStepData.getJSON()));
                    writeIn = false;
                    mGetDataListener = null;
                } else {
                    waitTime = 60 * 1000;
                    mWriteCommand.syncAllStepData();
                }
                break;
            case RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY:
                waitTime = 60 * 1000;
                if (!getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.Heart_Rate, lastConnectMac)
                        .equals(DateUtil.getFormatNow("yyyyMMdd"))) {
                    mWriteCommand.syncAllRateData();
                } else {
                    updateDataInfo(mDBDeviceDuDoData.getHistoryRate(Integer.parseInt(getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.Heart_Rate, lastConnectMac))));
                }
                RCLog.i(TAG, "开始获取历史心率");
                break;
            case RCBluetoothDataType.DATATYPE_STEP_HISTORY:
                waitTime = 60 * 1000;
                if (!getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.STEP, lastConnectMac)
                        .equals(DateUtil.getFormatNow("yyyyMMdd"))) {
                    mWriteCommand.syncAllStepData();
                } else {
                    updateDataInfo(mDBDeviceDuDoData.getHistoryStep(Integer.parseInt(getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.STEP, lastConnectMac))));
                }
                RCLog.i(TAG, "开始获取历史步数");
                break;
            case RCBluetoothDataType.DATATYPE_SLEPP_HISTORY:
                RCLog.i(TAG, "开始获取历史睡眠");
                waitTime = 40 * 1000;
                //判断最后同步睡眠数据的时间是否是今天
                if (!getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.SLEEP_TIME, lastConnectMac)
                        .equals(DateUtil.getFormatNow("yyyyMMdd"))) {
                    //步数今天，同步数据
                    mWriteCommand.syncAllSleepData();
                } else {
                    //为今天，获取数据
                    updateDataInfo(mDBDeviceDuDoData.getHistorySleepNew(Integer.parseInt(getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.SLEEP_TIME, lastConnectMac))));
                }
                break;
            case RCBluetoothDoType.DO_TEST_HEART_RATE_START:
                waitTime = 30 * 1000;
                mWriteCommand.sendRateTestCommand(GlobalVariable.RATE_TEST_START);
                RCLog.i(TAG, "开始测试心率");
                break;
            default:
                writeIn = false;
                break;
        }
        if (waitTime > 0) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, waitTime);
        }
    }


    private boolean writeIn = false;


    /**
     * 判断超时线程
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (writeIn) {
                writeIn = false;
                if (mGetDataListener != null)
                    mGetDataListener.getDataError(
                            RCBluetoothError.ERROR_GET_DATA_TIME_OUT, "超时"
                    );
            }
        }
    };


    @Override
    public void doDisconnect() {
        RCToast.TestCenter(mContext, "调用DuDo设备断开连接方法");
        isConnect = false;
        GlobalVariable.BLE_UPDATE = false;
        lastStepData = null;
        mBLEServiceOperate.disConnect();
    }

    private boolean isConnect = false;

    @Override
    public boolean isConnect() {
        return isConnect;
    }


    private boolean binarySearch(int[] s, int t) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] == t) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void OnResult(boolean result, int status) {
        // TODO Auto-generated method stub
//        RCLog.i(TAG, "result=" + result + ",status=" + status);
        if (status == ICallbackStatus.OFFLINE_STEP_SYNC_OK) {
            //离线步数同步完成
            writeIn = false;
            RCLog.i(TAG, "步数同步完成，开始读取步数数据");
            if (lastInstructType == RCBluetoothDataType.DATATYPE_STEP_HISTORY) {
                //查询历史步数
                int stepTime = Integer.parseInt(RCSPDeviceSaveTime.getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.STEP, lastConnectMac));
                if (stepTime <= 0) {
                    updateDataInfo(mDBDeviceDuDoData.getHistoryStep());
                } else {
                    updateDataInfo(mDBDeviceDuDoData.getHistoryStep(stepTime));
                }

                RCSPDeviceSaveTime.saveGetDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.STEP, lastConnectMac);
            }
            if (lastInstructType == RCBluetoothDataType.DATATYPE_STEP_TODAY) {
                //查询今日步数
                if (lastStepData != null)
                    updateDataInfo(new JSONArray().put(lastStepData.getJSON()));
                else
                    updateDataInfo(new JSONArray());
//                    updateDataInfo(mDBDeviceDuDoData.getHistoryStep(Integer.parseInt(DateUtil.getFormatNow("yyyyMMdd"))));


            }
        } else if (status == ICallbackStatus.OFFLINE_SLEEP_SYNC_OK) {
            //离线睡眠同步完成
            writeIn = false;
            RCLog.i(TAG, "睡眠同步完成，开始读取睡眠数据");
            if (lastInstructType == RCBluetoothDataType.DATATYPE_SLEPP_HISTORY) {
                //查询历史睡眠
                int sleepTime = Integer.parseInt(RCSPDeviceSaveTime.getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.SLEEP_TIME, lastConnectMac));
                if (sleepTime <= 0) {
                    updateDataInfo(mDBDeviceDuDoData.getHistorySleepNew());
                } else {
                    updateDataInfo(mDBDeviceDuDoData.getHistorySleepNew(sleepTime));
                }

                RCSPDeviceSaveTime.saveGetDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.SLEEP_TIME, lastConnectMac);
            }
            if (lastInstructType == RCBluetoothDataType.DATATYPE_SLEPP_TODAY) {
                //查询今日睡眠
                updateDataInfo(mDBDeviceDuDoData.getHistorySleepNew(Integer.parseInt(DateUtil.getFormatNow("yyyyMMdd"))));
            }

        } else if (status == ICallbackStatus.OFFLINE_RATE_SYNC_OK) {
            writeIn = false;
            RCLog.i(TAG, "心率同步完成，开始读取心率");
            if (lastInstructType == RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY) {
                //查询历史心率
                int rateTime = Integer.parseInt(RCSPDeviceSaveTime.getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.Heart_Rate, lastConnectMac));
                if (rateTime <= 0) {
                    updateDataInfo(mDBDeviceDuDoData.getHistoryRate());
                } else {
                    updateDataInfo(mDBDeviceDuDoData.getHistoryRate(rateTime));
                }

                RCSPDeviceSaveTime.saveGetDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.Heart_Rate, lastConnectMac);
            }
            if (lastInstructType == RCBluetoothDataType.DATATYPE_HEARTR_ATE_TODAY) {
                //查询今日心率
                updateDataInfo(mDBDeviceDuDoData.getTodayRate());
            }

        } else if (status == ICallbackStatus.SYNC_TIME_OK) {// after set time
            /* 时间校准成功后，执行获取数据指令（ps:设备连接成功后先校准时间）*/
            writeIn = false;
            doInstruct(lastInstructType);
        } else if (status == ICallbackStatus.GET_BLE_VERSION_OK) {// after read
            // localBleVersion
            // finish,
            // then sync
            // step
            // mWriteCommand.syncAllStepData();
        } else if (status == ICallbackStatus.DISCONNECT_STATUS) {
            isConnect = false;
        } else if (status == ICallbackStatus.CONNECTED_STATUS) {
            isConnect = true;
        }
    }

    /**
     * 交通卡接口回调结果
     *
     * @param result true 通信完成
     * @param status 如 ICallbackStatus 类中 述
     * @param data   BLE 返回的数据
     */
    @Override
    public void OnDataResult(boolean result, int status, byte[] data) {

    }


    @Override
    public void OnServiceStatuslt(int status) {
        if (status == ICallbackStatus.BLE_SERVICE_START_OK) {
            if (mBluetoothLeService == null) {
                mBluetoothLeService = mBLEServiceOperate.getBleService();
                mBluetoothLeService.setICallback(this);
            }
        }
    }

    /**
     * 更新获取到的数据
     *
     * @param array 数据格式JSONArray
     */
    private void updateDataInfo(JSONArray array) {
        if (mGetDataListener != null) {
            mGetDataListener.dataInfo(array);
        }
    }

    private RCDeviceStepDataDTO lastStepData;

    /**
     * 计步监听 在这里更新UI
     */
    private StepChangeListener mOnStepChangeListener = new StepChangeListener() {

        @Override
        public void onStepChange(int steps, float distance, int calories) {
            if (!getDataTime(RCDeviceDeviceID.HF_DUDO, RCDeviceIndicatorID.STEP, lastConnectMac)
                    .equals(DateUtil.getFormatNow("yyyyMMdd"))) {
                return;
            }
            RCLog.d(TAG, "步数监听：steps =" + steps + ",distance =" + distance
                    + ",calories =" + calories + "sp=" + sp.getInt(GlobalVariable.YC_PED_LAST_HOUR_STEP_SP, -1));
            JSONArray jsonArray = new JSONArray();
            RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
            dto.setCal(calories);
            dto.setStep(steps);
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setDate(DateUtil.getFormatToday());
            dto.setKm(distance);
            jsonArray.put(dto.getJSON());
            lastStepData = dto;
            if (lastInstructType == RCBluetoothDoType.DO_GET_REALTIME_STEP_START)
                updateDataInfo(jsonArray);
        }

    };

    /**
     * 获取今天的步数
     */
    private void queryStepInfo() {
        StepInfo stepInfo = mySQLOperate.queryStepInfo(CalendarUtils.getCalendar(0));
        int steps = stepInfo.getStep();
        int calories = stepInfo.getCalories();
        float distance = stepInfo.getDistance();
        RCLog.d(TAG, "今日步数：steps =" + steps + ",distance =" + distance
                + ",calories =" + calories);
        JSONArray jsonArray = new JSONArray();
        RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
        dto.setCal(calories);
        dto.setStep(steps);
        dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
        dto.setDate(DateUtil.getFormatToday());
        dto.setKm(distance);
        jsonArray.put(dto.getJSON());
        if (mGetDataListener != null) {
            mGetDataListener.dataInfo(jsonArray);
        }
    }

    /**
     * 睡眠监听 在这里更新UI
     */
    private SleepChangeListener mOnSlepChangeListener = new SleepChangeListener() {

        @Override
        public void onSleepChange() {
            RCLog.i(TAG, "睡眠有回调了吗");
        }

    };

    /**
     * 获取今天睡眠详细，并更新睡眠UI CalendarUtils.getCalendar(0)代表今天，也可写成"20141101"
     * CalendarUtils.getCalendar(-1)代表昨天，也可写成"20141031"
     * CalendarUtils.getCalendar(-2)代表前天，也可写成"20141030" 以此类推
     */
    private void querySleepInfo() {
        SleepTimeInfo sleepTimeInfo = mySQLOperate.querySleepInfo(
                CalendarUtils.getCalendar(-1), CalendarUtils.getCalendar(0));
        int deepTime, lightTime, awakeCount, sleepTotalTime;
        if (sleepTimeInfo != null) {
            deepTime = sleepTimeInfo.getDeepTime();
            lightTime = sleepTimeInfo.getLightTime();
            awakeCount = sleepTimeInfo.getAwakeCount();
            sleepTotalTime = sleepTimeInfo.getSleepTotalTime();

            int[] colorArray = sleepTimeInfo.getSleepStatueArray();// 绘图中不同睡眠状态可用不同颜色表示，颜色自定义
            int[] timeArray = sleepTimeInfo.getDurationTimeArray();
            int[] timePointArray = sleepTimeInfo.getTimePointArray();

            RCLog.d("getSleepInfo", "Calendar=" + CalendarUtils.getCalendar(0)
                    + ",timeArray =" + timeArray + ",timeArray.length ="
                    + timeArray.length + ",colorArray =" + colorArray
                    + ",colorArray.length =" + colorArray.length
                    + ",timePointArray =" + timePointArray
                    + ",timePointArray.length =" + timePointArray.length);

//            double total_hour = ((float) sleepTotalTime / 60f);
//            DecimalFormat df1 = new DecimalFormat("0.0"); // 保留1位小数，带前导零
//
//            int deep_hour = deepTime / 60;
//            int deep_minute = (deepTime - deep_hour * 60);
//            int light_hour = lightTime / 60;
//            int light_minute = (lightTime - light_hour * 60);
//            int active_count = awakeCount;
//            String total_hour_str = df1.format(total_hour);
//
//            if (total_hour_str.equals("0.0")) {
//                total_hour_str = "0";
//            }

            JSONArray jsonArray = new JSONArray();
            RCDeviceSleepDataDTO dto = new RCDeviceSleepDataDTO();
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setDeep(deepTime);
            dto.setShallow(lightTime);
            dto.setAll(sleepTotalTime);
            dto.setUpNumber(awakeCount);

            jsonArray.put(dto.getJSON());
            if (mGetDataListener != null) {
                mGetDataListener.dataInfo(jsonArray);
            }

        }
    }

    /**
     * 心率监听 在这里更新UI
     */
    private RateChangeListener mOnRateListener = new RateChangeListener() {

        @Override
        public void onRateChange(int rate, int status) {
            if (lastInstructType == RCBluetoothDoType.DO_TEST_HEART_RATE_START) {
                writeIn = false;
                mWriteCommand.sendRateTestCommand(GlobalVariable.RATE_TEST_STOP);
            }
            JSONArray jsonArray = new JSONArray();
            RCDeviceHeartRateDataDTO dto = new RCDeviceHeartRateDataDTO();
            dto.setNumber(rate);
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setDate(DateUtil.getFormatToday());
            jsonArray.put(dto.getJSON());
//            if (mGetDataListener != null)
//                mGetDataListener.dataInfo(jsonArray);
            updateDataInfo(jsonArray);
        }

    };

    /**
     * 获取一天最新心率值、最高、最低、平均心率值
     */
    private void queryRateInfo() {

        List<RateOneDayInfo> detailInfo = mySQLOperate.queryRateOneDayDetailInfo(CalendarUtils.getCalendar(0));
        JSONArray jsonArray = new JSONArray();
        RCDeviceHeartRateDataDTO dto;
        for (int i = 0; i < detailInfo.size() && detailInfo.size() > 0; i++) {
            dto = new RCDeviceHeartRateDataDTO();
            RateOneDayInfo oneDayInfo = detailInfo.get(i);
            dto.setNumber(oneDayInfo.getRate());
            dto.setDeviceId(RCDeviceDeviceID.HF_DUDO);
            dto.setDate(oneDayInfo.getTime());
            jsonArray.put(dto.getJSON());
            if (mGetDataListener != null)
                mGetDataListener.dataInfo(jsonArray);
        }

        RCLog.i(TAG, "今日心率读取完成，可以更新ui" + jsonArray.toString());
    }

}
