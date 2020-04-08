package com.rocedar.deviceplatform.dto.target;

/**
 * 作者：lxz
 * 日期：17/7/10 上午11:47
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCIndicatorTargetWeekProgressDayDTO {
    /**
     * 日期
     */
    private int week_date;
    /**
     * 进度值
     */
    private float progress;

    public int getWeek_date() {
        return week_date;
    }

    public void setWeek_date(int week_date) {
        this.week_date = week_date;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
