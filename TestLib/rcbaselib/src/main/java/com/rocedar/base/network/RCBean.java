package com.rocedar.base.network;


import com.rocedar.base.shareprefernces.RCSPBaseInfo;

/**
 * Created by phj on 2016/12/17.
 */

public class RCBean {

    /**
     * actionName
     */
    public String actionName;

    public String baseUserId;

    public String baseNetUrl;

    public String getBaseNetUrl() {
        return baseNetUrl;
    }

    public void setBaseNetUrl(String baseNetUrl) {
        this.baseNetUrl = baseNetUrl;
    }

    public String getBaseUserId() {
        return RCSPBaseInfo.getLastUserId() + "";
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }


}
