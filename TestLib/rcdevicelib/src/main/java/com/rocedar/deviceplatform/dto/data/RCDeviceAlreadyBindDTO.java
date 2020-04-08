package com.rocedar.deviceplatform.dto.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author liuyi
 * @date 2017/2/14
 * @desc 查询已绑定的蓝牙设备返回的数据
 * @veison V1.0
 */

public class RCDeviceAlreadyBindDTO {

    /**
     * device_id:"1219001",
     * device_no:"F3:C5:28:79:D6:7D",
     * device_name:"B15P"
     */

    /**
     * 设备id
     */
    private int device_id;
    /**
     * 设备号(蓝牙mac地址)
     */
    private String device_no;
    /**
     * 厂商显示名,多个名称逗号分隔
     */
    private String device_name = "";

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }


    public static JSONObject toJSON(RCDeviceAlreadyBindDTO dto) {
        try {
            JSONObject object = new JSONObject();
            object.put("device_id", dto.device_id);
            object.put("device_no", dto.device_no);
            object.put("device_name", dto.device_name);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RCDeviceAlreadyBindDTO toDTO(JSONObject jsonObject) {
        RCDeviceAlreadyBindDTO dto = new RCDeviceAlreadyBindDTO();
        dto.setDevice_id(jsonObject.optInt("device_id"));
        dto.setDevice_no(jsonObject.optString("device_no"));
        dto.setDevice_name(jsonObject.optString("device_name"));
        return dto;
    }
}
