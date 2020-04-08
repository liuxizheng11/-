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

    public String userId;

    public String token;

    public String getToken() {
        return RCSPBaseInfo.getLastToken();
    }

    public String getUserId() {
        return RCSPBaseInfo.getLastUserId() + "";
    }


    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }


}
