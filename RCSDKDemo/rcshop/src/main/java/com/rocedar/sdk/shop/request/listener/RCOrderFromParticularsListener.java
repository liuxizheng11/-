package com.rocedar.sdk.shop.request.listener;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.shop.dto.RCOrderFromParticularsDTO;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午3:29
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCOrderFromParticularsListener extends IRCBaseListener {

    void getDataSuccess(RCOrderFromParticularsDTO particularsDTO);

}
