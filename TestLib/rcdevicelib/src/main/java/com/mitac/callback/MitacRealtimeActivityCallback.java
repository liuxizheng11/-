package com.mitac.callback;

import com.mitac.ble.MitacActivityData;

public abstract interface MitacRealtimeActivityCallback
{
  public abstract void didActivityReceived(MitacActivityData paramMitacActivityData, Error paramError);
}
