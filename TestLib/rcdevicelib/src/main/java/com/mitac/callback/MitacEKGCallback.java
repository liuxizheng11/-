package com.mitac.callback;

import com.mitac.ble.MitacEKGData;

public abstract interface MitacEKGCallback
{
  public abstract void didEKGDataReceived(MitacEKGData paramMitacEKGData, Error paramError);
  
  public abstract void didEKGDataFinalAnalysis(MitacEKGData paramMitacEKGData, Error paramError);
}
