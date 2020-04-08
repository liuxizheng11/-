package com.rocedar.base.chart.dto;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午3:30
 * 版本：V1.0.00
 * 描述：X轴点的图片配置
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XDotImage {


    //默认图片Id
    public int defaultImageId = -1;
    //选中图片ID
    public int focusImageId = -1;

    public int getDefaultImageId() {
        return defaultImageId;
    }

    public void setDefaultImageId(int defaultImageId) {
        this.defaultImageId = defaultImageId;
    }

    public int getFocusImageId() {
        return focusImageId;
    }

    public void setFocusImageId(int focusImageId) {
        this.focusImageId = focusImageId;
    }
}
