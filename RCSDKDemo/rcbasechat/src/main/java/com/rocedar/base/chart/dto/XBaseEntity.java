package com.rocedar.base.chart.dto;

/**
 * 项目名称：基础库-图表
 * <p>
 * 作者：phj
 * 日期：2018/1/2 下午2:21
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class XBaseEntity extends XBaseColorEntity{

    //第一行的文字
    private String lineOne = null;

    //第二行的文字
    private String lineTwo = null;

    public XBaseEntity(String lineOne) {
        this.lineOne = lineOne;
    }

    public XBaseEntity(String lineOne, String lineTwo) {
        this.lineOne = lineOne;
        this.lineTwo = lineTwo;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }
}
