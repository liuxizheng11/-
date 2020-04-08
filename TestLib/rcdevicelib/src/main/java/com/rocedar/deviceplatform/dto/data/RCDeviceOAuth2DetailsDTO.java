package com.rocedar.deviceplatform.dto.data;

/**
 * @author liuyi
 * @date 2017/2/16
 * @desc OAuth2设备详情DTO
 * @veison V1.0
 */

public class RCDeviceOAuth2DetailsDTO {
    /**
     * 跳转第三方账号登录地址
     */
    private String login_url;

    /**
     * 回调url中截取code参数名
     */
    private String param_name;

    /**
     * 判断回调url关键字
     */
    private String redirect_url;

    /**
     * 在产品中显示名(设备名称)
     */
    private String display_name;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
}
