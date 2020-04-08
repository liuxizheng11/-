package com.rocedar.deviceplatform.dto.target;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/10 上午11:19
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCIndicatorTargetWeekProgressDTO {

    /**
     * 日期
     */
    private int week_date;
    /**
     * 进度值
     */
    private float progress;

    /**
     * 每天进度 list(dict)
     */
    private List<RCIndicatorTargetWeekProgressDayDTO> mList;


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

    public List<RCIndicatorTargetWeekProgressDayDTO> getmList() {
        return mList;
    }

    public void setmList(List<RCIndicatorTargetWeekProgressDayDTO> mList) {
        this.mList = mList;
    }


}
