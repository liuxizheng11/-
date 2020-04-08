package com.rocedar.lib.base.network;


import com.rocedar.lib.base.config.Config;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;

/**
 * Created by phj on 2016/12/17.
 */

public class RCBean {

    /**
     * actionName
     */
    private String actionName;

    private String baseNetUrl = Config.P_NETWORK_URL;

    public String p_token = RCSPUserInfo.getLastSDKToken();

    private boolean checkToken = true;

    public boolean isCheckToken() {
        return checkToken;
    }

    public void setCheckToken(boolean checkToken) {
        this.checkToken = checkToken;
    }

    public NetworkMethod networkMethod = NetworkMethod.SDK;


    public String getP_token() {
        return p_token;
    }


    public NetworkMethod getNetworkMethod() {
        return networkMethod;
    }

    public void setNetworkMethod(NetworkMethod networkMethod) {
        this.networkMethod = networkMethod;
    }

    public void setP_token(String p_token) {
        this.p_token = p_token;
    }

    public String getBaseNetUrl() {
        return baseNetUrl;
    }

    public void setBaseNetUrl(String baseNetUrl) {
        this.baseNetUrl = baseNetUrl;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }


}
