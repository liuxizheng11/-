package com.rocedar.sdk.shop.config;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.shop.dto.RCShopChooseUserDTO;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/30 下午5:27
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCShopGoodsChooseUserListener extends IRCBaseListener {

    void getDataSuccess(List<RCShopChooseUserDTO> dtoList);

}
