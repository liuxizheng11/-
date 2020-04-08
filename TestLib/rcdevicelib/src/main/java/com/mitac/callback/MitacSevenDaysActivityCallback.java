package com.mitac.callback;

import com.mitac.ble.MitacActivityData;

public abstract interface MitacSevenDaysActivityCallback
{
  public abstract void didReceiveSevenDaysActivityData(MitacActivityData[] paramArrayOfMitacActivityData, Error paramError);
}
