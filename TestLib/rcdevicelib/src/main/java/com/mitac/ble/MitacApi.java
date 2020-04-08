package com.mitac.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.mitac.callback.MitacAlarmCallback;
import com.mitac.callback.MitacBLEStateChangeCallback;
import com.mitac.callback.MitacBatteryLevelCallback;
import com.mitac.callback.MitacDateTimeCallback;
import com.mitac.callback.MitacDeviceModelCallback;
import com.mitac.callback.MitacDidConnectCallback;
import com.mitac.callback.MitacDidDisconnectCallback;
import com.mitac.callback.MitacEKGCallback;
import com.mitac.callback.MitacFWRamSizeCallback;
import com.mitac.callback.MitacFWVersionCallback;
import com.mitac.callback.MitacFirmwareModeCallback;
import com.mitac.callback.MitacGoalSettingCallback;
import com.mitac.callback.MitacHistorySleepCallback;
import com.mitac.callback.MitacMacAddrCallback;
import com.mitac.callback.MitacOTAUpdateCallback;
import com.mitac.callback.MitacProfileCallback;
import com.mitac.callback.MitacRealtimeActivityCallback;
import com.mitac.callback.MitacScanCallback;
import com.mitac.callback.MitacSetInfoCallback;
import com.mitac.callback.MitacSevenDaysActivityCallback;
import com.mitac.callback.MitacSleepStatusCallback;
import com.mitac.callback.MitacTimeToSleepCallback;
import com.mitac.callback.MitacUIDCallback;
import com.mitac.callback.MitacUUIDCallback;
import com.mitac.callback.MitacUnitDistanceCallback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MitacApi
{
  private static final String TAG = MitacApi.class.getSimpleName();
  private static final boolean bWriteData = false;
  private static Context mContext;
  private static MitacScanCallback mScanCallback;
  private static MitacDidConnectCallback mConnectCallback;
  private static MitacDidDisconnectCallback mDisconnectCallback;
  private static MitacEKGCallback mEKGCallback;
  private static MitacRealtimeActivityCallback mRealtimeActivityCallback;
  private static MitacFWVersionCallback mReceiveFWVersionCallback;
  private static MitacUUIDCallback mUUIDCallback;
  private static MitacDeviceModelCallback mDeviceModelCallback;
  private static MitacMacAddrCallback mMacAddrCallback;
  private static MitacDateTimeCallback mDateTimeCallback;
  private static MitacBatteryLevelCallback mBatteryLevelCallback;
  private static MitacProfileCallback mProfileCallback;
  private static MitacGoalSettingCallback mGoalSettingCallback;
  private static MitacSleepStatusCallback mSleepStatusCallback;
  private static MitacUIDCallback mUIDCallback;
  private static MitacAlarmCallback mAlarmCallback;
  private static MitacTimeToSleepCallback mTimeToSleepCallback;
  private static MitacSevenDaysActivityCallback mSevenDaysActivityCallback;
  private static MitacHistorySleepCallback mHistorySleepCallback;
  private static MitacSetInfoCallback mSetInfoCallback;
  private static MitacOTAUpdateCallback mOTAUpdateCallback;
  private static MitacFirmwareModeCallback mFWModeCallback;
  private static MitacUnitDistanceCallback mUnitDistCallback;
  private static MitacFWRamSizeCallback mFWRamSizeCallback;
  private static MitacBLEStateChangeCallback mBLEStateChangecallback;
  private static HashMap<MitacAttributes.ECommandStatus, Timer> mTimerList = new HashMap();
  private static MitacBluetoothLe mMitacBluetoothLe;
  private static MitacApi mMitacApi = null;
  private static int iEKGLastAge;
  private static int iEKGLastEthnicity;
  private static int iEKGLastType;
  private static int iEKGLastLevel;
  private static boolean bIsReStart = false;
  private static List<Integer> mEKGRaw = new ArrayList();
  private static List<MitacEKGData> mEKGResults = new ArrayList();
  static BufferedReader br = null;




  public static synchronized MitacApi getSharedInstance(Context context)
  {
    if (mMitacApi == null) {
      mMitacApi = new MitacApi(context);
    }
    return mMitacApi;
  }
  
  public MitacApi(Context context)
  {

    LoadLibrary();
  }





  public void onEKGDataReceived(int[] data, Error error)
  {
    if (error != null)
    {

      EKGEnd(false);
    }
    else
    {
    //  if (!bIsReStart) {
       EKGLoop(data);
    //  }



    }
  }

  

  private static void LoadLibrary()
  {
    System.loadLibrary("EKGLib");
    System.loadLibrary("mitaclib");
  }
  
  private static final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context context, Intent intent)
    {
      String action = intent.getAction();
      if ("com.mitac.bluetooth.le.ACTION_SCAN_RECEIVE".equals(action))
      {
        if (MitacApi.mScanCallback != null)
        {
          MitacBleDevice device = new MitacBleDevice(intent.getStringExtra("FIELD_DEVICE_NAME"), 
            intent.getStringExtra("FIELD_DEVICE_MAC"), 
            intent.getStringExtra("FIELD_DEVICE_TYPE"));
          
          MitacApi.mScanCallback.didScanWristBand(device, null);
        }
      }
      else if ("com.mitac.bluetooth.le.ACTION_GATT_CONNECTED".equals(action))
      {
        if (MitacApi.mConnectCallback != null)
        {
          MitacApi.mConnectCallback.didConnectToWristBand(MitacApi.mMitacBluetoothLe.getLastConnectDevice(), null);
          MitacApi.mConnectCallback = null;
        }
      }
      else if (("com.mitac.bluetooth.le.ACTION_GATT_DISCONNECTED".equals(action)) && 
        (MitacApi.mDisconnectCallback != null))
      {
        MitacApi.mDisconnectCallback.didDisconnectFromWristBand(MitacApi.mMitacBluetoothLe.getLastConnectDevice(), null);
        MitacApi.mDisconnectCallback = null;
      }
    }
  };
  
  public void scan(MitacScanCallback callback)
  {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (bluetoothAdapter == null)
    {
      callback.didScanWristBand(null, new Error(MitacError.ERROR_NOT_SUPPORT_BT.toString()));
      return;
    }
    if (!bluetoothAdapter.isEnabled())
    {
      callback.didScanWristBand(null, new Error(MitacError.ERROR_BT_DISABLE.toString()));
      return;
    }
    if (mMitacBluetoothLe.scanDevice() == SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didScanWristBand(null, new Error(MitacError.ERROR_ALREADY_CONNECTED.toString()));
      return;
    }
    mScanCallback = callback;
  }
  
  public void stopScan()
  {
    mScanCallback = null;
    
    mMitacBluetoothLe.stopScanDevice();
  }
  
  public void registerBLEStatusChangeReceiver(MitacBLEStateChangeCallback callback)
  {
    if (callback != null) {
      mBLEStateChangecallback = callback;
    }
  }
  
  public void unregisterBLEStatusChangeReceiver()
  {
    mBLEStateChangecallback = null;
  }
  
  public void connectToDevice(final String strMacAddr, final MitacDidConnectCallback callback)
  {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (bluetoothAdapter == null)
    {
      callback.didConnectToWristBand(null, new Error(MitacError.ERROR_NOT_SUPPORT_BT.toString()));
      return;
    }
    if (!bluetoothAdapter.isEnabled())
    {
      callback.didConnectToWristBand(null, new Error(MitacError.ERROR_BT_DISABLE.toString()));
      return;
    }
    if (mMitacBluetoothLe.getConnectStatus() == SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didConnectToWristBand(null, new Error(MitacError.ERROR_ALREADY_CONNECTED.toString()));
      return;
    }
    if (!checkBTAddrFormat(strMacAddr))
    {
      callback.didConnectToWristBand(null, new Error(MitacError.ERROR_WRONG_BTADDR.toString()));
      return;
    }
    if (mMitacBluetoothLe.getConnectStatus() == SampleGattAttributes.EConnectStatus.STATE_SCANING) {
      stopScan();
    }
    scan(new MitacScanCallback()
    {
      public void didScanWristBand(MitacBleDevice device, Error error)
      {
        if ((error == null) && (strMacAddr.compareTo(device.mStrMacAddr) == 0))
        {
          MitacApi.this.stopScan();
          try
          {
            Thread.sleep(250L);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
          if (!MitacApi.mMitacBluetoothLe.connect(strMacAddr))
          {
            callback.didConnectToWristBand(null, new Error(MitacError.ERROR_CONNECT_FAILED.toString()));
            
            return;
          }
          MitacApi.mConnectCallback = callback;
        }
      }
    });
  }
  
  private boolean checkBTAddrFormat(String strMacAddr)
  {
    String[] sub = strMacAddr.split(":");
    if (sub.length != 6) {
      return false;
    }
    String[] arrayOfString1;
    int j = (arrayOfString1 = sub).length;
    for (int i = 0; i < j; i++)
    {
      String str = arrayOfString1[i];
      int iValue = 0;
      try
      {
        iValue = Integer.parseInt(str, 16);
      }
      catch (NumberFormatException e)
      {
        return false;
      }
    }
    return true;
  }
  
  public void disconnect(MitacDidDisconnectCallback callback)
  {
    if ((mMitacBluetoothLe.getConnectStatus() == SampleGattAttributes.EConnectStatus.STATE_DISCONNECTED) || 
      (mMitacBluetoothLe.getConnectStatus() == SampleGattAttributes.EConnectStatus.STATE_SCANING))
    {
      callback.didDisconnectFromWristBand(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mMitacBluetoothLe.getConnectStatus() == SampleGattAttributes.EConnectStatus.STATE_CONNECTING)
    {
      callback.didDisconnectFromWristBand(null, new Error(MitacError.ERROR_CONNECTING.toString()));
    }
    else
    {
      mDisconnectCallback = callback;
      mMitacBluetoothLe.disconnect(true);
    }
    checkCallbackDisconnected();
  }
  
  private static void checkCallbackDisconnected()
  {
    if (mEKGCallback != null)
    {
      EKGEnd(true);
      mEKGCallback.didEKGDataReceived(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mEKGCallback = null;
    }
    if (mOTAUpdateCallback != null)
    {
      mOTAUpdateCallback.didOTAUpdating(-1.0F, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mOTAUpdateCallback = null;
    }
    if (mRealtimeActivityCallback != null)
    {
      mRealtimeActivityCallback.didActivityReceived(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mRealtimeActivityCallback = null;
    }
    if (mReceiveFWVersionCallback != null)
    {
      mReceiveFWVersionCallback.didReceiveFirmwareVersion(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mReceiveFWVersionCallback = null;
    }
    if (mUUIDCallback != null)
    {
      mUUIDCallback.didReceiveUUID(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mUUIDCallback = null;
    }
    if (mDeviceModelCallback != null)
    {
      mDeviceModelCallback.didReceiveDeviceModel(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mDeviceModelCallback = null;
    }
    if (mMacAddrCallback != null)
    {
      mMacAddrCallback.didReceiveMacAddress(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mMacAddrCallback = null;
    }
    if (mDateTimeCallback != null)
    {
      mDateTimeCallback.didReceiveDateTime(null, null, false, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mDateTimeCallback = null;
    }
    if (mBatteryLevelCallback != null)
    {
      mBatteryLevelCallback.didReceiveBatteryLevel(-1, false, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mBatteryLevelCallback = null;
    }
    if (mProfileCallback != null)
    {
      mProfileCallback.didReceiveUserProfile(-1, -1.0F, -1.0F, false, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mProfileCallback = null;
    }
    if (mGoalSettingCallback != null)
    {
      mGoalSettingCallback.didReceiveTarget(-1, -1, -1.0F, -1, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mGoalSettingCallback = null;
    }
    if (mSleepStatusCallback != null)
    {
      mSleepStatusCallback.didReceiveSleepMonitorStatus(false, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mSleepStatusCallback = null;
    }
    if (mUIDCallback != null)
    {
      mUIDCallback.didReceiveUID(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mUIDCallback = null;
    }
    if (mAlarmCallback != null)
    {
      mAlarmCallback.didReceiveAlarm(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mAlarmCallback = null;
    }
    if (mTimeToSleepCallback != null)
    {
      mTimeToSleepCallback.didReceiveTimeToSleep(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mTimeToSleepCallback = null;
    }
    if (mSevenDaysActivityCallback != null)
    {
      mSevenDaysActivityCallback.didReceiveSevenDaysActivityData(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mSevenDaysActivityCallback = null;
    }
    if (mHistorySleepCallback != null)
    {
      mHistorySleepCallback.didReceiveHistorySleep(null, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mHistorySleepCallback = null;
    }
    if (mSetInfoCallback != null)
    {
      mSetInfoCallback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mSetInfoCallback = null;
    }
    if (mFWModeCallback != null)
    {
      mFWModeCallback.didReceiveFirmwareMode(false, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mFWModeCallback = null;
    }
    if (mFWRamSizeCallback != null)
    {
      mFWRamSizeCallback.didReceiveFirmwareRAMSize(false, new Error(MitacError.ERROR_DISCONNECTED.toString()));
      mFWRamSizeCallback = null;
    }
  }
  
  public SampleGattAttributes.EConnectStatus getConnectStatus()
  {
    return mMitacBluetoothLe.getConnectStatus();
  }
  
  private static native int nativeJAnt_Create(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
 private static native int nativeJAnt_Destroy();
  
  private static native int EKGInitial(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private static native int EKGLoop(int[] paramArrayOfInt);
  
  private static native int EKGEnd(boolean paramBoolean);
  
  private static void EKGCallBack(int rr_ii, int rrInterval, int instantHR, int sdnnSys, int pnnSys, int poincareSys, int symbolicSys, int meanHR, int cpcScore, int matchResult, int catchUp, int poor_count, int good_count, int perfect_count, int successRatio, boolean isSuccess, boolean bIsFinalAnalysis, boolean isLeadoff, int Z, int[] final_RRInterval, int[] final_BeatTime, int flag, int QI)
  {
    MitacEKGData data = new MitacEKGData(rr_ii, rrInterval, 
      instantHR, sdnnSys, pnnSys, poincareSys, symbolicSys, 
      meanHR, cpcScore, matchResult, catchUp, poor_count, good_count, perfect_count, successRatio, isSuccess, isLeadoff, Z, final_RRInterval, final_BeatTime, QI);
    if ((flag == -2) || (flag == -3))
    {
      if (mEKGCallback != null)
      {
        if (flag == -2) {
          mEKGCallback.didEKGDataFinalAnalysis(data, new Error(MitacError.ERROR_MEASUREMENT_FAILURE.toString()));
        } else {
          mEKGCallback.didEKGDataFinalAnalysis(data, new Error(MitacError.ERROR_MEASURE_TIMEOUT.toString()));
        }
        mEKGCallback = null;
      }
     // mMitacBluetoothLe.stopReceivingEKGData();
    }
    else if (mEKGCallback != null)
    {
      if (bIsFinalAnalysis)
      {
        mEKGCallback.didEKGDataFinalAnalysis(data, null);
        
        mEKGCallback = null;
      }
      else
      {
        mEKGCallback.didEKGDataReceived(data, null);
      }
    }
  }
  
  public void startEKG(int iAge, int iEthnicity, int type, int level, MitacEKGCallback callback)
  {

    int AntStatus = EKGInitial(type, level, iAge, iEthnicity);
    if (AntStatus == 1)
    {
      Log.w("EKG", "EKGInitial Success!!");
      
      iEKGLastAge = iAge;
      iEKGLastEthnicity = iEthnicity;
      iEKGLastType = type;
      iEKGLastLevel = level;
      
      mEKGCallback = callback;
     // mMitacBluetoothLe.startReceivingEKGData(type);
    }
    else
    {
      Log.e("EKG", "EKGInitial Failed!!");
      callback.didEKGDataReceived(null, new Error(MitacError.ERROR_EKGINITIAL_FAILED.toString()));
      
      EKGEnd(false);
    }
  }
  
  public synchronized boolean restartEKGAlgorithm()
  {
    if (mEKGCallback == null) {
      return false;
    }
    bIsReStart = true;
    EKGEnd(true);
    try
    {
      Thread.sleep(200L);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    int AntStatus = EKGInitial(iEKGLastType, iEKGLastLevel, iEKGLastAge, iEKGLastEthnicity);
    bIsReStart = false;
    if (AntStatus == 1)
    {
      Log.w("EKG", "EKGInitial Success!!");
    }
    else
    {
      Log.e("EKG", "EKGInitial Failed!!");
      
      stopEKG();

      return false;
    }
    return true;
  }
  
  public synchronized void stopEKG()
  {
    EKGEnd(false);
    writeEKGRawData();
  }
  
  public void startRealtimeActivity(MitacRealtimeActivityCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didActivityReceived(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mRealtimeActivityCallback != null)
    {
      callback.didActivityReceived(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mRealtimeActivityCallback = callback;
    mMitacBluetoothLe.startReceivingActivityData();
  }
  
  public void stopRealtimeActivity()
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      if (mRealtimeActivityCallback != null) {
        mRealtimeActivityCallback.didActivityReceived(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      }
      return;
    }
    if (mRealtimeActivityCallback == null) {
      return;
    }
    mMitacBluetoothLe.stopReceivingActivityData();
    mRealtimeActivityCallback = null;
  }
  
  public synchronized void requestSevenDaysActivityData(MitacSevenDaysActivityCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveSevenDaysActivityData(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mSevenDaysActivityCallback != null)
    {
      callback.didReceiveSevenDaysActivityData(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mSevenDaysActivityCallback = callback;
    mMitacBluetoothLe.startSevenDaysActivityData();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mSevenDaysActivityCallback != null)
        {
          MitacApi.mSevenDaysActivityCallback.didReceiveSevenDaysActivityData(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mSevenDaysActivityCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_SEVENDAYSACTIVITY);
          MitacApi.mMitacBluetoothLe.stopSevenDaysActivityData();
        }
      }
    }, 5000L);
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_SEVENDAYSACTIVITY, timer);
  }
  
  public void requestFirmwareVersion(MitacFWVersionCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveFirmwareVersion(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mReceiveFWVersionCallback != null)
    {
      callback.didReceiveFirmwareVersion(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mReceiveFWVersionCallback = callback;
    mMitacBluetoothLe.getFWVersion();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mReceiveFWVersionCallback != null)
        {
          MitacApi.mReceiveFWVersionCallback.didReceiveFirmwareVersion(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mReceiveFWVersionCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_FWVERSION);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_FWVERSION, timer);
  }
  
  public void requestFirmwareRAMSize(MitacFWRamSizeCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveFirmwareRAMSize(false, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mFWRamSizeCallback != null)
    {
      callback.didReceiveFirmwareRAMSize(false, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mFWRamSizeCallback = callback;
    mMitacBluetoothLe.getFWRamSize();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mFWRamSizeCallback != null)
        {
          MitacApi.mFWRamSizeCallback.didReceiveFirmwareRAMSize(false, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mFWRamSizeCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_FWRAMSIZE);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_FWRAMSIZE, timer);
  }
  
  public void requestFirmwareMode(MitacFirmwareModeCallback callback)
  {
    if (getConnectStatus() == SampleGattAttributes.EConnectStatus.STATE_DISCONNECTED)
    {
      callback.didReceiveFirmwareMode(false, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mFWModeCallback != null)
    {
      callback.didReceiveFirmwareMode(false, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mFWModeCallback = callback;
    
    mMitacBluetoothLe.fetchFWMode();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mFWModeCallback != null)
        {
          MitacApi.mFWModeCallback.didReceiveFirmwareMode(false, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mFWModeCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_FWMODE);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_FWMODE, timer);
  }
  
  public void startOTAUpdate(String strFile, MitacOTAUpdateCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didOTAUpdating(-1.0F, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      
      return;
    }
    if (mOTAUpdateCallback != null)
    {
      callback.didOTAUpdating(-1.0F, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mOTAUpdateCallback = callback;
    mMitacBluetoothLe.initFirmwareUpdate(strFile);
  }
  
  public void requestSerialNumber(MitacUUIDCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveUUID(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mUUIDCallback != null)
    {
      callback.didReceiveUUID(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mUUIDCallback = callback;
    mMitacBluetoothLe.getDeviceUUID();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mUUIDCallback != null)
        {
          MitacApi.mUUIDCallback.didReceiveUUID(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mUUIDCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_UUID);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_UUID, timer);
  }
  
  public void getDeviceModel(MitacDeviceModelCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveDeviceModel(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mDeviceModelCallback != null)
    {
      callback.didReceiveDeviceModel(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mDeviceModelCallback = callback;
    mMitacBluetoothLe.getDeviceUUID();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mDeviceModelCallback != null)
        {
          MitacApi.mDeviceModelCallback.didReceiveDeviceModel(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mDeviceModelCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_MODEL);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_MODEL, timer);
  }
  
  public void getDeviceMacAddress(MitacMacAddrCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveMacAddress(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mMacAddrCallback != null)
    {
      callback.didReceiveMacAddress(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mMacAddrCallback = callback;
    mMitacBluetoothLe.getDeviceMacAddr();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mMacAddrCallback != null)
        {
          MitacApi.mMacAddrCallback.didReceiveMacAddress(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mMacAddrCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_MACADDR);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_MACADDR, timer);
  }
  
  public void requestBatteryLevel(MitacBatteryLevelCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveBatteryLevel(-1, false, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mBatteryLevelCallback != null)
    {
      callback.didReceiveBatteryLevel(-1, false, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mBatteryLevelCallback = callback;
    mMitacBluetoothLe.getBatteryLevel();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mBatteryLevelCallback != null)
        {
          MitacApi.mBatteryLevelCallback.didReceiveBatteryLevel(-1, false, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mBatteryLevelCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_BATTERYSTATUS);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_BATTERYSTATUS, timer);
  }
  
  public void updateDateTime(Date date, TimeZone timezone, boolean bIs12hrMode, boolean bIsReset, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    mMitacBluetoothLe.setDate(date, timezone, bIs12hrMode, bIsReset);
    startSetInfoTimer();
  }
  
  public void requestCurrentDateTime(MitacDateTimeCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveDateTime(null, null, false, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mDateTimeCallback != null)
    {
      callback.didReceiveDateTime(null, null, false, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mDateTimeCallback = callback;
    mMitacBluetoothLe.getDate();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mDateTimeCallback != null)
        {
          MitacApi.mDateTimeCallback.didReceiveDateTime(null, null, false, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mDateTimeCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_DATETIME);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_DATETIME, timer);
  }
  
  public void requestUserProfile(MitacProfileCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveUserProfile(-1, -1.0F, -1.0F, false, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mProfileCallback != null)
    {
      callback.didReceiveUserProfile(-1, -1.0F, -1.0F, false, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mProfileCallback = callback;
    mMitacBluetoothLe.getProfile();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mProfileCallback != null)
        {
          MitacApi.mProfileCallback.didReceiveUserProfile(-1, -1.0F, -1.0F, false, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mProfileCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_PROFILE);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_PROFILE, timer);
  }
  
  public void updateUserProfile(int iAge, float fHeight, float fWeight, boolean bIsMale, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    mMitacBluetoothLe.setProfileToDevice(iAge, fHeight, fWeight, bIsMale);
    startSetInfoTimer();
  }
  
  public void requestTargetSettings(MitacGoalSettingCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveTarget(-1, -1, -1.0F, -1, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mGoalSettingCallback != null)
    {
      callback.didReceiveTarget(-1, -1, -1.0F, -1, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mGoalSettingCallback = callback;
    mMitacBluetoothLe.getGoalFromDevice();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mGoalSettingCallback != null)
        {
          MitacApi.mGoalSettingCallback.didReceiveTarget(-1, -1, -1.0F, -1, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mGoalSettingCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_TARGET);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_TARGET, timer);
  }
  
  public void updateTarget(int iActivityTime, int iSteps, float fDistance, int iCalories, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    mMitacBluetoothLe.setGoalToDevice(iActivityTime, iSteps, (int)(fDistance * 1000.0F), iCalories);
    startSetInfoTimer();
  }
  
  public void controlSleepMonitor(boolean bStart, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    mMitacBluetoothLe.setSleepMonitor(bStart);
    startSetInfoTimer();
  }
  
  public void requestSleepMonitorStatus(MitacSleepStatusCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveSleepMonitorStatus(false, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mSleepStatusCallback != null)
    {
      callback.didReceiveSleepMonitorStatus(false, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mSleepStatusCallback = callback;
    mMitacBluetoothLe.getSleepStatus();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mSleepStatusCallback != null)
        {
          MitacApi.mSleepStatusCallback.didReceiveSleepMonitorStatus(false, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mSleepStatusCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_SLEEPSTATUS);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_SLEEPSTATUS, timer);
  }
  
  public void requestHistorySleepData(MitacHistorySleepCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveHistorySleep(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mHistorySleepCallback != null)
    {
      callback.didReceiveHistorySleep(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mHistorySleepCallback = callback;
    mMitacBluetoothLe.startReceivingSleepData();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mHistorySleepCallback != null)
        {
          MitacApi.mHistorySleepCallback.didReceiveHistorySleep(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_SLEEPDATA);
          MitacApi.mMitacBluetoothLe.stopReceivingSleepData();
          MitacApi.mHistorySleepCallback = null;
        }
      }
    }, 5000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_SLEEPDATA, timer);
  }
  
  public void updateUID(String strUID, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    try
    {
      byte[] strbyte = strUID.getBytes("UTF-8");
      if (strbyte.length > 32)
      {
        strbyte = Arrays.copyOfRange(strbyte, 0, 32);
        mMitacBluetoothLe.setUID(strbyte);
      }
      else if (strbyte.length < 32)
      {
        byte[] uidByte = new byte[32];
        Arrays.fill(uidByte, (byte)32);
        System.arraycopy(strbyte, 0, uidByte, 0, strbyte.length);
        
        mMitacBluetoothLe.setUID(uidByte);
      }
      else
      {
        mMitacBluetoothLe.setUID(strbyte);
      }
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    startSetInfoTimer();
  }
  
  public void requestUID(MitacUIDCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveUID(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mUIDCallback != null)
    {
      callback.didReceiveUID(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mUIDCallback = callback;
    mMitacBluetoothLe.getUID();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mUIDCallback != null)
        {
          MitacApi.mUIDCallback.didReceiveUID(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mUIDCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_UID);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_UID, timer);
  }
  
  public void updateAlarm(MitacAlarmData[] alarmData, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    if (alarmData.length != 5)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRONG_SIZE.toString()));
      return;
    }
    mMitacBluetoothLe.setAlarm(alarmData);
    startSetInfoTimer();
  }
  
  public void requestAlarm(MitacAlarmCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveAlarm(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mAlarmCallback != null)
    {
      callback.didReceiveAlarm(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mAlarmCallback = callback;
    mMitacBluetoothLe.getAlarm();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mAlarmCallback != null)
        {
          MitacApi.mAlarmCallback.didReceiveAlarm(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mAlarmCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_ALARM);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_ALARM, timer);
  }
  
  public void updateTimeToSleep(MitacAlarmData[] alarmData, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    if (alarmData.length != 4)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRONG_SIZE.toString()));
      return;
    }
    mMitacBluetoothLe.setTimeToSleep(alarmData);
    startSetInfoTimer();
  }
  
  public void requestTimeToSleep(MitacTimeToSleepCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveTimeToSleep(null, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mTimeToSleepCallback != null)
    {
      callback.didReceiveTimeToSleep(null, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mTimeToSleepCallback = callback;
    mMitacBluetoothLe.getTimeToSleep();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mTimeToSleepCallback != null)
        {
          MitacApi.mTimeToSleepCallback.didReceiveTimeToSleep(null, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mTimeToSleepCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_TIMETOSLEEP);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_TIMETOSLEEP, timer);
  }
  
  public void requestUnitDistance(MitacUnitDistanceCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveUnitFormatStatus(false, new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return;
    }
    if (mUnitDistCallback != null)
    {
      callback.didReceiveUnitFormatStatus(false, new Error(MitacError.ERROR_ALREADY_START.toString()));
      return;
    }
    mUnitDistCallback = callback;
    mMitacBluetoothLe.getUnitFormat();
    
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mUnitDistCallback != null)
        {
          MitacApi.mUnitDistCallback.didReceiveUnitFormatStatus(false, new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mUnitDistCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_GET_UNITFORMAT);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_GET_UNITFORMAT, timer);
  }
  
  public void updateUnitDistance(boolean bIsMetric, MitacSetInfoCallback callback)
  {
    if (!checkConnectStatus(callback)) {
      return;
    }
    if (mSetInfoCallback != null)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_WRITING_DATA.toString()));
      return;
    }
    mSetInfoCallback = callback;
    mMitacBluetoothLe.setUnitFormat(bIsMetric);
    startSetInfoTimer();
  }
  
  public void resetToDefault()
  {
    mMitacBluetoothLe.resetToDefault();
  }
  
  private void startSetInfoTimer()
  {
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        if (MitacApi.mSetInfoCallback != null)
        {
          MitacApi.mSetInfoCallback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_TIMEOUT.toString()));
          MitacApi.mSetInfoCallback = null;
          MitacApi.mTimerList.remove(MitacAttributes.ECommandStatus.ECS_SET_DEVICEINFO);
        }
      }
    }, 2000L);
    
    addTimerToList(MitacAttributes.ECommandStatus.ECS_SET_DEVICEINFO, timer);
  }
  
  private boolean checkConnectStatus(MitacSetInfoCallback callback)
  {
    if (getConnectStatus() != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
    {
      callback.didReceiveSetInfoFeedback(new Error(MitacError.ERROR_NO_CONNECTION.toString()));
      return false;
    }
    return true;
  }
  
  private static synchronized void addTimerToList(MitacAttributes.ECommandStatus eKey, Timer timer)
  {
    Timer _timer = (Timer)mTimerList.get(eKey);
    if (_timer != null) {
      _timer.cancel();
    }
    mTimerList.put(eKey, timer);
  }
  
  private static void writeEKGResultData()
  {
    mEKGResults.clear();
  return;
/*
    File sdcard = new File(Environment.getExternalStorageDirectory(), "/EKG");
    if (!sdcard.exists()) {
      sdcard.mkdir();
    }
    String strFile = String.format("/[EKG] Result_%s.txt", new Object[] { new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) });
    File yourFile = new File(sdcard, strFile);
    if (!yourFile.exists()) {
      try
      {
        yourFile.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    FileWriter writer = null;
    try
    {
      writer = new FileWriter(yourFile);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    if (writer != null)
    {
      if (!mEKGResults.isEmpty())
      {
        for (int i = 0; i < mEKGResults.size(); i++)
        {
          MitacEKGData data = (MitacEKGData)mEKGResults.get(i);
          try
          {
            writer.write(String.format("%d %d %d %d %d %d\r\n", new Object[] { Integer.valueOf(data.mrrInterval), Integer.valueOf(data.mHeartRate), Integer.valueOf(data.mANSAge), Integer.valueOf(data.mEnergy), Integer.valueOf(data.mBalance), Integer.valueOf(data.mStress) }));
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
        mEKGResults.clear();
      }
      try
      {
        writer.close();
        writer = null;
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    */
  }
  
  private void writeEKGRawData()
  {
    mEKGRaw.clear();
return;
/*
    File sdcard = new File(Environment.getExternalStorageDirectory(), "/EKG");
    if (!sdcard.exists()) {
      sdcard.mkdir();
    }
    String strFile = String.format("/[EKG] %s.txt", new Object[] { new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) });
    File yourFile = new File(sdcard, strFile);
    if (!yourFile.exists()) {
      try
      {
        yourFile.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    FileWriter writer = null;
    try
    {
      writer = new FileWriter(yourFile);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    if (writer != null)
    {
      if (!mEKGRaw.isEmpty())
      {
        for (int i = 0; i < mEKGRaw.size(); i++) {
          try
          {
            writer.write(String.format("%d\r\n", new Object[] { mEKGRaw.get(i) }));
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
        mEKGRaw.clear();
      }
      try
      {
        writer.close();
        writer = null;
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  */
  }

}
