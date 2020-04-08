package com.rocedar.sdk.familydoctor.request.listener.mingyi;

import com.rocedar.lib.base.network.IRCBaseListener;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/23 下午7:24
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCMingYiPostOrderListener extends IRCBaseListener {

    void getDataSuccess(int orderId);
}
