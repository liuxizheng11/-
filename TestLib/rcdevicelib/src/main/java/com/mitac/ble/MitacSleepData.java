package com.mitac.ble;

import java.util.Date;

public class MitacSleepData
{
  public Date mStartTime;
  public Date mEndTime;
  public int mTotalBedTime;
  public int mLatency;
  public float mSleepDuration;
  public float mAwakenDuration;
  public int mAwakenCount;
  public int mEfficiency;
  public int mPeriodTime;
  
  MitacSleepData(Date startTime, Date endTime, int totalBed, int latency, int awakenCount, int efficiency, int periodTime)
  {
    this.mStartTime = startTime;
    this.mEndTime = endTime;
    this.mTotalBedTime = totalBed;
    this.mLatency = latency;
    this.mAwakenDuration = (awakenCount * 0.5F);
    this.mAwakenCount = awakenCount;
    
    this.mSleepDuration = (totalBed * efficiency / 100.0F);
    this.mEfficiency = efficiency;
    this.mPeriodTime = periodTime;
  }
}
