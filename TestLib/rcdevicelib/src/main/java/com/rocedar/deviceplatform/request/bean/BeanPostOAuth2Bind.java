package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/2/10
 * @desc OAuth2设备绑定
 * @veison
 */

public class BeanPostOAuth2Bind extends BasePlatformBean{
    /**
     * 设备id
     */
    public String device_id;
    /**
     * OAuth2返回url中code参数
     */
    public String code;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
