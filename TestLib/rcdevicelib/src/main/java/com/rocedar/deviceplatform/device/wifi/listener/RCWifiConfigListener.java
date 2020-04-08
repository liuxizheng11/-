package com.rocedar.deviceplatform.device.wifi.listener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/5 下午8:58
 * 版本：V1.0
 * 描述：wifi连接监听接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCWifiConfigListener {

    void configOk();

    void error(int status, String msg);
}
