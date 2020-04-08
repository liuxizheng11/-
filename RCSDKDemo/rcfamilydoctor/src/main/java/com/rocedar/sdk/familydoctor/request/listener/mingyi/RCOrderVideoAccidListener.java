package com.rocedar.sdk.familydoctor.request.listener.mingyi;//package com.rcoedar.lib.sdk.yunxin.request.listener;
import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCOrderVideoAccidDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/8/2 下午4:44
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCOrderVideoAccidListener extends IRCBaseListener {

    void getDataSuccess(RCOrderVideoAccidDTO videoAccidDTO);
}
