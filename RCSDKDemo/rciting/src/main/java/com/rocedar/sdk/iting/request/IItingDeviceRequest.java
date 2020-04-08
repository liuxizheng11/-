package com.rocedar.sdk.iting.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.iting.request.listener.ITingMineBindingListener;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/3/30 2:40 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IItingDeviceRequest {


    void bindingDevice(String mac, int deviceId, IRCPostListener listener);

    void unBindingDevice(int deviceId, String deviceNo, IRCPostListener listener);

    void getBindingDevice(ITingMineBindingListener listener);

}
