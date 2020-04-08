package com.heha.mitacsdk;

import java.io.Serializable;
import java.util.Date;
/**
 * Created by jeff_leung on 11/9/15.
 */
public class StepHistory implements  Cloneable,Serializable {


    public Date timestamp;
    public long step;
    public float distance;
    public long runtime;
    public long calories;
    public long accum_step;
    public MitacAttributes.ETimeZone eTimeZone = MitacAttributes.ETimeZone.TimeZone_UNKNOWN;

    //Timestamp:Sat Feb 18 10:23:04 GMT+08:00 2017 step:24
    // Accumulative step:0 distance:0.0 runtime:0
    // calories:758 Timezone:

    public String toString(){
        return "Timestamp:"+timestamp+" step:"+step+" Accumulative step:"+accum_step+" distance:"+distance+" runtime:"+runtime+" calories:"+calories+" Timezone:"+eTimeZone.toString();
    }
}
