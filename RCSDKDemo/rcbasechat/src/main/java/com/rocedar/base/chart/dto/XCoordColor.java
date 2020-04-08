package com.rocedar.base.chart.dto;

/**
 * 项目名称：图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 上午11:38
 * 版本：V1.0.00
 * 描述：X轴坐标显示颜色值定义
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XCoordColor {

    //文字颜色
    public int textColor = -1;

    //背景颜色
    public int backgroundColor = -1;


    public XCoordColor(int textColor) {
        this.textColor = textColor;
    }

    public XCoordColor(int textColor, int backgroundColor) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
