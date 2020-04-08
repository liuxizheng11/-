package app.phj.com.testlib;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.rocedar.base.RCLog;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.LogUnit;
import com.rocedar.deviceplatform.app.RCGetBuletoothData;
import com.rocedar.deviceplatform.app.RCServiceUtil;
import com.rocedar.deviceplatform.app.RCUploadDevceData;
import com.rocedar.deviceplatform.device.phone.RCPhoneStepUtil;
import com.rocedar.deviceplatform.unit.DateUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/20 下午9:03
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class TestService extends Service {


    private String TAG = "TestService";


    private RCPhoneStepUtil phoneStepUtil;

    private RCServiceUtil rcServiceUtil;

    private Context mAppContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


//    private RCBindingDeviceDataGet dateUtil;

    private static String logFrom = "服务";

    public static void writeSD(String info) {
        LogUnit.writeDeviceLogtoSD(
                LogUnit.logString(
                        info
                        , logFrom)
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        phoneStepUtil = RCPhoneStepUtil.getInstance(mAppContext);
        rcServiceUtil = new RCServiceUtil(this);
        RCLog.d(TAG, "Service开启－Handler");
        initHandler();
        RCLog.d(TAG, "Service开启－Timer");
        initTimer();
        RCLog.d(TAG, "Service开启－初始化计步器");
        writeSD("服务启动");
        rcServiceUtil.getBuletoothData().setGetBluetoothDataTestListener(
                new RCGetBuletoothData.GetBluetoothDataTestListener() {
                    @Override
                    public void getDataOver(String info) {
                        Intent intent = new Intent("con.rc.test.getdatainfo");
                        intent.putExtra("info", info);
                        sendBroadcast(intent);
                    }
                }
        );
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        writeSD("服务结束");
        closeTimer();
        phoneStepUtil.closeUtil();
        rcServiceUtil.onDestory();
    }

    /**
     * 上传数据定时器
     */
    private final static int Minute_5 = 1000 * 5;

    /**
     * 线程保护定时器
     */
    private final static int Minute_60 = 1000 * 60;


    //----------------定时器－－－－－－－－－－－S
    /**
     * 10s任务（节点记录）
     */
    private Timer timerRecords;

    private class TaskRecord extends TimerTask {

        @Override
        public void run() {
            RCLog.d(TAG, "10s定时任务：" + DateUtil.getFormatNow("HHmmssSSS"));
            Message message = new Message();
            message.what = TEN_SECOND;
            timeHandler.sendMessage(message);
        }
    }


    /**
     * 10分钟定时任务（上传数据，第三方数据获取）
     */
    private Timer timerUploads;

    private class TaskUpload extends TimerTask {

        @Override
        public void run() {
            RCLog.d(TAG, "5分钟定时任务：" + DateUtil.getFormatNow("HHmmssSSS"));
            Message message = new Message();
            message.what = FIVE_MINUTES;
            timeHandler.sendMessage(message);
        }
    }

    /**
     * 开启Timer
     */
    private void initTimer() {
        /* 定时器－生成节点*/
        if (timerRecords != null) {
            timerRecords.cancel();
        }
        timerRecords = new Timer();
        timerRecords.schedule(new TaskRecord(), 10000, Minute_5);
        /* 定时器－数据*/
        if (timerUploads != null) {
            timerUploads.cancel();
        }
        timerUploads = new Timer();
        timerUploads.schedule(new TaskUpload(), 10000, Minute_60);
    }

    /**
     * 关闭Timer
     */
    private void closeTimer() {
        /** 服务结束时 关闭任务*/
        if (timerRecords != null) {
            timerRecords.cancel();
            timerRecords = null;
        }
        if (timerUploads != null) {
            timerUploads.cancel();
            timerUploads = null;
        }
    }

    private Handler timeHandler;


    private final static int TEN_SECOND = 1;

    private final static int FIVE_MINUTES = 2;


    private void initHandler() {
        timeHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TEN_SECOND:
                        phoneStepUtil.GenerateNodes_New();
                        writeSD(LogUnit.s13);
                        if (RCSPBaseInfo.getLastUserId() > 0
                                && !RCSPBaseInfo.getLastToken().equals("")) {
                            RCUploadDevceData.postBlueDeviceData(mAppContext);
                        } else {
                            writeSD(String.format(LogUnit.s14, "未登录"));
                        }
                        break;
                    case FIVE_MINUTES:
                        writeSD(LogUnit.s7);
                        RCLog.i(TAG, "FIVE_MINUTES->");
                        if (RCSPBaseInfo.getLastUserId() > 0
                                && !RCSPBaseInfo.getLastToken().equals("")) {
                            RCLog.i(TAG, "数据获取保活线程运行-有登录");
                            rcServiceUtil.getBuletoothData().startGetData();
                        } else {
                            RCLog.i(TAG, "数据获取保活线程运行-没有登录");
                            rcServiceUtil.getBuletoothData().doDestroyDevice();
                            writeSD(String.format(LogUnit.s8, "未登录"));
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }


    //---------------------定时器－－－－－－－－－－－E

}
