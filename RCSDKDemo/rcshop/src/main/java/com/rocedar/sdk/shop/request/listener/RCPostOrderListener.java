package com.rocedar.sdk.shop.request.listener;

import com.rocedar.lib.base.network.IRCBaseListener;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/12 下午5:30
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCPostOrderListener extends IRCBaseListener {

    void getDataSuccess(int orderId);

}
