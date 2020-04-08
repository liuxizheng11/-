package com.mitac.callback;

import com.mitac.ble.MitacBleDevice;

public abstract interface MitacDidDisconnectCallback
{
  public abstract void didDisconnectFromWristBand(MitacBleDevice paramMitacBleDevice, Error paramError);
}
