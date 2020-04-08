package com.rocedar.sdk.iting.device.envet;

/**
 * 项目名称：项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/15 4:31 PM
 * 版本：V1.1.00
 * 描述：设备设置后通知EventBean
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingDeviceEvent {

    public int type;

    public boolean success;

    public RCITingDeviceEvent(int type, boolean success) {
        this.type = type;
        this.success = success;
    }

    public Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
