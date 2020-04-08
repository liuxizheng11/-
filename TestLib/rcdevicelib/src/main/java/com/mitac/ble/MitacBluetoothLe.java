package com.mitac.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MitacBluetoothLe
{
  private static final String TAG = MitacBluetoothLe.class.getSimpleName();
  private Context mContext;
  private BluetoothManager mBluetoothManager;
  private static BluetoothAdapter mBluetoothAdapter;
  private String mBluetoothDeviceAddress;
  private static BluetoothGatt mBluetoothGatt;
  private static SampleGattAttributes.EConnectStatus mConnectionState = SampleGattAttributes.EConnectStatus.STATE_DISCONNECTED;
  private MitacBleDevice mLastConnectDevice = null;
  private HashMap<String, BluetoothDevice> mDeviceList = new HashMap();
  private byte[] uidBytes = new byte[32];
  private static int mCurrentPacketNumber;
  private static int mPacketNumber;
  private static int mLastDataByteNumber;
  private static byte[] mFWData = null;
  private MitacAttributes.EOTAUpdateStatus mOTAStatus = MitacAttributes.EOTAUpdateStatus.EOTA_NONE;
  private MitacAttributes.EFirmwareMode mFWMode = MitacAttributes.EFirmwareMode.EFWMode_UNKNOWN;
  private MitacAttributes.EHistoryTransferStatus mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_NONE;
  private static ByteArrayOutputStream mActivityBytes = new ByteArrayOutputStream();
  private static ByteArrayOutputStream mSleepBytes = new ByteArrayOutputStream();
  private static List<MitacSleepData> mSleepDataList = new ArrayList();
  private static int[] vv = new int[9];
  private Queue<BluetoothGattDescriptor> descriptorWriteQueue = new LinkedList();
  private BLEReceiveCallback mCallback;
  private LeScanCallback mLeScanCallback = new LeScanCallback()
  {
    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
    {
      new Thread(new Runnable()
      {
        public void run()
        {
          if ((device != null) && 
            (MitacBluetoothLe.this.needToAdd(device))) {
            MitacBluetoothLe.this.mCallback.onDeviceScanned(device);
          }
        }
      })
      
        .start();
    }
  };
  private LeScanCallback mReconnectScanCallback = new LeScanCallback()
  {
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
    {


      if (device.getAddress().compareTo(MitacBluetoothLe.this.mLastConnectDevice.mStrMacAddr) == 0)
      {
        MitacBluetoothLe.mBluetoothAdapter.stopLeScan(MitacBluetoothLe.this.mReconnectScanCallback);
        String strName = device.getName();
        if (strName != null) {
          MitacBluetoothLe.this.mLastConnectDevice.mStrName = strName;

        }
        try
        {
          Thread.sleep(250L);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        Log.d(TAG,"connected:"+MitacBluetoothLe.this.mLastConnectDevice.mStrMacAddr);
        MitacBluetoothLe.this.connect(MitacBluetoothLe.this.mLastConnectDevice.mStrMacAddr);
      }
    }
  };
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context context, Intent intent)
    {
      String action = intent.getAction();
      if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED"))
      {
        int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 
          Integer.MIN_VALUE);
        switch (state)
        {
        case 10: 
          break;
        case 13: 
          if (MitacBluetoothLe.mConnectionState == SampleGattAttributes.EConnectStatus.STATE_SCANING) {
            MitacBluetoothLe.this.stopScanDevice();
          }
          if (MitacBluetoothLe.this.mOTAStatus != MitacAttributes.EOTAUpdateStatus.EOTA_NONE)
          {
            MitacBluetoothLe.this.resetOTAStatus();
            MitacBluetoothLe.this.mCallback.onConnectStatusChange(MitacAttributes.EGattStatus.EGS_DISCONNECTED);
          }
          break;
        case 12: 
          break;
        }
      }
    }
  };
  private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
  {
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
    {
      if (newState == 2)
      {
        Log.i(MitacBluetoothLe.TAG, "Connected to GATT server.");
        
        MitacBluetoothLe.mBluetoothGatt = gatt;
        String strDeviceName = gatt.getDevice().getName();
        if (strDeviceName != null) {
          MitacBluetoothLe.this.mLastConnectDevice.mStrName = strDeviceName;
        }
        Log.i(MitacBluetoothLe.TAG, "Attempting to start service discovery:" + 
          MitacBluetoothLe.mBluetoothGatt.discoverServices());
      }
      else if (newState == 0)
      {
        if (MitacBluetoothLe.mConnectionState == SampleGattAttributes.EConnectStatus.STATE_CONNECTING)
        {
          Log.w(MitacBluetoothLe.TAG, "Connect GATT failed! reconnect");
          MitacBluetoothLe.this.connect();
          return;
        }
        if (MitacBluetoothLe.this.mOTAStatus == MitacAttributes.EOTAUpdateStatus.EOTA_INIT)
        {
          MitacBluetoothLe.this.reconnect();
          return;
        }
        MitacBluetoothLe.this.disconnect();
        
        Log.i(MitacBluetoothLe.TAG, "Disconnected from GATT server.");
        MitacBluetoothLe.this.mCallback.onConnectStatusChange(MitacAttributes.EGattStatus.EGS_DISCONNECTED);
      }
    }
    
    public void onServicesDiscovered(BluetoothGatt gatt, int status)
    {
      if (status == 0)
      {
        if (!MitacBluetoothLe.this.setCharacteristicNotification(MitacAttributes.TRANSFER_WEARABLE_CONTROL_UUID, true)) {
          Log.e(MitacBluetoothLe.TAG, "[Failed] setCharacteristicNotification(TRANSFER_WEARABLE_CONTROL_UUID)");
        }
        if ((!MitacBluetoothLe.this.isNewFWVersion()) && 
          (MitacBluetoothLe.this.mOTAStatus == MitacAttributes.EOTAUpdateStatus.EOTA_NONE))
        {
          if (!MitacBluetoothLe.this.setCharacteristicNotification(MitacAttributes.TRANSFER_WEARABLE_EKG_UUID, true)) {
            Log.e(MitacBluetoothLe.TAG, "[Failed] setCharacteristicNotification(TRANSFER_WEARABLE_EKG_UUID");
          }
          if (!MitacBluetoothLe.this.setCharacteristicNotification(MitacAttributes.TRANSFER_WEARABLE_ACTIVITY_UUID, true)) {
            Log.e(MitacBluetoothLe.TAG, "[Failed] setCharacteristicNotification(TRANSFER_WEARABLE_ACTIVITY_UUID");
          }
          if (!MitacBluetoothLe.this.setCharacteristicNotification(MitacAttributes.TRANSFER_WEARABLE_SLEEPCONTENT_UUID, true)) {
            Log.e(MitacBluetoothLe.TAG, "[Failed] setCharacteristicNotification(TRANSFER_WEARABLE_SLEEPCONTENT_UUID");
          }
        }
      }
      else
      {
        Log.w(MitacBluetoothLe.TAG, "onServicesDiscovered received: " + status);
      }
    }
    
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
      if (status == 0) {
        MitacBluetoothLe.this.sendUpdate("com.mitac.bluetooth.le.ACTION_DATA_AVAILABLE", characteristic);
      }
    }
    
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
    {
      MitacBluetoothLe.this.sendUpdate("com.mitac.bluetooth.le.ACTION_DATA_AVAILABLE", characteristic);
    }
    
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
    {
      if (status == 0)
      {
        Log.d(MitacBluetoothLe.TAG, "Callback: Wrote GATT Descriptor successfully.");
        
        MitacBluetoothLe.this.descriptorWriteQueue.remove();
        if ((!MitacBluetoothLe.this.isNewFWVersion()) || (MitacBluetoothLe.this.mFWMode != MitacAttributes.EFirmwareMode.EFWMode_UNKNOWN)) {}
      }
      else
      {
        Log.e(MitacBluetoothLe.TAG, "Callback: Error writing GATT Descriptor: " + status);
        MitacBluetoothLe.this.descriptorWriteQueue.clear();
        MitacBluetoothLe.this.disconnect();
        MitacBluetoothLe.this.mCallback.onConnectStatusChange(MitacAttributes.EGattStatus.EGS_DISCONNECTED);
        
        return;
      }
      if (MitacBluetoothLe.this.descriptorWriteQueue.size() > 0)
      {
        MitacBluetoothLe.mBluetoothGatt.writeDescriptor((BluetoothGattDescriptor)MitacBluetoothLe.this.descriptorWriteQueue.element());
      }
      else
      {
        MitacBluetoothLe.mConnectionState = SampleGattAttributes.EConnectStatus.STATE_CONNECTED;
        if (MitacBluetoothLe.this.mOTAStatus == MitacAttributes.EOTAUpdateStatus.EOTA_NONE) {
          MitacBluetoothLe.this.mCallback.onConnectStatusChange(MitacAttributes.EGattStatus.EGS_CONNECTED);
        } else if (MitacBluetoothLe.this.mOTAStatus == MitacAttributes.EOTAUpdateStatus.EOTA_START) {
          MitacBluetoothLe.this.mOTAStatus = MitacAttributes.EOTAUpdateStatus.EOTA_INIT;
        }
      }
    }
  };
  
  private void writeGattDescriptor(BluetoothGattDescriptor d)
  {
    this.descriptorWriteQueue.add(d);
    if (this.descriptorWriteQueue.size() == 1) {
      mBluetoothGatt.writeDescriptor(d);
    }
  }
  
  public MitacBluetoothLe(Context context, BLEReceiveCallback callback)
  {
    this.mContext = context;
    this.mCallback = callback;
    initialize();
  }
  
  public void finalize()
    throws Throwable
  {
    this.mContext.unregisterReceiver(this.mReceiver);
    super.finalize();
  }
  
  public boolean initialize()
  {
    if (this.mBluetoothManager == null)
    {
      this.mBluetoothManager = ((BluetoothManager)this.mContext.getSystemService("bluetooth"));
      if (this.mBluetoothManager == null)
      {
        Log.e(TAG, "Unable to initialize BluetoothManager.");
        return false;
      }
    }
    mBluetoothAdapter = this.mBluetoothManager.getAdapter();
    if (mBluetoothAdapter == null)
    {
      Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
      return false;
    }
    IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
    this.mContext.registerReceiver(this.mReceiver, filter);
    
    return true;
  }
  
  public boolean connect(final String address)
  {
    Log.d(TAG,"yyy:"+address);
    if ((mBluetoothAdapter == null) || (address == null))
    {
      Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
      return false;
    }
    if ((this.mBluetoothDeviceAddress != null) && (address.equals(this.mBluetoothDeviceAddress)) && 
      (mBluetoothGatt != null))
    {
      Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
      if (mBluetoothGatt.connect())
      {
        mConnectionState = SampleGattAttributes.EConnectStatus.STATE_CONNECTING;
        return true;
      }
      return false;
    }
    final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
    if (device == null)
    {
      Log.w(TAG, "Device not found.  Unable to connect.");
      return false;
    }
    if (this.mLastConnectDevice == null) {
      this.mLastConnectDevice = new MitacBleDevice(null, address, "DEVICE_TYPE_LE");
    } else {
      this.mLastConnectDevice.mStrMacAddr = address;
    }
    Handler handler = new Handler(this.mContext.getMainLooper());
    handler.post(new Runnable()
    {
      public void run()
      {
        MitacBluetoothLe.mBluetoothGatt = device.connectGatt(MitacBluetoothLe.this.mContext, false, MitacBluetoothLe.this.mGattCallback);
        Log.d(MitacBluetoothLe.TAG, "Trying to create a new connection.");
        MitacBluetoothLe.this.mBluetoothDeviceAddress = address;
        MitacBluetoothLe.mConnectionState = SampleGattAttributes.EConnectStatus.STATE_CONNECTING;
      }
    });
    return true;
  }
  
  public boolean connect()
  {
    return connect(this.mLastConnectDevice.mStrMacAddr);
  }
  
  public void disconnect()
  {
    disconnect(false);
  }
  
  public void disconnect(final boolean isApiCall)
  {
    Handler handler = new Handler(this.mContext.getMainLooper());
    handler.post(new Runnable()
    {
      public void run()
      {
        if (MitacBluetoothLe.mBluetoothGatt != null) {
          MitacBluetoothLe.mBluetoothGatt.close();
        }
        MitacBluetoothLe.mConnectionState = SampleGattAttributes.EConnectStatus.STATE_DISCONNECTED;
        MitacBluetoothLe.this.mBluetoothDeviceAddress = null;
        MitacBluetoothLe.mBluetoothGatt = null;
        MitacBluetoothLe.this.mFWMode = MitacAttributes.EFirmwareMode.EFWMode_UNKNOWN;
        MitacBluetoothLe.this.mDeviceList.clear();
        if (isApiCall)
        {
          MitacBluetoothLe.this.resetOTAStatus();
          MitacBluetoothLe.this.mCallback.onConnectStatusChange(MitacAttributes.EGattStatus.EGS_DISCONNECTED);
        }
      }
    });
  }
  
  public void reconnect(final int iDelay)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(250L);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        MitacBluetoothLe.this.disconnect();
        try
        {
          Thread.sleep(iDelay);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        MitacBluetoothLe.mBluetoothAdapter.startLeScan(MitacBluetoothLe.this.mReconnectScanCallback);
      }
    })
    
      .start();
  }
  
  public void reconnect()
  {
    reconnect(1000);
  }
  
  public SampleGattAttributes.EConnectStatus scanDevice()
  {
    SampleGattAttributes.EConnectStatus status = mConnectionState;
    if (mConnectionState == SampleGattAttributes.EConnectStatus.STATE_CONNECTING)
    {
      disconnect();
    }
    else
    {
      if (mConnectionState == SampleGattAttributes.EConnectStatus.STATE_CONNECTED) {
        return status;
      }
      if (mConnectionState == SampleGattAttributes.EConnectStatus.STATE_SCANING)
      {
        this.mDeviceList.clear();
        return status;
      }
    }
    mConnectionState = SampleGattAttributes.EConnectStatus.STATE_SCANING;
    this.mDeviceList.clear();
    mBluetoothAdapter.startLeScan(this.mLeScanCallback);
    return status;
  }
  
  public void stopScanDevice()
  {
    this.mDeviceList.clear();
    
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if ((bluetoothAdapter == null) || (!bluetoothAdapter.isEnabled())) {
      return;
    }
    if (mConnectionState == SampleGattAttributes.EConnectStatus.STATE_SCANING) {
      mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
    }
    mConnectionState = SampleGattAttributes.EConnectStatus.STATE_DISCONNECTED;
  }
  
  private void sendUpdate(String action, BluetoothGattCharacteristic characteristic)
  {
    byte[] data = characteristic.getValue();
    if (MitacAttributes.TRANSFER_WEARABLE_ACTIVITY_UUID.equals(characteristic.getUuid()))
    {
      if ((data != null) && (data.length > 0))
      {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        byte[] arrayOfByte1;
        int j = (arrayOfByte1 = data).length;
        for (int i = 0; i < j; i++)
        {
          byte byteChar = arrayOfByte1[i];
          stringBuilder.append(String.format("%02x ", new Object[] { Byte.valueOf(byteChar) }));
        }
        int iStep = (data[3] & 0xFF) + (data[2] << 8) + (data[1] << 16) + (data[0] << 24);
        
        int iDistance = (data[7] & 0xFF) + (data[6] << 8);
        int iActivityTime = (data[9] & 0xFF) + (data[8] << 8);
        int iCalorie = (data[11] & 0xFF) + (data[10] << 8);
        
        this.mCallback.onRealtimeActivityDataReceived(new MitacActivityData(iStep, iCalorie, iActivityTime, (float)(iDistance / 10.0D)), null);
      }
    }
    else if (MitacAttributes.TRANSFER_WEARABLE_EKG_UUID.equals(characteristic.getUuid()))
    {
      if ((data != null) && (data.length > 0))
      {
        for (int i = 2; i < data.length; i += 2) {
          vv[(i / 2 - 1)] = (data[i] << 8 | data[(i + 1)] & 0xFF);
        }
        this.mCallback.onEKGDataReceived(vv, null);
      }
    }
    else if (MitacAttributes.TRANSFER_WEARABLE_SLEEPCONTENT_UUID.equals(characteristic.getUuid()))
    {
      if ((data != null) && (data.length > 0))
      {
        if (this.mTransferStatus == MitacAttributes.EHistoryTransferStatus.EHTS_SleepStart) {
          mSleepBytes.write(data, 2, data.length - 2);
        } else {
          mActivityBytes.write(data, 2, data.length - 2);
        }
        if (data[1] == -1) {
          if (data[0] == -1)
          {
            if (this.mTransferStatus == MitacAttributes.EHistoryTransferStatus.EHTS_SleepStart)
            {
              Log.i(TAG, "[TRANSFER_WEARABLE_SLEEPCONTENT_UUID] stopReceivingSleepData");
              stopReceivingSleepData();
              if (mSleepDataList.size() > 0)
              {
                MitacSleepData[] sleepData = new MitacSleepData[mSleepDataList.size()];
                sleepData = (MitacSleepData[])mSleepDataList.toArray(sleepData);
                this.mCallback.onReceiveHistorySleep(sleepData, null);
                mSleepDataList.clear();
              }
              else
              {
                this.mCallback.onReceiveHistorySleep(null, new Error(MitacError.ERROR_NO_RECORD.toString()));
              }
              mSleepBytes.reset();
            }
            else
            {
              stopSevenDaysActivityData();
              analyzeActivityData();
            }
          }
          else if (this.mTransferStatus == MitacAttributes.EHistoryTransferStatus.EHTS_SleepStart) {
            if (analyzeSleepData())
            {
              continueReceivingSleepData();
            }
            else
            {
              stopReceivingSleepData();
              this.mCallback.onReceiveHistorySleep(null, new Error(MitacError.ERROR_LOST_PACKAGE.toString()));
            }
          }
        }
      }
    }
    else if (MitacAttributes.TRANSFER_WEARABLE_CONTROL_UUID.equals(characteristic.getUuid()))
    {
      byte opcode = data[0];
      switch (opcode)
      {
      case 1: 
        if ((data[1] == 12) && (data[2] == 1)) {
          new Thread(new Runnable()
          {
            public void run()
            {
              MitacBluetoothLe.this.updateFirmwareTransfer();
            }
          })
          
            .start();
        }
        break;
      case 11: 
        this.mFWMode = MitacAttributes.EFirmwareMode.EFWMode_OTA;
        if (mConnectionState != SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
        {
          mConnectionState = SampleGattAttributes.EConnectStatus.STATE_CONNECTED;
          this.mCallback.onConnectStatusChange(MitacAttributes.EGattStatus.EGS_CONNECTED);
        }
        if (this.mOTAStatus == MitacAttributes.EOTAUpdateStatus.EOTA_INIT) {
          startFirmwareUpdate();
        } else {
          this.mOTAStatus = MitacAttributes.EOTAUpdateStatus.EOTA_INIT;
        }
        break;
      case 13: 
        if (mFWData != null)
        {
          int packetNumber = Byte2Int(data, 1, 2);
          if (packetNumber == 0) {
            mCurrentPacketNumber = packetNumber;
          } else if (packetNumber == 65535) {
            mCurrentPacketNumber = mPacketNumber;
          } else {
            mCurrentPacketNumber = packetNumber + 1;
          }
          new Thread(new Runnable()
          {
            public void run()
            {
              MitacBluetoothLe.this.updateFirmwareTransfer();
            }
          })
          
            .start();
          if (this.mCallback != null) {
            this.mCallback.onFirmwareTransferring(mCurrentPacketNumber / mPacketNumber * 100.0F, null);
          }
        }
        break;
      case 14: 
        resetOTAStatus();
        if (data[1] == 0) {
          this.mCallback.onFirmwareTransferring(100.0F, null);
        } else if (data[1] == 1) {
          this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_OTA_FAILED.toString()));
        } else if ((data[1] == 2) || (data[1] == 3)) {
          this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_TIMEOUT.toString()));
        } else if (data[1] == 4) {
          this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_WRONG_SIZE.toString()));
        }
        reconnect();
        
        break;
      case 25: 
        this.mCallback.onReceiveSleepStatus(data[1] == 1);
        break;
      case 37: 
        this.mCallback.onReceiveFWMode(data[1] == 1);
        break;
      case 42: 
        if (isNewFWVersion())
        {
          this.mFWMode = MitacAttributes.EFirmwareMode.EFWMode_NORMAL;
          if (!setCharacteristicNotification(MitacAttributes.TRANSFER_WEARABLE_EKG_UUID, true)) {
            Log.e(TAG, "[Failed] setCharacteristicNotification(TRANSFER_WEARABLE_EKG_UUID");
          }
          if (!setCharacteristicNotification(MitacAttributes.TRANSFER_WEARABLE_ACTIVITY_UUID, true)) {
            Log.e(TAG, "[Failed] setCharacteristicNotification(TRANSFER_WEARABLE_ACTIVITY_UUID");
          }
          if (!setCharacteristicNotification(MitacAttributes.TRANSFER_WEARABLE_SLEEPCONTENT_UUID, true)) {
            Log.e(TAG, "[Failed] setCharacteristicNotification(TRANSFER_WEARABLE_SLEEPCONTENT_UUID");
          }
        }
      case 26: 
        this.mCallback.onReceiveFWRamSize(data[1] == 32);
        break;
      case 15: 
        switch (data[1])
        {
        case 1: 
          this.mCallback.onReceiveFWVersion(String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(data[2] & 0xFF), Integer.valueOf(data[3] & 0xFF), Integer.valueOf(data[4] & 0xFF), Integer.valueOf(data[5] & 0xFF) }));
          break;
        case 2: 
          this.mCallback.onReceiveUUID(String.format("%02X%02X%02X%02X%02X%02X%02X%02X", new Object[] { Byte.valueOf(data[4]), Byte.valueOf(data[5]), Byte.valueOf(data[6]), Byte.valueOf(data[7]), Byte.valueOf(data[8]), Byte.valueOf(data[9]), Byte.valueOf(data[10]), Byte.valueOf(data[11]) }), 
            String.format("%02X%02X", new Object[] { Byte.valueOf(data[2]), Byte.valueOf(data[3]) }));
          break;
        case 3: 
          this.mCallback.onReceiveMacAddr(String.format("%02X:%02X:%02X:%02X:%02X:%02X", new Object[] { Byte.valueOf(data[7]), Byte.valueOf(data[6]), Byte.valueOf(data[5]), Byte.valueOf(data[4]), Byte.valueOf(data[3]), Byte.valueOf(data[2]) }));
          break;
        case 25: 
          GregorianCalendar dateInfo = new GregorianCalendar((data[2] & 0xFF) * 100 + (data[3] & 0xFF), 
            (data[4] & 0xFF) - 1, 
            data[5] & 0xFF, 
            data[6] & 0xFF, 
            data[7] & 0xFF, 
            data[8] & 0xFF);
          
          int iTimeZone = Byte2Int(data, 9, 4);
          
          TimeZone timezone = TimeZone.getDefault();
          timezone.setRawOffset(iTimeZone);
          
          boolean bIs12hrMode = data[13] == 1;
          this.mCallback.onReceiveDateTime(dateInfo.getTime(), timezone, bIs12hrMode);
          break;
        case 23: 
          this.mCallback.onReceiveBatteryLevel(data[3], data[2] == 0);
          break;
        case 18: 
          float height = ByteBuffer.wrap(Arrays.copyOfRange(data, 4, 8)).order(ByteOrder.BIG_ENDIAN).getFloat();
          float weight = ByteBuffer.wrap(Arrays.copyOfRange(data, 8, 12)).order(ByteOrder.BIG_ENDIAN).getFloat();
          this.mCallback.onReceiveProfile(data[2] & 0xFF, height, weight, data[3] == 1);
          break;
        case 19: 
          this.mCallback.onReceiveGoalSetting(Byte2Int(data, 14, 4), Byte2Int(data, 2, 4), Byte2Int(data, 10, 4), Byte2Int(data, 6, 4));
          break;
        case 28: 
          for (int i = 0; i < 16; i++) {
            this.uidBytes[i] = data[(i + 2)];
          }
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                Thread.sleep(60L);
              }
              catch (InterruptedException e)
              {
                e.printStackTrace();
              }
              MitacBluetoothLe.this.getUIDContinue();
            }
          }).start();
          break;
        case 29: 
          for (int i = 0; i < 16; i++) {
            this.uidBytes[(i + 16)] = data[(i + 2)];
          }
          boolean bIsNull = true;
          for (int i = 0; i < this.uidBytes.length; i++) {
            if (this.uidBytes[i] != -1)
            {
              bIsNull = false;
              break;
            }
          }
          if (bIsNull) {
            this.mCallback.onReceiveUID(null);
          } else {
            try
            {
              String strUID = new String(this.uidBytes, "UTF-8");
              this.mCallback.onReceiveUID(strUID);
            }
            catch (UnsupportedEncodingException e)
            {
              e.printStackTrace();
            }
          }
          break;
        case 30: 
          MitacAlarmData[] alarmData = { new MitacAlarmData(data[3], data[4], data[2] == 1), 
            new MitacAlarmData(data[6], data[7], data[5] == 1), 
            new MitacAlarmData(data[9], data[10], data[8] == 1), 
            new MitacAlarmData(data[12], data[13], data[11] == 1), 
            new MitacAlarmData(data[15], data[16], data[14] == 1) };
          this.mCallback.onReceiveSleepAlarm(alarmData);
          break;
        case 31: 
          MitacAlarmData[] alarmData2 = { new MitacAlarmData(data[3], data[4], data[2] == 1),
            new MitacAlarmData(data[6], data[7], data[5] == 1), 
            new MitacAlarmData(data[9], data[10], data[8] == 1), 
            new MitacAlarmData(data[12], data[13], data[11] == 1) };
          this.mCallback.onReceiveTimeToSleep(alarmData2);
        }
        break;
      case 3: 
        if (this.mTransferStatus == MitacAttributes.EHistoryTransferStatus.EHTS_SleepInit) {
          if (data[1] == 0)
          {
            this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_SleepStart;
          }
          else if (data[1] == 1)
          {
            this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_NONE;
            this.mCallback.onReceiveHistorySleep(null, new Error(MitacError.ERROR_SLEEP_MODE.toString()));
          }
          else if (data[1] == 2)
          {
            this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_NONE;
            this.mCallback.onReceiveHistorySleep(null, new Error(MitacError.ERROR_TRANSFERRING.toString()));
          }
        }
        break;
      case 19: 
        if (this.mTransferStatus == MitacAttributes.EHistoryTransferStatus.EHTS_ActivityInit) {
          if (data[1] == 0)
          {
            this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_ActivityStart;
          }
          else if (data[1] == 1)
          {
            this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_NONE;
            this.mCallback.onReceiveSevenDaysActivity(null, new Error(MitacError.ERROR_WRITE_ACTIVITY_DATA.toString()));
          }
          else if (data[1] == 2)
          {
            this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_NONE;
            this.mCallback.onReceiveSevenDaysActivity(null, new Error(MitacError.ERROR_TRANSFERRING.toString()));
          }
        }
        break;
      case 8: 
        if (data[1] == 2) {
          this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_BATTERY_LOW.toString()));
        } else if (data[1] == 4) {
          this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_SLEEP_MODE.toString()));
        } else if (data[1] == 8) {
          this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_WRITING_DATA.toString()));
        } else if (data[1] == 16) {
          this.mCallback.onEKGDataReceived(null, new Error(MitacError.ERROR_HEARTRATE_ON_DEVICE.toString()));
        }
        break;
      case 17: 
        if (data[1] == 1) {
          this.mCallback.onRealtimeActivityDataReceived(null, new Error(MitacError.ERROR_TRANSFERRING.toString()));
        }
        break;
      case 7: 
        if (data[1] == 0) {
          this.mCallback.onReceiveSetInfoError(null);
        } else if (data[1] == 99) {
          this.mCallback.onReceiveSetInfoError(new Error(MitacError.ERROR_SLEEP_MODE.toString()));
        } else {
          this.mCallback.onReceiveSetInfoError(new Error(MitacError.ERROR_UNKNOW_ERROR.toString()));
        }
        break;
      case 16: 
        if (data[1] == 16) {
          if (data[3] == 0) {
            this.mCallback.onReceiveSetInfoError(null);
          } else if (data[3] == 99) {
            this.mCallback.onReceiveSetInfoError(new Error(MitacError.ERROR_SLEEP_MODE.toString()));
          } else {
            this.mCallback.onReceiveSetInfoError(new Error(MitacError.ERROR_UNKNOW_ERROR.toString()));
          }
        }
        break;
      case 22: 
      case 23: 
        if (data[1] == 0) {
          this.mCallback.onReceiveSetInfoError(null);
        } else {
          this.mCallback.onReceiveSetInfoError(new Error(MitacError.ERROR_UNKNOW_ERROR.toString()));
        }
        break;
      case 30: 
        this.mCallback.onReveiveUnitFormat(data[1] == 0);
        break;
      case 29: 
        if ((data[1] == 0) || (data[1] == 1)) {
          this.mCallback.onReceiveSetInfoError(null);
        } else {
          this.mCallback.onReceiveSetInfoError(new Error(MitacError.ERROR_UNKNOW_ERROR.toString()));
        }
        break;
      }
    }
  }
  
  private int Byte2Int(byte[] data, int iIndex, int iLength)
  {
    int iValue = 0;
    for (int i = iIndex; i < iIndex + iLength; i++)
    {
      iValue <<= 8;
      iValue |= data[i] & 0xFF;
    }
    return iValue;
  }
  
  private long Byte2IntLBS(byte[] data, int iIndex, int iLength)
  {
    long iValue = 0L;
    for (int i = iIndex + iLength - 1; i >= iIndex; i--)
    {
      iValue <<= 8;
      iValue |= data[i] & 0xFF;
    }
    return iValue;
  }
  
  public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled)
  {
    if ((mBluetoothAdapter == null) || (mBluetoothGatt == null))
    {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return false;
    }
    int charaProp = characteristic.getProperties();
    if ((charaProp | 0x10) > 0)
    {
      if (!mBluetoothGatt.setCharacteristicNotification(characteristic, true))
      {
        Log.w(TAG, "[Failed] mBluetoothGatt.setCharacteristicNotification - 1");
        return false;
      }
    }
    else
    {
      Log.w(TAG, "[Failed] not notification");
      return false;
    }
    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
    descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
    
    writeGattDescriptor(descriptor);
    
    return true;
  }
  
  public boolean setCharacteristicNotification(UUID uuid, boolean enabled)
  {
    if ((mBluetoothAdapter == null) || (mBluetoothGatt == null))
    {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return false;
    }
    BluetoothGattService LumService = mBluetoothGatt.getService(MitacAttributes.TRANSFER_WEARABLE_SERVICE_UUID);
    if (LumService == null)
    {
      Log.w(TAG, "Get Service Failed - TRANSFER_WEARABLE_SERVICE_UUID");
      return false;
    }
    BluetoothGattCharacteristic LumChar = LumService.getCharacteristic(uuid);
    if (LumChar == null)
    {
      Log.w(TAG, "Get Gatt Failed");
      return false;
    }
    boolean bIsSuccess = setCharacteristicNotification(LumChar, enabled);
    
    return bIsSuccess;
  }
  
  public void startReceivingEKGData(int type)
  {
    byte[] value = new byte[2];
    value[0] = 8;
    if (type == 0) {
      value[1] = 0;
    } else if (type == 2) {
      value[1] = 1;
    }
    writeCharacteristic(value);
  }
  
  public void stopReceivingEKGData()
  {
    byte[] value = { 9 };
    writeCharacteristic(value);
  }
  
  public void startReceivingActivityData()
  {
    byte[] value = { 17 };
    writeCharacteristic(value);
  }
  
  public void stopReceivingActivityData()
  {
    byte[] value = { 18 };
    writeCharacteristic(value);
  }
  
  public synchronized void startSevenDaysActivityData()
  {
    if (this.mTransferStatus != MitacAttributes.EHistoryTransferStatus.EHTS_NONE)
    {
      this.mCallback.onReceiveSevenDaysActivity(null, new Error(MitacError.ERROR_TRANSFERRING.toString()));
      return;
    }
    this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_ActivityInit;
    
    String strSleep = "BETASLEEP";
    byte[] bytes = null;
    try
    {
      bytes = strSleep.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    byte[] value = { 19, bytes[0], bytes[1], bytes[2], bytes[3], 
      bytes[4], bytes[5], bytes[6], bytes[7], bytes[8] };
    writeCharacteristic(value);
  }
  
  public void stopSevenDaysActivityData()
  {
    this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_NONE;
    byte[] value = { 21 };
    writeCharacteristic(value);
  }
  
  public void analyzeActivityData()
  {
    if (mActivityBytes.size() > 18)
    {
      byte[] activityData = mActivityBytes.toByteArray();
      int iRecordLength = (int)Byte2IntLBS(activityData, 0, 4);
      if (mActivityBytes.size() >= iRecordLength)
      {
        int iRecordCount = iRecordLength / 12;
        MitacActivityData[] activityList = new MitacActivityData[iRecordCount];
        for (int i = 0; i < iRecordCount; i++)
        {
          int iOffset = i * 12 + 4;
          
          int iYear = 1900 + activityData[(iOffset + 3)];
          int iMonth = activityData[(iOffset + 2)];
          int iDay = activityData[(iOffset + 1)];
          int iHour = activityData[iOffset];
          GregorianCalendar dateInfo = new GregorianCalendar(iYear, iMonth, iDay, iHour, 0);
          
          int iStep = ((activityData[(iOffset + 7)] & 0xFF) << 12) + ((activityData[(iOffset + 6)] & 0xFF) << 4) + ((activityData[(iOffset + 5)] & 0xF0) >> 4);
          float fDistance = (float)((((activityData[(iOffset + 5)] & 0xF) << 8) + (activityData[(iOffset + 4)] & 0xFF)) / 10.0D);
          int iCalories = ((activityData[(iOffset + 9)] & 0xFF) << 8) + (activityData[(iOffset + 8)] & 0xFF);
          int iActivityTime = ((activityData[(iOffset + 11)] & 0xFF) << 8) + (activityData[(iOffset + 10)] & 0xFF);
          
          activityList[i] = new MitacActivityData(iStep, iCalories, iActivityTime, fDistance, dateInfo.getTime());
        }
        this.mCallback.onReceiveSevenDaysActivity(activityList, null);
      }
      else
      {
        this.mCallback.onReceiveSevenDaysActivity(null, new Error(MitacError.ERROR_LOST_PACKAGE.toString()));
      }
    }
    else
    {
      this.mCallback.onReceiveSevenDaysActivity(null, new Error(MitacError.ERROR_NO_RECORD.toString()));
    }
    mActivityBytes.reset();
  }
  
  public void getFWVersion()
  {
    byte[] value = { 15, 1 };
    writeCharacteristic(value);
  }
  
  public void getFWRamSize()
  {
    byte[] value = { 26 };
    writeCharacteristic(value);
  }
  
  public MitacAttributes.EFirmwareMode getFWMode()
  {
    return this.mFWMode;
  }
  
  public void fetchFWMode()
  {
    byte[] value = { 37 };
    writeCharacteristic(value);
  }
  
  public MitacAttributes.EOTAUpdateStatus getOTAStatus()
  {
    return this.mOTAStatus;
  }
  
  private boolean initFirmwareUpdate()
  {
    if (mFWData == null) {
      return false;
    }
    if (this.mOTAStatus == MitacAttributes.EOTAUpdateStatus.EOTA_INIT)
    {
      startFirmwareUpdate();
    }
    else
    {
      byte[] value = { 10 };
      writeCharacteristic(value);
      
      mCurrentPacketNumber = 0;
      this.mOTAStatus = MitacAttributes.EOTAUpdateStatus.EOTA_INIT;
      if (this.mFWMode != MitacAttributes.EFirmwareMode.EFWMode_OTA)
      {
        reconnect();
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
          public void run()
          {
            if (MitacBluetoothLe.mConnectionState == SampleGattAttributes.EConnectStatus.STATE_CONNECTED)
            {
              byte[] value = { 10 };
              MitacBluetoothLe.writeCharacteristic(value);
            }
            else
            {
              MitacBluetoothLe.mBluetoothAdapter.stopLeScan(MitacBluetoothLe.this.mReconnectScanCallback);
              
              MitacBluetoothLe.this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_OTA_FAILED.toString()));
            }
          }
        }, 6000L);
      }
    }
    return true;
  }
  
  public void initFirmwareUpdate(String strFile)
  {
    if (!initFirmwareData(strFile))
    {
      this.mCallback.onFirmwareTransferring(-1.0F, new Error(MitacError.ERROR_READ_FILE_FAILED.toString()));
      return;
    }
    initFirmwareUpdate();
  }
  
  private boolean initFirmwareData(String strFile)
  {
    File file = new File(strFile);
    if (!file.exists()) {
      return false;
    }
    try
    {
      FileInputStream fis = new FileInputStream(file);
      int length = (int)file.length();
      mFWData = new byte[length];
      try
      {
        fis.read(mFWData);
        fis.close();
        
        mCurrentPacketNumber = 0;
        mPacketNumber = length / 16;
        mLastDataByteNumber = length % 16;
        if (mLastDataByteNumber != 0) {
          mPacketNumber += 1;
        } else {
          mLastDataByteNumber = 16;
        }
        SharedPreferences settings = this.mContext.getSharedPreferences("MitacBand", 0);
        settings.edit().putString("LastOTAPath", strFile).commit();
        
        return true;
      }
      catch (IOException e)
      {
        e.printStackTrace();
        return false;
      }

    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  private void startFirmwareUpdate()
  {
    this.mOTAStatus = MitacAttributes.EOTAUpdateStatus.EOTA_START;
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(500L);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        MitacBluetoothLe.mCurrentPacketNumber = 0;
        
        byte[] bytes = ByteBuffer.allocate(4).putInt(MitacBluetoothLe.mPacketNumber).array();
        
        byte[] value = { 12, bytes[0], bytes[1], bytes[2], bytes[3] };
        MitacBluetoothLe.writeCharacteristic(value);
      }
    })
    
      .start();
  }
  
  private synchronized void updateFirmwareTransfer()
  {
    if (mCurrentPacketNumber == mPacketNumber)
    {
      int checksum = 0;
      for (int i = 0; i < mFWData.length; i++) {
        checksum += (mFWData[i] & 0xFF);
      }
      byte[] bytes = ByteBuffer.allocate(4).putInt(checksum).array();
      byte[] value = { 14, bytes[0], bytes[1], bytes[2], bytes[3] };
      writeCharacteristic(value);
    }
    else if (mCurrentPacketNumber < mPacketNumber - 1)
    {
      int count = 0;
      while ((mCurrentPacketNumber != mPacketNumber - 1) && (count < 64))
      {
        if (this.mOTAStatus != MitacAttributes.EOTAUpdateStatus.EOTA_START) {
          return;
        }
        byte[] bytes = ByteBuffer.allocate(4).putInt(mCurrentPacketNumber).array();
        byte[] value = { 13, bytes[2], bytes[3], 
          mFWData[(mCurrentPacketNumber * 16 + 15)], mFWData[(mCurrentPacketNumber * 16 + 14)], 
          mFWData[(mCurrentPacketNumber * 16 + 13)], mFWData[(mCurrentPacketNumber * 16 + 12)], 
          mFWData[(mCurrentPacketNumber * 16 + 11)], mFWData[(mCurrentPacketNumber * 16 + 10)], 
          mFWData[(mCurrentPacketNumber * 16 + 9)], mFWData[(mCurrentPacketNumber * 16 + 8)], 
          mFWData[(mCurrentPacketNumber * 16 + 7)], mFWData[(mCurrentPacketNumber * 16 + 6)], 
          mFWData[(mCurrentPacketNumber * 16 + 5)], mFWData[(mCurrentPacketNumber * 16 + 4)], 
          mFWData[(mCurrentPacketNumber * 16 + 3)], mFWData[(mCurrentPacketNumber * 16 + 2)], 
          mFWData[(mCurrentPacketNumber * 16 + 1)], mFWData[(mCurrentPacketNumber * 16)] };
        
        writeCharacteristic(value);
        mCurrentPacketNumber += 1;
        SystemClock.sleep(5L);
        count++;
      }
      if (count < 64) {
        new Thread(new Runnable()
        {
          public void run()
          {
            MitacBluetoothLe.this.updateFirmwareTransfer();
          }
        })
        
          .start();
      }
    }
    else if (mCurrentPacketNumber == mPacketNumber - 1)
    {
      byte[] lastbytes = ByteBuffer.allocate(16).putInt(0).array();
      for (int i = 0; i < mLastDataByteNumber; i++) {
        lastbytes[i] = mFWData[(mCurrentPacketNumber * 16 + i)];
      }
      byte[] bytes = ByteBuffer.allocate(4).putInt(mCurrentPacketNumber).array();
      byte[] value = { 13, bytes[2], bytes[3], 
        lastbytes[15], lastbytes[14], lastbytes[13], lastbytes[12], 
        lastbytes[11], lastbytes[10], lastbytes[9], lastbytes[8], 
        lastbytes[7], lastbytes[6], lastbytes[5], lastbytes[4], 
        lastbytes[3], lastbytes[2], lastbytes[1], lastbytes[0] };
      writeCharacteristic(value);
      mCurrentPacketNumber += 1;
    }
  }
  
  public boolean getDeviceUUID()
  {
    byte[] value = { 15, 2 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public boolean getDeviceMacAddr()
  {
    byte[] value = { 15, 3 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public boolean getBatteryLevel()
  {
    byte[] value = { 15, 23 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public void setDate(Date date, TimeZone timezone, boolean bIs12hrMode, boolean bIsReset)
  {
    int sinceSec = (int)(date.getTime() / 1000L - MitacAttributes.TimeIntervalSince1970);
    int offsetSec = timezone.getOffset(date.getTime()) / 1000;
    byte byte12hrMode = (byte)(bIs12hrMode ? 1 : 0);
    byte byteReset = (byte)(bIsReset ? 1 : 0);
    
    byte[] sinceBytes = ByteBuffer.allocate(4).putInt(sinceSec).array();
    byte[] offsetBytes = ByteBuffer.allocate(4).putInt(offsetSec).array();
    byte[] value = { 7, sinceBytes[0], sinceBytes[1], sinceBytes[2], sinceBytes[3], 
      offsetBytes[0], offsetBytes[1], offsetBytes[2], offsetBytes[3], 
      byte12hrMode, byteReset };
    
    writeCharacteristic(value);
  }
  
  public boolean getDate()
  {
    byte[] value = { 15, 25 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public void setProfileToDevice(int iAge, float fHeight, float fWeight, boolean bIsMale)
  {
    byte[] startValue = { 16, 15 };
    writeCharacteristic(startValue);
    
    byte[] height = ByteBuffer.allocate(4).putFloat(fHeight).array();
    byte[] weight = ByteBuffer.allocate(4).putFloat(fWeight).array();
    byte[] value = { 16, 4, 
      (byte)iAge, (byte)(bIsMale ? 1 : 0), height[0], height[1], height[2], height[3], weight[0], weight[1], weight[2], weight[3] };
    writeCharacteristic(value);
    
    byte[] endValue = { 16, 16 };
    writeCharacteristic(endValue);
  }
  
  public boolean getProfile()
  {
    byte[] value = { 15, 18 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public boolean setGoalToDevice(int iActivityTime, int iSteps, int iDistance, int iCalories)
  {
    byte[] startValue = { 16, 15 };
    writeCharacteristic(startValue);
    
    byte[] activityTime = ByteBuffer.allocate(4).putInt(iActivityTime).array();
    byte[] step = ByteBuffer.allocate(4).putInt(iSteps).array();
    byte[] distance = ByteBuffer.allocate(4).putInt(iDistance).array();
    byte[] calories = ByteBuffer.allocate(4).putInt(iCalories).array();
    byte[] value = { 16, 5, 
      step[0], step[1], step[2], step[3], 
      calories[0], calories[1], calories[2], calories[3], 
      distance[0], distance[1], distance[2], distance[3], 
      activityTime[0], activityTime[1], activityTime[2], activityTime[3] };
    writeCharacteristic(value);
    
    byte[] endValue = { 16, 16 };
    writeCharacteristic(endValue);
    
    return true;
  }
  
  public boolean getGoalFromDevice()
  {
    byte[] value = { 15, 19 };
    writeCharacteristic(value);
    
    return true;
  }

  public boolean setSleepMonitor(boolean bStart)
  {
    byte[] startValue = { 22 };
    byte[] stopValue = { 23 };
    if (bStart) {
      writeCharacteristic(startValue);
    } else {
      writeCharacteristic(stopValue);
    }
    return true;
  }
  
  public boolean getSleepStatus()
  {
    byte[] value = { 25 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public boolean startReceivingSleepData()
  {
    if (this.mTransferStatus != MitacAttributes.EHistoryTransferStatus.EHTS_NONE)
    {
      this.mCallback.onReceiveHistorySleep(null, new Error(MitacError.ERROR_TRANSFERRING.toString()));
      return false;
    }
    this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_SleepInit;
    
    String strSleep = "BETASLEEP";
    byte[] bytes = null;
    try
    {
      bytes = strSleep.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    byte[] value = { 3, bytes[0], bytes[1], bytes[2], bytes[3], 
      bytes[4], bytes[5], bytes[6], bytes[7], bytes[8] };
    writeCharacteristic(value);
    
    return false;
  }
  
  private void continueReceivingSleepData()
  {
    String strSleep = "BETASLEEP";
    byte[] bytes = null;
    try
    {
      bytes = strSleep.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    byte[] value = { 4, bytes[0], bytes[1], bytes[2], bytes[3], 
      bytes[4], bytes[5], bytes[6], bytes[7], bytes[8] };
    writeCharacteristic(value);
  }
  
  public void stopReceivingSleepData()
  {
    this.mTransferStatus = MitacAttributes.EHistoryTransferStatus.EHTS_NONE;
    byte[] value = { 5 };
    writeCharacteristic(value);
  }
  
  public boolean analyzeSleepData()
  {
    if (mSleepBytes.size() >= 40)
    {
      byte[] data = mSleepBytes.toByteArray();
      
      int startIdx = 0;
      int recordLen = (int)Byte2IntLBS(data, startIdx, 4) * 4;
      if (recordLen > data.length) {
        return false;
      }
      startIdx = 4;
      long sinceSec = Byte2IntLBS(data, startIdx, 4) * 1000L;
      
      startIdx = recordLen - 28;
      int awaknCount = (data[(startIdx + 3)] << 24) + (data[(startIdx + 2)] << 16) + (data[(startIdx + 1)] << 8) + (data[(startIdx + 0)] & 0xFF);
      
      startIdx = recordLen - 24;
      long eSec = Byte2IntLBS(data, startIdx, 4) * 1000L;
      
      int offsetSec = TimeZone.getDefault().getOffset(new Date().getTime());
      sinceSec -= offsetSec;
      eSec -= offsetSec;
      
      startIdx = recordLen - 20;
      int bedTime = (int)Byte2IntLBS(data, startIdx, 4);
      
      startIdx = recordLen - 16;
      int latency = (int)Byte2IntLBS(data, startIdx, 4);
      
      startIdx = recordLen - 12;
      int effiviency = (int)Byte2IntLBS(data, startIdx, 4);
      
      startIdx = recordLen - 8;
      int period = (int)Byte2IntLBS(data, startIdx, 4);
      
      MitacSleepData sleepData = new MitacSleepData(new Date(sinceSec), new Date(eSec), bedTime, latency, awaknCount, effiviency, period);
      mSleepDataList.add(sleepData);
      mSleepBytes.reset();
      
      return true;
    }
    return false;
  }
  
  public boolean setUID(byte[] uidbyte)
  {
    byte[] startValue = { 16, 15 };
    writeCharacteristic(startValue);
    
    byte[] firstValue = { 16, 26, 
      uidbyte[0], uidbyte[1], uidbyte[2], uidbyte[3], uidbyte[4], uidbyte[5], uidbyte[6], uidbyte[7], 
      uidbyte[8], uidbyte[9], uidbyte[10], uidbyte[11], uidbyte[12], uidbyte[13], uidbyte[14], uidbyte[15] };
    writeCharacteristic(firstValue);
    
    byte[] lastValue = { 16, 27, 
      uidbyte[16], uidbyte[17], uidbyte[18], uidbyte[19], uidbyte[20], uidbyte[21], uidbyte[22], uidbyte[23], 
      uidbyte[24], uidbyte[25], uidbyte[26], uidbyte[27], uidbyte[28], uidbyte[29], uidbyte[30], uidbyte[31] };
    writeCharacteristic(lastValue);
    
    byte[] endValue = { 16, 16 };
    writeCharacteristic(endValue);
    
    return true;
  }
  
  public boolean getUID()
  {
    byte[] firstValue = { 15, 28 };
    writeCharacteristic(firstValue);
    
    return true;
  }
  
  private boolean getUIDContinue()
  {
    byte[] lastValue = { 15, 29 };
    writeCharacteristic(lastValue);
    
    return true;
  }
  
  public boolean getAlarm()
  {
    byte[] value = { 15, 30 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public boolean setAlarm(MitacAlarmData[] alarmData)
  {
    byte[] startValue = { 16, 15 };
    writeCharacteristic(startValue);
    
    byte[] value = { 16, 10, 
            (byte)(alarmData[0].mIsEnable ? 1 : 0), (byte)alarmData[0].mHr, (byte)alarmData[0].mMin,
            (byte)(alarmData[1].mIsEnable ? 1 : 0), (byte)alarmData[1].mHr, (byte)alarmData[1].mMin,
            (byte)(alarmData[2].mIsEnable ? 1 : 0), (byte)alarmData[2].mHr, (byte)alarmData[2].mMin,
            (byte)(alarmData[3].mIsEnable ? 1 : 0), (byte)alarmData[3].mHr, (byte)alarmData[3].mMin,
            (byte)(alarmData[4].mIsEnable ? 1 : 0), (byte)alarmData[4].mHr, (byte)alarmData[4].mMin };
    writeCharacteristic(value);
    
    byte[] endValue = { 16, 16 };
    writeCharacteristic(endValue);
    
    return true;
  }
  
  public boolean getTimeToSleep()
  {
    byte[] value = { 15, 31 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public boolean setTimeToSleep(MitacAlarmData[] alarmData)
  {
    byte[] startValue = { 16, 11 };
    writeCharacteristic(startValue);
    
    byte[] value = { 16, 11, 
            (byte)(alarmData[0].mIsEnable ? 1 : 0), (byte)alarmData[0].mHr, (byte)alarmData[0].mMin,
            (byte)(alarmData[1].mIsEnable ? 1 : 0), (byte)alarmData[1].mHr, (byte)alarmData[1].mMin,
            (byte)(alarmData[2].mIsEnable ? 1 : 0), (byte)alarmData[2].mHr, (byte)alarmData[2].mMin,
            (byte)(alarmData[3].mIsEnable ? 1 : 0), (byte)alarmData[3].mHr, (byte)alarmData[3].mMin };
    writeCharacteristic(value);
    
    byte[] endValue = { 16, 16 };
    writeCharacteristic(endValue);
    
    return true;
  }
  
  public boolean setUnitFormat(boolean bIsMetric)
  {
    byte[] value = { 29, (byte)(bIsMetric ? 0 : 1 )};
    writeCharacteristic(value);
    
    return true;
  }
  
  public boolean getUnitFormat()
  {
    byte[] value = { 30 };
    writeCharacteristic(value);
    
    return true;
  }
  
  public static boolean setCharacteristicControl(BluetoothGattCharacteristic characteristic)
  {
    if ((mBluetoothAdapter == null) || (mBluetoothGatt == null))
    {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return false;
    }
    return mBluetoothGatt.writeCharacteristic(characteristic);
  }
  
  private static void writeCharacteristic(byte[] value)
  {
    if ((mBluetoothAdapter == null) || (mBluetoothGatt == null))
    {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return;
    }
    BluetoothGattService LumService = mBluetoothGatt.getService(MitacAttributes.TRANSFER_WEARABLE_SERVICE_UUID);
    if (LumService == null) {
      return;
    }
    BluetoothGattCharacteristic LumChar = LumService.getCharacteristic(MitacAttributes.TRANSFER_WEARABLE_CONTROL_UUID);
    if (LumChar == null) {
      return;
    }
    LumChar.setValue(value);
    setCharacteristicControl(LumChar);
  }
  
  private boolean needToAdd(BluetoothDevice device)
  {
    if (device.getName() == null) {
      return false;
    }
    if ((!device.getName().equalsIgnoreCase("GLORY")) && (
      (device.getName().length() != 10) || (!device.getName().substring(0, 4).equals("S000")))) {
      return false;
    }
    if (this.mDeviceList.get(device.getAddress()) != null) {
      return false;
    }
    this.mDeviceList.put(device.getAddress(), device);
    return true;
  }
  
  public String getDeviceType(BluetoothDevice device)
  {
    if (device.getType() == 1) {
      return "DEVICE_TYPE_CLASSIC";
    }
    if (device.getType() == 3) {
      return "DEVICE_TYPE_DUAL";
    }
    if (device.getType() == 2) {
      return "DEVICE_TYPE_LE";
    }
    return "DEVICE_TYPE_UNKNOWN";
  }
  
  public MitacBleDevice getLastConnectDevice()
  {
    return this.mLastConnectDevice;
  }
  
  public SampleGattAttributes.EConnectStatus getConnectStatus()
  {
    return mConnectionState;
  }
  
  public boolean resetToDefault()
  {
    byte[] value = { 24 };
    writeCharacteristic(value);
    
    return true;
  }
  
  private boolean isNewFWVersion()
  {
    if (this.mLastConnectDevice.mStrName == null) {
      return false;
    }
    return this.mLastConnectDevice.mStrName.compareTo("GLORY") == 0;
  }
  
  private void resetOTAStatus()
  {
    mConnectionState = SampleGattAttributes.EConnectStatus.STATE_DISCONNECTED;
    
    this.mOTAStatus = MitacAttributes.EOTAUpdateStatus.EOTA_NONE;
    mFWData = null;
    mCurrentPacketNumber = 0;
    mPacketNumber = 0;
    mLastDataByteNumber = 0;
  }
  
  public static abstract interface BLEReceiveCallback
  {
    public abstract void onConnectStatusChange(MitacAttributes.EGattStatus paramEGattStatus);
    
    public abstract void onDeviceScanned(BluetoothDevice paramBluetoothDevice);
    
    public abstract void onEKGDataReceived(int[] paramArrayOfInt, Error paramError);
    
    public abstract void onRealtimeActivityDataReceived(MitacActivityData paramMitacActivityData, Error paramError);
    
    public abstract void onReceiveFWVersion(String paramString);
    
    public abstract void onReceiveUUID(String paramString1, String paramString2);
    
    public abstract void onReceiveMacAddr(String paramString);
    
    public abstract void onReceiveDateTime(Date paramDate, TimeZone paramTimeZone, boolean paramBoolean);
    
    public abstract void onReceiveBatteryLevel(int paramInt, boolean paramBoolean);
    
    public abstract void onReceiveProfile(int paramInt, float paramFloat1, float paramFloat2, boolean paramBoolean);
    
    public abstract void onReceiveGoalSetting(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
    
    public abstract void onReceiveSleepStatus(boolean paramBoolean);
    
    public abstract void onReceiveUID(String paramString);
    
    public abstract void onReceiveSleepAlarm(MitacAlarmData[] paramArrayOfMitacAlarmData);
    
    public abstract void onReceiveTimeToSleep(MitacAlarmData[] paramArrayOfMitacAlarmData);
    
    public abstract void onReceiveSevenDaysActivity(MitacActivityData[] paramArrayOfMitacActivityData, Error paramError);
    
    public abstract void onReceiveHistorySleep(MitacSleepData[] paramArrayOfMitacSleepData, Error paramError);
    
    public abstract void onReceiveSetInfoError(Error paramError);
    
    public abstract void onFirmwareTransferring(float paramFloat, Error paramError);
    
    public abstract void onReceiveFWMode(boolean paramBoolean);
    
    public abstract void onReceiveFWRamSize(boolean paramBoolean);
    
    public abstract void onReveiveUnitFormat(boolean paramBoolean);
  }
}
