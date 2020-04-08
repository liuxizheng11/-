package com.rocedar.sdk.shop.request.listener;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.shop.dto.RCServerGoodsInfoDTO;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/20 下午5:44
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCGetServerGoodsSkuListener extends IRCBaseListener {

    void getDataSuccess(RCServerGoodsInfoDTO infoDTO);

}
