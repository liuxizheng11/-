package com.rocedar.base.chart.dto;

import com.rocedar.base.chart.dto.base.DotType;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/1/2 上午11:53
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XLineBaseEntity extends XBaseColorEntity {

    //折线的颜色
    private int XLineColor = -1;

    //折线的宽度（DP）
    private double XLineWidth = -1;

    //是否画点
    private boolean isDrawDot;

    //点的类型
    private DotType dotType = DotType.NONE;

    //点的颜色（和点的图片互斥）(非必需设置，在这里设置为单个设置，在XLineEntity中可以设置整体样式)
    private XDotColor xDotColor;

    //点的图片（和点的颜色互斥）(非必需设置，在这里设置为单个设置，在XLineEntity中可以设置整体样式)
    private XDotImage xDotImage;


    public int getXLineColor() {
        return XLineColor;
    }

    public void setXLineColor(int XLineColor) {
        this.XLineColor = XLineColor;
    }

    public double getXLineWidth() {
        return XLineWidth;
    }

    public void setXLineWidth(double XLineWidth) {
        this.XLineWidth = XLineWidth;
    }


    public boolean isDrawDot() {
        return isDrawDot;
    }

    public void setDrawDot(boolean drawDot) {
        isDrawDot = drawDot;
    }


    public DotType getDotType() {
        return dotType;
    }

    /**
     * 设置点的颜色(非必需设置，在这里设置为单个设置，在XLineEntity中可以设置整体样式)
     */
    public void setXDotColor(XDotColor xDotColor) {
        if (xDotColor != null) {
            this.xDotColor = xDotColor;
        } else {
            this.xDotColor = new XDotColor();
        }
        dotType = DotType.Color;
    }

    /**
     * 设置点的图片(非必需设置，在这里设置为单个设置，在XLineEntity中可以设置整体样式)
     */
    public void setXDotImage(XDotImage xDotImage) {
        if (xDotImage != null) {
            this.xDotImage = xDotImage;
        } else {
            this.xDotImage = new XDotImage();
        }
        dotType = DotType.IMAGE;
    }

    public XDotColor getxDotColor() {
        return xDotColor;
    }

    public XDotImage getxDotImage() {
        return xDotImage;
    }


}
