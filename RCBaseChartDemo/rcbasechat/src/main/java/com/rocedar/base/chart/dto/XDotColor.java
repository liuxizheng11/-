package com.rocedar.base.chart.dto;


import android.graphics.Color;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午3:29
 * 版本：V1.0.00
 * 描述：X轴点的颜色配置
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XDotColor {

    //默认的点的颜色
    public int defaultDotColor = Color.parseColor("#414a58");

    //选中的点的颜色
    public int focusDotColor = -1;

    //是否是圆
    public boolean isCircle = false;

    //矩形时的弧度
    public int radian = 0;

    //点的宽（DP）
    public int dotWidth = 8;

    //点的高度（DP）
    public int dotHeight = 8;


    public int getDotWidth() {
        return dotWidth;
    }

    public void setDotWidth(int dotWidth) {
        this.dotWidth = dotWidth;
    }

    public int getDotHeight() {
        return dotHeight;
    }

    public void setDotHeight(int dotHeight) {
        this.dotHeight = dotHeight;
    }

    public int getRadian() {
        return radian;
    }

    public void setRadian(int radian) {
        this.radian = radian;
    }

    public int getDefaultDotColor() {
        return defaultDotColor;
    }

    public void setDefaultDotColor(int defaultDotColor) {
        this.defaultDotColor = defaultDotColor;
    }

    public int getFocusDotColor() {
        return focusDotColor;
    }

    public void setFocusDotColor(int focusDotColor) {
        this.focusDotColor = focusDotColor;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }
}
