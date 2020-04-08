package com.rocedar.sdk.shop.request.listener;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.shop.dto.RCServerGoodsParticularsDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/10/15 上午9:36
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCServerGoodsParticularsListener extends IRCBaseListener {
    void getDataSuccess(RCServerGoodsParticularsDTO infoDTO);
}
