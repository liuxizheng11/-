package com.rocedar.sdk.iting.device;

import android.content.Context;

import com.rocedar.sdk.iting.device.dto.RCITingRemindWayEnum;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/3/30 2:27 PM
 * 版本：V1.1.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public enum ITingDeviceEnum {

    CREATE_ONE_A("CREATE ONE-A", "P03A", ITingDeviceID.DEVICE_TYPE_CREATE_ONE_A, 360, RCITingRemindWayEnum.Sound),
    CREATE_ONE_B("CREATE ONE-A", "P03B", ITingDeviceID.DEVICE_TYPE_CREATE_ONE_B, 360, RCITingRemindWayEnum.Sound);


    public String nameKeyword;
    public String modelKeyword;
    public int imageWight;
    public int deviceID;
    public RCITingRemindWayEnum[] remindWay;

    /**
     * @param nameKeyword  设备名称
     * @param modelKeyword 设备型号
     * @param deviceID     设备ID
     */
    ITingDeviceEnum(String nameKeyword, String modelKeyword, int deviceID, int imageWight, RCITingRemindWayEnum... remindWay) {
        this.nameKeyword = nameKeyword;
        this.modelKeyword = modelKeyword;
        this.deviceID = deviceID;
        this.remindWay = remindWay;
        this.imageWight = imageWight;
    }

    public String getNameKeyword() {
        return nameKeyword;
    }


    public String getModelKeyword() {
        return modelKeyword;
    }


    public int getDeviceID() {
        return deviceID;
    }

    public RCITingRemindWayEnum[] getRemindWay() {
        return remindWay;
    }

    public static ITingDeviceEnum checkFromDeviceId(String modelKeyword) {
        for (ITingDeviceEnum e : ITingDeviceEnum.values()) {
            if (modelKeyword.toUpperCase().contains(e.modelKeyword.toUpperCase())) {
                return e;
            }
        }
        return null;
    }

    public static ITingDeviceEnum checkFromDeviceId(int deviceID) {
        for (ITingDeviceEnum e : ITingDeviceEnum.values()) {
            if (deviceID == e.deviceID) {
                return e;
            }
        }
        return null;
    }


    public String[] getRemindWayString(Context context) {
        String temp[] = new String[remindWay.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = context.getString(remindWay[i].getTypeName());
        }
        return temp;
    }

}
