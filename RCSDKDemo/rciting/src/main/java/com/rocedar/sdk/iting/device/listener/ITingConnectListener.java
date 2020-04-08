package com.rocedar.sdk.iting.device.listener;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/3/29 2:52 PM
 * 版本：V1.1.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingConnectListener {

    void startConnect();

    void onConnect();

    void disConnect();

    void notInit();

    void onError(int code, String msg);

}
