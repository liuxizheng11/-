package com.mitac.callback;

import com.mitac.ble.SampleGattAttributes;

public abstract interface MitacBLEStateChangeCallback
{
  public abstract void onConnectionStateChange(SampleGattAttributes.EConnectStatus paramEConnectStatus, Error paramError);
}
