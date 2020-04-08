package com.rocedar.sdk.shop.config;

import android.app.Activity;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/9 下午12:01
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCShopConfig {

    void yunXinUnRegisterBroad();

    void yunXinAdvisory(Activity activity, int orderId);

    void yunXinAdvisoryOver(int orderId, int status);

}
