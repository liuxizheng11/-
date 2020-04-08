package com.rocedar.lib.base.network;

/**
 * Created by phj on 16/3/7.
 */
public interface IRequestCode {

    /* 无网络*/
    public final static int STATUS_CODE_NOT_NETWORK = -1;

    /**
     * OK
     */
    public final static int STATUS_CODE_OK = 0;

    /**
     * 操作次数已达上限
     */
    public final static int STATUS_CODE_Operating_limit = 450401;

    public final static int STATUS_CODE_LOGIN_OUT = 450103;

    public final static String STATUS_MSG_LOGIN_OUT = "没有登陆";

    public final static int STATUS_CODE_LOGIN_ERROR = 440101;

    public final static int STATUS_CODE_LOGIN_OUT_FORCE = 460103;

    public final static int STATUS_CODE_TOKEN_INVALID = 450501;

    public final static int STATUS_CODE_TOKEN_OVERDUE = 450502;

    public final static int STATUS_CODE_PHONE_BIND = 450105;

    /**
     * 设备绑定-部分成功
     */
    int STATUS_CODE_Binding_Partial_Success = 700003;
    /**
     * 国际手机号验证码短信限制1小时最多发送3次，超出限制的错误码
     */
    int STATUS_CODE_VERIFICATION_OVERRUN = 450106;
}
