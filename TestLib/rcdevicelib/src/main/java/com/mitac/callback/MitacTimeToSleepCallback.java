package com.mitac.callback;

import com.mitac.ble.MitacAlarmData;

public abstract interface MitacTimeToSleepCallback
{
  public abstract void didReceiveTimeToSleep(MitacAlarmData[] paramArrayOfMitacAlarmData, Error paramError);
}
