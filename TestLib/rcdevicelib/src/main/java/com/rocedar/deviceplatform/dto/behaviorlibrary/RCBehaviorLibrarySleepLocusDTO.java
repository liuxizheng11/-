package com.rocedar.deviceplatform.dto.behaviorlibrary;

/**
 * 作者：lxz
 * 日期：17/7/28 下午2:46
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBehaviorLibrarySleepLocusDTO {

    /**
     * (状态：1-浅睡/2-深睡/3-清醒, )
     */
    private int status;
    /**
     * (时长：秒)
     */
    private int duration;
    //开始时间
    private long startTime;
    //结束时间
    private long stopTime;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
}
