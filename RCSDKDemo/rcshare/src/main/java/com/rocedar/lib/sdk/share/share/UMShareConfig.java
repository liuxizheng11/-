package com.rocedar.lib.sdk.share.share;

import android.content.Context;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by phj on 2016/12/5.
 * 分享配置，在不同的应用中改类需要配置
 */
public class UMShareConfig {


//    private String SHARE_KEY_WEIXIN_APPID = "wx87d15f9e8f968ccc";
//    private String SHARE_KEY_WEIXIN_SECRET = "8e85e3f484c97147a018b469cd5a2a11";
//    private String SHARE_KEY_QQ_ID = "1105865246";
//    private String SHARE_KEY_QQ_KEY = "ntcmhrcle9TFlkCI";

    public static String SHARE_KEY_WEIXIN_APPID = "wxdacc6f451141f627";
    public static String SHARE_KEY_WEIXIN_SECRET = "53d85d3bfc06d21f275bd96afb73143a";
    public static String SHARE_KEY_QQ_ID = "1104610967";
    public static String SHARE_KEY_QQ_KEY = "ejYsTxjwn81vsCEl";


    public UMShareConfig(String SHARE_KEY_WEIXIN_APPID, String SHARE_KEY_WEIXIN_SECRET,
                         String SHARE_KEY_QQ_ID, String SHARE_KEY_QQ_KEY) {
        this.SHARE_KEY_WEIXIN_APPID = SHARE_KEY_WEIXIN_APPID;
        this.SHARE_KEY_WEIXIN_SECRET = SHARE_KEY_WEIXIN_SECRET;
        this.SHARE_KEY_QQ_ID = SHARE_KEY_QQ_ID;
        this.SHARE_KEY_QQ_KEY = SHARE_KEY_QQ_KEY;
    }

    /**
     * 初始化统计方法
     */
    public void initShare(Context context) {
        UMShareAPI.get(context);
        //微信
        PlatformConfig.setWeixin(SHARE_KEY_WEIXIN_APPID, SHARE_KEY_WEIXIN_SECRET);
        //QQ
        PlatformConfig.setQQZone(SHARE_KEY_QQ_ID, SHARE_KEY_QQ_KEY);
    }


}

