package com.rocedar.sdk.iting.device.envet;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/5/17 5:21 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingConnectEvent {


    public static final int TYPE_CONNECTTING = 0;
    public static final int TYPE_CONNECTED = 1;
    public static final int TYPE_DISCONNECT = 2;

    public int type;

    public RCITingConnectEvent(int type) {
        this.type = type;
    }
}
