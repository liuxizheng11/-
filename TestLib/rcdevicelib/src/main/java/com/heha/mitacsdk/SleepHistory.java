package com.heha.mitacsdk;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeff_leung on 20/10/15.
 */


public class SleepHistory implements  Cloneable,Serializable {



    public Date StartTime;
    public Date EndTime;
    public int TotalBedTime;
    public int Latency;
    public float SleepDuration;
    public float AwakenDuration;
    public int AwakenCount;
    public int Efficiency;
    public int PeriodTime;
    public MitacAttributes.ETimeZone eTimeZone = MitacAttributes.ETimeZone.TimeZone_UNKNOWN;




public String toString(){

 return   "Start:"+StartTime.toString()+" End:"+EndTime.toString()+" Timezone: "+eTimeZone.toString()+" Totalbedtime:"+TotalBedTime+" Lantency:"+Latency+" SleepDuration:"+ SleepDuration+" AwakenDurcation:"+AwakenDuration+" Efficiency:"+Efficiency+" Period:"+PeriodTime;
}

}
