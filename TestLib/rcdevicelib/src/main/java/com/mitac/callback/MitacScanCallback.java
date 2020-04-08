package com.mitac.callback;

import com.mitac.ble.MitacBleDevice;

public abstract interface MitacScanCallback
{
  public abstract void didScanWristBand(MitacBleDevice paramMitacBleDevice, Error paramError);
}
