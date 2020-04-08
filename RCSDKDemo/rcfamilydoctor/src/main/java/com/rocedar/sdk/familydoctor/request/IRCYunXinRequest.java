package com.rocedar.sdk.familydoctor.request;//package com.rcoedar.lib.sdk.yunxin.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCOrderVideoAccidListener;

/**
 * 作者：lxz
 * 日期：2018/8/2 下午4:29
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCYunXinRequest {
    /**
     * 获取云信账号信息
     *
     * @param order_id
     * @param listener
     */
    public void getOrderVideoAccid(int order_id, RCOrderVideoAccidListener listener);

    /**
     * 结束视频通话
     *
     * @param order_id
     * @param listener
     */
    public void putOverOrderVider(int order_id, IRCPostListener listener);
}
