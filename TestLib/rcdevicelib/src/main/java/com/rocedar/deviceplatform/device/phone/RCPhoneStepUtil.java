package com.rocedar.deviceplatform.device.phone;

import android.content.Context;
import android.os.SystemClock;
import android.view.WindowManager;

import com.rocedar.base.RCAndroid;
import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.app.RCUploadDevceData;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.db.step.DBDataStep;
import com.rocedar.deviceplatform.db.step.StepInfoDTO;
import com.rocedar.deviceplatform.device.phone.listener.InterfaceCallBack;
import com.rocedar.deviceplatform.device.phone.listener.RCPhonePedometerListener;
import com.rocedar.deviceplatform.device.phone.pedometer.PedometerContext;
import com.rocedar.deviceplatform.device.phone.pedometer.PedometerManager;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/16 下午2:47
 * 版本：V1.0
 * 描述：手机计步工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCPhoneStepUtil implements InterfaceCallBack.StepDetectorCallback,
        InterfaceCallBack.StepNumCallback {


    private static final String TAG = "RCDevice_phone_StepUtil";

    private Context mAppContext;


    private static RCPhoneStepUtil ourInstance;

    public static RCPhoneStepUtil getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new RCPhoneStepUtil(context);
        return ourInstance;
    }


    private RCPhoneStepUtil(Context mAppContext) {
        this.mAppContext = mAppContext;
        //如果是模拟器，不进行手机计步
        if (RCAndroid.isEmulator(mAppContext)) {
            return;
        }
        initAppPedometer();
        //清除10天前的计步数据
        getDbDataSteps().deleteTenDaysAgo();
    }


    /**
     * APP计步器-生成数据节点，并将数据保存到本地DB中(服务中调用)
     */
    public void GenerateNodes_New() {
        if (RCAndroid.isEmulator(mAppContext)) {
            return;
        }
        RCLog.d(TAG, "保存节点-->" + DateUtil.getFormatNow("yyyyMMddHHmmss"));
        StepInfoDTO infoNew = new StepInfoDTO();
        infoNew.setDate(DateUtil.getFormatNow("yyyyMMdd"));
        infoNew.setSensorType(stepDeviceType);
        infoNew.setTimes(Long.parseLong(DateUtil.getFormatNow("yyyyMMddHHmm")) / 10);
        infoNew.setStep(stepNum);
        infoNew.setOtherone("");
        //写入数据到DB
        getDbDataSteps().addStepInfo(infoNew);
        //清空记录
        stepNum = 0;
        countNumber = 0;
        if (countNumberAll > 0) {
            SPDeviceDataPhone.saveStepCounterInfo(countNumberAll);
        }
        RCLog.d(TAG, "10S记录一个数据节点-有步数记录一个节点\n时间：" + infoNew.getTimes());
    }


    private RCPhonePedometerListener rcPhonePedometerListener;


    public void setRcPhonePedometerListener(RCPhonePedometerListener rcPhonePedometerListener) {
        this.rcPhonePedometerListener = rcPhonePedometerListener;
    }

    /**
     * 传感器数据有变动时回调用该方法（通知APP更新数据）
     */
    private void sendStepToView() {
        int stepNumber = calculationTodaySteps();
        //保存到待上传列表
        JSONArray jsonArray = new JSONArray();
        RCDeviceStepDataDTO dto = new RCDeviceStepDataDTO();
        dto.setStep(stepNumber);
        dto.setDeviceId(RCDeviceDeviceID.Phone);
        dto.setDate(DateUtil.getFormatToday());
        jsonArray.put(dto.getJSON());
        RCUploadDevceData.saveBlueDeviceData(jsonArray);
        if (rcPhonePedometerListener != null) {
            rcPhonePedometerListener.stepChange(stepNumber);
        }
    }

    /**
     * 结束时调用，释放资源
     */
    public void closeUtil() {
        closedAppPedometer();
        if (getDbDataSteps() != null)
            getDbDataSteps().close();
    }


    //------------------手机计步－－－－－－－－－－S


    /**
     * 计步数据库工具类
     */
    private DBDataStep dbDataSteps;

    private DBDataStep getDbDataSteps() {
        RCLog.d(TAG, "使用数据库");
        if (dbDataSteps == null) {
            RCLog.d(TAG, "使用数据库为NUll，初始化");
            dbDataSteps = new DBDataStep(mAppContext);
        } else {
            RCLog.d(TAG, "使用数据库不为空");
            if (dbDataSteps.isclose()) {
                RCLog.d(TAG, "使用数据库不为空,但Close，重新NEW");
                dbDataSteps = new DBDataStep(mAppContext);
            } else {
                RCLog.d(TAG, "使用数据库不为空,没有Close");
            }
        }
        return dbDataSteps;
    }


    private PedometerManager pedometerManager;


    /**
     * 1个时间节点内记录的数据
     */
    private int stepNum = 0;

    //传感器类型
    private int stepDeviceType = -1;


    /**
     * APP计步器-初始化
     */
    private void initAppPedometer() {
        RCLog.d(TAG, "initAppPedometer-->(pedometerManager == null)" + (pedometerManager == null));
        /** Pedometer初始化*/
        if (pedometerManager == null) {

            WindowManager wm = (WindowManager) mAppContext
                    .getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            pedometerManager = PedometerContext.getManager();
            int i = pedometerManager.initSC(height);
            stepDeviceType = i;
            RCLog.d(TAG, "动吖计步器(计步传感器)-初始化监听" + i);
            if (i == 1) {
                pedometerManager.setStepDetectorCallback(this);
                pedometerManager.setStepNumCallback(this);
            } else if (i == 2) {
                pedometerManager.setStepDetectorCallback(this);
                pedometerManager.setStepNumCallback(this);
            }
        }
        /* 计步器开始*/
        pedometerManager.startPedometer();
    }


    /**
     * App计步器－关闭计步服务
     */
    private void closedAppPedometer() {
        RCLog.d(TAG, "closedAppPedometer");
        /** 服务结束时将数据保存*/
        GenerateNodes_New();
    }


    //加速度传感器存储上一步数据的时间，时间间隔小于300ms抛弃数据
    private long lastTimeTemp = 0;

    //行走步数回调(用的是加速度传感器)
    @Override
    public void onStepDetectorCallback() {
        long nowTime = Long.parseLong(DateUtil.getFormatNow("ddHHmmssSSS"));
        RCLog.d(TAG, "动吖计步器(加速度传感器)" + nowTime);
        if (nowTime - lastTimeTemp > 300) {
            ++stepNum;
            sendStepToView();
            lastTimeTemp = nowTime;
        }
    }


    //踩下脚步回调(用的是计步传感器)
    @Override
    public void onStepNumCallbackAcc(int num) {
        long nowTime = Long.parseLong(DateUtil.getFormatNow("ddHHmmssSSS"));
        RCLog.d(TAG, "动吖计步器（踩下脚步（一次计步））-回调步数" + nowTime);
        if (nowTime - lastTimeTemp > 100) {
            stepNum += num;
            sendStepToView();
            lastTimeTemp = nowTime;
        }
    }


    //踩下脚步回调(用的是计步传感器)
    @Override
    public void onStepNumCallbackCou(int num, long time) {
        RCLog.d(TAG, "动吖计步器(计步传感器)" + DateUtil.getFormatNow("ddHHmmssSSS"));
        setCountNumber(num);
    }


    private int countNumber = 0;
    private int countNumberAll = 0;


    /**
     * 计步传感器数据处理
     * 逻辑说明：
     * <p>
     * 前置说明1.计步传感器中获取的步数是从开机到现在的总步数
     * 前置说明2.计步传感器不能按天取数据
     * <p>
     * 逻辑步骤
     * 1.获取保存的计步传感器的最后获取时间是否存在并且是不是今天
     * >1-1.如果没有数据或者时间不在今天，判断开机时间是否在存储时间之前
     * >>1-1-1.如果存储时间在开机时间之前，则计步传感器的所有数据计入今天数据(记录到数据库中时存入缓存标识)
     * >>1-2-1.如果开机时间在存储时间之前，取存储的步数是否小于当前步数
     * >>>1-2-1-1.如果存储的步数小于当前步数，取步数差存入临时存储（10s时间节点内记录的数据）
     * >>>1-2-1-1.如果存储的步数大于当前步数，将计步传感器的所数据存入缓存，作为计步传感器今天步数的起点值
     * <p>
     * >1-2.如果在今天,判断开机时间是否在今天
     * >>1-2-1.如果开机时间在今天，则计步传感器的所有数据计入今天数据
     * >>1-2-1.如果开机时间不在今天，将计步传感器的所数据存入缓存，作为计步传感器今天步数的起点值
     *
     * @param num 传感器中的总数据
     */
    private void setCountNumber(int num) {
        countNumberAll = num;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        RCLog.d(TAG, "最后开机时间->" + elapsedRealtime + "<-是否在今天>" + (getTodayAllM() > elapsedRealtime));
        if (SPDeviceDataPhone.getStepCountertime() > 0
                && SPDeviceDataPhone.getStepCountertime() == Long.parseLong(DateUtil.getFormatNow("yyyyMMdd"))) {
            RCLog.d(TAG, "计步存储最后日期是是今天->" + SPDeviceDataPhone.getStepCountertime()
                    + "<->" + SPDeviceDataPhone.getStepCounterAll());
            //计步存储最后日期是是今天
            //开机时间在最后一次存储之后,计步全部是
            if ((new Date().getTime() - SPDeviceDataPhone.getStepCounterTimestamp()) > elapsedRealtime) {
                if (elapsedRealtime / 1000 < stepNum) {
                    stepNum = num;
                    countNumber = num;
                    RCLog.d(TAG, "计步存储最后日期是是今天，并且开机时间是今天" +
                            "-1->stepNum:" + stepNum + "<-countNumber->" + countNumber + "<-num->" + num);
                } else {
                    stepNum = 1;
                    countNumber = 1;
                    RCLog.e(TAG, "计步存储最后日期是是今天，并且开机时间是今天,计算异常" +
                            "-1->stepNum:" + stepNum + "<-countNumber->" + countNumber + "<-num->" + num);
                }
                sendStepToView();
            } else {
                //手机开机是在上次存储数据之前
                //1。记计步器步数 大于 存储的步数 取差值
                if (num - SPDeviceDataPhone.getStepCounterAll() >= 0 && SPDeviceDataPhone.getStepCounterAll() > 0) {
                    stepNum = (int) (stepNum + (num - SPDeviceDataPhone.getStepCounterAll() - countNumber));
                    countNumber = num - (int) SPDeviceDataPhone.getStepCounterAll();
                    RCLog.d(TAG, "计步存储最后日期是是今天-2->stepNum:" + stepNum + "<-countNumber->" + countNumber + "<-num->" + num);
                    sendStepToView();

                } else {
                    //1。记计步器步数 小于 存储的步数 全部算今天
                    RCLog.d(TAG, "计步存储最后日期是是今天-3->stepNum:" + stepNum + "<-countNumber->" + countNumber + "<-num->" + num);
                    SPDeviceDataPhone.saveStepCounterInfo(num);
                }
            }
        } else {
            //开机时间在今天,计步全部是今天的
            if (getTodayAllM() > elapsedRealtime) {
                if (elapsedRealtime / 1000 < stepNum) {
                    stepNum = num;
                    countNumber = num;
                } else {
                    stepNum = 1;
                    countNumber = 1;
                }
                RCLog.d(TAG, "开机时间在今天,计步全部是今天的-4->stepNum:" + stepNum + "<-countNumber->" + countNumber + "<-num->" + num);
                sendStepToView();
            }
            SPDeviceDataPhone.saveStepCounterInfo(num);
        }
    }


    /**
     * 计算出今天总步数，用户UI显示
     */
    public int calculationTodaySteps() {
        //当前步数显示为本地今日数据总和
        return stepNum + (int) getDbDataSteps().getStepNumberFromDate(DateUtil.getFormatNow("yyyyMMdd"), true);
    }


    /**
     * 获取今天总毫秒数，00:00:000到现在的毫秒数
     *
     * @return
     */
    private long getTodayAllM() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            return (new Date().getTime()) - simpleDateFormat.parse(DateUtil.getFormatNow("yyyyMMdd") + "000000000").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
