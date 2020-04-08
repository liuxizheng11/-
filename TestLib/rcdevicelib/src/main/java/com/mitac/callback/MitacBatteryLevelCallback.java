package com.mitac.callback;

public abstract interface MitacBatteryLevelCallback
{
  public abstract void didReceiveBatteryLevel(int paramInt, boolean paramBoolean, Error paramError);
}
