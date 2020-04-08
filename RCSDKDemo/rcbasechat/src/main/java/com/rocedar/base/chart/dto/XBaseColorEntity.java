package com.rocedar.base.chart.dto;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/1/2 上午10:48
 * 版本：V1.0.00
 * 描述：X轴的基本配置
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XBaseColorEntity {

    //x轴默认颜色值
    private XCoordColor xColorDefault = null;

    //x轴选中颜色值
    private XCoordColor xColorFocus = null;


    public XCoordColor getxColorDefault() {
        return xColorDefault;
    }


    public void setxColorDefault(XCoordColor xColorDefault) {
        this.xColorDefault = xColorDefault;
    }

    public XCoordColor getxColorFocus() {
        return xColorFocus;
    }

    public void setxColorFocus(XCoordColor xColorFocus) {
        this.xColorFocus = xColorFocus;
    }


}
