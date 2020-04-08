package com.rocedar.deviceplatform.dto.data;

/**
 * @author liuyi
 * @date 2017/2/16
 * @desc 蓝牙设备详情DTO
 * @veison V1.0
 */

public class RCDeviceBlueToothDetailsDTO {
    /**
     * 蓝牙设备默认名(多个名字逗号分隔)
     */
    private String device_name;


    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
    /**
     *  在产品中显示名(设备名称)
    */
    private String display_name;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

}
