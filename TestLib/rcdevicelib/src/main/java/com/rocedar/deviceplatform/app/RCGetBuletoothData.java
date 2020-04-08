package com.rocedar.deviceplatform.app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.device.bluetooth.BluetoothUtil;
import com.rocedar.deviceplatform.device.bluetooth.RCDeviceConfigUtil;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.ble.BluetoothBleUtils;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceSaveTime;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/25 下午4:17
 * 版本：V1.0
 * 描述：获取数据工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCGetBuletoothData {
    /**
     * 数据获取逻辑：
     * PS：
     * 1.有几个设备开几个线程去同时获取数据
     * 2.
     */

    private static String TAG = "RCDevice_binding_data_get";

    private Context mAppContext;

    private Handler handler;

    private Map<Integer, RCBlueTooth> blueToothMap;

    public RCGetBuletoothData(Context mAppContext) {
        this.mAppContext = mAppContext;
        handler = new Handler();
        blueToothMap = new HashMap<>();

    }

    /**
     * 断开所有连接
     */
    public void doDestroyDevice() {
        stopGetData();
        if (blueToothMap != null && blueToothMap.size() > 0) {
            Iterator iter = blueToothMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                if (blueToothMap.get(key) != null) {
                    blueToothMap.get(key).doDisconnect();
                }
            }
        }
        BluetoothBleUtils.getInstance(mAppContext).onDestroyAll();
    }

    /*----设备数据获取------S--*/
    private long lastGetDataUserId = -1;

    private List<RCDeviceAlreadyBindDTO> bindDeviceList;


    /**
     * 开始获取数据
     */
    public void startGetData() {
        if (lastGetDataUserId == -1) {
            lastGetDataUserId = RCSPBaseInfo.getLastUserId();
        } else {
            if (lastGetDataUserId != RCSPBaseInfo.getLastUserId()) {
                lastGetDataUserId = RCSPBaseInfo.getLastUserId();
                RCLog.e(TAG, "切换登录了，断开所有连接");
                doDestroyDevice();
            }
        }
        if (lastGetDataUserId == -1) {
            RCLog.e(TAG, "没有登录");
            return;
        }
        //用户绑定的设备列表
        bindDeviceList = RCSPDeviceInfo.getBlueToothInfo();

        RCLog.d(TAG, "开始获取数据,设备总个数->" + bindDeviceList.size());
        for (int i = 0; i < bindDeviceList.size(); i++) {
            initBluetooth(bindDeviceList.get(i).getDevice_id());
            doGetDeviceData(bindDeviceList.get(i));
        }

    }


    /**
     * 结束获取数据
     */
    public void stopGetData() {
        getDataRunnableRun.clear();
        for (String mac : getDataRunnableMap.keySet()) {
            handler.removeCallbacks(getDataRunnableMap.get(mac));
        }
        getDataRunnableMap.clear();
    }


    /**
     * 暂停获取数据
     *
     * @param mac
     */
    public void pauseGetData(String mac) {
        if (getDataRunnableRun.containsKey(mac)) {
            getDataRunnableRun.put(mac, true);
            if (getDataRunnableMap.containsKey(mac)) {
                handler.removeCallbacks(getDataRunnableMap.get(mac));
            }
        }
    }

    /**
     * 恢复获取数据
     *
     * @param mac
     */
    public void resumeGetData(String mac) {
        getDataRunnableRun.put(mac, false);
        if (!getDataRunnableMap.containsKey(mac) && bindDeviceList != null) {
            for (int i = 0; i < bindDeviceList.size(); i++) {
                if (bindDeviceList.get(i).getDevice_no().equals(mac)) {
                    if (!getDataRunnableMap.containsKey(mac))
                        getDataRunnableMap.put(mac, new GetDataRunnable(bindDeviceList.get(i)));
                }
            }
        }
        handler.post(getDataRunnableMap.get(mac));
    }

    private void initBluetooth(int deviceId) {
        if (blueToothMap.containsKey(deviceId))
            return;
        blueToothMap.put(deviceId,
                RCDeviceConfigUtil.getBluetoothImpl(mAppContext, deviceId));
    }

    private Map<String, Boolean> getDataRunnableRun = new HashMap<>();
    private Map<String, GetDataRunnable> getDataRunnableMap = new HashMap<>();

    private Map<String, Long> lastRunTime = new HashMap<>();

    private void doGetDeviceData(RCDeviceAlreadyBindDTO alreadyBindDTO) {
        if (getDataRunnableRun.containsKey(alreadyBindDTO.getDevice_no())
                && getDataRunnableRun.get(alreadyBindDTO.getDevice_no())) {
            if (lastRunTime.containsKey(alreadyBindDTO.getDevice_no())
                    && new Date().getTime() - lastRunTime.get(alreadyBindDTO.getDevice_no()) > 60 * 1000) {
                lastRunTime.put(alreadyBindDTO.getDevice_no(), 0L);
                getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), false);
            } else {
                //正在运行
                RCLog.i(TAG, "doGetDeviceData: 线程正在运行，%s", alreadyBindDTO.getDevice_no());
                return;
            }
        }
        if (!getDataRunnableMap.containsKey(alreadyBindDTO.getDevice_no()))
            getDataRunnableMap.put(alreadyBindDTO.getDevice_no(), new GetDataRunnable(alreadyBindDTO));
        handler.removeCallbacks(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()));
        handler.post(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()));
    }


    private class GetDataRunnable implements Runnable {

        private Map<String, Boolean> getDataIn = new HashMap<>();

        private RCDeviceAlreadyBindDTO alreadyBindDTO;

        //线程运行间隔（第一次获取数据完成后开始第二次数据获取时间间隔）
        private int wTime = 5 * 1000;

        public GetDataRunnable(RCDeviceAlreadyBindDTO alreadyBindDTO) {
            this.alreadyBindDTO = alreadyBindDTO;
            if (alreadyBindDTO.getDevice_id() == RCDeviceDeviceID.YD) {
                wTime = 1000 * 30;
            } else if (alreadyBindDTO.getDevice_id() == RCDeviceDeviceID.BONG_4
                    || alreadyBindDTO.getDevice_id() == RCDeviceDeviceID.BONG_2PH
                    || alreadyBindDTO.getDevice_id() == RCDeviceDeviceID.BONG_FIT) {
                wTime = 1000 * 60;
            }
        }

        private List<Integer> integerList;


        @Override
        public void run() {
            lastRunTime.put(alreadyBindDTO.getDevice_no(), new Date().getTime());
            RCLog.i(TAG, "开始运行获取数据线程，获取数据的设备MAC为%s，设备ID为%d", alreadyBindDTO.getDevice_no(), alreadyBindDTO.getDevice_id());
            if (!BluetoothUtil.checkBluetoothIsOpen()) {
                getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), false);
                RCLog.i(TAG, "获取%d设备（%s），蓝牙没有打开", alreadyBindDTO.getDevice_id(), alreadyBindDTO.getDevice_name());
                Activity activity = RCBaseManage.getScreenManger().currentActivity();
                if (activity != null)
                    BluetoothUtil.openBluetoothDialog(activity);
                //10S后重试
                handler.postDelayed(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()), 30 * 1000);
                return;
            }
            //getDataRunnableRun标记显示线程正在运行时，不执行获取数据线程
            //PS:出现该情况的两种可能为：1、服务中的保活定时器的调用。2、获取实时数据时发送的暂停获取数据的消息
            if (getDataRunnableRun.containsKey(alreadyBindDTO.getDevice_no()) &&
                    getDataRunnableRun.get(alreadyBindDTO.getDevice_no())) {
                RCLog.i(TAG, "停止获取%d设备（%s）", alreadyBindDTO.getDevice_id(), alreadyBindDTO.getDevice_name());
                return;
            }
            //最后保存的设备mac地址为空，设备已经解除绑定
            if (RCSPDeviceInfo.getBlueToothMac(alreadyBindDTO.getDevice_id()).equals("")) {
                //如果当前有连接，则断开连接
                if (blueToothMap.containsKey(alreadyBindDTO.getDevice_id())) {
                    if (blueToothMap.get(alreadyBindDTO.getDevice_id()).isConnect()) {
                        blueToothMap.get(alreadyBindDTO.getDevice_id()).doDisconnect();
                    }
                }
                for (int i = 0; i < bindDeviceList.size(); i++) {
                    if (bindDeviceList.get(i).getDevice_id() == alreadyBindDTO.getDevice_id()) {
                        bindDeviceList.remove(i);
                        return;
                    }
                }
                return;
            }
            if (!alreadyBindDTO.getDevice_no().equals(RCSPDeviceInfo.getBlueToothMac(alreadyBindDTO.getDevice_id()))) {
                RCLog.i(TAG, "MAC地址有变化");
                if (blueToothMap.containsKey(alreadyBindDTO.getDevice_id())) {
                    if (blueToothMap.get(alreadyBindDTO.getDevice_id()).isConnect()) {
                        blueToothMap.get(alreadyBindDTO.getDevice_id()).doDisconnect();
                    }
                } else {
                    initBluetooth(alreadyBindDTO.getDevice_id());
                }

                alreadyBindDTO.setDevice_no(RCSPDeviceInfo.getBlueToothMac(alreadyBindDTO.getDevice_id()));
                for (int i = 0; i < bindDeviceList.size(); i++) {
                    if (bindDeviceList.get(i).getDevice_id() == alreadyBindDTO.getDevice_id()) {
                        bindDeviceList.get(i).setDevice_no(RCSPDeviceInfo.getBlueToothMac(alreadyBindDTO.getDevice_id()));
                    }
                }
            }
            getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), true);
            if (blueToothMap.containsKey(alreadyBindDTO.getDevice_id())) {
                if (alreadyBindDTO.getDevice_id() == RCDeviceDeviceID.MJK_ANDROID) {
                    RCLog.i(TAG, "开始获取数据-mjk");
                    blueToothMap.get(alreadyBindDTO.getDevice_id()).sendInstruct(new RCBluetoothGetDataListener() {
                        @Override
                        public void getDataError(int status, String msg) {
                            RCLog.i(TAG, "MJK获取出错%d，msg为%s", status, msg);
                            getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), false);
                            handler.postDelayed(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()), wTime);
                        }

                        @Override
                        public void getDataStart() {

                        }

                        @Override
                        public void dataInfo(JSONArray array) {
                            RCLog.i(TAG, "MJK获取数据成功，数据为%s", array.toString());
                            if (getRealTimeDataListener != null)
                                getRealTimeDataListener.getDataOver(array);
                            saveData(array, alreadyBindDTO.getDevice_no());
                        }
                    }, alreadyBindDTO.getDevice_no(), RCBluetoothDoType.DO_GET_REALTIME_STRP_AND_RUN_START);
                } else {
                    RCLog.i(TAG, "开始获取数据------");
                    if (integerList == null || integerList.size() == 0) {
                        integerList = getDeviceInstruct(alreadyBindDTO.getDevice_id(), alreadyBindDTO.getDevice_no());
                    }
                    if (integerList == null || integerList.size() == 0) {
                        getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), false);
                        return;
                    }
                    final int instruct = integerList.get(0);
                    integerList.remove(0);
                    RCLog.i(TAG, "开始获取数据-----" + instruct);
                    final String key = RCUtilEncode.getMd5StrUpper16(instruct + alreadyBindDTO.getDevice_no() + new Date().getTime());
                    getDataIn.put(key, true);
                    blueToothMap.get(alreadyBindDTO.getDevice_id()).sendInstruct(
                            new RCBluetoothGetDataListener() {
                                @Override
                                public void getDataError(int status, String msg) {
                                    if (!getDataIn.containsKey(key) || !getDataIn.get(key)) {
                                        return;
                                    }
                                    if (getDataIn.containsKey(key) && getDataIn.get(key)) {
                                        getDataIn.remove(key);
                                    }
                                    if (getBluetoothDataTestListener != null) {
                                        getBluetoothDataTestListener.getDataOver(
                                                String.format("获取%d设备(%s)数据出错，错误码：，msg：",
                                                        alreadyBindDTO.getDevice_id(), alreadyBindDTO.getDevice_no()
                                                        , status, msg)
                                        );
                                    }
                                    RCLog.i(TAG, "获取%d设备数据出错，错误消息%s，消息ID：%d", alreadyBindDTO.getDevice_id(), msg, status);
                                    if (status == RCBluetoothError.ERROR_DEVICE_BUSY) {
                                        return;
                                    }
                                    if (status == RCBluetoothError.ERROR_CONNECT) {
                                        if (getDataRunnableMap.containsKey(alreadyBindDTO.getDevice_no())) {
                                            handler.removeCallbacks(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()));
                                        }
                                        if (blueToothMap.containsKey(alreadyBindDTO.getDevice_no())
                                                && blueToothMap.get(alreadyBindDTO.getDevice_no()).isConnect()) {
                                            blueToothMap.get(alreadyBindDTO.getDevice_no()).doDisconnect();
                                        }
                                        getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), false);
                                        if (getDataRunnableMap.containsKey(alreadyBindDTO.getDevice_no()))
                                            handler.postDelayed(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()), Math.max(wTime, 5 * 1000));
                                        return;
                                    }
                                    if (getDataRunnableRun.containsKey(alreadyBindDTO.getDevice_no())
                                            && getDataRunnableRun.get(alreadyBindDTO.getDevice_no())) {
                                        if (getDataRunnableMap.containsKey(alreadyBindDTO.getDevice_no())) {
                                            getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), false);
                                            handler.postDelayed(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()), wTime);
                                        }
                                    } else {
                                        handler.removeCallbacks(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()));
                                    }
                                }

                                @Override
                                public void getDataStart() {
                                    RCLog.i(TAG, "获取%d设备数据开始，获取的数据类型为%d", alreadyBindDTO.getDevice_id(), instruct);
                                    if (getBluetoothDataTestListener != null) {
                                        getBluetoothDataTestListener.getDataOver(
                                                String.format("开始获取%d设备(%s)的%d数据。",
                                                        alreadyBindDTO.getDevice_id(), alreadyBindDTO.getDevice_no()
                                                        , instruct)
                                        );
                                    }
                                }

                                @Override
                                public void dataInfo(JSONArray array) {
                                    if (!getDataIn.containsKey(key) || !getDataIn.get(key)) {
                                        return;
                                    }
                                    if (getDataIn.containsKey(key) && getDataIn.get(key)) {
                                        getDataIn.remove(key);
                                    }
                                    RCLog.i(TAG, "获取数据成功，数据为%s", array.toString());
                                    if (getRealTimeDataListener != null)
                                        getRealTimeDataListener.getDataOver(array);
                                    if (getBluetoothDataTestListener != null) {
                                        getBluetoothDataTestListener.getDataOver(
                                                String.format("%d设备(%s)数据获取数据成功，数据为：\n%s",
                                                        alreadyBindDTO.getDevice_id(), alreadyBindDTO.getDevice_no(),
                                                        array.toString())
                                        );
                                    }
                                    saveData(array, alreadyBindDTO.getDevice_no());
                                    if (alreadyBindDTO.getDevice_id() == RCDeviceDeviceID.HF_DUDO) {
                                        if (instruct == RCBluetoothDataType.DATATYPE_STEP_HISTORY) {
                                            RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                    , RCDeviceIndicatorID.STEP, alreadyBindDTO.getDevice_no());
                                        }
                                        if (instruct == RCBluetoothDataType.DATATYPE_STEP_TODAY) {
                                            if (lastGetIsToday(alreadyBindDTO.getDevice_id(), RCDeviceIndicatorID.STEP, alreadyBindDTO.getDevice_no())) {
                                                RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                        , RCDeviceIndicatorID.STEP, alreadyBindDTO.getDevice_no());
                                            }
                                        }
                                        if (instruct == RCBluetoothDataType.DATATYPE_SLEPP_HISTORY) {
                                            RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                    , RCDeviceIndicatorID.SLEEP_TIME, alreadyBindDTO.getDevice_no());
                                        }
                                        if (instruct == RCBluetoothDataType.DATATYPE_STEP_HISTORY) {
                                            RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                    , RCDeviceIndicatorID.Heart_Rate, alreadyBindDTO.getDevice_no());
                                        }
                                        if (instruct == RCBluetoothDataType.DATATYPE_STEP_TODAY) {
                                            if (lastGetIsToday(alreadyBindDTO.getDevice_id(), RCDeviceIndicatorID.Heart_Rate, alreadyBindDTO.getDevice_no())) {
                                                RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                        , RCDeviceIndicatorID.Heart_Rate, alreadyBindDTO.getDevice_no());
                                            }
                                        }
                                    } else {
                                        if (instruct == RCBluetoothDataType.DATATYPE_SLEPP_TODAY
                                                || instruct == RCBluetoothDataType.DATATYPE_SLEPP_HISTORY) {
                                            RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                    , RCDeviceIndicatorID.SLEEP_TIME, alreadyBindDTO.getDevice_no());
                                        }
                                        if (instruct == RCBluetoothDataType.DATATYPE_STEP_HISTORY) {
                                            RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                    , RCDeviceIndicatorID.STEP, alreadyBindDTO.getDevice_no());
                                        }
                                        if (instruct == RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY) {
                                            RCSPDeviceSaveTime.saveGetDataTime(alreadyBindDTO.getDevice_id()
                                                    , RCDeviceIndicatorID.Heart_Rate, alreadyBindDTO.getDevice_no());
                                        }
                                        if (getDataRunnableRun.containsKey(alreadyBindDTO.getDevice_no())
                                                && getDataRunnableRun.get(alreadyBindDTO.getDevice_no())) {
                                            if (getDataRunnableMap.containsKey(alreadyBindDTO.getDevice_no())) {
                                                getDataRunnableRun.put(alreadyBindDTO.getDevice_no(), false);
                                                handler.postDelayed(getDataRunnableMap.get(alreadyBindDTO.getDevice_no()), wTime);
                                            }
                                        }
                                    }
                                }

                            }, alreadyBindDTO.getDevice_no(), instruct
                    );
                }
            }
        }

    }


    /**
     * 实时数据中获取数据监听
     */
    private GetBluetoothDataListener getRealTimeDataListener;

    public void setGetRealTimeDataListener(GetBluetoothDataListener getRealTimeDataListener) {
        this.getRealTimeDataListener = getRealTimeDataListener;
    }


    public interface GetBluetoothDataListener {

        void getDataOver(JSONArray info);

    }

    private GetBluetoothDataTestListener getBluetoothDataTestListener;

    public void setGetBluetoothDataTestListener(GetBluetoothDataTestListener getBluetoothDataTestListener) {
        this.getBluetoothDataTestListener = getBluetoothDataTestListener;
    }

    public interface GetBluetoothDataTestListener {
        void getDataOver(String info);

    }


    //------------------指令集计算逻辑（不同的设备需要定制不同的指令集）
    //---PS:所有设备基本遵循以下逻辑，首先判断是否已经获取设备的历史数据，如果没有获取设备的历史数据，则先获取设备的
    //历史数据，如果已经获取了设备的历史数据，则开始获取设备今天的数据

    /**
     * 获得设备的指令集
     */
    private List<Integer> getDeviceInstruct(int deviceId, String mac) {
        if (RCSPDeviceInfo.getBlueToothMac(deviceId).equals("")) {
            if (getDataRunnableMap.containsKey(mac)) {
                handler.removeCallbacks(getDataRunnableMap.get(mac));
            }
            if (blueToothMap.containsKey(mac)) {
                blueToothMap.get(mac).doDisconnect();
                blueToothMap.remove(mac);
            }
            a:
            for (int i = 0; i < bindDeviceList.size(); i++) {
                if (bindDeviceList.get(i).getDevice_no().equals(mac)) {
                    bindDeviceList.remove(i);
                    break a;
                }
            }
            return null;
        }
        List<Integer> integers = new ArrayList<>();
        switch (deviceId) {
            case RCDeviceDeviceID.BZL:
                //1.判断是否需要取历史步数，是则取历史步数
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.STEP, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_STEP_HISTORY);
                }
                //2.取今日步数
                integers.add(RCBluetoothDataType.DATATYPE_STEP_TODAY);
                //3.判断是否需要取历史睡眠，是则取历史睡眠和今天睡眠
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.SLEEP_TIME, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_SLEPP_HISTORY);
                    integers.add(RCBluetoothDataType.DATATYPE_SLEPP_TODAY);
                }
                //4.判断是否需要取历史心率，是则取历史心率
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.Heart_Rate, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY);
                }
                //5.取今日最新心率
                integers.add(RCBluetoothDataType.DATATYPE_HEARTR_ATE_TODAY);
                break;
            case RCDeviceDeviceID.MJK_ANDROID:
                integers.add(RCBluetoothDataType.DATATYPE_STEP_AND_RUN_NOW);
                break;
            case RCDeviceDeviceID.YD:
                integers.add(RCBluetoothDataType.DATATYPE_STEP_TODAY);
                break;
            case RCDeviceDeviceID.HEHAQI:
                //1.判断是否需要取历史步数，是则取历史步数
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.STEP, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_STEP_HISTORY);
                }
                //2.取今日步数
                integers.add(RCBluetoothDataType.DATATYPE_STEP_TODAY);
                //3.判断是否需要取历史睡眠，是则取历史睡眠和今天睡眠
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.SLEEP_TIME, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_SLEPP_HISTORY);
                }
                break;
            case RCDeviceDeviceID.HF_DUDO:
                //1.判断是否需要取历史步数，是则取历史步数
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.STEP, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_STEP_HISTORY);
                }
                //2.取今日步数
                if (!lastGetIsThisMinute(deviceId, RCDeviceIndicatorID.STEP, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_STEP_TODAY);
                }
                //3.判断是否需要取历史睡眠，是则取历史睡眠和今天睡眠
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.SLEEP_TIME, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_SLEPP_HISTORY);
                }
                //4.判断是否需要取历史心率，是则取历史心率
                if (!lastGetIsToday(deviceId, RCDeviceIndicatorID.Heart_Rate, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_HEARTR_ATE_HISTORY);
                }
                //5.取今日最新心率
                if (!lastGetIsThisMinute(deviceId, RCDeviceIndicatorID.STEP, mac)) {
                    integers.add(RCBluetoothDataType.DATATYPE_HEARTR_ATE_TODAY);
                }
                break;
            case RCDeviceDeviceID.BONG_4:
            case RCDeviceDeviceID.BONG_2PH:
                integers.add(RCBluetoothDoType.DO_SYNC);
                break;
            case RCDeviceDeviceID.BONG_FIT:
                integers.add(RCBluetoothDoType.DO_CONNECT);
                break;
            case RCDeviceDeviceID.NJ_SH:
                integers.add(RCBluetoothDataType.DATATYPE_STEP_TODAY);
                integers.add(RCBluetoothDataType.DATATYPE_STEP_HISTORY);
                break;
        }
        return integers;
    }

    private boolean lastGetIsThisMinute(int deviceId, int indicator, String mac) {
        if (RCSPDeviceSaveTime.getDataTimeD(deviceId, indicator, mac).startsWith(
                DateUtil.getFormatNow("yyyyMMddHHmm")
        )) {
            return true;
        }
        return false;
    }

    private boolean lastGetIsThisTenMinute(int deviceId, int indicator, String mac) {
        if (RCSPDeviceSaveTime.getDataTimeD(deviceId, indicator, mac).startsWith(
                DateUtil.getFormatNow("yyyyMMddHHm")
        )) {
            return true;
        }
        return false;
    }

    private boolean lastGetIsThisHours(int deviceId, int indicator, String mac) {
        if (RCSPDeviceSaveTime.getDataTimeD(deviceId, indicator, mac).startsWith(
                DateUtil.getFormatNow("yyyyMMddHH")
        )) {
            return true;
        }
        return false;
    }

    private boolean lastGetIsToday(int deviceId, int indicator, String mac) {
        if (RCSPDeviceSaveTime.getDataTime(deviceId, indicator, mac).equals(
                DateUtil.getFormatNow("yyyyMMdd")
        )) {
            return true;
        }
        return false;
    }

    //-----------数据保存逻辑（数据去重逻辑），主要是步数和跑步

    private Map<String, Integer> lastData = new HashMap<>();

    private void saveData(final JSONArray jsonArray, final String mac) {
        new Thread() { // 开启线程执行防止阻塞
            @Override
            public void run() {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    if (object.optInt("conduct_id") == RCDeviceConductID.WALK) {
                        String key = mac + "" + RCDeviceConductID.WALK;
                        if (!lastData.containsKey(key) || lastData.get(key) != object.optJSONObject("value").
                                optInt(RCDeviceIndicatorID.STEP + "")) {
                            RCUploadDevceData.saveBlueDeviceData(object);
                            lastData.put(key, object.optJSONObject("value").optInt(RCDeviceIndicatorID.STEP + ""));
                        }
                    } else if (object.optInt("conduct_id") == RCDeviceConductID.RUN) {
                        String key = mac + "" + RCDeviceConductID.RUN;
                        if (!lastData.containsKey(key) || lastData.get(key) !=
                                (object.optJSONObject("value").optInt(RCDeviceIndicatorID.RUN_STEP + "") +
                                        object.optJSONObject("value").optInt(RCDeviceIndicatorID.RUN_TIME + ""))) {
                            lastData.put(key,
                                    object.optJSONObject("value").optInt(RCDeviceIndicatorID.RUN_STEP + "") +
                                            object.optJSONObject("value").optInt(RCDeviceIndicatorID.RUN_TIME + ""));
                            RCUploadDevceData.saveBlueDeviceData(object);
                        }
                    } else {
                        RCUploadDevceData.saveBlueDeviceData(object);
                    }
                }
            }
        }.start();
    }


}
