package com.rocedar.deviceplatform.dto.data;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc 设备数据列表类型DTO
 * @veison
 */

public class RCDeviceDataListTypeDTO {
    /**
     * 设备类型ID
     */
    private int type_id;

    /**
     * 设备类型名字
     */
    private String type_name;

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
