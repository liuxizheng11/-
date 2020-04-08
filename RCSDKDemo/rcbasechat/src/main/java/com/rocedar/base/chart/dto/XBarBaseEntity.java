package com.rocedar.base.chart.dto;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/1/3 下午4:50
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XBarBaseEntity extends XBaseColorEntity {

    //柱的颜色(整体)
    private int XBarColor = -1;

    //柱的宽度（DP）(整体)
    private double XBarWidth = -1;

    //头部的圆角
    private int topRadian = 0;


    public int getXBarColor() {
        return XBarColor;
    }

    public void setXBarColor(int XBarColor) {
        this.XBarColor = XBarColor;
    }

    public double getXBarWidth() {
        return XBarWidth;
    }

    public void setXBarWidth(double XBarWidth) {
        this.XBarWidth = XBarWidth;
    }

    public int getTopRadian() {
        return topRadian;
    }

    public void setTopRadian(int topRadian) {
        this.topRadian = topRadian;
    }
}
