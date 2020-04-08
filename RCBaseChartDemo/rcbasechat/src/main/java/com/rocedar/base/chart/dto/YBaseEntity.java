package com.rocedar.base.chart.dto;

import java.util.List;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2017/12/28 下午3:54
 * 版本：V1.0.00
 * 描述：Y轴的实例
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class YBaseEntity {

    private String unitShow;

    private int unitColor;

    private List<Double> showDataList;

    private int dataTextColor;

    private double dateTextSize = 10;


    public String getUnitShow() {
        return unitShow;
    }

    /**
     * 设置显示的单位内容
     *
     * @param unitShow
     */
    public void setUnitShow(String unitShow) {
        this.unitShow = unitShow;
    }

    public int getUnitColor() {
        return unitColor;
    }

    /**
     * 设置显示的单位颜色
     *
     * @param unitColor
     */
    public void setUnitColor(int unitColor) {
        this.unitColor = unitColor;
    }

    public List<Double> getShowDataList() {
        return showDataList;
    }

    /**
     * 设置显示的Y轴数值
     *
     * @param showDataList
     */
    public void setShowDataList(List<Double> showDataList) {
        this.showDataList = showDataList;
    }


    public int getDataTextColor() {
        return dataTextColor;
    }

    /**
     * 设置Y轴数值的颜色
     *
     * @param dataTextColor
     */
    public void setDataTextColor(int dataTextColor) {
        this.dataTextColor = dataTextColor;
    }

    public double getDateTextSize() {
        return dateTextSize;
    }

    /**
     * 设置Y轴数值的字体大小
     *
     * @param dateTextSize
     */
    public void setDateTextSize(double dateTextSize) {
        this.dateTextSize = dateTextSize;
    }
}
