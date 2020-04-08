package com.mitac.callback;

import com.mitac.ble.MitacAlarmData;

public abstract interface MitacAlarmCallback
{
  public abstract void didReceiveAlarm(MitacAlarmData[] paramArrayOfMitacAlarmData, Error paramError);
}
