package com.rocedar.deviceplatform.dto.indicatorconduct;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/3/8 下午5:33
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class ConductIndicatorHistoryDataDetailDTO {

    //名称
    private String name;
    //数值
    private String value;
    //单位
    private String unit;

    //附属值
    private String sub_value;

    public String getSub_value() {
        return sub_value;
    }

    public void setSub_value(String sub_value) {
        this.sub_value = sub_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
