package com.rocedar.base.chart.dto;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/2/27 上午10:28
 * 版本：V1.0.00
 * 描述：横向条形图数据实例
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class YTransverseBarEntity extends XDataBaseEntity {

    //条的颜色
    public int barColor;
    //Y轴的颜色
    public String yText;


    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public String getYText() {
        return yText;
    }

    public void setYText(String yText) {
        this.yText = yText;
    }
}
