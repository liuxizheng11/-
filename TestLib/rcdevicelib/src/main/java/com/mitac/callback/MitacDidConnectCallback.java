package com.mitac.callback;

import com.mitac.ble.MitacBleDevice;

public abstract interface MitacDidConnectCallback
{
  public abstract void didConnectToWristBand(MitacBleDevice paramMitacBleDevice, Error paramError);
}
