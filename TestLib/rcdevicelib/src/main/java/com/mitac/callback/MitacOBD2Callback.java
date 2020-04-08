package com.mitac.callback;

public abstract interface MitacOBD2Callback
{
  public abstract void didOBD2Received(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Error paramError);
}
