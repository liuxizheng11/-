package com.mitac.callback;

import com.mitac.ble.MitacSleepData;

public abstract interface MitacHistorySleepCallback
{
  public abstract void didReceiveHistorySleep(MitacSleepData[] paramArrayOfMitacSleepData, Error paramError);
}
