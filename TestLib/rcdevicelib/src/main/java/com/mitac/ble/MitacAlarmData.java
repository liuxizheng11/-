package com.mitac.ble;

public class MitacAlarmData
{
  public int mHr;
  public int mMin;
  public boolean mIsEnable;
  
  public MitacAlarmData(int iHr, int iMin, boolean bIsEnable)
  {
    this.mHr = iHr;
    this.mMin = iMin;
    this.mIsEnable = bIsEnable;
  }
}
