package com.mitac.ble;

import java.util.Date;

public class MitacActivityData
{
  public int mStep = 0;
  public int mCalorie = 0;
  public float mDistance = 0.0F;
  public int mActivityTime = 0;
  public Date mDate = null;
  
  public MitacActivityData(int step, int calorie, int activityTime, float distance)
  {
    this(step, calorie, activityTime, distance, new Date());
  }
  
  public MitacActivityData(int step, int calorie, int activityTime, float distance, Date date)
  {
    this.mStep = step;
    this.mCalorie = calorie;
    this.mDistance = distance;
    this.mActivityTime = activityTime;
    this.mDate = date;
  }
}
