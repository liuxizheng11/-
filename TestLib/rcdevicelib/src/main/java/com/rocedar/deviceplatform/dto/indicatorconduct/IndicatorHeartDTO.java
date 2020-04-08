package com.rocedar.deviceplatform.dto.indicatorconduct;

import com.rocedar.base.RCDateUtil;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/25 下午4:58
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class IndicatorHeartDTO {

    //心率图表中X轴的值
    private int xVal = 0;
    //心率图表中Y轴的值（心率值）
    private int yVal;
    //数据时间(yyyyMMddHHmmss)
    private String dateTime;

    public int getxVal() {
        return xVal;
    }

    public int getyVal() {
        return yVal;
    }

    public void setyVal(int yVal) {
        this.yVal = yVal;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setxVal(int xVal) {
        this.xVal = xVal;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
        setdate(dateTime);
    }


    private void setdate(String dateTime) {
        long dateTimeTemp = RCDateUtil.getServiceTimeToDate(dateTime);
        long dateZero = RCDateUtil.getServiceTimeToDate(RCDateUtil.formatServiceTime(dateTime, "yyyyMMdd") + "000000");
        if (dateTimeTemp > dateZero) {
            this.xVal = (int) (dateTimeTemp - dateZero)/1000;
        }
    }
}
