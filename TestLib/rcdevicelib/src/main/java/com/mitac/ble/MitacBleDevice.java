package com.mitac.ble;

public class MitacBleDevice
{
  public String mStrName;
  public String mStrMacAddr;
  public String mStrType;
  
  public MitacBleDevice(String strName, String strMacAddr, String strType)
  {
    this.mStrName = strName;
    this.mStrMacAddr = strMacAddr;
    this.mStrType = strType;
  }
}
