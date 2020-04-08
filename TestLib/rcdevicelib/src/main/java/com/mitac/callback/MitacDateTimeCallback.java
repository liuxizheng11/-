package com.mitac.callback;

import java.util.Date;
import java.util.TimeZone;

public abstract interface MitacDateTimeCallback
{
  public abstract void didReceiveDateTime(Date paramDate, TimeZone paramTimeZone, boolean paramBoolean, Error paramError);
}
