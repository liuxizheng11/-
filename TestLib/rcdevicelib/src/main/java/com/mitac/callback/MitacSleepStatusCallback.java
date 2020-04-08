package com.mitac.callback;

public abstract interface MitacSleepStatusCallback
{
  public abstract void didReceiveSleepMonitorStatus(boolean paramBoolean, Error paramError);
}
