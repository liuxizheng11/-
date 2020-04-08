package com.rocedar.deviceplatform.device.phone.pedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.device.phone.listener.InterfaceCallBack;

import java.util.List;

/**
 * 计步器控制模块
 */
public class PedometerManager implements SensorEventListener {
    private Context mContext;

    private String TAG = "RCDevice_phone_step";

    private InterfaceCallBack.StepNumCallback mStepNumCallback;         //步数回调
    private InterfaceCallBack.StepDetectorCallback mStepDetectorCallback;       //踩下脚步时回调


    /*加速度传感器模拟计步*/
    public static float SENSITIVITY = 10;   //灵敏度1，9753 2.9630 4.4444 6.6667 10  15  22.5 33.75
    private float mLastValues[] = new float[3 * 2];


    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;

    private PedometerService pedometerService;


    public PedometerManager(Context context) {
        mContext = context;
        pedometerService = new PedometerService(mContext);
    }


    private boolean stepCounter_Enabled = false; // 系统是否带有计步传感器(步数)
    private boolean stepDetector_Enabled = false; // 系统是否带有计步传感器(踩下脚步)
    private boolean accelerometer_Enabled = false; // 系统是否带有加速度传感器

    private float mScale[] = new float[2];
    private float mYOffset;

    /**
     * 判断设备支持的计步方式
     * 1:计步传感器
     * 2：加速度传感器
     * 0:设备不具备计步功能
     */
    public int initSC(int h) {
        RCLog.d(TAG, "------------------" + h);
        //获取传感器
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);

        //获取全部感应器
        final List<Sensor> allSensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
        try {
            int sz = allSensor.size();            //获取传感器数量
            for (int i = 0; i < sz; i++) {
                Sensor s = allSensor.get(i);
                //  常量值
                if (s.getType() == Sensor.TYPE_ACCELEROMETER) {                 /*加速度传感器*/
                    accelerometer_Enabled = true;
                    mYOffset = h * 0.5f;
                    mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
                    mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
                    //SENSITIVITY = 3;
                } else if (s.getType() == Sensor.TYPE_STEP_DETECTOR) {                /*踩下脚步传感器*/
                    stepDetector_Enabled = true;
                } else if (s.getType() == Sensor.TYPE_STEP_COUNTER) {                    /*计步传感器*/
                    stepCounter_Enabled = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stepCounter_Enabled == true || stepDetector_Enabled == true) {
            return 1;
        } else if (accelerometer_Enabled == true) {
            return 2;
        } else {
            return 0;
        }
    }

    //打开计步
    public void startPedometer() {
        pedometerService.initStep();
    }

//    //关闭计步
//    public void closePedometer() {
//        if (pedometerService != null) {
//            pedometerService.closeAll();
//        }
//
//    }

    //踩下脚步传感器回调
    public void setStepDetectorCallback(InterfaceCallBack.StepDetectorCallback stepDetectorCallback) {
        this.mStepDetectorCallback = stepDetectorCallback;
    }

    //计步回调
    public void setStepNumCallback(InterfaceCallBack.StepNumCallback stepNumCallback) {
        this.mStepNumCallback = stepNumCallback;
    }

    /*加速度传感器计步 记一步的结束时间*/
    private static long oneStepEnd = 0;
    /*加速度传感器计步 记一步的开始时间*/
    private static long oneStepStart = 0;


    //感应器接口
    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        /*计步传感器*/
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            RCLog.d(TAG, "传感器回调1->" + this);
            float fcs = event.values[0];
            if (mStepNumCallback != null)
                mStepNumCallback.onStepNumCallbackCou((int) fcs, event.timestamp);
            RCLog.d(TAG, "计步传感器回调");
            return;
        }
         /*踩下脚步传感器*/
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            RCLog.d(TAG, "传感器回调2->" + this);
            step();
            return;
        }
        /*加速度传感器（模拟计步传感器）*/
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
            if (j == 1) {
                float vSum = 0;
                for (int i = 0; i < 3; i++) {
                    final float v = mYOffset + event.values[i] * mScale[j];
                    vSum += v;
                }
                int k = 0;
                float v = vSum / 3;

                float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                if (direction == -mLastDirections[k]) {
                    // Direction changed
                    int extType = (direction > 0 ? 0 : 1); // minumum or
                    // maximum?
                    mLastExtremes[extType][k] = mLastValues[k];
                    float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);
                    if (diff > SENSITIVITY && diff < 500) {
                        RCLog.d(TAG, "加速度传感器-diff=" + diff + "<-->SENSITIVITY=" +
                                SENSITIVITY + "<-->" + (diff > SENSITIVITY));
                        boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                        boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                        boolean isNotContra = (mLastMatch != 1 - extType);
                        if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                            oneStepEnd = System.currentTimeMillis();
                            if (oneStepEnd - oneStepStart > 500) {// 一步的间隔时间不能小于500ms，此时判断为走了一步
                                mLastMatch = extType;
                                oneStepStart = oneStepEnd;
                                //计步
                                step2();
                            }
                        } else {
                            mLastMatch = -1;
                        }
                    }
                    mLastDiff[k] = diff;
                }
                mLastDirections[k] = direction;
                mLastValues[k] = v;
            }
            return;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //计步(计步传感器)
    private void step() {
        if (mStepDetectorCallback != null) {
            //踩下脚步
            RCLog.d(TAG, "PedometerManager", "踩下脚步传感器回调");
            mStepDetectorCallback.onStepDetectorCallback();
        }
    }

    //计步(加速度传感器)
    private void step2() {
        RCLog.d(TAG, "PedometerManager", "加速度传感器回调");
        if (mStepNumCallback != null) {
            mStepNumCallback.onStepNumCallbackAcc(1);
        }
    }

}
