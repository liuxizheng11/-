package com.rocedar.base.chart.dto;

import android.graphics.Color;

import com.rocedar.base.chart.dto.base.ConnectDotType;

import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/1/2 下午6:33
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XMultiLineBaseEntity extends XLineBaseEntity {

    //是否连接点
    private ConnectDotType connectDotType = ConnectDotType.SELECT;

    //连接线的颜色
    private int XConnectLineColor = Color.parseColor("#abd4cc");

    //连接的宽度（DP）
    private double XConnectLineWidth = 0.67;

    //每条线的样式(如果不设置取基本样式)
    private List<XLineBaseEntity> xLineBaseEntityList = null;

    public int getXConnectLineColor() {
        return XConnectLineColor;
    }

    public void setXConnectLineColor(int XConnectLineColor) {
        this.XConnectLineColor = XConnectLineColor;
    }

    public double getXConnectLineWidth() {
        return XConnectLineWidth;
    }

    public void setXConnectLineWidth(double XConnectLineWidth) {
        this.XConnectLineWidth = XConnectLineWidth;
    }

    public ConnectDotType getConnectDotType() {
        return connectDotType;
    }

    public void setConnectDotType(ConnectDotType connectDotType) {
        this.connectDotType = connectDotType;
    }

    public List<XLineBaseEntity> getxLineBaseEntityList() {
        return xLineBaseEntityList;
    }

    public void setxLineBaseEntityList(List<XLineBaseEntity> xLineBaseEntityList) {
        this.xLineBaseEntityList = xLineBaseEntityList;
    }
}
