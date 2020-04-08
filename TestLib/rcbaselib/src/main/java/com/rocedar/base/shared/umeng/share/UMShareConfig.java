package com.rocedar.base.shared.umeng.share;

import android.content.Context;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by phj on 2016/12/5.
 * 分享配置，在不同的应用中改类需要配置
 */
public class UMShareConfig {


    public static String SHARE_KEY_WEIXIN_APPID_N3 = "wx87d15f9e8f968ccc";
    public static String SHARE_KEY_WEIXIN_SECRET_N3 = "8e85e3f484c97147a018b469cd5a2a11";

    public static String SHARE_KEY_QQ_ID_N3 = "1105865246";
    public static String SHARE_KEY_QQ_KEY_N3 = "ntcmhrcle9TFlkCI";


    /**
     * 初始化统计方法
     */
    public static void initShareN3(Context context) {
        UMShareAPI.get(context);
        //微信
        PlatformConfig.setWeixin(SHARE_KEY_WEIXIN_APPID_N3, SHARE_KEY_WEIXIN_SECRET_N3);
        //QQ
        PlatformConfig.setQQZone(SHARE_KEY_QQ_ID_N3, SHARE_KEY_QQ_KEY_N3);

    }

    public static String SHARE_KEY_WEIXIN_APPID_DONGYA = "wxdacc6f451141f627";
    public static String SHARE_KEY_WEIXIN_SECRET_DONGYA = "53d85d3bfc06d21f275bd96afb73143a";

    public static String SHARE_KEY_QQ_ID_DONGYA = "1104610967";
    public static String SHARE_KEY_QQ_KEY_DONGYA = "ejYsTxjwn81vsCEl";

    /**
     * 初始化统计方法
     */
    public static void initShareDongYa(Context context) {
        UMShareAPI.get(context);
        //微信
        PlatformConfig.setWeixin(SHARE_KEY_WEIXIN_APPID_DONGYA, SHARE_KEY_WEIXIN_SECRET_DONGYA);
        //QQ
        PlatformConfig.setQQZone(SHARE_KEY_QQ_ID_DONGYA, SHARE_KEY_QQ_KEY_DONGYA);
    }

}

