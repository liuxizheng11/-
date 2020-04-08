package com.rocedar.base.chart.dto;

import com.rocedar.base.chart.dto.base.DotType;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午4:07
 * 版本：V1.0.00
 * 描述：折线的数据实例
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XLineDataEntity extends XDataBaseEntity {

    //点的类型
    private DotType dotType = DotType.NONE;

    //点的颜色（和点的图片互斥）(非必需设置，在这里设置为单个设置，在XLineEntity中可以设置整体样式)
    private XDotColor xDotColor;

    //点的图片（和点的颜色互斥）(非必需设置，在这里设置为单个设置，在XLineEntity中可以设置整体样式)
    private XDotImage xDotImage;


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
