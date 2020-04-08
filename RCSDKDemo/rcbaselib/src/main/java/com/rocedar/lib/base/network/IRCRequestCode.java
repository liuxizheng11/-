package com.rocedar.lib.base.network;

/**
 * Created by phj on 16/3/7.
 */
public interface IRCRequestCode {


    //token失效
    int STATUS_APP_CODE_TOKEN_OVERDUE = 1001;
    //无手机号
    int STATUS_APP_CODE_NO_PHONE_NUMBER = 1101;
    //手机号已被绑定
    int STATUS_APP_CODE_PHONE_NUMBER_INVALID = 1102;


}
