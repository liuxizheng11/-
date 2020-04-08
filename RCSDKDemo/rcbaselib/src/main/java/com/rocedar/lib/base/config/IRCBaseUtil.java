package com.rocedar.lib.base.config;

import android.app.Activity;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/25 下午4:49
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCBaseUtil {

    /**
     * @return 设置加载状态的loading图片资源
     */
    int imageResLoading();


    /**
     * @return 设置默认图片
     */
    int imageResDefaultHead();


    /**
     * @return 头像默认资源
     */
    int imageResDefault();


    /**
     * 是否有实现分享
     *
     * @return
     */
    boolean share();

    /**
     * 分享
     *
     * @param context
     * @param image
     * @param title
     * @param msg
     * @param url
     * @return
     */
    void share(Activity context, String image, String title, String msg, String url);


}
