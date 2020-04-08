package com.rocedar.sdk.iting.device;

import com.rocedar.sdk.iting.request.dto.RCITingWatchInfoDTO;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/13 4:18 PM
 * 版本：V1.1.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ITingDeviceInfoUtil {


    private String deviceInfo;

    private String[] infos;

    public ITingDeviceInfoUtil(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        try {
            infos = deviceInfo.split("\\|");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDeviceId() {
        if (infos != null) {
            ITingDeviceEnum deviceEnum = null;
            if (infos.length == 5)
                deviceEnum = ITingDeviceEnum.checkFromDeviceId(infos[4]);
            if (infos.length == 4)
                deviceEnum = ITingDeviceEnum.checkFromDeviceId(infos[3]);
            if (deviceEnum != null) {
                return deviceEnum.deviceID;
            }
        }
        return -1;
    }

    public String getDeviceMac() {
        if (infos != null && infos.length > 2) {
            if (infos[1] != null && infos[1].length() == 12) {
                return stringToMac(infos[1]);
            }
        }
        return "";
    }

    public String getDeviceModel() {
        if (infos != null) {
            if (infos.length == 5)
                return infos[4];
            if (infos.length == 4)
                return infos[3];
        }
        return "";
    }


    public String getDeviceSN() {
        if (infos != null && infos.length > 0) {
            return infos[2];
        }
        return "";
    }

    public String getDeviceNo() {
        if (infos != null && infos.length > 0) {
            return infos[0];
        }
        return "";
    }

    public RCITingWatchInfoDTO getDeviceDTO() {
        RCITingWatchInfoDTO dto = new RCITingWatchInfoDTO();
        dto.setDeviceNo(getDeviceNo());
        dto.setDeviceId(getDeviceId());
        dto.setDeviceMac(getDeviceMac());
        dto.setDeviceModel(getDeviceModel());
        dto.setDeviceSN(getDeviceSN());
        dto.setInfo(deviceInfo);
        return dto;
    }

    private String stringToMac(String info) {
        StringBuilder builder = new StringBuilder();
        char[] macs = info.toCharArray();
        for (int i = 1; i <= macs.length; i++) {
            builder.append(macs[i - 1]);
            if (i % 2 == 0 && i != macs.length) {
                builder.append(":");
            }
        }
        return builder.toString().toUpperCase();
    }


    /**
     * https://d.appscomm.cn/0004?Info=Helio #03721|
     * 4c5919321408|FCP03A19011201003721|A0.3R0.6T1.1H0.5B0.2|P03A
     */


}
