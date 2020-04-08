package com.rocedar.sharedlib.umeng.share;

import android.app.Activity;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by phj on 2016/12/5.
 * 分享配置，在不同的应用中改类需要配置
 */
public class UMShareConfig {

    /**
     * 初始化统计方法
     */
    public static void initShare(Activity activity) {
        UMShareAPI.get(activity);
        //微信
        PlatformConfig.setWeixin("wx87d15f9e8f968ccc", "8e85e3f484c97147a018b469cd5a2a11");
        //QQ
        PlatformConfig.setQQZone("1105865246", "ntcmhrcle9TFlkCI");
    }

}

