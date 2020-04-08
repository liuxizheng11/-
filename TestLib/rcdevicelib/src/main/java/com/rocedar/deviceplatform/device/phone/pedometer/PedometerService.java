package com.rocedar.deviceplatform.device.phone.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.PowerManager;

import com.rocedar.base.RCLog;

/**
 * 计步传感器的后台服务
 */
public class PedometerService {

    private static SensorManager mSensorManager;// 传感器服务
    private static PedometerManager manager;// 传感器监听对象
    private PowerManager.WakeLock mWakeLock;


    private String TAG = "RCDevice_phone_stepService";

    private Context context;

    public PedometerService(Context context) {
        this.context = context;
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);// CPU保存运行
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);// 屏幕熄掉后依然运行
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mWakeLock.acquire();
        context.registerReceiver(mReceiver, filter);

    }

    private boolean isScreenOn = true;

    private Handler handler = new Handler();

    public void initStep() {
        RCLog.e(TAG, "打开/重启了计步器服务");
        handler.removeCallbacks(serviceRunable);
        handler.postDelayed(serviceRunable, 1000 * 2);
    }

    private Runnable serviceRunable = new Runnable() {
        public void run() {
            //execute the task
            manager = PedometerContext.getManager();
            //获取传感器服务
            mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
            mSensorManager.unregisterListener(manager);
            if (mSensorManager.getSensorList(Sensor.TYPE_ALL) == null || mSensorManager.getSensorList(Sensor.TYPE_ALL).size() < 1) {
                mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
                boolean booltSensor = false;
                if (mSensorManager.getSensorList(Sensor.TYPE_ALL) != null) {
                    booltSensor = true;
                }
                RCLog.e(TAG, "SensorManager boolt" + booltSensor);
                if (booltSensor) return;
            }
            registerSensor();
            handler.postDelayed(serviceRunable, 1000 * 60);
        }
    };

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                RCLog.e(TAG, "关闭屏幕");
                isScreenOn = false;
                registerSensor();
                return;
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                RCLog.e(TAG, "打开屏幕");
                isScreenOn = true;
                registerSensor();
                return;
            }
        }
    };

    private void registerSensor() {
        if (mSensorManager != null) {//取消监听后重写监听，以保持后台运行
            mSensorManager.unregisterListener(manager);
            Sensor sensorCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            Sensor sensorDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            Sensor sensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //注册传感器，传感器为null，registerListener返回false不做处理
            int type = isScreenOn ? SensorManager.SENSOR_DELAY_FASTEST : SensorManager.SENSOR_DELAY_NORMAL;
            if (sensorCounter != null) {
                boolean boolt = mSensorManager.registerListener(manager, sensorCounter, type);
                RCLog.w(TAG, "注册mSensorManager－>sensorCounter->" + boolt);
                if (boolt)
                    return;
            }
            if (sensorDetector != null && isScreenOn) {
                //mSensorManager.registerListener(manager, sensorDetector, );
                boolean boolt = mSensorManager.registerListener(manager, sensorDetector, type);
                RCLog.w(TAG, "注册mSensorManager－>sensorDetector->" + boolt);
                if (boolt)
                    return;
            }
            if (sensorAcc != null) {
                boolean boolt = mSensorManager.registerListener(manager, sensorAcc, type);
                RCLog.w(TAG, "注册mSensorManager－>sensorAcc->" + boolt);
                if (boolt)
                    return;
            }
        }
    }


//    public void closeAll() {
//        if (manager != null) {
//            mSensorManager.unregisterListener(manager);
//        }
//        handler.removeCallbacks(serviceRunable);
//    }
}
