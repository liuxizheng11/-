package com.mitac.ble;

import java.util.HashMap;

public class SampleGattAttributes
{
  private static HashMap<String, String> attributes = new HashMap();
  public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
  public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
  public static String TRANSFER_WEARABLE_SERVICE_UUID = "0a0aee03-0a00-1000-8000-00805f9b34fb";
  public static String TRANSFER_WEARABLE_CONTROL_UUID = "0a0ae00a-0a00-1000-8000-00805f9b34fb";
  public static String TRANSFER_WEARABLE_SLEEPCONTENT_UUID = "0a0ae00b-0a00-1000-8000-00805f9b34fb";
  public static String TRANSFER_WEARABLE_EKG_UUID = "0a0ae00c-0a00-1000-8000-00805f9b34fb";
  public static String TRANSFER_WEARABLE_ACTIVITY_UUID = "0a0ae00d-0a00-1000-8000-00805f9b34fb";
  public static String CHARACTERISTIC_CHANGE_CONNECTION_INTERVAL = "ffffccc2-00f7-4000-b000-000000000000";
  public static String SERVICE_CHANGE_CONNECTION_INTERVAL = "ffffccc0-00f7-4000-b000-000000000000";
  
  static
  {
    attributes.put("0000180d-0000-1000-8000-00805f9b34fb", 
      "Heart Rate Service");
    attributes.put("0000180a-0000-1000-8000-00805f9b34fb", 
      "Device Information Service");
    
    attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
    attributes.put("00002a29-0000-1000-8000-00805f9b34fb", 
      "Manufacturer Name String");
    
    attributes.put(TRANSFER_WEARABLE_SERVICE_UUID, "MiWell Transfer Service");
    attributes.put(TRANSFER_WEARABLE_CONTROL_UUID, "MiWell Transfer Control");
    attributes.put(TRANSFER_WEARABLE_SLEEPCONTENT_UUID, "MiWell Transfer Sleep Control");
    attributes.put(TRANSFER_WEARABLE_EKG_UUID, "MiWell Transfer EKG");
    attributes.put(TRANSFER_WEARABLE_ACTIVITY_UUID, "MiWell Transfer Activity");
  }
  
  public static String lookup(String uuid, String defaultName)
  {
    String name = (String)attributes.get(uuid);
    return name == null ? defaultName : name;
  }
  
  public static String lookup(String uuid)
  {
    return lookup(uuid, uuid);
  }
  
  public static enum EConnectStatus
  {
    STATE_DISCONNECTED,  STATE_CONNECTING,  STATE_CONNECTED,  STATE_SCANING;
  }
}
